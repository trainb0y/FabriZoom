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
	var minimumZoomDivisor = 3.0
	var maximumZoomDivisor = 20.0


	// Linear zoom transition
	var minimumLinearStep = 0.125
	var maximumLinearStep = 0.25

	// Smooth zoom transition
	var smoothMultiplier = 0.4f

	// Zoom scrolling step
	var scrollStep = 1.0

	// ZOom Overlay
	var zoomOverlayEnabled = false

	var transition = Config.Transition.SMOOTH
}