package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.config.Config
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
		Keybinds.register()
		ClientTickEvents.END_CLIENT_TICK.register(
			ClientTickEvents.EndTick { Keybinds.onTick() }
		)
	}

	companion object {
		val logger: Logger = LoggerFactory.getLogger("fabrizoom")
	}
}