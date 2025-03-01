import androidx.compose.runtime.*
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import sh.bitsy.lib.kaddie.getDependency

@Composable
fun ShowWindow(
    windowName: String,
    windowList: List<Int>,
    id: Int,
    onClose: (Int) -> Unit,
    content: @Composable FrameWindowScope.() -> Unit
) {
    if (id in windowList) Window(
        onCloseRequest = { onClose(id) }, title = windowName, content = content
    )
}

fun main() {
    appInit()
    application {
        var windows by remember { mutableStateOf((0 until 2).toList()) }

        ShowWindow(
            "Main Window",
            windows,
            0, {
                windows -= it
                exitIfEmpty(windows)
            }
        ) {
            getDependency<App>().Show()
        }

        ShowWindow(
            "Secondary Window",
            windows,
            1, {
                windows -= it
                exitIfEmpty(windows)
            }
        ) {
            getDependency<App>().Show()
        }
    }
}

private fun ApplicationScope.exitIfEmpty(windows: List<Int>) {
    if (windows.isEmpty())
        exitApplication()
}