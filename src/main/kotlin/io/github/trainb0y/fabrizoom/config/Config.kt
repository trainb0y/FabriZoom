package io.github.trainb0y.fabrizoom.config

object Config {
	@JvmField
	var features = FeaturesGroup()
	@JvmField
	var values = ValuesGroup()

	class FeaturesGroup {
		// Defines the cinematic camera while zooming.\n\"OFF\" disables the cinematic camera.\n\"VANILLA\" uses Vanilla's cinematic camera.\n\"MULTIPLIED\" is a multiplied variant of \"VANILLA\".")
		@JvmField
		var cinematicCamera = CinematicCameraOptions.OFF

		enum class CinematicCameraOptions {
			OFF, VANILLA, MULTIPLIED
		}

		enum class ZoomModes {
			HOLD, TOGGLE, PERSISTENT
		}

		// Reduces the mouse sensitivity when zooming
		@JvmField
		var reduceSensitivity = true
		@JvmField
		var resetZoomWithMouse = true

		// Adds transitions between zooms.\n\"OFF\" disables transitions.\n\"SMOOTH\" replicates Vanilla's dynamic FOV.\n\"LINEAR\" removes the smoothiness.
		@JvmField
		var zoomTransition = ZoomTransitionOptions.SMOOTH

		enum class ZoomTransitionOptions {
			OFF, SMOOTH, LINEAR
		}

		// Allows to increase or decrease zoom by scrolling.
		@JvmField
		var zoomScrolling = true

		companion object {
			// The behavior of the zoom key.\n\"HOLD\" needs the zoom key to be hold.\n\"TOGGLE\" has the zoom key toggle the zoom.\n\"PERSISTENT\" makes the zoom permanent.
			@JvmField
			var zoomMode = ZoomModes.HOLD
		}
	}

	class ValuesGroup {
		// The divisor applied to the FOV when zooming
		@JvmField
		var zoomDivisor = 4.0

		// The minimum value that you can scroll down.
		@JvmField
		var minimumZoomDivisor = 1.0

		// The maximum value that you can scroll up.
		@JvmField
		var maximumZoomDivisor = 50.0

		// The number which is de/incremented by zoom scrolling. Used when the zoom divisor is above the starting point.
		@JvmField
		var scrollStep = 1.0

		// The number which is de/incremented by zoom scrolling. Used when the zoom divisor is below the starting point.
		@JvmField
		var lesserScrollStep = 0.5

		// The multiplier used for the multiplied cinematic camera.
		@JvmField
		var cinematicMultiplier = 4.0

		// The multiplier used for smooth transitions.
		@JvmField
		var smoothMultiplier = 0.75

		// The minimum value which the linear transition step can reach.
		@JvmField
		var minimumLinearStep = 0.125

		// The maximum value which the linear transition step can reach.
		@JvmField
		var maximumLinearStep = 0.25
	}
}