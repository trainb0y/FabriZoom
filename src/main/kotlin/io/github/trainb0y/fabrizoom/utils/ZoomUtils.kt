package io.github.trainb0y.fabrizoom.utils

import io.github.trainb0y.fabrizoom.config.Config
import net.minecraft.util.math.MathHelper
import org.apache.logging.log4j.LogManager

//The class that contains most of the logic behind the zoom itself.
object ZoomUtils {
	//The logger, used everywhere to print messages to the console.
	val modLogger = LogManager.getFormatterLogger("Ok Zoomer")

	//The zoom signal, which is managed in an event and used by other mixins.
	@JvmField
	var zoomState = false

	//Used for post-zoom actions like updating the terrain.
	@JvmField
	var lastZoomState = false

	//The zoom divisor, managed by the zoom press and zoom scrolling. Used by other mixins.
	@JvmField
	var zoomDivisor = Config.values.zoomDivisor

	//The zoom FOV multipliers. Used by the GameRenderer mixin.
	@JvmField
	var zoomFovMultiplier = 1.0f
	@JvmField
	var lastZoomFovMultiplier = 1.0f

	//The method used for changing the zoom divisor, used by zoom scrolling and the keybinds.
	@JvmStatic
	fun changeZoomDivisor(increase: Boolean) {
		var changedZoomDivisor: Double
		val lesserChangedZoomDivisor: Double
		if (increase) {
			changedZoomDivisor = zoomDivisor + Config.values.scrollStep
			lesserChangedZoomDivisor = zoomDivisor + Config.values.lesserScrollStep
		} else {
			changedZoomDivisor = zoomDivisor - Config.values.scrollStep
			lesserChangedZoomDivisor = zoomDivisor - Config.values.lesserScrollStep
			lastZoomState = true
		}
		if (lesserChangedZoomDivisor <= Config.values.zoomDivisor) changedZoomDivisor = lesserChangedZoomDivisor
		if (changedZoomDivisor >= Config.values.minimumZoomDivisor &&
			changedZoomDivisor <= Config.values.maximumZoomDivisor
		) zoomDivisor = changedZoomDivisor
	}

	//The method used by both the "Reset Zoom" keybind and the "Reset Zoom With Mouse" tweak.
	@JvmStatic
	fun resetZoomDivisor() {
		zoomDivisor = Config.values.zoomDivisor
		lastZoomState = true
	}

	//The equivalent of GameRenderer's updateFovMultiplier but for zooming. Used by zoom transitions.
	@JvmStatic
	fun updateZoomFovMultiplier() {
		var zoomMultiplier = 1.0f
		val dividedZoomMultiplier = 1.0 / zoomDivisor
		if (zoomState) zoomMultiplier = dividedZoomMultiplier.toFloat()
		lastZoomFovMultiplier = zoomFovMultiplier
		if (Config.features.zoomTransition == Config.FeaturesGroup.ZoomTransitionOptions.SMOOTH) {
			zoomFovMultiplier += ((zoomMultiplier - zoomFovMultiplier) * Config.values.smoothMultiplier).toFloat()
		} else if (Config.features.zoomTransition == Config.FeaturesGroup.ZoomTransitionOptions.LINEAR) {
			val linearStep = Math.max(
				Config.values.minimumLinearStep,
				Math.min(dividedZoomMultiplier, Config.values.maximumLinearStep)
			)
			zoomFovMultiplier = MathHelper.stepTowards(zoomFovMultiplier, zoomMultiplier, linearStep.toFloat())
		}
	}
}