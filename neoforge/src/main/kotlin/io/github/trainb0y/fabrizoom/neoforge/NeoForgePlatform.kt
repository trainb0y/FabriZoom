package io.github.trainb0y.fabrizoom.neoforge

import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import io.github.trainb0y.fabrizoom.FabriZoom
import io.github.trainb0y.fabrizoom.Keybinds
import io.github.trainb0y.fabrizoom.Platform
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.commands.CommandSourceStack
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.loading.FMLPaths
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.TickEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_CONTEXT
import java.nio.file.Path

class NeoForgePlatform : Platform {
	override val platformName: String = "NeoForge"

	private lateinit var onTick: (Minecraft) -> Unit

	init {

		NeoForge.EVENT_BUS.register(object {
			@SubscribeEvent
			fun onClientTick(event: TickEvent.ClientTickEvent) {
				if (event.phase == TickEvent.Phase.END && ::onTick.isInitialized) onTick(Minecraft.getInstance())
			}
			@SubscribeEvent
			fun onAddClientCommands(event: RegisterClientCommandsEvent) = event.dispatcher.register(literal<CommandSourceStack>("fabrizoom").executes {
				FabriZoom.shouldOpenConfigScreen = true
				1
			})
		})
		MOD_CONTEXT.getKEventBus().register(object {
			@SubscribeEvent
			fun onAddKeybinds(event: RegisterKeyMappingsEvent) = Keybinds.all.forEach { event.register(it) }
		})
	}

	override fun getConfigPath(): Path = FMLPaths.CONFIGDIR.get().resolve("fabrizoom.json")
	override fun registerKeybinds() {} // can be ignored, on (neo)forge we use an event to do it instead
	override fun registerCommand() {} // also ignored, uses neoforge event
	override fun registerTick(onTick: (Minecraft) -> Unit) {
		this.onTick = onTick
	}
}