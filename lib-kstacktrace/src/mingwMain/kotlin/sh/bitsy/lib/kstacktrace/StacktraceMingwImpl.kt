package sh.bitsy.lib.kstacktrace

private const val BASE_OFFSET = 2
private const val LINE_TO_REMOVE_NOT_FRAME = 1

internal class StacktraceMingwImpl(throwableReference: Throwable, extraOffset: Int) : StackTraceCommon() {
    override val listFrame: List<FrameMingwImpl> = throwableReference
        .stackTraceToString()
        .trim()
        .split("\n")
        .run {
            takeLast(size - LINE_TO_REMOVE_NOT_FRAME - (extraOffset + BASE_OFFSET))
                .takeWhile { it.contains("kfun:") }
                .map { FrameMingwImpl(it) }
        }

    override val currentFrame: FrameMingwImpl = listFrame[0]
}

internal class FrameMingwImpl(stackTraceLine: String) : Frame, FrameExtraInfoMingw {
    val regex = Regex("""at\s+\d+\s+(\S+)\s+([0-9a-f]+)\s+([^+]+)\s+\+\s+(\d+)""").find(stackTraceLine)

    override val rawDefinition: String = regex?.groupValues?.getOrNull(3) ?: "#"
    override val lineOrOffset: Int = regex?.groupValues?.getOrNull(4)?.toIntOrNull() ?: 0
    override val methodName: String by lazy {
        val function = rawDefinition
        val regex = Regex("#([^(]+)\\(")
        val match = regex.find(function)
        if (match != null) {
            match.groupValues[1]
        } else {
            "#"
        }
    }
    override val referencePath: String by lazy {
        val match = Regex("""([^#]+)#""").find(rawDefinition.replace("kfun:", ""))
        if (match != null) {
            match.groupValues[1]
        } else {
            "<root>"
        }
    }

    // Extra Mingw
    override val hexAddress: String = regex?.groupValues?.getOrNull(2) ?: "0x00000000"
    override val symbol: String = regex?.groupValues?.getOrNull(1) ?: ""

    override fun toString(): String = "$referencePath#$methodName() + $lineOrOffset"
}