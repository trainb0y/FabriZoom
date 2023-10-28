package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.config.ConfigHandler
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/** Main mod entrypoint */
@Suppress("Unused")
class FabriZoom : ClientModInitializer {
	override fun onInitializeClient() {
		ConfigHandler.loadConfig()
		Keybinds.registerKeybinds()
		ClientTickEvents.END_CLIENT_TICK.register(::onTick)
	}

	private fun onTick(client: MinecraftClient) {
		if (ZoomLogic.isZooming) {
			if (Keybinds.decreaseKey.isPressed) ZoomLogic.changeZoomDivisor(false)
			if (Keybinds.increaseKey.isPressed) ZoomLogic.changeZoomDivisor(true)
			if (Keybinds.resetKey.isPressed || !Keybinds.zoomKey.isPressed) ZoomLogic.zoomDivisor = ConfigHandler.values.zoomDivisor
		}

		if (ConfigHandler.values.zoomSound) {
			if (!ZoomLogic.isZooming && Keybinds.zoomKey.isPressed) client.player?.playSound(
				SoundEvents.ITEM_SPYGLASS_USE, 1f, 1f)
			if (ZoomLogic.isZooming && !Keybinds.zoomKey.isPressed)  client.player?.playSound(
				SoundEvents.ITEM_SPYGLASS_STOP_USING, 1f, 1f)
		}

		ZoomLogic.isZooming = Keybinds.zoomKey.isPressed
	}

	companion object {
		/** Logger for this mod, prefixes logs with the mod name */
		val logger: Logger = LoggerFactory.getLogger("fabrizoom")
	}
}