package io.github.trainb0y.fabrizoom

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import org.lwjgl.glfw.GLFW


/**
 * Handles the mod's zoom-related keybinds
 */
object Keybinds {
	/** Translation key for the keybind category */
	private const val CATEGORY = "category.fabrizoom.keybinds"

	/** The primary key for zooming */
	val zoomKey = KeyMapping("key.fabrizoom.zoom", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY)

	/** The key to increase the zoom divisor */
	val increaseKey = KeyMapping("key.fabrizoom.increase", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.value, CATEGORY)

	/** The key to decrease the zoom divisor */
	val decreaseKey = KeyMapping("key.fabrizoom.decrease", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.value, CATEGORY)

	/** The key to reset the zoom divisor */
	val resetKey = KeyMapping("key.fabrizoom.reset", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.value, CATEGORY)
}