package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.config.Config
import io.github.trainb0y.fabrizoom.config.Config.values
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.SmoothUtil
import net.minecraft.util.math.MathHelper

object ZoomLogic {
	@JvmStatic
	var zoomOverlayAlpha: Float = 0f
		private set;

	@JvmStatic
	var lastZoomOverlayAlpha: Float = zoomOverlayAlpha
		private set;

	@JvmStatic
	var zooming = false

	@JvmStatic
	var zoomDivisor = values.zoomDivisor

	private val cursorXZoomSmoother = SmoothUtil()
	private val cursorYZoomSmoother = SmoothUtil()

	var currentZoomFovMultiplier = 1.0f
		private set;

	var lastZoomFovMultiplier = 1.0f
		private set;

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

	@JvmStatic
	fun tick(client: MinecraftClient) {
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

	//The method used for changing the zoom divisor, used by zoom scrolling and the keybinds.
	@JvmStatic
	fun changeZoomDivisor(increase: Boolean) {
		val changedZoomDivisor =
			if (increase) zoomDivisor + values.scrollStep
			else zoomDivisor - values.scrollStep

		zoomDivisor = changedZoomDivisor.coerceIn(values.minimumZoomDivisor, values.maximumZoomDivisor)
	}

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