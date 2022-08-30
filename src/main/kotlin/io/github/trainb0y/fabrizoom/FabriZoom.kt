package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.keybinds.ZoomKeybinds
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("Unused")
class FabriZoom : ClientModInitializer {
	override fun onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient? ->
			val shouldZoom = ZoomKeybinds.zoomKey.isPressed
			Zoom.zooming = shouldZoom
			if (!shouldZoom) Zoom.currentZoomFovMultiplier = 1f
		})
	}

	companion object {
		val logger: Logger = LoggerFactory.getLogger("fabrizoom")
	}
}