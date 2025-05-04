package sh.bitsy.lib.kloggy

import android.util.Log
import sh.bitsy.lib.kloggy.LogLevels as LogEnum

actual fun log(tag: String, message: String, logLevel: LogEnum) {
    val formatedTag = getRandomEmoji(tag) + " " + tag
    when (logLevel) {
        LogEnum.Verbose -> Log.v(formatedTag, message)
        LogEnum.Debug -> Log.d(formatedTag, message)
        LogEnum.Info -> Log.i(formatedTag, message)
        LogEnum.Warning -> Log.w(formatedTag, message)
        LogEnum.Error -> Log.e(formatedTag, message)
        LogEnum.Assert -> Log.wtf(formatedTag, message)
    }
}