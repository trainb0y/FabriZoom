package io.github.trainb0y.fabrizoom.forge

import io.github.trainb0y.fabrizoom.FabriZoom
import io.github.trainb0y.fabrizoom.config.createConfigScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.fml.IExtensionPoint.DisplayTest
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLEnvironment


@Mod("fabrizoom")
object ForgeEntrypoint {
	init {
		if (FMLEnvironment.dist.isClient) {
			FabriZoom.init(ForgePlatform())
		}

		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
			ConfigScreenHandler.ConfigScreenFactory { _: Minecraft?, parent: Screen? ->
				createConfigScreen(parent)
			}
		}
		ModLoadingContext.get().registerExtensionPoint(DisplayTest::class.java) {
			// don't worry about version compatibility with servers
			DisplayTest({"catstare"}){ _: String?, _: Boolean? -> true }
		}
	}
}