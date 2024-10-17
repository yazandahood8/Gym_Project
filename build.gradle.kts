plugins {
    id("com.android.application") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false // Ensure Kotlin plugin is here too
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // Ensures that Kotlin and other plugins are fetched correctly
        maven { url = uri("https://jitpack.io") } // Add this line
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0") // Ensure the Kotlin plugin version
    }
}

allprojects {
    repositories {
//        google()
      //  mavenCentral()
    }
}
