package io.github.trainb0y.fabrizoom.mixin;

import io.github.trainb0y.fabrizoom.config.Config;
import io.github.trainb0y.fabrizoom.keybinds.ZoomKeybinds;
import io.github.trainb0y.fabrizoom.utils.ZoomUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.SmoothUtil;
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
	@Final
	@Shadow
	private MinecraftClient client;

	@Shadow
	private final SmoothUtil cursorXSmoother = new SmoothUtil();

	@Shadow
	private final SmoothUtil cursorYSmoother = new SmoothUtil();

	@Shadow
	private double cursorDeltaX;

	@Shadow
	private double cursorDeltaY;

	@Shadow
	private double eventDeltaWheel;

	@Unique
	private final SmoothUtil cursorXZoomSmoother = new SmoothUtil();

	@Unique
	private final SmoothUtil cursorYZoomSmoother = new SmoothUtil();

	@Unique
	private double extractedE;
	@Unique
	private double adjustedG;

	//This mixin handles the "Reduce Sensitivity" option and extracts the g variable for the cinematic cameras.
	@ModifyVariable(
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/Mouse;client:Lnet/minecraft/client/MinecraftClient;",
					ordinal = 2
			),
			method = "updateMouse()V",
			ordinal = 2
	)
	private double applyReduceSensitivity(double g) {
		double modifiedMouseSensitivity = this.client.options.getMouseSensitivity().getValue();
		if (Config.features.reduceSensitivity) {
			if (!Config.features.zoomTransition.equals(Config.FeaturesGroup.ZoomTransitionOptions.OFF))
				modifiedMouseSensitivity *= ZoomUtils.zoomFovMultiplier;
			else if (ZoomUtils.zoomState) modifiedMouseSensitivity /= ZoomUtils.zoomDivisor;
		}
		double appliedMouseSensitivity = modifiedMouseSensitivity * 0.6 + 0.2;
		g = appliedMouseSensitivity * appliedMouseSensitivity * appliedMouseSensitivity * 8.0;
		this.adjustedG = g;
		return g;
	}

	//Extracts the e variable for the cinematic cameras.
	@Inject(
			at = @At(
					value = "INVOKE",
					target = "net/minecraft/client/Mouse.isCursorLocked()Z"
			),
			method = "updateMouse()V",
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void obtainCinematicCameraValues(CallbackInfo info, double d, double e) {
		this.extractedE = e;
	}

	//Applies the cinematic camera on the mouse's X.
	@ModifyVariable(
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/Mouse;cursorDeltaX:D",
					ordinal = 3,
					shift = At.Shift.BEFORE
			),
			method = "updateMouse()V",
			ordinal = 1
	)
	private double applyCinematicModeX(double l) {
		if (Config.features.cinematicCamera.equals(Config.FeaturesGroup.CinematicCameraOptions.OFF)) return l;
		if (ZoomUtils.zoomState) {
			l = this.cursorXZoomSmoother.smooth(this.cursorDeltaX * this.adjustedG, (this.extractedE * this.adjustedG));
			if (this.client.options.smoothCameraEnabled) this.cursorXZoomSmoother.clear();

			if (Config.features.cinematicCamera.equals(Config.FeaturesGroup.CinematicCameraOptions.MULTIPLIED))
				l *= Config.values.cinematicMultiplier;

		} else this.cursorXZoomSmoother.clear();

		return l;
	}

	//Applies the cinematic camera on the mouse's Y.
	@ModifyVariable(
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/Mouse;cursorDeltaY:D",
					ordinal = 3,
					shift = At.Shift.BEFORE
			),
			method = "updateMouse()V",
			ordinal = 2
	)
	private double applyCinematicModeY(double m) {
		if (Config.features.cinematicCamera.equals(Config.FeaturesGroup.CinematicCameraOptions.OFF)) return m;
		if (ZoomUtils.zoomState) {
			this.cursorYZoomSmoother.smooth(this.cursorDeltaY * this.adjustedG, (this.extractedE * this.adjustedG));
			if (this.client.options.smoothCameraEnabled) this.cursorYZoomSmoother.clear();

			if (Config.features.cinematicCamera.equals(Config.FeaturesGroup.CinematicCameraOptions.MULTIPLIED))
				m *= Config.values.cinematicMultiplier;

		} else this.cursorYZoomSmoother.clear();
		return m;
	}

	//Handles zoom scrolling.
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
		if (this.eventDeltaWheel == 0.0 || !Config.features.zoomScrolling) return;
		if (Config.FeaturesGroup.zoomMode.equals(Config.FeaturesGroup.ZoomModes.PERSISTENT))
			if (!ZoomKeybinds.zoomKey.isPressed()) return;

		if (ZoomUtils.zoomState) {
			ZoomUtils.changeZoomDivisor(this.eventDeltaWheel > 0.0);
			info.cancel();
		}
	}

	//Handles the zoom scrolling reset through the middle button.
	@Inject(
			at = @At(
					value = "INVOKE",
					target = "net/minecraft/client/option/KeyBinding.setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V"
			),
			method = "onMouseButton(JIII)V",
			cancellable = true,
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void onMouseButton(long window, int button, int action, int mods, CallbackInfo info, boolean bl, int i) {
		if (!Config.features.zoomScrolling) return;
		if (Config.FeaturesGroup.zoomMode.equals(Config.FeaturesGroup.ZoomModes.PERSISTENT))
			if (!ZoomKeybinds.zoomKey.isPressed()) return; // is this needed?

		if (button == 2 && bl && ZoomKeybinds.zoomKey.isPressed()) {
			if (Config.features.resetZoomWithMouse) {
				ZoomUtils.resetZoomDivisor();
				info.cancel();
			}
		}
	}
}
