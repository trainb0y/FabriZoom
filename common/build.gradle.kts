architectury {
	common((property("enabled_platforms") as String).split(","))
}

loom {
	accessWidenerPath.set(file("src/main/resources/fabrizoom.accesswidener"))
}

dependencies {
	modCompileOnly("dev.isxander.yacl:yet-another-config-lib-common:${property("yacl_version")}")

	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
}