package io.github.trainb0y.fabrizoom.mixin;

import io.github.trainb0y.fabrizoom.ZoomLogic;
import io.github.trainb0y.fabrizoom.config.ConfigHandler;
import io.github.trainb0y.fabrizoom.config.ZoomTransition;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Handles rendering the zoom vignette and/or spyglass overlay
 */
@Mixin(Gui.class)
public class InGameHudMixin {
	@Unique
	private static final ResourceLocation ZOOM_OVERLAY = new ResourceLocation("fabrizoom:textures/zoom_overlay.png");

	@Shadow
	private void renderSpyglassOverlay(GuiGraphics context, float scale) {}

	@Shadow
	private void renderTextureOverlay(GuiGraphics context, ResourceLocation texture, float opacity) {}

	@Inject(
			at = @At(value = "INVOKE", target = "net/minecraft/world/entity/player/Inventory.getArmor(I)Lnet/minecraft/world/item/ItemStack;"),
			method = "render(Lnet/minecraft/client/gui/GuiGraphics;F)V"
	)
	public void injectZoomOverlay(GuiGraphics context, float tickDelta, CallbackInfo ci) {
		if (ZoomLogic.INSTANCE.getCurrentZoomFovMultiplier() >= 0.99) return;

		switch (ConfigHandler.getValues().getZoomOverlay()) {
			case NONE -> {}
			case VIGNETTE -> {
				float alpha;
				if (ConfigHandler.getValues().getTransition() != ZoomTransition.NONE) {
					// smooth and linear transition
					alpha = 1 - Mth.lerp(tickDelta, ZoomLogic.getLastZoomOverlayAlpha(), ZoomLogic.getZoomOverlayAlpha());
				} else {
					// no transition
					alpha = ZoomLogic.getZoomOverlayAlpha();
				}

				renderTextureOverlay(context, ZOOM_OVERLAY, alpha);
			}
			case SPYGLASS -> {
				if (ZoomLogic.isZooming()) {
					float scale = (1f / (float) ZoomLogic.getZoomDivisor()) / ZoomLogic.INSTANCE.getCurrentZoomFovMultiplier();
					scale += 0.125f; // this is jank, but the vanilla spyglass lerps from 0.5 to 1.125
					scale = Mth.clamp(scale, 0.5f, 1.125f);
					this.renderSpyglassOverlay(context, scale);
				}
			}
		}
	}
}