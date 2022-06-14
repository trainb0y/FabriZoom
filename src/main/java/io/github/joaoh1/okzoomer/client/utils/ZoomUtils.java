package io.github.joaoh1.okzoomer.client.utils;

import io.github.joaoh1.okzoomer.client.config.Config;
import io.github.joaoh1.okzoomer.client.keybinds.ZoomKeybinds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//The class that contains most of the logic behind the zoom itself.
public class ZoomUtils {
	//The logger, used everywhere to print messages to the console.
	public static final Logger modLogger = LogManager.getFormatterLogger("Ok Zoomer");

	//The zoom signal, which is managed in an event and used by other mixins.
	public static boolean zoomState = false;

	//Used for post-zoom actions like updating the terrain.
	public static boolean lastZoomState = false;

	//The zoom divisor, managed by the zoom press and zoom scrolling. Used by other mixins.
	public static double zoomDivisor = Config.values.zoomDivisor;

	//The zoom FOV multipliers. Used by the GameRenderer mixin.
	public static float zoomFovMultiplier = 1.0F;
	public static float lastZoomFovMultiplier = 1.0F;

	//The zoom overlay's alpha. Used by the InGameHud mixin.
	public static float zoomOverlayAlpha = 0.0F;
	public static float lastZoomOverlayAlpha = 0.0F;

	//The method used for changing the zoom divisor, used by zoom scrolling and the keybinds.
	public static final void changeZoomDivisor(boolean increase) {
		double changedZoomDivisor;
		double lesserChangedZoomDivisor;

		if (increase) {
			changedZoomDivisor = zoomDivisor + Config.values.scrollStep;
			lesserChangedZoomDivisor = zoomDivisor + Config.values.lesserScrollStep;
		} else {
			changedZoomDivisor = zoomDivisor - Config.values.scrollStep;
			lesserChangedZoomDivisor = zoomDivisor - Config.values.lesserScrollStep;
			lastZoomState = true;
		}

		if (lesserChangedZoomDivisor <= Config.values.zoomDivisor) {
			changedZoomDivisor = lesserChangedZoomDivisor;
		}

		if (changedZoomDivisor >= Config.values.minimumZoomDivisor) {
			if (changedZoomDivisor <= Config.values.maximumZoomDivisor) {
				zoomDivisor = changedZoomDivisor;
			}
		}
	}

	//The method used by both the "Reset Zoom" keybind and the "Reset Zoom With Mouse" tweak.
	public static final void resetZoomDivisor() {
		zoomDivisor = Config.values.zoomDivisor;
		lastZoomState = true;
	}

	//The method used for unbinding the "Save Toolbar Activator"
	public static final void unbindConflictingKey(MinecraftClient client, boolean userPrompted) {
		if (ZoomKeybinds.zoomKey.isDefault()) {
			if (client.options.keySaveToolbarActivator.isDefault()) {
				if (userPrompted) {
					ZoomUtils.modLogger.info("[Ok Zoomer] The \"Save Toolbar Activator\" keybind was occupying C! Unbinding...");
				} else {
					ZoomUtils.modLogger.info("[Ok Zoomer] The \"Save Toolbar Activator\" keybind was occupying C! Unbinding... This process won't be repeated until specified in the config.");
				}
				client.options.keySaveToolbarActivator.setBoundKey(InputUtil.UNKNOWN_KEY);
				client.options.write();
				KeyBinding.updateKeysByCode();
			} else {
				ZoomUtils.modLogger.info("[Ok Zoomer] No conflicts with the \"Save Toolbar Activator\" keybind was found!");
			}
		}
	}

	//The equivalent of GameRenderer's updateFovMultiplier but for zooming. Used by zoom transitions.
	public static final void updateZoomFovMultiplier() {
		float zoomMultiplier = 1.0F;
		double dividedZoomMultiplier = 1.0 / ZoomUtils.zoomDivisor;

		if (ZoomUtils.zoomState) {
			zoomMultiplier = (float) dividedZoomMultiplier;
		}

		lastZoomFovMultiplier = zoomFovMultiplier;

		if (Config.features.zoomTransition.equals(Config.FeaturesGroup.ZoomTransitionOptions.SMOOTH)) {
			zoomFovMultiplier += (zoomMultiplier - zoomFovMultiplier) * Config.values.smoothMultiplier;
		} else if (Config.features.zoomTransition.equals(Config.FeaturesGroup.ZoomTransitionOptions.LINEAR)) {
			double linearStep = dividedZoomMultiplier;
			if (linearStep < Config.values.minimumLinearStep) {
				linearStep = Config.values.minimumLinearStep;
			}
			if (linearStep > Config.values.maximumLinearStep) {
				linearStep = Config.values.maximumLinearStep;
			}
			zoomFovMultiplier = MathHelper.stepTowards(zoomFovMultiplier, zoomMultiplier, (float) linearStep);
		}
	}

	//Handles the zoom overlay transparency with transitions. Used by zoom overlay.
	public static final void updateZoomOverlayAlpha() {
		float zoomMultiplier = 0.0F;

		if (ZoomUtils.zoomState) {
			zoomMultiplier = 1.0F;
		}

		lastZoomOverlayAlpha = zoomOverlayAlpha;

		if (Config.features.zoomTransition.equals(Config.FeaturesGroup.ZoomTransitionOptions.SMOOTH)) {
			zoomOverlayAlpha += (zoomMultiplier - zoomOverlayAlpha) * Config.values.smoothMultiplier;
		} else if (Config.features.zoomTransition.equals(Config.FeaturesGroup.ZoomTransitionOptions.LINEAR)) {
			double linearStep = 1.0F / zoomDivisor;
			if (linearStep < Config.values.minimumLinearStep) {
				linearStep = Config.values.minimumLinearStep;
			}
			if (linearStep > Config.values.maximumLinearStep) {
				linearStep = Config.values.maximumLinearStep;
			}
			zoomOverlayAlpha = MathHelper.stepTowards(zoomOverlayAlpha, zoomMultiplier, (float) linearStep);
		}
	}
}
