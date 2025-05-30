package sh.bitsy.lib.kstacktrace

private const val THREAD_BASE_OFFSET = 4
private const val THROWABLE_BASE_OFFSET = 0

internal class StacktraceAndroidImpl(val throwableReference: Throwable?, extraOffset: Int) : StackTraceCommon() {
    private var finalOffset = 0
    private val nativeStacktrace = run {
        val stackTrace = throwableReference?.stackTrace
        if (stackTrace != null) {
            finalOffset = THROWABLE_BASE_OFFSET
            stackTrace
        } else {
            finalOffset = THREAD_BASE_OFFSET
            Thread.currentThread().stackTrace
        }
    }

    override val listFrame: List<Frame> =
        nativeStacktrace
        .toList()
        .run { takeLast(size - (finalOffset + extraOffset)).map { FrameAndroid(it) } }

    override val currentFrame: Frame = listFrame.getOrNull(0) ?: listFrame.last()
}

internal class FrameAndroid(val stackTraceElement: StackTraceElement) : Frame, FrameExtraInfoJvmAndroid {
    override val referencePath: String get() = stackTraceElement.toString()
    override val methodName: String get() = stackTraceElement.methodName
    override val lineOrOffset: Int get() = stackTraceElement.lineNumber
    override val rawDefinition: String by lazy {
        try {
            if (methodName == "<init>") return@lazy stackTraceElement.toString()
            Class.forName(stackTraceElement.className)
                .declaredMethods
                .find { it.name == methodName }!!
                .toGenericString()
        } catch (_: Exception) {
            "unknown"
        }
    }

    // Extra Android
    override val fileName: String get() = stackTraceElement.fileName ?: "no-file"
    override val isNativeMethod: Boolean get() = stackTraceElement.isNativeMethod

    override fun toString(): String {
        return stackTraceElement.toString()
    }
}