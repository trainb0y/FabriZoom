package io.github.trainb0y.fabrizoom.forge

import io.github.trainb0y.fabrizoom.Keybinds
import io.github.trainb0y.fabrizoom.Platform
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.loading.FMLPaths
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT
import java.nio.file.Path

class ForgePlatform: Platform {
	override val platformName: String = "Forge"

	private lateinit var onTick: (Minecraft) -> Unit

	init {
		MinecraftForge.EVENT_BUS.register(object {
			@SubscribeEvent
			fun onClientTick(event: TickEvent.ClientTickEvent) {
				if (event.phase == TickEvent.Phase.END && ::onTick.isInitialized) onTick(Minecraft.getInstance())
			}
		})
		MOD_CONTEXT.getKEventBus().register(object {
			@SubscribeEvent
			fun onAddKeybinds(event: RegisterKeyMappingsEvent) = Keybinds.all.forEach { event.register(it) }
		})
	}

	override fun getConfigPath(): Path = FMLPaths.CONFIGDIR.get().resolve("fabrizoom.json")

	override fun registerKeybinds() {} // can be ignored, on forge we use an event to do it

	override fun registerTick(onTick: (Minecraft) -> Unit) {
		this.onTick = onTick
	}
}