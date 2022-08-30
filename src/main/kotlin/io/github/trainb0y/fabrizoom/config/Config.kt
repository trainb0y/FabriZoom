package io.github.trainb0y.fabrizoom.config

object Config {
	@JvmStatic
	val cinematicCameraMultiplier = 4.0

	@JvmStatic
	val zoomTransition = true

	@JvmStatic
	val zoomDivisor = 4.0

	@JvmStatic
	val smoothMultiplier = 2.0

	val lesserScrollStep = 0.5
	val scrollStep = 1.0

	val minimumZoomDivisor = 4.0
	val maximumZoomDivisor = 50.0
}