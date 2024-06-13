import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
	id("architectury-plugin")
	id("dev.architectury.loom") apply false
	id("com.github.johnrengelman.shadow") apply false
	kotlin("jvm")
	kotlin("plugin.serialization")
	java
}

architectury {
	minecraft = property("minecraft_version") as String
}

subprojects {
	apply(plugin = "dev.architectury.loom")

	val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")

	loom.silentMojangMappingsLicense()

	dependencies {
		"minecraft"("com.mojang:minecraft:${property("minecraft_version")}")
		"mappings"(loom.officialMojangMappings())
	}
}

kotlin {
	jvmToolchain(21)
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}


allprojects {
	apply(plugin = "java")
	apply(plugin = "kotlin")
	apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
	apply(plugin = "architectury-plugin")
	apply(plugin = "com.github.johnrengelman.shadow")

	base {
		archivesName.set(property("archives_base_name") as String)
	}

	version = property("mod_version") as String
	group = property("maven_group") as String


	repositories {
		maven(uri("https://maven.terraformersmc.com/"))
		maven(uri("https://maven.gegy.dev"))
		maven(uri("https://maven.isxander.dev/releases"))

		maven(uri("https://maven.isxander.dev/snapshots")) // todo: yacl 3.4

		// duct tape for yacl 3.1.1 wanting to use a snapshot version of twelvemonkeys imageio
		// todo: remove
		maven(uri("https://oss.sonatype.org/content/repositories/snapshots"))

		mavenCentral()
		// mavenLocal() // currently using a 1.20.5 version of yacl for neoforge, which doesn't officially exist
	}

	tasks {
		jar {
			from("LICENSE")
		}
	}
}
