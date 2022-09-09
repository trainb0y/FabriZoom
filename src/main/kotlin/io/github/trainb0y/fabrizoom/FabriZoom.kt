package io.github.trainb0y.fabrizoom

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("Unused")
class FabriZoom : ClientModInitializer {
	override fun onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient? ->
			val shouldZoom = zoomKey.isPressed
			ZoomLogic.zooming = shouldZoom
			if (!shouldZoom) ZoomLogic.currentZoomFovMultiplier = 1f
		})
	}

	companion object {
		val zoomKey: KeyBinding = KeyBindingHelper.registerKeyBinding(
			KeyBinding("key.fabrizoom.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "key.fabrizoom.category")
		)
		val logger: Logger = LoggerFactory.getLogger("fabrizoom")
	}
}