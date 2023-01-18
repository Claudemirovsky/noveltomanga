plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jmailen.kotlinter")
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
    //compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("com.github.davidmoten:word-wrap:0.1.9")
}

tasks.preBuild { dependsOn(tasks.lintKotlin) }
tasks.lintKotlin { dependsOn(tasks.formatKotlin) }
