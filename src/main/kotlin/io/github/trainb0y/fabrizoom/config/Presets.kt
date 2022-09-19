import io.github.trainb0y.fabrizoom.config.Config
import io.github.trainb0y.fabrizoom.config.ConfigurableValues

enum class Presets(val key: String, val values: ConfigurableValues?) {
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
			Config.Transition.SMOOTH
		)
	),
	OPTIFINE(
		"presets.fabrizoom.optifine",
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
			Config.Transition.SMOOTH
		)
	),
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