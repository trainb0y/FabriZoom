package io.github.trainb0y.fabrizoom.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
object Config {

	@Suppress("Unused")
	private const val version = "1" // config version, doesn't necc. match mod version

	// Normal camera mouse mode
	var mouseSensitivity = 30 // reasonable 10-40

	// Cinematic camera mouse mode
	@JvmStatic
	var cinematicCameraEnabled = false
	var cinematicCameraMultiplier = 4.0

	// Amount to initially zoom
	@JvmStatic
	var zoomDivisor = 4.0

	// Linear zoom transition
	var zoomTransition = true
	var minimumLinearStep = 0.125
	var maximumLinearStep = 0.25

	// Zoom scrolling step
	var scrollStep = 1.0

	var minimumZoomDivisor = 4.0
	var maximumZoomDivisor = 50.0

	fun saveConfig() {}
	fun loadConfig() {}
	fun applyDefaultConfig() {}
}