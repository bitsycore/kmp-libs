import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

android {
    namespace = "sh.bitsy.test"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
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

    listOf(
        macosArm64(),
        macosX64(),
        mingwX64(),
        linuxX64(),
        linuxArm64()
    ).forEach {
        it.binaries.executable()
        it.binaries.staticLib()
        it.binaries.sharedLib()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.libKaddie)
            implementation(projects.libKloggy)
            implementation(projects.libKstacktrace)
        }
    }
}
