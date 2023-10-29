architectury {
	platformSetupLoomIde()
	fabric()
}

loom {
	accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating // Don't use shadow from the shadow plugin because we don't want IDEA to index this.
val developmentFabric: Configuration = configurations.getByName("developmentFabric")
configurations {
	compileClasspath.get().extendsFrom(configurations["common"])
	runtimeClasspath.get().extendsFrom(configurations["common"])
	developmentFabric.extendsFrom(configurations["common"])
}


dependencies {
	modImplementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
	modImplementation("com.terraformersmc:modmenu:${property("fabric_mod_menu_version")}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

	modImplementation("dev.isxander.yacl:yet-another-config-lib-fabric:${property("yacl_version")}")

	common(project(":common", configuration = "namedElements")) { isTransitive = true }
	shadowCommon(project(":common", configuration = "transformProductionFabric")) { isTransitive = true }

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
		configurations = listOf(project.configurations["shadowCommon"])
		archiveClassifier.set("dev-shadow")
	}

	remapJar {
		injectAccessWidener.set(true)
		inputFile.set(shadowJar.flatMap { it.archiveFile })
		dependsOn(shadowJar)
		archiveClassifier.set("fabric")
	}

	jar {
		archiveClassifier.set("dev")
	}
}