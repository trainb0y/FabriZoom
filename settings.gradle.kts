pluginManagement {
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net/")
        gradlePluginPortal()
    }

    @Suppress("LocalVariableName")
    plugins {
        val kotlin_version: String by settings
        val architectury_gradle_plugin_version: String by settings
        val architectury_loom_version: String by settings
        val shadow_version: String by settings

        kotlin("jvm") version kotlin_version
        kotlin("plugin.serialization") version kotlin_version
        id("architectury-plugin") version architectury_gradle_plugin_version
        id("dev.architectury.loom") version architectury_loom_version
        id("com.github.johnrengelman.shadow") version shadow_version
    }
}

rootProject.name = "fabrizoom"

include("common", "fabric", "forge")