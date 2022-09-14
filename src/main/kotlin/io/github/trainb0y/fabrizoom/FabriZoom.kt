package io.github.trainb0y.fabrizoom

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("Unused")
class FabriZoom : ClientModInitializer {
	override fun onInitializeClient() {
		Keybinds.register()
		ClientTickEvents.END_CLIENT_TICK.register(
			ClientTickEvents.EndTick { Keybinds.onTick() }
		)
	}

	companion object {
		val logger: Logger = LoggerFactory.getLogger("fabrizoom")
	}
}