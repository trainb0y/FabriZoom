package io.github.trainb0y.fabrizoom.fabric

import io.github.trainb0y.fabrizoom.FabriZoom
import net.fabricmc.api.ClientModInitializer

@Suppress("unused")
class FabricEntrypoint : ClientModInitializer {
	override fun onInitializeClient() {
		FabriZoom.init(FabricPlatform())
	}
}