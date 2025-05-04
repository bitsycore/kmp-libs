package sh.bitsy.lib.katstrace

actual fun Stacktrace(): Stacktrace = StacktraceJvmImpl(null, 0)
actual fun Stacktrace(extraOffset: Int): Stacktrace = StacktraceJvmImpl(null, extraOffset)
actual fun Stacktrace(throwable: Throwable): Stacktrace = StacktraceJvmImpl(throwable, 0)
actual fun Stacktrace(throwable: Throwable, extraOffset: Int): Stacktrace = StacktraceJvmImpl(throwable, extraOffset)