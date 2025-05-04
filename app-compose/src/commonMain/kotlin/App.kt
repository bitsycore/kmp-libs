import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kmp_libs.composeapp.generated.resources.Res
import kmp_libs.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import sh.bitsy.lib.kaddie.registerDependency

fun appInit() {
    registerDependency(App())
}

class App {
    var value by mutableIntStateOf(0)

    @Composable
    @Preview
    fun Show() {
        MaterialTheme {
            var showContent by remember { mutableStateOf(false) }
            Column(
                Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.safeDrawing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { showContent = !showContent; value++ }) {
                    Text("Click me!")
                }
                AnimatedVisibility(showContent) {
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Text("Hello, world! : $value")
                    }
                }
            }
        }
    }
}