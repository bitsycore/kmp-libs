package sh.bitsy.lib.katstrace

actual fun Stacktrace(): Stacktrace = StacktraceAndroidImpl(null, 0)
actual fun Stacktrace(extraOffset: Int): Stacktrace = StacktraceAndroidImpl(null, extraOffset)
actual fun Stacktrace(throwable: Throwable): Stacktrace = StacktraceAndroidImpl(throwable, 0)
actual fun Stacktrace(throwable: Throwable, extraOffset: Int): Stacktrace = StacktraceAndroidImpl(throwable, extraOffset)