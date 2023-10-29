architectury {
	platformSetupLoomIde()
	forge()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
	forge {
		mixinConfig("fabrizoom.mixins.json")
		convertAccessWideners.set(true)
		extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
	}
}


val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating // Don't use shadow from the shadow plugin because we don't want IDEA to index this.
val developmentForge: Configuration = configurations.getByName("developmentForge")

configurations {
	compileClasspath.get().extendsFrom(configurations["common"])
	runtimeClasspath.get().extendsFrom(configurations["common"])
	developmentForge.extendsFrom(configurations["common"])
}

repositories {
	maven("https://thedarkcolour.github.io/KotlinForForge/")
}

dependencies {
	forge("net.minecraftforge:forge:${property("minecraft_version")}-${property("forge_version")}")
	implementation("thedarkcolour:kotlinforforge:${property("forge_kotlin_version")}")

	common(project(":common", configuration = "namedElements")) { isTransitive = false }
	shadowCommon(project(":common", configuration = "transformProductionForge")) { isTransitive = false }

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
		archiveClassifier.set("forge")
	}

	jar {
		archiveClassifier.set("dev")
	}
}