package io.github.trainb0y.fabrizoom.utils;

import io.github.trainb0y.fabrizoom.config.Config;
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


	//The method used for changing the zoom divisor, used by zoom scrolling and the keybinds.
	public static void changeZoomDivisor(boolean increase) {
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

		if (lesserChangedZoomDivisor <= Config.values.zoomDivisor)
			changedZoomDivisor = lesserChangedZoomDivisor;


		if (changedZoomDivisor >= Config.values.minimumZoomDivisor &&
				changedZoomDivisor <= Config.values.maximumZoomDivisor)
			zoomDivisor = changedZoomDivisor;
	}

	//The method used by both the "Reset Zoom" keybind and the "Reset Zoom With Mouse" tweak.
	public static void resetZoomDivisor() {
		zoomDivisor = Config.values.zoomDivisor;
		lastZoomState = true;
	}

	//The equivalent of GameRenderer's updateFovMultiplier but for zooming. Used by zoom transitions.
	public static void updateZoomFovMultiplier() {
		float zoomMultiplier = 1.0F;
		double dividedZoomMultiplier = 1.0 / ZoomUtils.zoomDivisor;
		if (ZoomUtils.zoomState) zoomMultiplier = (float) dividedZoomMultiplier;

		lastZoomFovMultiplier = zoomFovMultiplier;

		if (Config.features.zoomTransition.equals(Config.FeaturesGroup.ZoomTransitionOptions.SMOOTH)) {
			zoomFovMultiplier += (zoomMultiplier - zoomFovMultiplier) * Config.values.smoothMultiplier;
		} else if (Config.features.zoomTransition.equals(Config.FeaturesGroup.ZoomTransitionOptions.LINEAR)) {
			double linearStep = Math.max(
					Config.values.minimumLinearStep,
					Math.min(dividedZoomMultiplier, Config.values.maximumLinearStep)
			);
			zoomFovMultiplier = MathHelper.stepTowards(zoomFovMultiplier, zoomMultiplier, (float) linearStep);
		}
	}
}
