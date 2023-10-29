package io.github.trainb0y.fabrizoom.config

enum class ZoomTransition(
	val translationKey: String
) {
	LINEAR("transition.fabrizoom.linear"),
	SMOOTH("transition.fabrizoom.smooth"),
	NONE("transition.fabrizoom.none");
}