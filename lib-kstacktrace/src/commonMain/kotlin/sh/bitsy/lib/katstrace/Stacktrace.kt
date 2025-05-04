package sh.bitsy.lib.katstrace

interface Stacktrace {
    val listFrame: List<Frame>
    val currentFrame: Frame
}

interface Frame {
    val referencePath: String
    val methodName: String
    val rawDefinition: String
    val lineOrOffset: Int
}

interface FrameExtraInfoMingw {
    val hexAddress : String
    val symbol : String
}

interface FrameExtraInfoJvmAndroid {
    val fileName: String
    val isNativeMethod: Boolean
}

interface FrameExtraInfoJvmNonAndroid : FrameExtraInfoJvmAndroid{
    val moduleName: String
}

expect fun Stacktrace(throwable: Throwable, extraOffset: Int) : Stacktrace
expect fun Stacktrace(throwable: Throwable) : Stacktrace
expect fun Stacktrace(extraOffset: Int) : Stacktrace
expect fun Stacktrace() : Stacktrace


internal abstract class StackTraceCommon() : Stacktrace {
    override fun toString(): String {
        return buildString {
            listFrame.forEachIndexed { index, frame ->
                append("\t[$index] : $frame\n")
            }
        }
    }
}