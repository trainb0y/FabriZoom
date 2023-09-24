package io.github.trainb0y.fabrizoom.config

enum class ZoomOverlay(
	/** Translation key for the name of this zoom overlay */
	val key: String
) {
	NONE("overlay.fabrizoom.none"),
	VIGNETTE("overlay.fabrizoom.vignette"),
	SPYGLASS("overlay.fabrizoom.spyglass");
}