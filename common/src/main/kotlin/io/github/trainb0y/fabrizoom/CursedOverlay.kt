package io.github.trainb0y.fabrizoom

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

interface CursedOverlay {
	// Has to be outside of the mixin package
	// Semi-dirty hack for the Forge platform mixins, since Forge overrides Gui.render
	// which is where we normally inject the overlays
	// todo: there's probably a better way to do this
	fun invokeRenderSpyglassOverlay(context: GuiGraphics?, scale: Float)
	fun invokeRenderTextureOverlay(context: GuiGraphics?, texture: ResourceLocation?, opacity: Float)
}