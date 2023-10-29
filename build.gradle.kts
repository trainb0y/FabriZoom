import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
	id("architectury-plugin")
	id("dev.architectury.loom") apply false
	id("com.github.johnrengelman.shadow") apply false
	kotlin("jvm")
	java
}

architectury {
	minecraft = property("minecraft_version") as String
}

subprojects {
	apply(plugin="dev.architectury.loom")

	val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")

	loom.silentMojangMappingsLicense()

	dependencies {
		"minecraft"("com.mojang:minecraft:${property("minecraft_version")}")
		"mappings"(loom.officialMojangMappings())
	}
}

allprojects {
	apply(plugin="java")
	apply(plugin="kotlin")
	apply(plugin="architectury-plugin")
	apply(plugin="com.github.johnrengelman.shadow")

	base {
		archivesName.set(property("archives_base_name") as String)
	}

	version = property("mod_version") as String
	group = property("maven_group") as String


	repositories {
		maven(uri("https://maven.terraformersmc.com/"))
		maven(uri("https://maven.gegy.dev"))
		maven(uri("https://maven.isxander.dev/releases"))

		mavenCentral()
	}

	tasks {
		jar {
			from("LICENSE")
		}
		compileKotlin {
			kotlinOptions.jvmTarget = "17"
		}
	}
}
