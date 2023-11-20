architectury {
	platformSetupLoomIde()
	neoForge()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
	neoForge {

	}
}


val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating // Don't use shadow from the shadow plugin because we don't want IDEA to index this.
val developmentNeoForge: Configuration = configurations.getByName("developmentNeoForge")

configurations {
	compileClasspath.get().extendsFrom(configurations["common"])
	runtimeClasspath.get().extendsFrom(configurations["common"])
	developmentNeoForge.extendsFrom(configurations["common"])
}

repositories {
	maven("https://maven.neoforged.net/releases")
	maven("https://thedarkcolour.github.io/KotlinForForge/")
}

dependencies {
	neoForge("net.neoforged:neoforge:${property("neoforge_version")}")
	implementation("thedarkcolour:kotlinforforge-neoforge:${property("forge_kotlin_version")}")

	common(project(":common", configuration = "namedElements")) { isTransitive = false }
	shadowCommon(project(":common", configuration = "transformProductionNeoForge")) { isTransitive = false }

	modImplementation("dev.isxander.yacl:yet-another-config-lib-forge:${property("yacl_version")}")
}

tasks {
	processResources {
		inputs.property("version", project.version)

		filesMatching("META-INF/mods.toml") {
			expand("mod_version" to project.version)
		}
	}

	shadowJar {
		exclude("architectury.common.json")

		configurations = listOf(project.configurations["shadowCommon"])
		archiveClassifier.set("dev-shadow")
	}

	remapJar {
		inputFile.set(shadowJar.flatMap { it.archiveFile })
		dependsOn(shadowJar)
		archiveClassifier.set("neoforge")
		injectAccessWidener.set(true)
	}

	jar {
		archiveClassifier.set("dev")
	}
}