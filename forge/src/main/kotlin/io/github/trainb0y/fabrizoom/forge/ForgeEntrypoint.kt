package io.github.trainb0y.fabrizoom.forge

import io.github.trainb0y.fabrizoom.FabriZoom
import net.minecraftforge.fml.common.Mod

@Mod("fabrizoom")
object ForgeEntrypoint {
	init {
		FabriZoom.init(ForgePlatform())
	}
}