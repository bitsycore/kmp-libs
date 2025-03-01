package sh.bitsy.app.kaddietest

import App
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import appInit
import main
import sh.bitsy.lib.kaddie.getDependency
import sh.bitsy.lib.kaddie.registerDependency

class MainActivity : ComponentActivity() {
    private lateinit var app : App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        registerDependency<Context>(this)
        appInit()
        main()
        app = getDependency()
        setContent {
            SetAndroidTopBar()
            app.Show()
        }
    }

    @Composable
    private fun SetAndroidTopBar() {
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            }
        }
    }
}