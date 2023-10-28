package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.config.ConfigHandler.values
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.sound.SoundEvents
import org.lwjgl.glfw.GLFW


/**
 * Handles the mod's zoom-related keybinds
 */
object Keybinds {
	/** Translation key for the keybind category */
	private const val CATEGORY = "category.fabrizoom.keybinds"

	/** The primary key for zooming */
	lateinit var zoomKey: KeyBinding

	/** The key to increase the zoom divisor */
	lateinit var increaseKey: KeyBinding

	/** The key to decrease the zoom divisor */
	lateinit var decreaseKey: KeyBinding

	/** The key to reset the zoom divisor */
	lateinit var resetKey: KeyBinding

	fun registerKeybinds() {
		// can't register and initialize at the same time, because they won't appear in the keybind menu
		zoomKey = KeyBindingHelper.registerKeyBinding(
			KeyBinding("key.fabrizoom.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY)
		)
		increaseKey = KeyBindingHelper.registerKeyBinding(
			KeyBinding("key.fabrizoom.increase", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.code, CATEGORY)
		)
		decreaseKey = KeyBindingHelper.registerKeyBinding(
			KeyBinding("key.fabrizoom.decrease", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.code, CATEGORY)
		)
		resetKey = KeyBindingHelper.registerKeyBinding(
			KeyBinding("key.fabrizoom.reset", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.code, CATEGORY)
		)
	}
}