package sh.bitsy.lib.kloggy

actual fun log(tag: String, message: String, logLevel: LogLevels) = logPrintln(tag, message, logLevel, true)