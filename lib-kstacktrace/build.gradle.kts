import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

android {
    namespace = "sh.bitsy.lib.kstacktrace"
    compileSdk = rootProject.extra["compileSdk"] as Int
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = rootProject.extra["minSdk"] as Int
    }
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    val xcf = XCFramework("KStacktrace")
    listOf(
        macosArm64(),
        macosX64(),
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "KStacktrace"
            isStatic = true
            xcf.add(this)
        }
    }

    listOf(
        mingwX64(),
        linuxX64(),
        linuxArm64()
    ).forEach {
        it.binaries {
            sharedLib()
            staticLib()
        }
    }

    sourceSets {
        commonMain.dependencies {
        }
    }
}