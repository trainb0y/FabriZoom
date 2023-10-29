package io.github.trainb0y.fabrizoom.fabric

import io.github.trainb0y.fabrizoom.Keybinds
import io.github.trainb0y.fabrizoom.Platform
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import java.nio.file.Path
import kotlin.reflect.full.memberProperties

class FabricPlatform: Platform {
	override val platformName = "Fabric"
	override fun getConfigPath(): Path = FabricLoader.getInstance().configDir.resolve("fabrizoom.json")
	override fun registerKeybinds() {
		Keybinds::class.memberProperties
			.filter { it.returnType == KeyMapping::class }
			.forEach { prop ->
				KeyBindingHelper.registerKeyBinding(prop.get(Keybinds) as KeyMapping)
			}
	}

	override fun registerTick(onTick: (Minecraft) -> Unit) {
		ClientTickEvents.END_CLIENT_TICK.register(onTick)
	}
}