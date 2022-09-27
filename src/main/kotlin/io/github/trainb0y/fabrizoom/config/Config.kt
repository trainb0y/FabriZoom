package io.github.trainb0y.fabrizoom.config

import io.github.trainb0y.fabrizoom.FabriZoom
import net.fabricmc.loader.api.FabricLoader
import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.nio.file.Path

/**
 * Handles loading and saving of the mod configuration
 */
@ConfigSerializable
object Config {

	/** The path to the config file */
	private val configPath: Path = FabricLoader.getInstance().configDir.resolve("fabrizoom.conf")

	/** The config version, doesn't necessarily match mod version */
	@Suppress("Unused")
	private const val version = "1"

	/** The current configuration values */
	@JvmStatic
	var values = Presets.DEFAULT.values!!.copy()  // this should get overwritten

	/** A type of Zoom Transition */
	enum class Transition(
		/** Translation key for the name of this zoom transition */
		val key: String
	) {
		LINEAR("transition.fabrizoom.linear"),
		SMOOTH("transition.fabrizoom.smooth"),
		NONE("transition.fabrizoom.none");

		fun next(): Transition { // used for toggle button in gui
			return try {
				Transition.values()[this.ordinal + 1]
			} catch (e: IndexOutOfBoundsException) {
				Transition.values().first()
			}
		}
	}

	/**
	 * Save the current configuration [values] to [configPath]
	 */
	fun saveConfig() {

		val loader = HoconConfigurationLoader.builder()
			.path(configPath)
			.build()
		try {
			val root = loader.load()
			root.node("version").set(version)
			root.node("values").set(values)
			loader.save(root)
			FabriZoom.logger.info("Saved configuration")
		} catch (e: ConfigurateException) {
			FabriZoom.logger.warn("Failed to save configuration! $e")
		}

	}

	/**
	 * Load configuration [values] from the file at [configPath]
	 */
	fun loadConfig() {
		val loader = HoconConfigurationLoader.builder()
			.path(configPath)
			.build()
		try {
			val root = loader.load()
			val configVersion = root.node("version").string!!
			if (version != configVersion) {
				FabriZoom.logger.warn("Found config version: $configVersion, current version: $version")
				FabriZoom.logger.warn("Attempting to load anyway")
			}

			values = root.node("values").get()!! // configurate doesn't error when file not found

			FabriZoom.logger.info("Loaded existing configuration")
			return

		} catch (e: ConfigurateException) {
			FabriZoom.logger.warn("Failed to load existing configuration! Using defaults. $e")
		} catch (e: NullPointerException) {
			FabriZoom.logger.warn("Invalid configuration file! Using defaults.")
		}
		applyDefaultConfig()
	}

	/**
	 * Apply the default preset config [values]
	 *
	 * @see Presets.DEFAULT
	 */
	fun applyDefaultConfig() {
		values = Presets.DEFAULT.values!!.copy()
	}
}