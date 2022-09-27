package io.github.trainb0y.fabrizoom.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class ConfigurableValues(
	// Normal camera mouse mode
	var mouseSensitivity: Int, // reasonable 10-40

	// Cinematic camera mouse mode
	var cinematicCameraEnabled: Boolean,
	var cinematicCameraMultiplier: Double,

	// Amount to initially zoom
	var zoomDivisor: Double,
	var minimumZoomDivisor: Double,
	var maximumZoomDivisor: Double,


	// Linear zoom transition
	var minimumLinearStep: Double,
	var maximumLinearStep: Double,

	// Smooth zoom transition
	var smoothMultiplier: Float,

	// Zoom scrolling step
	var scrollStep: Double,

	// Zoom Overlay
	var zoomOverlayEnabled: Boolean,

	var transition: Config.Transition
)