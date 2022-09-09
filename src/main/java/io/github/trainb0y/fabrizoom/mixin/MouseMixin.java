package io.github.trainb0y.fabrizoom.mixin;

import io.github.trainb0y.fabrizoom.ZoomLogic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//This mixin is responsible for the mouse-behavior-changing part of the zoom.
@Mixin(Mouse.class)
public class MouseMixin {
	@Shadow
	private double eventDeltaWheel;


	@Shadow @Final private MinecraftClient client;
	@Unique
	private boolean modifyMouse;

	@Unique
	private double finalCursorDeltaX;

	@Unique
	private double finalCursorDeltaY;

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
		ZoomLogic.tick(client); // should this go here?
		this.finalCursorDeltaX = k;
		this.finalCursorDeltaY = l;
	}

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


	@Inject(
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/Mouse;eventDeltaWheel:D",
					ordinal = 7
			),
			method = "onMouseScroll(JDD)V",
			cancellable = true
	)
	private void onMouseScroll(CallbackInfo info) {
		if (this.eventDeltaWheel == 0.0 || ! ZoomLogic.getZooming()) return;

		ZoomLogic.changeZoomDivisor(this.eventDeltaWheel > 0.0);
		info.cancel();
	}
}
