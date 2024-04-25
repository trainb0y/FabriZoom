package io.github.trainb0y.fabrizoom.neoforge

import io.github.trainb0y.fabrizoom.FabriZoom
import io.github.trainb0y.fabrizoom.config.createConfigScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.neoforged.fml.IExtensionPoint
import net.neoforged.fml.ModLoadingContext
import net.neoforged.fml.common.Mod
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.neoforge.client.gui.IConfigScreenFactory


@Mod("fabrizoom")
object NeoForgeEntrypoint {
	init {
		if (FMLEnvironment.dist.isClient) {
			FabriZoom.init(NeoForgePlatform())
		}

		ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory::class.java) {
			IConfigScreenFactory { _: Minecraft?, parent: Screen? ->
				createConfigScreen(parent)
			}
		}
	}
}