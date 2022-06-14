package io.github.joaoh1.okzoomer.client.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

//Manages the zoom keybinds themselves.
public class ZoomKeybinds {
	//The zoom keybinding, which will be registered.
	public static final KeyBinding zoomKey = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.okzoomer.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "key.okzoomer.category"));

	//The "Decrease Zoom" keybinding.
	public static final KeyBinding decreaseZoomKey = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.okzoomer.decrease_zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.okzoomer.category"));

	//The "Increase Zoom" keybinding.
	public static final KeyBinding increaseZoomKey = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.okzoomer.increase_zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.okzoomer.category"));

	//The "Reset Zoom" keybinding.
	public static final KeyBinding resetZoomKey = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.okzoomer.reset_zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "key.okzoomer.category"));

}
