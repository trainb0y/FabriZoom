plugins {
	id("fabric-loom")
	`maven-publish`
	java
	kotlin("jvm") version "1.7.20"
}

group = property("maven_group")!!
		version = property("mod_version")!!

		repositories {
			// Add repositories to retrieve artifacts from in here.
			// You should only use this when depending on other mods because
			// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
			// See https://docs.gradle.org/current/userguide/declaring_repositories.html
			// for more information about repositories.
			maven(uri("https://maven.terraformersmc.com/"))
			maven(uri("https://maven.gegy.dev"))
			maven(uri("https://maven.isxander.dev/releases"))
			mavenCentral()
		}

dependencies {
	minecraft("com.mojang:minecraft:${property("minecraft_version")}")
	mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")

	// Configurate
	implementation("org.spongepowered:configurate-hocon:${property("configurate_version")}")
	implementation("org.spongepowered:configurate-extra-kotlin:${property("configurate_version")}")

	include("org.spongepowered:configurate-core:${property("configurate_version")}")
	include("org.spongepowered:configurate-hocon:${property("configurate_version")}")
	include("org.spongepowered:configurate-extra-kotlin:${property("configurate_version")}")

	// Configurate's dependencies
	include("com.google.guava:guava:31.1-jre")   // guava
	include("com.typesafe:config:1.4.2")         // hocon
	include("io.leangen.geantyref:geantyref:1.3.12") // reflection

	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
	modImplementation("com.terraformersmc:modmenu:${property("mod_menu_version")}")
	modImplementation("dev.isxander:yet-another-config-lib:${property("yacl_version")}")
	include("dev.isxander:yet-another-config-lib:${property("yacl_version")}") // this might? cause issues
	modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")
}

tasks {
	processResources {
		inputs.property("version", project.version)
		filesMatching("fabric.mod.json") {
			expand(mutableMapOf("version" to project.version))
		}
	}

	jar {
		from("LICENSE")
	}

	compileKotlin {
		kotlinOptions.jvmTarget = "17"
	}
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()
}