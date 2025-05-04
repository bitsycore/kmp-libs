package sh.bitsy.lib.kstacktrace

actual fun Stacktrace(): Stacktrace = StacktraceMingwImpl(Throwable(), 0)
actual fun Stacktrace(extraOffset: Int): Stacktrace = StacktraceMingwImpl(Throwable(), extraOffset)
actual fun Stacktrace(throwable: Throwable): Stacktrace = StacktraceMingwImpl(throwable, 0)
actual fun Stacktrace(throwable: Throwable, extraOffset: Int): Stacktrace = StacktraceMingwImpl(throwable, extraOffset)