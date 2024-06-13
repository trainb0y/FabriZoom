package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.config.ConfigHandler
import io.github.trainb0y.fabrizoom.config.createConfigScreen
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object FabriZoom {
	val ZOOM_OVERLAY = ResourceLocation.parse("fabrizoom:textures/zoom_overlay.png")

	lateinit var platform: Platform

	var shouldOpenConfigScreen = false

	fun init(platform: Platform) {
		logger.info("FabriZoom on ${platform.platformName}!")
		this.platform = platform
		ConfigHandler.loadConfig()
		platform.registerKeybinds()
		platform.registerTick(::onTick)
		platform.registerCommand()

	}

	/** Logger for this mod, prefixes logs with the mod name */
	val logger: Logger = LoggerFactory.getLogger("fabrizoom")

	private fun onTick(client: Minecraft) {
		if (shouldOpenConfigScreen) {
			// exists because opening a screen in the context of a command is pain
			client.setScreen(createConfigScreen(client.screen))
			shouldOpenConfigScreen = false
		}

		if (ZoomLogic.isZooming) {
			if (Keybinds.decreaseKey.isDown) ZoomLogic.changeZoomDivisor(false)
			if (Keybinds.increaseKey.isDown) ZoomLogic.changeZoomDivisor(true)
			if (Keybinds.resetKey.isDown || !Keybinds.zoomKey.isDown) ZoomLogic.zoomDivisor =
				ConfigHandler.values.zoomDivisor
		}

		if (ConfigHandler.values.zoomSound) {
			if (!ZoomLogic.isZooming && Keybinds.zoomKey.isDown) client.player?.playSound(
				SoundEvents.SPYGLASS_USE, 1f, 1f
			)
			if (ZoomLogic.isZooming && !Keybinds.zoomKey.isDown) client.player?.playSound(
				SoundEvents.SPYGLASS_STOP_USING, 1f, 1f
			)
		}

		ZoomLogic.isZooming = Keybinds.zoomKey.isDown
	}
}