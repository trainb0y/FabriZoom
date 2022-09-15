package io.github.trainb0y.fabrizoom.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
class ConfigurableValues {
	// Normal camera mouse mode
	var mouseSensitivity = 30 // reasonable 10-40 // todo: force in gui

	// Cinematic camera mouse mode
	var cinematicCameraEnabled = false
	var cinematicCameraMultiplier = 4.0

	// Amount to initially zoom
	var zoomDivisor = 4.0
	var minimumZoomDivisor = 4.0
	var maximumZoomDivisor = 50.0


	// Linear zoom transition
	var minimumLinearStep = 0.125
	var maximumLinearStep = 0.25

	// Smooth zoom transition
	var smoothMultiplier = 0.3f

	// Zoom scrolling step
	var scrollStep = 1.0

	var transition = Config.Transition.LINEAR
}