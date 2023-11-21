package io.github.trainb0y.fabrizoom.mixin;

import io.github.trainb0y.fabrizoom.CursedOverlay;
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
public abstract class GuiMixin implements CursedOverlay {
	@Invoker
	public abstract void invokeRenderSpyglassOverlay(GuiGraphics context, float scale);

	@Invoker
	public abstract void invokeRenderTextureOverlay(GuiGraphics context, ResourceLocation texture, float opacity);

	@Inject(
			at = @At(value = "INVOKE", target = "net/minecraft/world/entity/player/Inventory.getArmor(I)Lnet/minecraft/world/item/ItemStack;"),
			method = "render(Lnet/minecraft/client/gui/GuiGraphics;F)V"
	)
	public void injectZoomOverlay(GuiGraphics context, float tickDelta, CallbackInfo ci) {
		// WARNING: THIS DOES NOT GET CALLED ON FORGE, LOOK AT PLATFORM-SPECIFIC MIXINS!!
		// todo: maybe make this fabric-only, for clarity's sake
		ZoomLogic.renderZoomOverlay(context, tickDelta, this);
	}
}