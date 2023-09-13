package io.github.trainb0y.fabrizoom.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.trainb0y.fabrizoom.config.Config
import io.github.trainb0y.fabrizoom.config.openConfigScreen
import net.minecraft.client.gui.screen.Screen

class ModMenuIntegration : ModMenuApi {
	override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
		return ConfigScreenFactory<Screen> { parent: Screen? -> openConfigScreen(parent) }
	}
}