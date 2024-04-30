package io.github.trainb0y.fabrizoom

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

interface Overlay {
	fun invokeRenderSpyglassOverlay(context: GuiGraphics?, scale: Float)
	fun invokeRenderTextureOverlay(context: GuiGraphics?, texture: ResourceLocation?, opacity: Float)
}