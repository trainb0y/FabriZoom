package io.github.trainb0y.fabrizoom.forge

import io.github.trainb0y.fabrizoom.Platform
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.loading.FMLPaths
import java.nio.file.Path

class ForgePlatform: Platform {
	private lateinit var onTick: (Minecraft) -> Unit
	override val platformName: String = "Forge"

	override fun getConfigPath(): Path = FMLPaths.CONFIGDIR.get().resolve("fabrizoom.conf")

	override fun registerKeybinds() {

	}

	override fun registerTick(onTick: (Minecraft) -> Unit) {
		this.onTick = onTick
	}

	@SubscribeEvent
	fun onClientTick(event: TickEvent.ClientTickEvent) {
		if (event.phase == TickEvent.Phase.END && ::onTick.isInitialized) {
			onTick(Minecraft.getInstance())
		}
	}
}