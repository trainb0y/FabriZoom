package io.github.trainb0y.fabrizoom.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
object Config {

	private const val version = "1" // config version, doesn't necc. match mod version

	@JvmStatic
	var cinematicCameraMultiplier = 4.0

	@JvmStatic
	var zoomTransition = true

	@JvmStatic
	var zoomDivisor = 4.0

	// Zoom transition stepping
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