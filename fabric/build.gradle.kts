architectury {
	platformSetupLoomIde()
	fabric()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

val common by configurations.registering
val shadowCommon by configurations.registering
configurations {
	configurations.compileClasspath.get().extendsFrom(common.get())
	configurations.runtimeClasspath.get().extendsFrom(common.get())
	configurations["developmentFabric"].extendsFrom(common.get())
}


dependencies {
	modImplementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
	modImplementation("com.terraformersmc:modmenu:${property("fabric_mod_menu_version")}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

	common(project(":common", configuration = "namedElements")) { isTransitive = false }
	shadowCommon(project(":common", configuration = "transformProductionFabric")) { isTransitive = false }

	modImplementation("dev.isxander:yet-another-config-lib:${property("yacl_version")}-fabric")
}

tasks {
	processResources {
		inputs.property("version", project.version)

		filesMatching("fabric.mod.json") {
			expand("version" to project.version)
		}
	}

	shadowJar {
		exclude("architectury.common.json")
		configurations = listOf(shadowCommon.get())
		archiveClassifier.set("dev-shadow")
	}

	remapJar {
		dependsOn(shadowJar)
		from(rootProject.file("LICENSE"))
		injectAccessWidener.set(true)
		inputFile.set(shadowJar.get().archiveFile)
		archiveClassifier.set("fabric")
	}

	jar {
		archiveClassifier.set("dev")
	}
}