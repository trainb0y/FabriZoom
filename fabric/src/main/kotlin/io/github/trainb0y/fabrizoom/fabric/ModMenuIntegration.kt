package io.github.trainb0y.fabrizoom.fabric

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.trainb0y.fabrizoom.config.createConfigScreen
import net.minecraft.client.gui.screens.Screen

class ModMenuIntegration : ModMenuApi {
	override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
		return ConfigScreenFactory { parent: Screen? -> createConfigScreen(parent) }
	}
}