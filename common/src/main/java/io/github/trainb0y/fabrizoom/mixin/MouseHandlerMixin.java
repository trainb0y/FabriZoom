package io.github.trainb0y.fabrizoom.mixin;

import io.github.trainb0y.fabrizoom.ZoomLogic;
import io.github.trainb0y.fabrizoom.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.joml.Vector2d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Handles modifying mouse behavior when zoomed in
 */
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

	@Shadow
	private double accumulatedScrollY;

	@Unique
	private boolean fabrizoom$shouldModifyMouse;

	/**
	 * The zoom-modified mouse delta,
	 * Only applied if modifyMouse is true
	 *
	 * @see MouseHandlerMixin#fabrizoom$shouldModifyMouse
	 */
	@Unique
	private Vector2d fabrizoom$zoomCursorDelta;

	/**
	 * Calculate zoomCursorDelta by applying mouse modifiers in ZoomLogic
	 * If we should be zooming, sets modifyMouse to true
	 *
	 * @see MouseHandlerMixin#fabrizoom$shouldModifyMouse
	 * @see MouseHandlerMixin#fabrizoom$zoomCursorDelta
	 */
	@Inject(
			method = "turnPlayer",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/Options;invertYMouse()Lnet/minecraft/client/OptionInstance;"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void applyZoomChanges(CallbackInfo ci, double d, double e, double k, double l, double f, double g, double h, int m) {
		ZoomLogic.tick(); // todo: should this really go here?

		this.fabrizoom$shouldModifyMouse = false;
		if (ZoomLogic.isZooming()) {
			k = ZoomLogic.applyMouseXModifier(k, h, e);
			l = ZoomLogic.applyMouseYModifier(l, h, e);
			this.fabrizoom$shouldModifyMouse = true;
		}
		this.fabrizoom$zoomCursorDelta = new Vector2d(k, l);
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
		if (fabrizoom$shouldModifyMouse) {
			return fabrizoom$zoomCursorDelta.x;
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
		if (fabrizoom$shouldModifyMouse) {
			return fabrizoom$zoomCursorDelta.y * invert;
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
