package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.FabriZoom.ZOOM_OVERLAY
import io.github.trainb0y.fabrizoom.ZoomLogic.lastZoomOverlayAlpha
import io.github.trainb0y.fabrizoom.ZoomLogic.zoomDivisor
import io.github.trainb0y.fabrizoom.ZoomLogic.zoomOverlayAlpha
import io.github.trainb0y.fabrizoom.config.ConfigHandler
import io.github.trainb0y.fabrizoom.config.ConfigHandler.values
import io.github.trainb0y.fabrizoom.config.ZoomOverlay
import io.github.trainb0y.fabrizoom.config.ZoomTransition
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.util.Mth
import net.minecraft.util.SmoothDouble

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

	private val cursorXZoomSmoother = SmoothDouble()
	private val cursorYZoomSmoother = SmoothDouble()

	var currentZoomFovMultiplier = 1.0f
		private set

	private var lastZoomFovMultiplier = 1.0f
		private set

	/**
	 * Calculates the cursor's X delta while zooming
	 *
	 * @param cursorSensitivity the cursor sensitivity multiplier
	 * @see applyMouseYModifier
	 * @see io.github.trainb0y.fabrizoom.mixin.MouseHandlerMixin.applyZoomChanges
	 */
	@JvmStatic
	fun applyMouseXModifier(
		vanillaCursorDeltaX: Double,
		cursorSensitivity: Double,
		mouseUpdateTimeDelta: Double,
	): Double {
		return if (values.cinematicCameraEnabled) {
			val smoother: Double = mouseUpdateTimeDelta * values.cinematicCameraMultiplier * cursorSensitivity
			this.cursorXZoomSmoother.getNewDeltaValue(vanillaCursorDeltaX, smoother)
		} else {
			this.cursorXZoomSmoother.reset()
			vanillaCursorDeltaX * mouseUpdateTimeDelta * cursorSensitivity * values.mouseSensitivity
		}
	}

	/**
	 * Calculates the cursor's Y delta while zooming
	 *
	 * @param vanillaCursorDeltaY the original Y delta
	 * @param cursorSensitivity the cursor sensitivity multiplier
	 * @see applyMouseXModifier
	 * @see io.github.trainb0y.fabrizoom.mixin.MouseHandlerMixin.applyZoomChanges
	 */
	@JvmStatic
	fun applyMouseYModifier(
		vanillaCursorDeltaY: Double,
		cursorSensitivity: Double,
		mouseUpdateTimeDelta: Double,
	): Double {
		return if (values.cinematicCameraEnabled) {
			val smoother: Double = mouseUpdateTimeDelta * values.cinematicCameraMultiplier * cursorSensitivity
			this.cursorYZoomSmoother.getNewDeltaValue(vanillaCursorDeltaY, smoother)
		} else {
			this.cursorYZoomSmoother.reset()
			vanillaCursorDeltaY * mouseUpdateTimeDelta * cursorSensitivity * values.mouseSensitivity
		}
	}

	@JvmStatic
	fun update(delta: Double) {
		if (!isZooming) {
			this.cursorXZoomSmoother.reset()
			this.cursorYZoomSmoother.reset()
		}

		// calculate zoom fov multiplier
		lastZoomFovMultiplier = currentZoomFovMultiplier

		val zoomMultiplier = if (isZooming) {
			1.0 / zoomDivisor
		} else {
			1.0
		}

		// config values were tuned back when transitions were framerate-dependant (oops)
		// this just to make values from old configs stay sane
		// todo: should probably just migrate all values at some point or something
		val adjDelta = delta.toFloat() * 60

		currentZoomFovMultiplier = when (values.transition) {
			ZoomTransition.NONE -> lastZoomFovMultiplier
			ZoomTransition.LINEAR -> Mth.approach(
				currentZoomFovMultiplier,
				zoomMultiplier.toFloat(),
				zoomMultiplier.coerceIn(values.minimumLinearStep, values.maximumLinearStep).toFloat() * adjDelta
			)

			ZoomTransition.SMOOTH -> currentZoomFovMultiplier + (zoomMultiplier - currentZoomFovMultiplier).toFloat() * values.smoothMultiplier * adjDelta
		}

		lastZoomOverlayAlpha = zoomOverlayAlpha
		zoomOverlayAlpha = when (values.transition) {
			ZoomTransition.SMOOTH -> zoomOverlayAlpha + (zoomMultiplier - zoomOverlayAlpha).toFloat() * values.smoothMultiplier * adjDelta
			ZoomTransition.LINEAR -> {
				val linearStep = (1.0f / zoomDivisor).coerceIn(values.minimumLinearStep, values.maximumLinearStep)
				Mth.approach(zoomOverlayAlpha, zoomMultiplier.toFloat(), linearStep.toFloat() * adjDelta)
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
				if (currentZoomFovMultiplier != 1.0f) vanillaFov * Mth.lerp(
					frameDelta,
					lastZoomFovMultiplier,
					currentZoomFovMultiplier
				).toDouble()
				else vanillaFov
			}
		}

	@JvmStatic
	fun renderZoomOverlay(
		context: GuiGraphics,
		tickDelta: Float,
		gui: Overlay
	) {
		if (currentZoomFovMultiplier >= 0.99) return

		when (values.zoomOverlay) {
			ZoomOverlay.NONE -> {}
			ZoomOverlay.VIGNETTE -> {
				val alpha: Float
				alpha = if (values.transition != ZoomTransition.NONE) {
					// smooth and linear transition
					1 - Mth.lerp(tickDelta, lastZoomOverlayAlpha, zoomOverlayAlpha)
				} else {
					// no transition
					zoomOverlayAlpha
				}
				gui.invokeRenderTextureOverlay(context, ZOOM_OVERLAY, alpha)
			}

			ZoomOverlay.SPYGLASS -> {
				if (isZooming) {
					var scale = 1f / zoomDivisor.toFloat() / currentZoomFovMultiplier
					scale += 0.125f // this is jank, but the vanilla spyglass lerps from 0.5 to 1.125
					scale = Mth.clamp(scale, 0.5f, 1.125f)
					gui.invokeRenderSpyglassOverlay(context, scale)
				}
			}
		}
	}
}