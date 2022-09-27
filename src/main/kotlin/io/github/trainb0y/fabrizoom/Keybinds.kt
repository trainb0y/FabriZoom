package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.config.Config.values
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

/**
 * Handles the mod's zoom-related keybinds
 */
object Keybinds {
	/** Translation key for the keybind category */
	private const val category = "category.fabrizoom.keybinds"

	/** The primary key for zooming */
	private lateinit var zoomKey: KeyBinding

	/** The key to increase the zoom divisor */
	private lateinit var increaseKey: KeyBinding

	/** The key to decrease the zoom divisor */
	private lateinit var decreaseKey: KeyBinding

	/** The key to reset the zoom divisor */
	private lateinit var resetKey: KeyBinding

	/**
	 * Register the keybinds
	 */
	fun register() {
		// can't register and initialize at the same time, because they won't appear in the keybind menu
		zoomKey = KeyBindingHelper.registerKeyBinding(
			KeyBinding("key.fabrizoom.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, category)
		)
		increaseKey = KeyBindingHelper.registerKeyBinding(
			KeyBinding("key.fabrizoom.increase", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.code, category)
		)
		decreaseKey = KeyBindingHelper.registerKeyBinding(
			KeyBinding("key.fabrizoom.decrease", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.code, category)
		)
		resetKey = KeyBindingHelper.registerKeyBinding(
			KeyBinding("key.fabrizoom.reset", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.code, category)
		)
	}

	/**
	 * Update the zoom divisor based on the currently pressed keys
	 * @see ZoomLogic.changeZoomDivisor
	 * @see io.github.trainb0y.fabrizoom.mixin.MouseMixin.onMouseScroll
	 */
	fun onTick() {
		if (ZoomLogic.zooming) {
			if (decreaseKey.isPressed) ZoomLogic.changeZoomDivisor(false)
			if (increaseKey.isPressed) ZoomLogic.changeZoomDivisor(true)
			if (resetKey.isPressed || !zoomKey.isPressed) ZoomLogic.zoomDivisor = values.zoomDivisor
		}
		ZoomLogic.zooming = zoomKey.isPressed
	}
}