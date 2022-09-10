package io.github.trainb0y.fabrizoom.config.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.trainb0y.fabrizoom.config.ConfigScreen
import net.minecraft.client.gui.screen.Screen

class ModMenuIntegration : ModMenuApi {
	override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
		return ConfigScreenFactory<Screen> { parent: Screen? -> ConfigScreen(parent) }
	}
}