plugins {
    alias(libs.plugins.android.library) 
    alias(libs.plugins.kotlinter) 
    alias(libs.plugins.kotlin.android) 
}

android {
    compileSdkVersion(32)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(32)
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(libs.wordwrap)
}

tasks {
    preBuild {
        dependsOn(lintKotlin)
    }

    lintKotlin { 
        dependsOn(formatKotlin) 
    }
}
