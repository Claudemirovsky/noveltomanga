plugins {
    alias(libs.plugins.android.library) 
    alias(libs.plugins.kotlinter) 
    alias(libs.plugins.kotlin.android)
    `maven-publish`
}

android {
    compileSdk = 32
    namespace = "org.claudemirovsky.noveltomanga"

    defaultConfig {
        minSdk = 21
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    compileOnly(libs.coroutines)
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
            version = "1.2.1"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
