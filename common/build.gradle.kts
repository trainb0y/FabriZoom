architectury {
	common((property("enabled_platforms") as String).split(","))
}

loom {
	accessWidenerPath.set(file("src/main/resources/fabrizoom.accesswidener"))
}

dependencies {
	// I know it says "-fabric" but that's apparently the proper version
	// See YACL 3.4 changelog
	modCompileOnly("dev.isxander:yet-another-config-lib:${property("yacl_version")}-fabric")

	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${property("kotlinx_json_version")}")
}