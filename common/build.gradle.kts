architectury {
	common((property("enabled_platforms") as String).split(","))
}

loom {
	accessWidenerPath.set(file("src/main/resources/fabrizoom.accesswidener"))
}

dependencies {
	// Configurate
	implementation("org.spongepowered:configurate-core:${property("configurate_version")}")
	implementation("org.spongepowered:configurate-hocon:${property("configurate_version")}")
	implementation("org.spongepowered:configurate-extra-kotlin:${property("configurate_version")}")
	include("org.spongepowered:configurate-core:${property("configurate_version")}")
	include("org.spongepowered:configurate-hocon:${property("configurate_version")}")
	include("org.spongepowered:configurate-extra-kotlin:${property("configurate_version")}")

	// Configurate's dependencies
	include("com.google.guava:guava:31.1-jre")   // guava
	include("com.typesafe:config:1.4.2")         // hocon
	include("io.leangen.geantyref:geantyref:1.3.12") // reflection


	modImplementation("dev.isxander.yacl:yet-another-config-lib-common:${property("yacl_version")}")
}