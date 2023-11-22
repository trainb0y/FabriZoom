package io.github.trainb0y.fabrizoom.mixin;

import com.mojang.blaze3d.Blaze3D;
import io.github.trainb0y.fabrizoom.ZoomLogic;
import io.github.trainb0y.fabrizoom.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Handles modifying mouse behavior when zoomed in
 */
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

	@Shadow
	private double accumulatedScrollY;
	@Shadow
	private double lastMouseEventTime;

	@Unique // could capture from locals but it's different on fabric and forge
	private double fabrizoom$mouseUpdateDelta;
	@Unique
	private double fabrizoom$sensitivity;

	/**
	 * Calculate zoomCursorDelta by applying mouse modifiers in ZoomLogic
	 * If we should be zooming, sets modifyMouse to true
	 *
	 */
	@Inject(
			method = "turnPlayer",
			at = @At(
					value = "HEAD"
			)
	)
	public void tick(CallbackInfo ci) {
		ZoomLogic.tick();

		fabrizoom$mouseUpdateDelta = Blaze3D.getTime() - lastMouseEventTime;

		// same way vanilla calculates it
		fabrizoom$sensitivity = Math.pow(Minecraft.getInstance().options.sensitivity().get() * 0.6 + 0.2, 3) * 8.0;
	}

	// Since ModifyArgs breaks on Forge >1.17, we have to use two ModifyArg s instead
	@ModifyArg(
			method = "turnPlayer",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"
			),
			index = 0
	)
	private double modifyMouseDeltaX(double x) {
		if (ZoomLogic.isZooming()) {
			return ZoomLogic.applyMouseXModifier(x, fabrizoom$sensitivity, fabrizoom$mouseUpdateDelta);
		} else {
			return x;
		}
	}
	@ModifyArg(
			method = "turnPlayer",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"
			),
			index = 1
	)
	private double modifyMouseDeltaY(double y) {
		// could capture the local, but it's easier to just invert here
		var invert = Minecraft.getInstance().options.invertYMouse().get() ? -1 : 1;
		if (ZoomLogic.isZooming()) {
			return ZoomLogic.applyMouseYModifier(y, fabrizoom$sensitivity, fabrizoom$mouseUpdateDelta) * invert;
		} else {
			return y * invert;
		}
	}


	/**
	 * Handle changing the zoom when the player scrolls while zooming
	 *
	 * @see MouseHandlerMixin#accumulatedScrollY
	 * @see ZoomLogic#changeZoomDivisor
	 */
	@Inject(
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/MouseHandler;accumulatedScrollY:D",
					ordinal = 7
			),
			method = "onScroll(JDD)V",
			cancellable = true
	)
	private void onMouseScroll(CallbackInfo ci) {
		if (this.accumulatedScrollY == 0.0 || !ZoomLogic.isZooming() || !ConfigHandler.getValues().getZoomScroll())
			return;

		ZoomLogic.changeZoomDivisor(this.accumulatedScrollY > 0.0);
		ci.cancel();
	}
}
