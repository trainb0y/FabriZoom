package io.github.trainb0y.fabrizoom.keybinds

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

//Manages the zoom keybinds themselves.
object ZoomKeybinds {
	//The zoom keybinding, which will be registered.
	@JvmStatic
	val zoomKey = KeyBindingHelper.registerKeyBinding(
		KeyBinding("key.fabrizoom.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "key.fabrizoom.category")
	)

	//The "Decrease Zoom" keybinding.
	@JvmStatic
	val decreaseZoomKey = KeyBindingHelper.registerKeyBinding(
		KeyBinding(
			"key.fabrizoom.decrease_zoom",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_UNKNOWN,
			"key.fabrizoom.category"
		)
	)

	//The "Increase Zoom" keybinding.
	@JvmStatic
	val increaseZoomKey = KeyBindingHelper.registerKeyBinding(
		KeyBinding(
			"key.fabrizoom.increase_zoom",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_UNKNOWN,
			"key.fabrizoom.category"
		)
	)

	//The "Reset Zoom" keybinding.
	@JvmStatic
	val resetZoomKey = KeyBindingHelper.registerKeyBinding(
		KeyBinding("key.fabrizoom.reset_zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.fabrizoom.category")
	)
}