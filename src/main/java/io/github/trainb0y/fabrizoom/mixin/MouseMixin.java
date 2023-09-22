package io.github.trainb0y.fabrizoom.mixin;

import io.github.trainb0y.fabrizoom.ZoomLogic;
import io.github.trainb0y.fabrizoom.config.Config;
import net.minecraft.client.Mouse;
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
@Mixin(Mouse.class)
public class MouseMixin {

	/**
	 * The amount the scroll wheel has moved
	 */
	@Shadow
	private double eventDeltaVerticalWheel;

	/**
	 * Whether to apply changes to the mouse
	 *
	 * @see MouseMixin#finalCursorDeltaX
	 * @see MouseMixin#finalCursorDeltaY
	 */
	@Unique
	private boolean modifyMouse;

	/**
	 * The actual mouse delta X
	 * Only applied if modifyMouse is true
	 *
	 * @see MouseMixin#modifyMouse
	 */
	@Unique
	private double finalCursorDeltaX;

	/**
	 * The actual mouse delta Y
	 * Only applied if modifyMouse is true
	 *
	 * @see MouseMixin#modifyMouse
	 */
	@Unique
	private double finalCursorDeltaY;

	/**
	 * Calculate finalCursorDeltaX and finalCursorDeltaY by applying mouse modifiers in ZoomLogic
	 * If we should be zooming, sets modifyMouse to true
	 *
	 * @see MouseMixin#modifyMouse
	 * @see MouseMixin#finalCursorDeltaX
	 * @see MouseMixin#finalCursorDeltaY
	 * @see ZoomLogic#applyMouseXModifier
	 * @see ZoomLogic#applyMouseYModifier
	 */
	@Inject(
			method = "updateMouse()V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/option/GameOptions;getInvertYMouse()Lnet/minecraft/client/option/SimpleOption;"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void applyZoomChanges(CallbackInfo ci, double d, double e, double k, double l, double f, double g, double h, int m) {
		this.modifyMouse = false;
		if (ZoomLogic.getZooming()) {
			k = ZoomLogic.applyMouseXModifier(k, h, e);
			l = ZoomLogic.applyMouseYModifier(l, h, e);
			this.modifyMouse = true;
		}
		ZoomLogic.tick(); // should this really go here?
		this.finalCursorDeltaX = k;
		this.finalCursorDeltaY = l;
	}

	/**
	 * Apply the finalCursorDeltaX if modifyMouse
	 *
	 * @param k the vanilla X delta
	 * @return the modified mouse X delta
	 * @see MouseMixin#modifyMouse
	 * @see MouseMixin#finalCursorDeltaX
	 */
	@ModifyVariable(
			method = "updateMouse",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/option/GameOptions;getInvertYMouse()Lnet/minecraft/client/option/SimpleOption;"
			),
			ordinal = 2
	)
	private double modifyFinalCursorDeltaX(double k) {
		return this.modifyMouse ? finalCursorDeltaX : k;
	}

	/**
	 * Apply the finalCursorDeltaY if modifyMouse
	 *
	 * @param l the vanilla Y delta
	 * @return the modified mouse Y delta
	 * @see MouseMixin#modifyMouse
	 * @see MouseMixin#finalCursorDeltaY
	 */
	@ModifyVariable(
			method = "updateMouse",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/option/GameOptions;getInvertYMouse()Lnet/minecraft/client/option/SimpleOption;"
			),
			ordinal = 3
	)
	private double modifyFinalCursorDeltaY(double l) {
		return this.modifyMouse ? finalCursorDeltaY : l;
	}


	/**
	 * Handle changing the zoom when the player scrolls while zooming
	 *
	 * @see MouseMixin#eventDeltaVerticalWheel
	 * @see ZoomLogic#changeZoomDivisor
	 */
	@Inject(
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/Mouse;eventDeltaVerticalWheel:D",
					ordinal = 7
			),
			method = "onMouseScroll(JDD)V",
			cancellable = true
	)
	private void onMouseScroll(CallbackInfo info) {
		if (this.eventDeltaVerticalWheel == 0.0 || !ZoomLogic.getZooming() || !Config.getValues().getZoomScroll()) return;

		ZoomLogic.changeZoomDivisor(this.eventDeltaVerticalWheel > 0.0);
		info.cancel();
	}
}
