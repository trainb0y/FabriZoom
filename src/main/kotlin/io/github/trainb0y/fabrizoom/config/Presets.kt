package io.github.trainb0y.fabrizoom.config

/**
 * Presets of [ConfigurableValues]
 */
enum class Presets(
	/** Translation key for the name of this preset */
	val key: String,
	/** The values of this preset */
	val values: ConfigurableValues?
) {
	/**
	 * Default configuration preset
	 * @see Config.applyDefaultConfig
	 */
	DEFAULT(
		"presets.fabrizoom.default",
		ConfigurableValues(
			30,
			false,
			4.0,
			4.0,
			3.0,
			20.0,
			0.125,
			0.25,
			0.4f,
			1.0,
			false,
			Config.Transition.LINEAR
		)
	),

	/**
	 * Configuration preset for cinematic transitions and mouse
	 */
	CINEMATIC(
		"presets.fabrizoom.cinematic",
		ConfigurableValues(
			30,
			true,
			4.0,
			4.0,
			3.0,
			20.0,
			0.125,
			0.25,
			0.4f,
			1.0,
			false,
			Config.Transition.SMOOTH
		)
	),

	/**
	 * Configuration preset for a quick, abrupt toggle to zoom
	 */
	QUICK_TOGGLE(
		"presets.fabrizoom.quicktoggle",
		ConfigurableValues(
			30,
			false,
			4.0,
			4.0,
			3.0,
			20.0,
			0.125,
			0.25,
			0.4f,
			1.0,
			false,
			Config.Transition.NONE
		)
	),

	/**
	 * Placeholder custom preset
	 */
	CUSTOM(
		"presets.fabrizoom.custom",
		null
	);

	fun next(): Presets { // used for toggle button in gui
		return try {
			Presets.values()[this.ordinal + 1]
		} catch (e: IndexOutOfBoundsException) {
			Presets.values().first()
		}
	}
}