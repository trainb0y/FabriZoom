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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Handles modifying mouse behavior when zoomed in
 */
@Mixin(MouseHandler.class)
public class MouseMixin {

	@Shadow
	private double accumulatedScrollY;

	/**
	 * Whether to apply changes to the mouse
	 *
	 * @see MouseMixin#zoomCursorDelta
	 */
	@Unique
	private boolean modifyMouse;

	/**
	 * The actual mouse delta
	 * Only applied if modifyMouse is true
	 * @see MouseMixin#modifyMouse
	 */
	@Unique
	private Vector2d zoomCursorDelta;

	/**
	 * Calculate zoomCursorDelta by applying mouse modifiers in ZoomLogic
	 * If we should be zooming, sets modifyMouse to true
	 *
	 * @see MouseMixin#modifyMouse
	 * @see MouseMixin#zoomCursorDelta
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

		this.modifyMouse = false;
		if (ZoomLogic.isZooming()) {
			k = ZoomLogic.applyMouseXModifier(k, h, e);
			l = ZoomLogic.applyMouseYModifier(l, h, e);
			this.modifyMouse = true;
		}
		this.zoomCursorDelta = new Vector2d(k,l);
	}


	@ModifyVariable(
			method = "turnPlayer",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/Options;invertYMouse()Lnet/minecraft/client/OptionInstance;"
			),
			ordinal = 2
	)
	private double modifyFinalCursorDeltaX(double k) {
		return this.modifyMouse ? zoomCursorDelta.x : k;
	}

	@ModifyVariable(
			method = "turnPlayer",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/Options;invertYMouse()Lnet/minecraft/client/OptionInstance;"
			),
			ordinal = 3
	)
	private double modifyFinalCursorDeltaY(double l) {
		return this.modifyMouse ? zoomCursorDelta.y: l;
	}


	/**
	 * Handle changing the zoom when the player scrolls while zooming
	 *
	 * @see MouseMixin#accumulatedScrollY
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
		if (this.accumulatedScrollY == 0.0 || !ZoomLogic.isZooming() || !ConfigHandler.getValues().getZoomScroll()) return;

		ZoomLogic.changeZoomDivisor(this.accumulatedScrollY > 0.0);
		ci.cancel();
	}
}
