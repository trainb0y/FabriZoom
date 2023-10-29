package io.github.trainb0y.fabrizoom

import net.minecraft.client.Minecraft
import java.nio.file.Path

interface Platform {
	val platformName: String
	fun getConfigPath(): Path
	fun registerKeybinds()
	fun registerTick(onTick: (Minecraft) -> Unit)
}