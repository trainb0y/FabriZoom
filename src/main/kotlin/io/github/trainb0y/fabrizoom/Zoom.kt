package io.github.trainb0y.fabrizoom

import io.github.trainb0y.fabrizoom.config.Config
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.SmoothUtil
import net.minecraft.util.math.MathHelper

object Zoom {
	@JvmStatic
	var zooming = false

	@JvmStatic
	var zoomDivisor = Config.zoomDivisor

	private val cursorXZoomSmoother = SmoothUtil()
	private val cursorYZoomSmoother = SmoothUtil()
	private var cinematicCameraEnabled = false

	@JvmStatic
	var currentZoomFovMultiplier = 1.0f
	@JvmStatic
	var lastZoomFovMultiplier = 1.0f

	@JvmStatic
	fun applyMouseXModifier(
		cursorDeltaX: Double,
		cursorSensitivity: Double,
		mouseUpdateTimeDelta: Double,
	): Double {
		if (this.cinematicCameraEnabled) {
			this.cursorXZoomSmoother.clear()
			return cursorDeltaX
		}
		val smoother: Double = mouseUpdateTimeDelta * Config.cinematicCameraMultiplier * cursorSensitivity
		return this.cursorXZoomSmoother.smooth(cursorDeltaX, smoother)
	}

	@JvmStatic
	fun applyMouseYModifier(
		cursorDeltaY: Double,
		cursorSensitivity: Double,
		mouseUpdateTimeDelta: Double,
	): Double {
		if (this.cinematicCameraEnabled) {
			this.cursorYZoomSmoother.clear()
			return cursorDeltaY
		}
		val smoother: Double = mouseUpdateTimeDelta * Config.cinematicCameraMultiplier * cursorSensitivity
		return this.cursorYZoomSmoother.smooth(cursorDeltaY, smoother)
	}

	@JvmStatic
	fun tick(client: MinecraftClient) {
		this.cinematicCameraEnabled = client.options.smoothCameraEnabled
		if (!zooming) {
			this.cursorXZoomSmoother.clear()
			this.cursorYZoomSmoother.clear()
		}
	}

	//The equivalent of GameRenderer's updateFovMultiplier but for zooming. Used by zoom transitions.
	@JvmStatic
	fun updateZoomFovMultiplier() {
		val zoomMultiplier = if (zooming) { 1.0 / zoomDivisor } else { 1.0 }

		lastZoomFovMultiplier = currentZoomFovMultiplier

		val linearStep = MathHelper.clamp(zoomMultiplier, 0.125, 0.25)// minimumLinearStep, maximumLinearStep

		this.currentZoomFovMultiplier = MathHelper.stepTowards(currentZoomFovMultiplier, zoomMultiplier.toFloat(), linearStep.toFloat())
	}

	//The method used for changing the zoom divisor, used by zoom scrolling and the keybinds.
	@JvmStatic
	fun changeZoomDivisor(increase: Boolean) {
		var changedZoomDivisor: Double
		val lesserChangedZoomDivisor: Double
		if (increase) {
			changedZoomDivisor = zoomDivisor + Config.scrollStep
			lesserChangedZoomDivisor = zoomDivisor + Config.lesserScrollStep
		} else {
			changedZoomDivisor = zoomDivisor - Config.scrollStep
			lesserChangedZoomDivisor = zoomDivisor - Config.lesserScrollStep
		}
		if (lesserChangedZoomDivisor <= Config.zoomDivisor) changedZoomDivisor = lesserChangedZoomDivisor
		if (changedZoomDivisor >= Config.minimumZoomDivisor &&
			changedZoomDivisor <= Config.maximumZoomDivisor
		) zoomDivisor = changedZoomDivisor
	}
}