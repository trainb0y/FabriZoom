package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.config.Config
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.SmoothUtil
import net.minecraft.util.math.MathHelper

object ZoomLogic {
	@JvmStatic
	var zooming = false

	@JvmStatic
	var zoomDivisor = Config.zoomDivisor

	private val cursorXZoomSmoother = SmoothUtil()
	private val cursorYZoomSmoother = SmoothUtil()

	var currentZoomFovMultiplier = 1.0f
	var lastZoomFovMultiplier = 1.0f

	@JvmStatic
	fun applyMouseXModifier(
		cursorDeltaX: Double,
		cursorSensitivity: Double,
		mouseUpdateTimeDelta: Double,
	): Double {
		return if (!Config.cinematicCameraEnabled) {
			this.cursorXZoomSmoother.clear()
			cursorDeltaX * mouseUpdateTimeDelta * cursorSensitivity * Config.mouseSensitivity
		} else {
			val smoother: Double = mouseUpdateTimeDelta * Config.cinematicCameraMultiplier * cursorSensitivity
			this.cursorXZoomSmoother.smooth(cursorDeltaX, smoother)
		}
	}

	@JvmStatic
	fun applyMouseYModifier(
		cursorDeltaY: Double,
		cursorSensitivity: Double,
		mouseUpdateTimeDelta: Double,
	): Double {
		return if (!Config.cinematicCameraEnabled) {
			this.cursorYZoomSmoother.clear()
			cursorDeltaY * mouseUpdateTimeDelta * cursorSensitivity * Config.mouseSensitivity
		} else {
			val smoother: Double = mouseUpdateTimeDelta * Config.cinematicCameraMultiplier * cursorSensitivity
			this.cursorYZoomSmoother.smooth(cursorDeltaY, smoother)
		}
	}

	@JvmStatic
	fun tick(client: MinecraftClient) {
		if (!zooming) {
			this.cursorXZoomSmoother.clear()
			this.cursorYZoomSmoother.clear()
		}

		// update zoom fov multiplier
		lastZoomFovMultiplier = currentZoomFovMultiplier

		val zoomMultiplier = if (zooming) { 1.0 / zoomDivisor } else { 1.0 }

		this.currentZoomFovMultipler = when (Config.transition) {
			Config.Transition.LINEAR -> {
				this.currentZoomFovMultiplier = MathHelper.stepTowards(
					currentZoomFovMultiplier,
					zoomMultiplier.toFloat(),
					zoomMultiplier.coerceIn(Config.minimumLinearStep, Config.maximumLinearStep).toFloat()
				)
			}
			Config.Transition.SMOOTH -> {} //TODO: SMOOTH
			Config.Transition.NONE -> {}
		}
	}

	//The method used for changing the zoom divisor, used by zoom scrolling and the keybinds.
	@JvmStatic
	fun changeZoomDivisor(increase: Boolean) {
		val changedZoomDivisor =
			if (increase) zoomDivisor + Config.scrollStep
			else zoomDivisor - Config.scrollStep

		zoomDivisor = changedZoomDivisor.coerceIn(Config.minimumZoomDivisor, Config.maximumZoomDivisor)
	}

	@JvmStatic
	fun getFov(fov: Double, delta: Float): Double =
		when (Config.transition) {
			Config.Transition.NONE -> fov / zoomDivisor
			Config.Transition.LINEAR -> {
				if (currentZoomFovMultiplier != 1.0f) fov * MathHelper.lerp(delta, lastZoomFovMultiplier, currentZoomFovMultiplier).toDouble()
				else fov
			}
			Config.Transition.SMOOTH -> { fov / zoomDivisor} //TODO: SMOOTH
		}
}