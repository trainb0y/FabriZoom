package io.github.trainb0y.fabrizoom.mixin;

import io.github.trainb0y.fabrizoom.config.Config;
import io.github.trainb0y.fabrizoom.utils.ZoomUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//This mixin is responsible for managing the fov-changing part of the zoom.
@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Final
	@Shadow
	private MinecraftClient client;

	//Handle transitioned zoom FOV multiplier and zoom overlay alphas each tick.
	@Inject(
			at = @At("HEAD"),
			method = "tick()V"
	)
	private void zoomTick(CallbackInfo info) {
		//If zoom transitions are enabled, update the zoom FOV multiplier.
		if (!Config.features.zoomTransition.equals(Config.FeaturesGroup.ZoomTransitionOptions.OFF)) {
			ZoomUtils.updateZoomFovMultiplier();
		}
	}

	//Handles zooming of both modes (Transitionless and with Smooth Transitions).
	@Inject(
			at = @At("RETURN"),
			method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D",
			cancellable = true
	)
	private double getZoomedFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> info) {
		double fov = info.getReturnValue();

		if (!Config.features.zoomTransition.equals(Config.FeaturesGroup.ZoomTransitionOptions.OFF)) {
			//Handle the zoom with smooth transitions enabled.
			if (ZoomUtils.zoomFovMultiplier != 1.0F) {
				fov *= MathHelper.lerp(tickDelta, ZoomUtils.lastZoomFovMultiplier, ZoomUtils.zoomFovMultiplier);
				info.setReturnValue(fov);
			}
		} else if (ZoomUtils.zoomState){
			//Handle the zoom without smooth transitions.
			double zoomedFov = fov / ZoomUtils.zoomDivisor;
			info.setReturnValue(zoomedFov);
		}

		//Regardless of the mode, if the zoom is over, update the terrain in order to stop terrain glitches.
		if (ZoomUtils.lastZoomState && changingFov) {
			this.client.worldRenderer.scheduleTerrainUpdate();
		}

		return fov;
	}
}
