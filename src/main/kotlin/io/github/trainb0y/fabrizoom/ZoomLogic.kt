package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.config.Config
import io.github.trainb0y.fabrizoom.config.Config.values
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
	var zooming = false

	/** The current zoom divisor (amount to zoom in) */
	@JvmStatic
	var zoomDivisor = values.zoomDivisor

	private val cursorXZoomSmoother = SmoothUtil()
	private val cursorYZoomSmoother = SmoothUtil()

	var currentZoomFovMultiplier = 1.0f
		private set

	var lastZoomFovMultiplier = 1.0f
		private set

	/**
	 * Calculates the cursor's X delta while zooming
	 *
	 * @param cursorDeltaX the original X delta
	 * @param cursorSensitivity the cursor sensitivity multiplier
	 * @param mouseUpdateTimeDelta the time since the last mouse update
	 * @see applyMouseYModifier
	 * @see io.github.trainb0y.fabrizoom.mixin.MouseMixin.applyZoomChanges
	 */
	@JvmStatic
	fun applyMouseXModifier(
		cursorDeltaX: Double,
		cursorSensitivity: Double,
		mouseUpdateTimeDelta: Double,
	): Double {
		return if (values.cinematicCameraEnabled) {
			val smoother: Double = mouseUpdateTimeDelta * values.cinematicCameraMultiplier * cursorSensitivity
			this.cursorXZoomSmoother.smooth(cursorDeltaX, smoother)
		} else {
			this.cursorXZoomSmoother.clear()
			cursorDeltaX * mouseUpdateTimeDelta * cursorSensitivity * values.mouseSensitivity
		}
	}

	/**
	 * Calculates the cursor's Y delta while zooming
	 *
	 * @param cursorDeltaY the original Y delta
	 * @param cursorSensitivity the cursor sensitivity multiplier
	 * @param mouseUpdateTimeDelta the time since the last mouse update
	 * @see applyMouseXModifier
	 * @see io.github.trainb0y.fabrizoom.mixin.MouseMixin.applyZoomChanges
	 */
	@JvmStatic
	fun applyMouseYModifier(
		cursorDeltaY: Double,
		cursorSensitivity: Double,
		mouseUpdateTimeDelta: Double,
	): Double {
		return if (values.cinematicCameraEnabled) {
			val smoother: Double = mouseUpdateTimeDelta * values.cinematicCameraMultiplier * cursorSensitivity
			this.cursorYZoomSmoother.smooth(cursorDeltaY, smoother)
		} else {
			this.cursorYZoomSmoother.clear()
			cursorDeltaY * mouseUpdateTimeDelta * cursorSensitivity * values.mouseSensitivity
		}
	}

	/**
	 * Update the current zoom state, including the overlay alpha and the zoom FOV multipliers
	 */
	@JvmStatic
	fun tick() {
		if (!zooming) {
			this.cursorXZoomSmoother.clear()
			this.cursorYZoomSmoother.clear()
		}

		// calculate zoom fov multiplier
		lastZoomFovMultiplier = currentZoomFovMultiplier

		val zoomMultiplier = if (zooming) {
			1.0 / zoomDivisor
		} else {
			1.0
		}

		currentZoomFovMultiplier = when (values.transition) {
			Config.Transition.NONE -> lastZoomFovMultiplier
			Config.Transition.LINEAR -> MathHelper.stepTowards(
				currentZoomFovMultiplier,
				zoomMultiplier.toFloat(),
				zoomMultiplier.coerceIn(values.minimumLinearStep, values.maximumLinearStep).toFloat()
			)

			Config.Transition.SMOOTH -> currentZoomFovMultiplier + (zoomMultiplier - currentZoomFovMultiplier).toFloat() * values.smoothMultiplier
		}

		// Calculate zoom overlay alpha
		lastZoomOverlayAlpha = zoomOverlayAlpha
		zoomOverlayAlpha = when (values.transition) {
			Config.Transition.SMOOTH -> zoomOverlayAlpha + (zoomMultiplier - zoomOverlayAlpha).toFloat() * values.smoothMultiplier
			Config.Transition.LINEAR -> {
				val linearStep = (1.0f / zoomDivisor).coerceIn(values.minimumLinearStep, values.maximumLinearStep)
				MathHelper.stepTowards(zoomOverlayAlpha, zoomMultiplier.toFloat(), linearStep.toFloat())
			}

			Config.Transition.NONE -> if (zooming) {
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
	 * @see Config.values
	 * @see io.github.trainb0y.fabrizoom.config.ConfigurableValues.scrollStep
	 */
	@JvmStatic
	fun changeZoomDivisor(increase: Boolean) {
		val changedZoomDivisor =
			if (increase) zoomDivisor + values.scrollStep
			else zoomDivisor - values.scrollStep

		zoomDivisor = changedZoomDivisor.coerceIn(values.minimumZoomDivisor, values.maximumZoomDivisor)
	}

	/**
	 * @return the zoom-modified client FOV
	 * @param fov the vanilla FOV
	 * @param delta the frame delta
	 */
	@JvmStatic
	fun getFov(fov: Double, delta: Float): Double =
		when (values.transition) {
			Config.Transition.NONE -> if (zooming) {
				fov / zoomDivisor
			} else {
				fov
			}

			else -> {
				if (currentZoomFovMultiplier != 1.0f) fov * MathHelper.lerp(
					delta,
					lastZoomFovMultiplier,
					currentZoomFovMultiplier
				).toDouble()
				else fov
			}
		}
}