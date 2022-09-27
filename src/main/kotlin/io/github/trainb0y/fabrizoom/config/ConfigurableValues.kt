package io.github.trainb0y.fabrizoom.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

/**
 * Container for the user-configurable values
 */
@ConfigSerializable
data class ConfigurableValues(
	/**
	 * The zoomed mouse sensitivity for the normal mouse mode
	 * Reasonable 10-40
	 */
	var mouseSensitivity: Int,

	/** Whether to use the cinematic mouse mode while zooming */
	var cinematicCameraEnabled: Boolean,
	/** Multiplier for the cinematic mouse mode */
	var cinematicCameraMultiplier: Double,


	/** The initial amount to zoom in */
	var zoomDivisor: Double,
	/** The minimum allowed zoom factor */
	var minimumZoomDivisor: Double,
	/** The maximum allowed zoom factor */
	var maximumZoomDivisor: Double,


	/**
	 * The minimum step for the linear zoom transition
	 * @see Config.Transition.LINEAR
	 * @see maximumLinearStep
	 */
	var minimumLinearStep: Double,
	/**
	 * The minimum step for the linear zoom transition
	 * @see Config.Transition.LINEAR
	 * @see minimumLinearStep
	 */
	var maximumLinearStep: Double,

	/**
	 * Multiplier for the smooth zoom transition
	 * @see Config.Transition.SMOOTH
	 */
	var smoothMultiplier: Float,

	/**
	 * The amount to change the zoom divisor by
	 * @see io.github.trainb0y.fabrizoom.ZoomLogic.changeZoomDivisor
	 */
	var scrollStep: Double,

	/**
	 * Whether to render the zoom overlay
	 * @see io.github.trainb0y.fabrizoom.mixin.GameRendererMixin
	 */
	var zoomOverlayEnabled: Boolean,

	/** The zoom transition to use */
	var transition: Config.Transition
)