package io.github.trainb0y.fabrizoom.config

enum class ZoomTransition(
	/** Translation key for the name of this zoom transition */
	val key: String
) {
	LINEAR("transition.fabrizoom.linear"),
	SMOOTH("transition.fabrizoom.smooth"),
	NONE("transition.fabrizoom.none");
}