plugins {
    alias(libs.plugins.kotlinter) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false 
}

tasks {
    register<Delete>("clean"){
        delete(rootProject.buildDir)
    }
}
