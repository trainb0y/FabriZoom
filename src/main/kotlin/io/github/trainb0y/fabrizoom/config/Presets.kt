package io.github.trainb0y.fabrizoom.config

/**
 * Presets of [ConfigurableValues]
 */
enum class Presets(
	val translationKey: String,
	val values: ConfigurableValues?
) {
	DEFAULT(
		"presets.fabrizoom.default",
		ConfigurableValues(
			30,
			false,
			4.0,
			4.0,
			3.0,
			20.0,
			true,
			0.125,
			0.25,
			0.4f,
			1.0,
			true,
			ZoomOverlay.NONE,
			ZoomTransition.LINEAR
		)
	),
	CINEMATIC(
		"presets.fabrizoom.cinematic",
		ConfigurableValues(
			30,
			true,
			4.0,
			4.0,
			3.0,
			20.0,
			true,
			0.125,
			0.25,
			0.4f,
			1.0,
			false,
			ZoomOverlay.VIGNETTE,
			ZoomTransition.SMOOTH
		)
	),
	QUICK_TOGGLE(
		"presets.fabrizoom.quicktoggle",
		ConfigurableValues(
			30,
			false,
			4.0,
			4.0,
			3.0,
			20.0,
			false,
			0.125,
			0.25,
			0.4f,
			1.0,
			true,
			ZoomOverlay.NONE,
			ZoomTransition.NONE
		)
	),
	OPTIFINE(
		"presets.fabrizoom.optifine",
		ConfigurableValues(
			30,
			true,
			1.5,
			5.0,
			3.0,
			20.0,
			false,
			0.125,
			0.25,
			0.4f,
			1.0,
			false,
			ZoomOverlay.NONE,
			ZoomTransition.NONE
		)
	),
	CUSTOM(
		"presets.fabrizoom.custom",
		null
	);

	fun next(): Presets { // used for toggle button in gui
		return try {
			entries[this.ordinal + 1]
		} catch (e: IndexOutOfBoundsException) {
			entries.first()
		}
	}
}