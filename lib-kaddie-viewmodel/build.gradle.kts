plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

val javaVersion: JavaVersion by rootProject.extra

android {
    namespace = "sh.bitsy.lib.kaddie"
    compileSdk = rootProject.extra["compileSdk"] as Int

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    buildFeatures {
        compose = true
    }

    defaultConfig {
        minSdk = rootProject.extra["minSdk"] as Int
    }
}

dependencies {
    implementation(projects.libKaddie)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(kotlin("reflect"))
}