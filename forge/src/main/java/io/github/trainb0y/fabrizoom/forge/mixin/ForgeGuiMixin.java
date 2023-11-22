package io.github.trainb0y.fabrizoom.forge.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.trainb0y.fabrizoom.CursedOverlay;
import io.github.trainb0y.fabrizoom.ZoomLogic;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeGui.class)
public class ForgeGuiMixin {
	// Forge overrides the render() method, so the normal (fabric) mixin won't work  net.minecraft.util.RandomSource
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;setSeed(J)V"), method = "render(Lnet/minecraft/client/gui/GuiGraphics;F)V")
	void injectZoomOverlay(GuiGraphics context, float tickDelta, CallbackInfo ci) {
		RenderSystem.enableBlend();
		ZoomLogic.renderZoomOverlay(context, tickDelta, (CursedOverlay) this);
		RenderSystem.disableBlend();
	}
}
