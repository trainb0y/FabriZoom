package io.github.trainb0y.fabrizoom.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
object Config {

	@Suppress("Unused")
	private const val version = "1" // config version, doesn't necc. match mod version

	// Normal camera mouse mode
	var mouseSensitivity = 30 // reasonable 10-40 // todo: force in gui

	// Cinematic camera mouse mode
	@JvmStatic
	var cinematicCameraEnabled = false
	var cinematicCameraMultiplier = 4.0

	// Amount to initially zoom
	@JvmStatic
	var zoomDivisor = 4.0
	var minimumZoomDivisor = 4.0
	var maximumZoomDivisor = 50.0


	// Linear zoom transition
	var zoomTransition = true
	var minimumLinearStep = 0.125
	var maximumLinearStep = 0.25

	// Smooth zoom transition
	var smoothMultiplier = 0.3f

	// Zoom scrolling step
	var scrollStep = 1.0

	var transition = Transition.LINEAR

	enum class Transition(val key: String) {
		LINEAR("transition.fabrihud.linear"),
		SMOOTH("transition.fabrihud.smooth"),
		NONE("transition.fabrihud.none");

		fun next(): Transition { // used for toggle button in gui
			return try {
				Transition.values()[this.ordinal + 1]
			} catch (e: IndexOutOfBoundsException) {
				Transition.values().first()
			}
		}
	}

	fun saveConfig() {}
	fun loadConfig() {}
	fun applyDefaultConfig() {}
}