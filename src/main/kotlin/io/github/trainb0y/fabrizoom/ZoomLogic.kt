package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.config.ConfigHandler
import io.github.trainb0y.fabrizoom.config.ConfigHandler.values
import io.github.trainb0y.fabrizoom.config.ZoomTransition
import net.minecraft.client.util.SmoothUtil
import net.minecraft.util.math.MathHelper

/**
 * Handles the zoom logic
 */
object ZoomLogic {

	/**
	 * The current alpha of the zoom overlay
	 * @see lastZoomOverlayAlpha
	 */
	@JvmStatic
	var zoomOverlayAlpha: Float = 0f
		private set

	/**
	 * The [zoomOverlayAlpha] before the last [tick]
	 */
	@JvmStatic
	var lastZoomOverlayAlpha: Float = zoomOverlayAlpha
		private set

	/** Whether we are currently zooming */
	@JvmStatic
	var isZooming = false

	/** The current zoom divisor (amount to zoom in) */
	@JvmStatic
	var zoomDivisor = values.zoomDivisor

	private val cursorXZoomSmoother = SmoothUtil()
	private val cursorYZoomSmoother = SmoothUtil()

	var currentZoomFovMultiplier = 1.0f
		private set

	private var lastZoomFovMultiplier = 1.0f
		private set

	/**
	 * Calculates the cursor's X delta while zooming
	 *
	 * @param cursorSensitivity the cursor sensitivity multiplier
	 * @see applyMouseYModifier
	 * @see io.github.trainb0y.fabrizoom.mixin.MouseMixin.applyZoomChanges
	 */
	@JvmStatic
	fun applyMouseXModifier(
		vanillaCursorDeltaX: Double,
		cursorSensitivity: Double,
		mouseUpdateTimeDelta: Double,
	): Double {
		return if (values.cinematicCameraEnabled) {
			val smoother: Double = mouseUpdateTimeDelta * values.cinematicCameraMultiplier * cursorSensitivity
			this.cursorXZoomSmoother.smooth(vanillaCursorDeltaX, smoother)
		} else {
			this.cursorXZoomSmoother.clear()
			vanillaCursorDeltaX * mouseUpdateTimeDelta * cursorSensitivity * values.mouseSensitivity
		}
	}

	/**
	 * Calculates the cursor's Y delta while zooming
	 *
	 * @param vanillaCursorDeltaY the original Y delta
	 * @param cursorSensitivity the cursor sensitivity multiplier
	 * @see applyMouseXModifier
	 * @see io.github.trainb0y.fabrizoom.mixin.MouseMixin.applyZoomChanges
	 */
	@JvmStatic
	fun applyMouseYModifier(
		vanillaCursorDeltaY: Double,
		cursorSensitivity: Double,
		mouseUpdateTimeDelta: Double,
	): Double {
		return if (values.cinematicCameraEnabled) {
			val smoother: Double = mouseUpdateTimeDelta * values.cinematicCameraMultiplier * cursorSensitivity
			this.cursorYZoomSmoother.smooth(vanillaCursorDeltaY, smoother)
		} else {
			this.cursorYZoomSmoother.clear()
			vanillaCursorDeltaY * mouseUpdateTimeDelta * cursorSensitivity * values.mouseSensitivity
		}
	}

	@JvmStatic
	fun tick() {
		if (!isZooming) {
			this.cursorXZoomSmoother.clear()
			this.cursorYZoomSmoother.clear()
		}

		// calculate zoom fov multiplier
		lastZoomFovMultiplier = currentZoomFovMultiplier

		val zoomMultiplier = if (isZooming) {
			1.0 / zoomDivisor
		} else {
			1.0
		}

		currentZoomFovMultiplier = when (values.transition) {
			ZoomTransition.NONE -> lastZoomFovMultiplier
			ZoomTransition.LINEAR -> MathHelper.stepTowards(
				currentZoomFovMultiplier,
				zoomMultiplier.toFloat(),
				zoomMultiplier.coerceIn(values.minimumLinearStep, values.maximumLinearStep).toFloat()
			)
			ZoomTransition.SMOOTH -> currentZoomFovMultiplier + (zoomMultiplier - currentZoomFovMultiplier).toFloat() * values.smoothMultiplier
		}

		lastZoomOverlayAlpha = zoomOverlayAlpha
		zoomOverlayAlpha = when (values.transition) {
			ZoomTransition.SMOOTH -> zoomOverlayAlpha + (zoomMultiplier - zoomOverlayAlpha).toFloat() * values.smoothMultiplier
			ZoomTransition.LINEAR -> {
				val linearStep = (1.0f / zoomDivisor).coerceIn(values.minimumLinearStep, values.maximumLinearStep)
				MathHelper.stepTowards(zoomOverlayAlpha, zoomMultiplier.toFloat(), linearStep.toFloat())
			}
			ZoomTransition.NONE -> if (isZooming) {
				1f
			} else {
				0f
			}
		}
	}

	/**
	 * Increase or decrease the [zoomDivisor] by the scroll step
	 * Used by the keybinds and mouse zoom scrolling
	 *
	 * @param increase whether to increase or decrease the zoom
	 * @see ConfigHandler.values
	 * @see io.github.trainb0y.fabrizoom.config.ConfigurableValues.scrollStep
	 */
	@JvmStatic
	fun changeZoomDivisor(increase: Boolean) {
		val changedZoomDivisor =
			if (increase) zoomDivisor + values.scrollStep
			else zoomDivisor - values.scrollStep

		zoomDivisor = changedZoomDivisor.coerceIn(values.minimumZoomDivisor, values.maximumZoomDivisor)
	}

	/** @return the zoom-modified client FOV */
	@JvmStatic
	fun getZoomFov(vanillaFov: Double, frameDelta: Float): Double =
		when (values.transition) {
			ZoomTransition.NONE -> if (isZooming) {
				vanillaFov / zoomDivisor
			} else {
				vanillaFov
			}

			else -> {
				if (currentZoomFovMultiplier != 1.0f) vanillaFov * MathHelper.lerp(
					frameDelta,
					lastZoomFovMultiplier,
					currentZoomFovMultiplier
				).toDouble()
				else vanillaFov
			}
		}
}