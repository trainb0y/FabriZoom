package io.github.trainb0y.fabrizoom.config

import io.github.trainb0y.fabrizoom.FabriZoom
import org.spongepowered.configurate.ConfigurateException
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.objectmapping.ConfigSerializable

/**
 * Handles loading and saving of the mod configuration
 */
@ConfigSerializable
object ConfigHandler {

	/** The config version, doesn't necessarily match mod version */
	private const val VERSION = "2"

	/** The current configuration values */
	@JvmStatic
	var values = Presets.DEFAULT.values!!.copy()  // this should get overwritten

	fun saveConfig() {
		val loader = HoconConfigurationLoader.builder()
			.path(FabriZoom.platform.getConfigPath())
			.build()
		try {
			val root = loader.load()
			root.node("version").set(VERSION)
			root.node("values").set(values)
			loader.save(root)
			FabriZoom.logger.info("Saved configuration")
		} catch (e: ConfigurateException) {
			FabriZoom.logger.warn("Failed to save configuration! $e")
		}

	}

	fun loadConfig() {
		val loader = HoconConfigurationLoader.builder()
			.path(FabriZoom.platform.getConfigPath())
			.build()
		try {
			val root = loader.load()
			val configVersion = root.node("version").string!!
			if (VERSION != configVersion) {
				FabriZoom.logger.warn("Found config version: $configVersion, current version: $VERSION")
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

	private fun applyDefaultConfig() {
		values = Presets.DEFAULT.values!!.copy()
	}
}