package io.github.trainb0y.fabrizoom.mixin;

import io.github.trainb0y.fabrizoom.Overlay;
import io.github.trainb0y.fabrizoom.ZoomLogic;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin implements Overlay {
	@Invoker
	public abstract void invokeRenderSpyglassOverlay(GuiGraphics context, float scale);

	@Invoker
	public abstract void invokeRenderTextureOverlay(GuiGraphics context, ResourceLocation texture, float opacity);

	@Inject(
			at = @At(value = "INVOKE", target = "net/minecraft/world/entity/player/Inventory.getArmor(I)Lnet/minecraft/world/item/ItemStack;"),
			method = "renderCameraOverlays(Lnet/minecraft/client/gui/GuiGraphics;F)V"
	)
	public void injectZoomOverlay(GuiGraphics context, float tickDelta, CallbackInfo ci) {
		ZoomLogic.renderZoomOverlay(context, tickDelta, this);
	}
}