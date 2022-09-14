package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.config.Config
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object Keybinds {
	private const val category = "category.fabrizoom.keybinds"

	private lateinit var zoomKey: KeyBinding
	private lateinit var increaseKey: KeyBinding
	private lateinit var decreaseKey: KeyBinding
	private lateinit var resetKey: KeyBinding

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

	
	fun onTick() {
		if (ZoomLogic.zooming) {
			if (decreaseKey.isPressed) ZoomLogic.changeZoomDivisor(false)
			if (increaseKey.isPressed) ZoomLogic.changeZoomDivisor(true)
			if (resetKey.isPressed || !zoomKey.isPressed) ZoomLogic.zoomDivisor = Config.zoomDivisor
		}
		ZoomLogic.zooming = zoomKey.isPressed
	}
}