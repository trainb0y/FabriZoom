package io.github.trainb0y.fabrizoom.mixin;

import io.github.trainb0y.fabrizoom.ZoomLogic;
import io.github.trainb0y.fabrizoom.config.ConfigHandler;
import net.minecraft.client.MouseHandler;
import org.joml.Vector2d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

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

	@ModifyArgs(
			method = "turnPlayer",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"
			)
	)
	private void modifyMouseDelta(Args args) {
		if (fabrizoom$shouldModifyMouse) {
			args.set(0, fabrizoom$zoomCursorDelta.x);
			args.set(1, fabrizoom$zoomCursorDelta.y);
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
