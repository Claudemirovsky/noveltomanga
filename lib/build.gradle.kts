plugins {
    alias(libs.plugins.android.library) 
    alias(libs.plugins.kotlinter) 
    alias(libs.plugins.kotlin.android)
    `maven-publish`
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
    implementation(libs.coroutines)
}

tasks {
    preBuild {
        dependsOn(lintKotlin)
    }

    lintKotlin { 
        dependsOn(formatKotlin) 
    }
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.Claudemirovsky"
            artifactId = "noveltomanga"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
