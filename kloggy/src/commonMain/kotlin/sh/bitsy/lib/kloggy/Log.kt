package sh.bitsy.lib.kloggy

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.absoluteValue

enum class LogLevels(val setup: LogSetup) {
    Verbose(LogSetup(AnsiEscapeUtil.PURPLE, "🟪")),
    Debug(LogSetup(AnsiEscapeUtil.BLUE, "🟦")),
    Info(LogSetup(AnsiEscapeUtil.RESET, "⬜")),
    Warning(LogSetup(AnsiEscapeUtil.YELLOW, "🟨")),
    Error(LogSetup(AnsiEscapeUtil.RED,"🟥")),
    Assert(LogSetup(AnsiEscapeUtil.RED,"🆘"))
}

data class LogSetup(val color : String, val emoji : String)

fun getCurrentTimestamp(): Instant {
    return Clock.System.now()
}

expect fun log(tag: String = "Default", message: String, logLevel: LogLevels = LogLevels.Info)

fun formatTimestampForLogging(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.time}"
}

fun getTimestamp() = formatTimestampForLogging(getCurrentTimestamp()).take(13).padEnd(13)
fun getFormatedTag(tag: String) = "${getRandomEmoji(tag)} ${tag.take(21).padEnd(21)}"

// Extended list of shape sh.bitsy.lib.kloggy.emojis
val emojis = listOf(
    "🐶", // Dog
    "🐱", // Cat
    "🐭", // Mouse
    "🐹", // Hamster
    "🐰", // Rabbit
    "🦊", // Fish
    "🐸", // Frog
    "🐵", // Monkey
    "🦄", // Unicorn
    "🐼", // Panda
    "🦁", // Lion
    "🐯", // Tiger
    "🐨", // Koala
    "🦒", // Giraffe
    "🐧", // Penguin
    "🐦", // Bird
    "🦉", // Owl
    "🦘", // Kangaroo
    "🐴", // Horse
    "🦃", // Turkey
    "🦔"  // Hedgehog
)

// Get Random Emoji with a Tag, the emoji will always be the same for the same tag during app life.
val offset = (Clock.System.now().toEpochMilliseconds() / 1000).toInt() % emojis.size
fun getRandomEmoji(tag: String): String {
    val hash = tag.hashCode().absoluteValue
    val index = (hash + offset) % emojis.size
    return emojis[index]
}

internal fun logPrintln(tag: String = "Default", message: String, logLevel: LogLevels = LogLevels.Info, colored : Boolean = true) {
    var formatedLogLevel = logLevel.setup.emoji + " " + logLevel.name.uppercase().take(1) + " " + logLevel.setup.emoji
    val formatedTag = getFormatedTag(tag)

    if (colored) {
        formatedLogLevel = "| " + AnsiEscapeUtil.colorize(
            logLevel.name.uppercase().padEnd(7),
            logLevel.setup.color,
            bold = true
        ) + " |"
    }

    println("${getTimestamp()} $formatedTag $formatedLogLevel $message")
}
