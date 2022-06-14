package io.github.joaoh1.okzoomer.client.config;


public class Config {
	public static FeaturesGroup features = new FeaturesGroup();

	public static class FeaturesGroup {
		// Defines the cinematic camera while zooming.\n\"OFF\" disables the cinematic camera.\n\"VANILLA\" uses Vanilla's cinematic camera.\n\"MULTIPLIED\" is a multiplied variant of \"VANILLA\".")
		public FeaturesGroup.CinematicCameraOptions cinematicCamera = FeaturesGroup.CinematicCameraOptions.OFF;

		public enum CinematicCameraOptions {
			OFF,
			VANILLA,
			MULTIPLIED
		}

		public enum ZoomModes {
			HOLD,
			TOGGLE,
			PERSISTENT
		}

		// Reduces the mouse sensitivity when zooming
		public boolean reduceSensitivity = true;

		// Adds transitions between zooms.\n\"OFF\" disables transitions.\n\"SMOOTH\" replicates Vanilla's dynamic FOV.\n\"LINEAR\" removes the smoothiness.
		public FeaturesGroup.ZoomTransitionOptions zoomTransition = FeaturesGroup.ZoomTransitionOptions.SMOOTH;

		public enum ZoomTransitionOptions {
			OFF,
			SMOOTH,
			LINEAR
		}

		// The behavior of the zoom key.\n\"HOLD\" needs the zoom key to be hold.\n\"TOGGLE\" has the zoom key toggle the zoom.\n\"PERSISTENT\" makes the zoom permanent.
		public static FeaturesGroup.ZoomModes zoomMode =  FeaturesGroup.ZoomModes.HOLD;

		// Allows to increase or decrease zoom by scrolling.
		public boolean zoomScrolling = true;

		// Adds zoom manipulation keys along with the zoom key.
		public boolean extraKeybinds = true;

		// Adds an overlay in the screen during zoom. The overlay texture can be found at: assets/okzoomer/textures/misc/zoom_overlay.png
		public boolean zoomOverlay = false;
	}

	public static ValuesGroup values = new ValuesGroup();

	public static class ValuesGroup {
		// The divisor applied to the FOV when zooming
		public double zoomDivisor = 4.0;

		// The minimum value that you can scroll down.
		public double minimumZoomDivisor = 1.0;

		// The maximum value that you can scroll up.
		public double maximumZoomDivisor = 50.0;

		// The number which is de/incremented by zoom scrolling. Used when the zoom divisor is above the starting point.
		public double scrollStep = 1.0;

		// The number which is de/incremented by zoom scrolling. Used when the zoom divisor is below the starting point.
		public double lesserScrollStep = 0.5;

		// The multiplier used for the multiplied cinematic camera.
		public double cinematicMultiplier = 4.0;

		// The multiplier used for smooth transitions.
		public double smoothMultiplier = 0.75;

		// The minimum value which the linear transition step can reach.
		public double minimumLinearStep = 0.125;

		// The maximum value which the linear transition step can reach.
		public double maximumLinearStep = 0.25;
	}

	public static TweaksGroup tweaks = new TweaksGroup();

	public static class TweaksGroup {
		// Allows for resetting the zoom with the middle mouse button
		public boolean resetZoomWithMouse = true;
		// If enabled, the \"Save Toolbar Activator\" keybind will be unbound if there's a conflict with the zoom key.
		public boolean unbindConflictingKey = true;
		// Hides the zoom overlay while the HUD's hidden.
		public boolean hideZoomOverlay = false;
	}
}
