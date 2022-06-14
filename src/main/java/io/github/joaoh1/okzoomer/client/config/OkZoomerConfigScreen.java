package io.github.joaoh1.okzoomer.client.config;

import io.github.joaoh1.okzoomer.client.config.Config.FeaturesGroup;
import io.github.joaoh1.okzoomer.client.utils.ZoomUtils;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class OkZoomerConfigScreen {
	public static Screen getConfigScreen(Screen parentScreen) {
		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parentScreen)
				.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/yellow_concrete.png"))
				.setTitle(new TranslatableText("config.okzoomer.title"));

		builder.setGlobalized(true);
		builder.setGlobalizedExpanded(false);

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		ConfigCategory features = builder.getOrCreateCategory(new TranslatableText("config.okzoomer.category.features"));

		features.addEntry(entryBuilder.startSelector(new TranslatableText("config.okzoomer.cinematic_camera"), FeaturesGroup.CinematicCameraOptions.values(), Config.features.cinematicCamera)
				.setDefaultValue(FeaturesGroup.CinematicCameraOptions.OFF)
				.setNameProvider(value -> {
					if (value.equals(FeaturesGroup.CinematicCameraOptions.OFF)) {
						return new TranslatableText("config.okzoomer.cinematic_camera.off");
					} else if (value.equals(FeaturesGroup.CinematicCameraOptions.VANILLA)) {
						return new TranslatableText("config.okzoomer.cinematic_camera.vanilla");
					} else if (value.equals(FeaturesGroup.CinematicCameraOptions.MULTIPLIED)) {
						return new TranslatableText("config.okzoomer.cinematic_camera.multiplied");
					}
					return new LiteralText("Error");
				})
				.setSaveConsumer(value -> {
					Config.features.cinematicCamera = (FeaturesGroup.CinematicCameraOptions) value;
				})
				.setTooltip(new TranslatableText[]{
						new TranslatableText("config.okzoomer.cinematic_camera.tooltip"),
						new TranslatableText("config.okzoomer.cinematic_camera.tooltip.off"),
						new TranslatableText("config.okzoomer.cinematic_camera.tooltip.vanilla"),
						new TranslatableText("config.okzoomer.cinematic_camera.tooltip.multiplied")
				})
				.build());

		features.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("config.okzoomer.reduce_sensitivity"), Config.features.reduceSensitivity)
				.setDefaultValue(true)
				.setSaveConsumer(value -> {
					Config.features.reduceSensitivity = value;
				})
				.setTooltip(new TranslatableText("config.okzoomer.reduce_sensitivity.tooltip"))
				.build());

		features.addEntry(entryBuilder.startSelector(new TranslatableText("config.okzoomer.zoom_transition"), FeaturesGroup.ZoomTransitionOptions.values(), Config.features.zoomTransition)
				.setDefaultValue(FeaturesGroup.ZoomTransitionOptions.SMOOTH)
				.setNameProvider(value -> {
					if (value.equals(FeaturesGroup.ZoomTransitionOptions.OFF)) {
						return new TranslatableText("config.okzoomer.zoom_transition.off");
					} else if (value.equals(FeaturesGroup.ZoomTransitionOptions.SMOOTH)) {
						return new TranslatableText("config.okzoomer.zoom_transition.smooth");
					} else if (value.equals(FeaturesGroup.ZoomTransitionOptions.LINEAR)) {
						return new TranslatableText("config.okzoomer.zoom_transition.linear");
					}
					return new LiteralText("Error");
				})
				.setSaveConsumer(value -> {
					Config.features.zoomTransition = (FeaturesGroup.ZoomTransitionOptions) value;
				})
				.setTooltip(new TranslatableText[]{
						new TranslatableText("config.okzoomer.zoom_transition.tooltip"),
						new TranslatableText("config.okzoomer.zoom_transition.tooltip.off"),
						new TranslatableText("config.okzoomer.zoom_transition.tooltip.smooth"),
						new TranslatableText("config.okzoomer.zoom_transition.tooltip.linear")
				})
				.build());

		features.addEntry(entryBuilder.startSelector(new TranslatableText("config.okzoomer.zoom_mode"), FeaturesGroup.ZoomModes.values(), Config.features.zoomMode)
				.setDefaultValue(FeaturesGroup.ZoomModes.HOLD)
				.setNameProvider(value -> {
					if (value.equals(FeaturesGroup.ZoomModes.HOLD)) {
						return new TranslatableText("options.key.hold");
					} else if (value.equals(FeaturesGroup.ZoomModes.TOGGLE)) {
						return new TranslatableText("options.key.toggle");
					} else if (value.equals(FeaturesGroup.ZoomModes.PERSISTENT)) {
						return new TranslatableText("config.okzoomer.zoom_mode.persistent");
					}
					return new LiteralText("Error");
				})
				.setSaveConsumer(value -> {
					Config.features.zoomMode = (FeaturesGroup.ZoomModes) value;
				})
				.setTooltip(new TranslatableText("config.okzoomer.zoom_mode.tooltip"))
				.setTooltip(new TranslatableText[]{
						new TranslatableText("config.okzoomer.zoom_mode.tooltip"),
						new TranslatableText("config.okzoomer.zoom_mode.tooltip.hold"),
						new TranslatableText("config.okzoomer.zoom_mode.tooltip.toggle"),
						new TranslatableText("config.okzoomer.zoom_mode.tooltip.persistent")
				})
				.build());

		features.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("config.okzoomer.zoom_scrolling"), Config.features.zoomScrolling)
				.setDefaultValue(true)
				.setSaveConsumer(value -> {
					Config.features.zoomScrolling = value;
				})
				.setTooltip(new TranslatableText[]{
						new TranslatableText("config.okzoomer.zoom_scrolling.tooltip")
				})
				.build());

		features.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("config.okzoomer.extra_keybinds"), Config.features.extraKeybinds)
				.requireRestart()
				.setDefaultValue(true)
				.setSaveConsumer(value -> {
					Config.features.extraKeybinds = value;
				})
				.setTooltip(new TranslatableText[]{
						new TranslatableText("config.okzoomer.extra_keybinds.tooltip"),
						new TranslatableText("config.okzoomer.extra_keybinds.tooltip.warning")
				})
				.build());

		features.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("config.okzoomer.zoom_overlay"), Config.features.zoomOverlay)
				.setDefaultValue(false)
				.setSaveConsumer(value -> {
					Config.features.zoomOverlay = value;
				})
				.setTooltip(new TranslatableText[]{
						new TranslatableText("config.okzoomer.zoom_overlay.tooltip.1"),
						new TranslatableText("config.okzoomer.zoom_overlay.tooltip.2"),
						new TranslatableText("config.okzoomer.zoom_overlay.tooltip.3")
				})
				.build());

		ConfigCategory values = builder.getOrCreateCategory(new TranslatableText("config.okzoomer.category.values"))
				.setCategoryBackground(new Identifier("minecraft:textures/block/yellow_concrete_powder.png"));

		values.addEntry(entryBuilder.startDoubleField(new TranslatableText("config.okzoomer.zoom_divisor"), Config.values.zoomDivisor)
				.setDefaultValue(4.0)
				.setMin(Double.MIN_VALUE)
				.setSaveConsumer(value -> {
					Config.values.zoomDivisor = value;
				})
				.setTooltip(new TranslatableText("config.okzoomer.zoom_divisor.tooltip"))
				.build());

		values.addEntry(entryBuilder.startDoubleField(new TranslatableText("config.okzoomer.minimum_zoom_divisor"), Config.values.minimumZoomDivisor)
				.setDefaultValue(1.0)
				.setMin(Double.MIN_VALUE)
				.setSaveConsumer(value -> {
					Config.values.minimumZoomDivisor = value;
				})
				.setTooltip(new TranslatableText("config.okzoomer.minimum_zoom_divisor.tooltip"))
				.build());

		values.addEntry(entryBuilder.startDoubleField(new TranslatableText("config.okzoomer.maximum_zoom_divisor"), Config.values.maximumZoomDivisor)
				.setDefaultValue(50.0)
				.setMin(Double.MIN_VALUE)
				.setSaveConsumer(value -> {
					Config.values.maximumZoomDivisor = value;
				})
				.setTooltip(new TranslatableText("config.okzoomer.maximum_zoom_divisor.tooltip"))
				.build());

		values.addEntry(entryBuilder.startDoubleField(new TranslatableText("config.okzoomer.scroll_step"), Config.values.scrollStep)
				.setDefaultValue(1.0)
				.setMin(0.0)
				.setSaveConsumer(value -> {
					Config.values.scrollStep = value;
				})
				.setTooltip(new TranslatableText[]{
						new TranslatableText("config.okzoomer.scroll_step.tooltip.1"),
						new TranslatableText("config.okzoomer.scroll_step.tooltip.2")
				})
				.build());

		values.addEntry(entryBuilder.startDoubleField(new TranslatableText("config.okzoomer.lesser_scroll_step"), Config.values.lesserScrollStep)
				.setDefaultValue(0.5)
				.setMin(0.0)
				.setSaveConsumer(value -> {
					Config.values.lesserScrollStep = value;
				})
				.setTooltip(new TranslatableText[]{
						new TranslatableText("config.okzoomer.lesser_scroll_step.tooltip.1"),
						new TranslatableText("config.okzoomer.lesser_scroll_step.tooltip.2")
				})
				.build());

		values.addEntry(entryBuilder.startDoubleField(new TranslatableText("config.okzoomer.cinematic_multiplier"), Config.values.cinematicMultiplier)
				.setDefaultValue(4.0)
				.setMin(Double.MIN_VALUE)
				.setSaveConsumer(value -> {
					Config.values.cinematicMultiplier = value;
				})
				.setTooltip(new TranslatableText("config.okzoomer.cinematic_multiplier.tooltip"))
				.build());

		values.addEntry(entryBuilder.startDoubleField(new TranslatableText("config.okzoomer.smooth_multiplier"), Config.values.smoothMultiplier)
				.setDefaultValue(0.75)
				.setMin(Double.MIN_VALUE)
				.setMax(1.0)
				.setSaveConsumer(value -> {
					Config.values.smoothMultiplier = value;
				})
				.setTooltip(new TranslatableText("config.okzoomer.smooth_multiplier.tooltip"))
				.build());

		values.addEntry(entryBuilder.startDoubleField(new TranslatableText("config.okzoomer.minimum_linear_step"), Config.values.minimumLinearStep)
				.setDefaultValue(0.125)
				.setMin(0)
				.setSaveConsumer(value -> {
					Config.values.minimumLinearStep = value;
				})
				.setTooltip(new TranslatableText("config.okzoomer.minimum_linear_step.tooltip"))
				.build());

		values.addEntry(entryBuilder.startDoubleField(new TranslatableText("config.okzoomer.maximum_linear_step"), Config.values.maximumLinearStep)
				.setDefaultValue(0.25)
				.setMin(Double.MIN_VALUE)
				.setSaveConsumer(value -> {
					Config.values.maximumLinearStep = value;
				})
				.setTooltip(new TranslatableText("config.okzoomer.maximum_linear_step.tooltip"))
				.build());

		ConfigCategory tweaks = builder.getOrCreateCategory(new TranslatableText("config.okzoomer.category.tweaks"))
				.setCategoryBackground(new Identifier("minecraft:textures/block/yellow_glazed_terracotta.png"));

		tweaks.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("config.okzoomer.reset_zoom_with_mouse"), Config.tweaks.resetZoomWithMouse)
				.setDefaultValue(true)
				.setSaveConsumer(value -> {
					Config.tweaks.resetZoomWithMouse = value;
				})
				.setTooltip(new TranslatableText("config.okzoomer.reset_zoom_with_mouse.tooltip"))
				.build());

		tweaks.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("config.okzoomer.unbind_conflicting_key"), Config.tweaks.unbindConflictingKey)
				.setDefaultValue(false)
				.setSaveConsumer(value -> {
					if (value.equals(true)) {
						MinecraftClient client = MinecraftClient.getInstance();
						ZoomUtils.unbindConflictingKey(client, true);
					}
				})
				.setTooltip(new TranslatableText[]{
						new TranslatableText("config.okzoomer.unbind_conflicting_key.tooltip.1"),
						new TranslatableText("config.okzoomer.unbind_conflicting_key.tooltip.2")
				})
				.build());

		tweaks.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("config.okzoomer.print_owo_on_start"), Config.tweaks.printOwoOnStart)
				.setDefaultValue(false)
				.setSaveConsumer(value -> {
					Config.tweaks.printOwoOnStart = value;
				})
				.setTooltip(new TranslatableText("config.okzoomer.print_owo_on_start.tooltip"))
				.build());

		tweaks.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("config.okzoomer.hide_zoom_overlay"), Config.tweaks.hideZoomOverlay)
				.setDefaultValue(false)
				.setSaveConsumer(value -> {
					Config.tweaks.hideZoomOverlay = value;
				})
				.setTooltip(new TranslatableText("config.okzoomer.hide_zoom_overlay.tooltip"))
				.build());

		ConfigCategory presets = builder.getOrCreateCategory(new TranslatableText("config.okzoomer.category.presets"))
				.setCategoryBackground(new Identifier("minecraft:textures/block/yellow_wool.png"));

		String[] presetArray = new String[]{"None", "Default", "Classic", "Persistent"};
		presets.addEntry(entryBuilder.startSelector(new TranslatableText("config.okzoomer.reset_to_preset"), presetArray, presetArray[0])
				.setSaveConsumer(value -> {
					if (value.equals("Default")) {
						Config.features.cinematicCamera = FeaturesGroup.CinematicCameraOptions.OFF;
						Config.features.reduceSensitivity = true;
						Config.features.zoomTransition = FeaturesGroup.ZoomTransitionOptions.SMOOTH;
						Config.features.zoomMode = FeaturesGroup.ZoomModes.HOLD;
						Config.features.zoomScrolling = true;
						Config.features.extraKeybinds = true;
						Config.features.zoomOverlay = false;
						Config.values.zoomDivisor = 4.0;
						Config.values.minimumZoomDivisor = 1.0;
						Config.values.maximumZoomDivisor = 50.0;
						Config.values.scrollStep = 1.0;
						Config.values.lesserScrollStep = 0.5;
						Config.values.cinematicMultiplier = 4.0;
						Config.values.smoothMultiplier = 0.75;
						Config.values.minimumLinearStep = 0.125;
						Config.values.maximumLinearStep = 0.25;
						Config.tweaks.resetZoomWithMouse = true;
						Config.tweaks.printOwoOnStart = false;
					} else if (value.equals("Classic")) {
						Config.features.cinematicCamera = FeaturesGroup.CinematicCameraOptions.VANILLA;
						Config.features.reduceSensitivity = false;
						Config.features.zoomTransition = FeaturesGroup.ZoomTransitionOptions.OFF;
						Config.features.zoomMode = FeaturesGroup.ZoomModes.HOLD;
						Config.features.zoomScrolling = false;
						Config.features.extraKeybinds = false;
						Config.features.zoomOverlay = false;
						Config.values.zoomDivisor = 4.0;
						Config.values.minimumZoomDivisor = 1.0;
						Config.values.maximumZoomDivisor = 50.0;
						Config.values.scrollStep = 1.0;
						Config.values.lesserScrollStep = 0.5;
						Config.values.cinematicMultiplier = 4.0;
						Config.values.smoothMultiplier = 0.75;
						Config.values.minimumLinearStep = 0.125;
						Config.values.maximumLinearStep = 0.25;
						Config.tweaks.resetZoomWithMouse = false;
						Config.tweaks.printOwoOnStart = false;
					} else if (value.equals("Persistent")) {
						Config.features.cinematicCamera = FeaturesGroup.CinematicCameraOptions.OFF;
						Config.features.reduceSensitivity = true;
						Config.features.zoomTransition = FeaturesGroup.ZoomTransitionOptions.SMOOTH;
						Config.features.zoomMode = FeaturesGroup.ZoomModes.PERSISTENT;
						Config.features.zoomScrolling = true;
						Config.features.extraKeybinds = true;
						Config.features.zoomOverlay = false;
						Config.values.zoomDivisor = 1.0;
						Config.values.minimumZoomDivisor = 1.0;
						Config.values.maximumZoomDivisor = 50.0;
						Config.values.scrollStep = 1.0;
						Config.values.lesserScrollStep = 0.5;
						Config.values.cinematicMultiplier = 4.0;
						Config.values.smoothMultiplier = 0.75;
						Config.values.minimumLinearStep = 0.125;
						Config.values.maximumLinearStep = 0.25;
						Config.tweaks.resetZoomWithMouse = true;
						Config.tweaks.printOwoOnStart = false;
					}
					value = presetArray[0];
				})
				.setNameProvider(value -> {
					if (value.equals("None")) {
						return new TranslatableText("config.okzoomer.reset_to_preset.none");
					} else if (value.equals("Default")) {
						return new TranslatableText("config.okzoomer.reset_to_preset.default");
					} else if (value.equals("Classic")) {
						return new TranslatableText("config.okzoomer.reset_to_preset.classic");
					} else if (value.equals("Persistent")) {
						return new TranslatableText("config.okzoomer.reset_to_preset.persistent");
					}
					return new LiteralText("Error");
				})
				.setTooltip(new TranslatableText[]{
						new TranslatableText("config.okzoomer.reset_to_preset.tooltip"),
						new TranslatableText("config.okzoomer.reset_to_preset.tooltip.none"),
						new TranslatableText("config.okzoomer.reset_to_preset.tooltip.default"),
						new TranslatableText("config.okzoomer.reset_to_preset.tooltip.classic"),
						new TranslatableText("config.okzoomer.reset_to_preset.tooltip.persistent")
				})
				.build());

		builder.setSavingRunnable(() -> {
			// OkZoomerConfig.saveModConfig();
		});

		return builder.build();
	}
}
