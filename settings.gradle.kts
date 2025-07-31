pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    plugins {
        kotlin("multiplatform").version("1.9.20")
        id("org.jetbrains.compose").version("1.5.10")
    }
}
rootProject.name = "SaltPlayerRemote"