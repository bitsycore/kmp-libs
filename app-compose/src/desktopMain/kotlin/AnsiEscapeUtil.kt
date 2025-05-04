internal object AnsiEscapeUtil {
    const val RESET = "\u001B[0m"

    const val BLACK = "\u001B[30m"
    const val RED = "\u001B[31m"
    const val GREEN = "\u001B[32m"
    const val YELLOW = "\u001B[33m"
    const val BLUE = "\u001B[34m"
    const val PURPLE = "\u001B[35m"
    const val CYAN = "\u001B[36m"
    const val WHITE = "\u001B[37m"

    const val BLACK_BACKGROUND = "\u001B[40m"
    const val RED_BACKGROUND = "\u001B[41m"
    const val GREEN_BACKGROUND = "\u001B[42m"
    const val YELLOW_BACKGROUND = "\u001B[43m"
    const val BLUE_BACKGROUND = "\u001B[44m"
    const val PURPLE_BACKGROUND = "\u001B[45m"
    const val CYAN_BACKGROUND = "\u001B[46m"
    const val WHITE_BACKGROUND = "\u001B[47m"

    const val BOLD = "\u001B[1m"
    const val UNDERLINE = "\u001B[4m"
    const val REVERSED = "\u001B[7m"

    fun colorize(text: String, color: String, background: String = "", bold: Boolean = false, underline: Boolean = false, reversed: Boolean = false, disable: Boolean = false): String {
        if (disable)
            return text

        val style = StringBuilder()
        if (bold) style.append(BOLD)
        if (underline) style.append(UNDERLINE)
        if (reversed) style.append(REVERSED)
        style.append(color).append(background)
        return style.toString() + text + RESET
    }
}