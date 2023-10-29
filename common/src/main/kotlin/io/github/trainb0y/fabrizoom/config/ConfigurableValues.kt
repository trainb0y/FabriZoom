package io.github.trainb0y.fabrizoom.config

import kotlinx.serialization.Serializable

/**
 * Container for the user-configurable values
 */
@Serializable
data class ConfigurableValues(
	/**
	 * The zoomed mouse sensitivity for the normal mouse mode
	 * Reasonable 10-40
	 */
	var mouseSensitivity: Int = Presets.DEFAULT.values!!.mouseSensitivity,

	/** Whether to use the cinematic mouse mode while zooming */
	var cinematicCameraEnabled: Boolean = Presets.DEFAULT.values!!.cinematicCameraEnabled,
	/** Multiplier for the cinematic mouse mode */
	var cinematicCameraMultiplier: Double = Presets.DEFAULT.values!!.cinematicCameraMultiplier,


	/** The initial amount to zoom in */
	var zoomDivisor: Double = Presets.DEFAULT.values!!.zoomDivisor,
	/** The minimum allowed zoom factor */
	var minimumZoomDivisor: Double = Presets.DEFAULT.values!!.minimumZoomDivisor,
	/** The maximum allowed zoom factor */
	var maximumZoomDivisor: Double = Presets.DEFAULT.values!!.maximumZoomDivisor,

	/** Whether to play the spyglass sounds on zoom */
	var zoomSound: Boolean = Presets.DEFAULT.values!!.zoomSound,


	/**
	 * The minimum step for the linear zoom transition
	 * @see ZoomTransition.LINEAR
	 * @see maximumLinearStep
	 */
	var minimumLinearStep: Double = Presets.DEFAULT.values!!.minimumLinearStep,
	/**
	 * The minimum step for the linear zoom transition
	 * @see ZoomTransition.LINEAR
	 * @see minimumLinearStep
	 */
	var maximumLinearStep: Double = Presets.DEFAULT.values!!.maximumLinearStep,

	/**
	 * Multiplier for the smooth zoom transition
	 * @see ZoomTransition.SMOOTH
	 */
	var smoothMultiplier: Float = Presets.DEFAULT.values!!.smoothMultiplier,

	/**
	 * The amount to change the zoom divisor by
	 * @see io.github.trainb0y.fabrizoom.ZoomLogic.changeZoomDivisor
	 */
	var scrollStep: Double = Presets.DEFAULT.values!!.scrollStep,

	/**
	 * Whether scrolling should change the zoom
	 */
	var zoomScroll: Boolean = Presets.DEFAULT.values!!.zoomScroll,

	var zoomOverlay: ZoomOverlay = Presets.DEFAULT.values!!.zoomOverlay,

	/** The zoom transition to use */
	var transition: ZoomTransition = Presets.DEFAULT.values!!.transition,
) {
	/** The config version, doesn't necessarily match mod version */
	@Suppress("PropertyName")
	var CONFIG_VERSION: Int = 3
}