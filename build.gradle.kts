buildscript {
    val kotlin_version by extra("1.6.21")
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

plugins {
     id("org.jmailen.kotlinter") version "3.10.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks {
    register<Delete>("clean"){
        delete(rootProject.buildDir)
    }
}
