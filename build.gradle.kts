plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

project.extra["targetSdk"] = 34
project.extra["compileSdk"] = 34
project.extra["minSdk"] = 24
project.extra["javaVersion"] = JavaVersion.VERSION_17