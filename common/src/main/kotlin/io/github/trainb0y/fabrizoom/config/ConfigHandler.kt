package io.github.trainb0y.fabrizoom.config

import io.github.trainb0y.fabrizoom.FabriZoom
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.nio.file.StandardOpenOption
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream


/**
 * Handles loading and saving of the mod configuration
 */
object ConfigHandler {

	/** The current configuration values */
	@JvmStatic
	var values = Presets.DEFAULT.values!!.copy()  // this should get overwritten


	@OptIn(ExperimentalSerializationApi::class)
	fun saveConfig() {
		try {
			Json.encodeToStream(
				values,
				FabriZoom.platform.getConfigPath().outputStream(StandardOpenOption.TRUNCATE_EXISTING)
			)
			FabriZoom.logger.info("Saved configuration")
		} catch (e: Exception) {
			e.printStackTrace()
			FabriZoom.logger.warn("Could not save config!")
		}
	}

	@OptIn(ExperimentalSerializationApi::class)
	fun loadConfig() {
		try {
			val conf = Json.decodeFromStream<ConfigurableValues>(FabriZoom.platform.getConfigPath().inputStream())
			val currentVersion = Presets.DEFAULT.values!!.CONFIG_VERSION
			if (conf.CONFIG_VERSION != currentVersion) {
				FabriZoom.logger.warn("Config version mismatch! Existing configuration might break!")
				conf.CONFIG_VERSION = currentVersion
			}
			values = conf
			FabriZoom.logger.info("Loaded existing configuration")
		} catch (e: Exception) {
			e.printStackTrace()
			FabriZoom.logger.warn("Could not load config! Using default values.")
			applyDefaultConfig()
		}
	}

	private fun applyDefaultConfig() {
		values = Presets.DEFAULT.values!!.copy()
	}
}