package io.github.trainb0y.fabrizoom

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import org.lwjgl.glfw.GLFW


/**
 * Handles the mod's zoom-related keybinds
 */
object Keybinds {
	/** Translation key for the keybind category */
	private const val CATEGORY = "category.fabrizoom.keybinds"

	/** The primary key for zooming */
	lateinit var zoomKey: KeyMapping

	/** The key to increase the zoom divisor */
	lateinit var increaseKey: KeyMapping

	/** The key to decrease the zoom divisor */
	lateinit var decreaseKey: KeyMapping

	/** The key to reset the zoom divisor */
	lateinit var resetKey: KeyMapping

	fun registerKeybinds() {
		// can't register and initialize at the same time, because they won't appear in the keybind menu
		zoomKey = KeyBindingHelper.registerKeyBinding(
			KeyMapping("key.fabrizoom.zoom", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY)
		)
		increaseKey = KeyBindingHelper.registerKeyBinding(
			KeyMapping("key.fabrizoom.increase", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.value, CATEGORY)
		)
		decreaseKey = KeyBindingHelper.registerKeyBinding(
			KeyMapping("key.fabrizoom.decrease", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.value, CATEGORY)
		)
		resetKey = KeyBindingHelper.registerKeyBinding(
			KeyMapping("key.fabrizoom.reset", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.value, CATEGORY)
		)
	}
}