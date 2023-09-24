package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.config.ConfigHandler
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/** Main mod entrypoint */
@Suppress("Unused")
class FabriZoom : ClientModInitializer {
	override fun onInitializeClient() {
		ConfigHandler.loadConfig()
		Keybinds.register()
		ClientTickEvents.END_CLIENT_TICK.register(
			ClientTickEvents.EndTick { Keybinds.onTick() }
		)
	}

	companion object {
		/** Logger for this mod, prefixes logs with the mod name */
		val logger: Logger = LoggerFactory.getLogger("fabrizoom")
	}
}