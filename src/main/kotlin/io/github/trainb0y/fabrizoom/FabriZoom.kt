package io.github.trainb0y.fabrizoom

import net.fabricmc.api.ClientModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("Unused")
class FabriZoom : ClientModInitializer {
	override fun onInitializeClient() {

	}

	companion object {
		val logger: Logger = LoggerFactory.getLogger("fabrizoom")
	}
}