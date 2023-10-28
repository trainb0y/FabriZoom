package io.github.trainb0y.fabrizoom.mixin;

import io.github.trainb0y.fabrizoom.ZoomLogic;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Sets the game's Field of View (FOV) to the zoomed FOV
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Inject(
			at = @At("RETURN"),
			method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D",
			cancellable = true
	)
	private void getZoomedFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> info) {
		info.setReturnValue(ZoomLogic.getZoomFov(info.getReturnValue(), tickDelta));
	}
}
