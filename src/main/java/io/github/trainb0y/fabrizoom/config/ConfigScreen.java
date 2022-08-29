package io.github.trainb0y.fabrizoom.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ConfigScreen {
	public static Screen getConfigScreen(Screen parentScreen) {
		ConfigBuilder builder = ConfigBuilder.create()
				.setParentScreen(parentScreen)
				.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/yellow_concrete.png"))
				.setTitle(Text.translatable("config.fabrizoom.title"));

		builder.setGlobalized(true);
		builder.setGlobalizedExpanded(false);

		ConfigEntryBuilder entryBuilder = builder.entryBuilder();

		ConfigCategory features = builder.getOrCreateCategory(Text.translatable("config.fabrizoom.category.features"));

		features.addEntry(entryBuilder.startSelector(Text.translatable("config.fabrizoom.cinematic_camera"), Config.FeaturesGroup.CinematicCameraOptions.values(), Config.features.cinematicCamera)
				.setDefaultValue(Config.FeaturesGroup.CinematicCameraOptions.OFF)
				.setNameProvider(value -> {
					if (value.equals(Config.FeaturesGroup.CinematicCameraOptions.OFF)) {
						return Text.translatable("config.fabrizoom.cinematic_camera.off");
					} else if (value.equals(Config.FeaturesGroup.CinematicCameraOptions.VANILLA)) {
						return Text.translatable("config.fabrizoom.cinematic_camera.vanilla");
					} else if (value.equals(Config.FeaturesGroup.CinematicCameraOptions.MULTIPLIED)) {
						return Text.translatable("config.fabrizoom.cinematic_camera.multiplied");
					}
					return Text.literal("Error");
				})
				.setSaveConsumer(value -> {
					Config.features.cinematicCamera = value;
				})
				.setTooltip(new Text[]{
						Text.translatable("config.fabrizoom.cinematic_camera.tooltip"),
						Text.translatable("config.fabrizoom.cinematic_camera.tooltip.off"),
						Text.translatable("config.fabrizoom.cinematic_camera.tooltip.vanilla"),
						Text.translatable("config.fabrizoom.cinematic_camera.tooltip.multiplied")
				})
				.build());

		features.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.fabrizoom.reduce_sensitivity"), Config.features.reduceSensitivity)
				.setDefaultValue(true)
				.setSaveConsumer(value -> {
					Config.features.reduceSensitivity = value;
				})
				.setTooltip(Text.translatable("config.fabrizoom.reduce_sensitivity.tooltip"))
				.build());

		features.addEntry(entryBuilder.startSelector(Text.translatable("config.fabrizoom.zoom_transition"), Config.FeaturesGroup.ZoomTransitionOptions.values(), Config.features.zoomTransition)
				.setDefaultValue(Config.FeaturesGroup.ZoomTransitionOptions.SMOOTH)
				.setNameProvider(value -> {
					if (value.equals(Config.FeaturesGroup.ZoomTransitionOptions.OFF)) {
						return Text.translatable("config.fabrizoom.zoom_transition.off");
					} else if (value.equals(Config.FeaturesGroup.ZoomTransitionOptions.SMOOTH)) {
						return Text.translatable("config.fabrizoom.zoom_transition.smooth");
					} else if (value.equals(Config.FeaturesGroup.ZoomTransitionOptions.LINEAR)) {
						return Text.translatable("config.fabrizoom.zoom_transition.linear");
					}
					return Text.literal("Error");
				})
				.setSaveConsumer(value -> {
					Config.features.zoomTransition = value;
				})
				.setTooltip(new Text[]{
						Text.translatable("config.fabrizoom.zoom_transition.tooltip"),
						Text.translatable("config.fabrizoom.zoom_transition.tooltip.off"),
						Text.translatable("config.fabrizoom.zoom_transition.tooltip.smooth"),
						Text.translatable("config.fabrizoom.zoom_transition.tooltip.linear")
				})
				.build());

		features.addEntry(entryBuilder.startSelector(Text.translatable("config.fabrizoom.zoom_mode"), Config.FeaturesGroup.ZoomModes.values(), Config.FeaturesGroup.zoomMode)
				.setDefaultValue(Config.FeaturesGroup.ZoomModes.HOLD)
				.setNameProvider(value -> {
					if (value.equals(Config.FeaturesGroup.ZoomModes.HOLD)) {
						return Text.translatable("options.key.hold");
					} else if (value.equals(Config.FeaturesGroup.ZoomModes.TOGGLE)) {
						return Text.translatable("options.key.toggle");
					} else if (value.equals(Config.FeaturesGroup.ZoomModes.PERSISTENT)) {
						return Text.translatable("config.fabrizoom.zoom_mode.persistent");
					}
					return Text.literal("Error");
				})
				.setSaveConsumer(value -> {
					Config.FeaturesGroup.zoomMode = value;
				})
				.setTooltip(Text.translatable("config.fabrizoom.zoom_mode.tooltip"))
				.setTooltip(new Text[]{
						Text.translatable("config.fabrizoom.zoom_mode.tooltip"),
						Text.translatable("config.fabrizoom.zoom_mode.tooltip.hold"),
						Text.translatable("config.fabrizoom.zoom_mode.tooltip.toggle"),
						Text.translatable("config.fabrizoom.zoom_mode.tooltip.persistent")
				})
				.build());

		features.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.fabrizoom.zoom_scrolling"), Config.features.zoomScrolling)
				.setDefaultValue(true)
				.setSaveConsumer(value -> {
					Config.features.zoomScrolling = value;
				})
				.setTooltip(new Text[]{
						Text.translatable("config.fabrizoom.zoom_scrolling.tooltip")
				})
				.build());

		features.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.fabrizoom.reset_zoom_with_mouse"), Config.features.resetZoomWithMouse)
				.setDefaultValue(true)
				.setSaveConsumer(value -> {
					Config.features.resetZoomWithMouse = value;
				})
				.setTooltip(Text.translatable("config.fabrizoom.reset_zoom_with_mouse.tooltip"))
				.build());


		ConfigCategory values = builder.getOrCreateCategory(Text.translatable("config.fabrizoom.category.values"))
				.setCategoryBackground(new Identifier("minecraft:textures/block/yellow_concrete_powder.png"));

		values.addEntry(entryBuilder.startDoubleField(Text.translatable("config.fabrizoom.zoom_divisor"), Config.values.zoomDivisor)
				.setDefaultValue(4.0)
				.setMin(Double.MIN_VALUE)
				.setSaveConsumer(value -> {
					Config.values.zoomDivisor = value;
				})
				.setTooltip(Text.translatable("config.fabrizoom.zoom_divisor.tooltip"))
				.build());

		values.addEntry(entryBuilder.startDoubleField(Text.translatable("config.fabrizoom.minimum_zoom_divisor"), Config.values.minimumZoomDivisor)
				.setDefaultValue(1.0)
				.setMin(Double.MIN_VALUE)
				.setSaveConsumer(value -> {
					Config.values.minimumZoomDivisor = value;
				})
				.setTooltip(Text.translatable("config.fabrizoom.minimum_zoom_divisor.tooltip"))
				.build());

		values.addEntry(entryBuilder.startDoubleField(Text.translatable("config.fabrizoom.maximum_zoom_divisor"), Config.values.maximumZoomDivisor)
				.setDefaultValue(50.0)
				.setMin(Double.MIN_VALUE)
				.setSaveConsumer(value -> {
					Config.values.maximumZoomDivisor = value;
				})
				.setTooltip(Text.translatable("config.fabrizoom.maximum_zoom_divisor.tooltip"))
				.build());

		values.addEntry(entryBuilder.startDoubleField(Text.translatable("config.fabrizoom.scroll_step"), Config.values.scrollStep)
				.setDefaultValue(1.0)
				.setMin(0.0)
				.setSaveConsumer(value -> {
					Config.values.scrollStep = value;
				})
				.setTooltip(new Text[]{
						Text.translatable("config.fabrizoom.scroll_step.tooltip.1"),
						Text.translatable("config.fabrizoom.scroll_step.tooltip.2")
				})
				.build());

		values.addEntry(entryBuilder.startDoubleField(Text.translatable("config.fabrizoom.lesser_scroll_step"), Config.values.lesserScrollStep)
				.setDefaultValue(0.5)
				.setMin(0.0)
				.setSaveConsumer(value -> {
					Config.values.lesserScrollStep = value;
				})
				.setTooltip(new Text[]{
						Text.translatable("config.fabrizoom.lesser_scroll_step.tooltip.1"),
						Text.translatable("config.fabrizoom.lesser_scroll_step.tooltip.2")
				})
				.build());

		values.addEntry(entryBuilder.startDoubleField(Text.translatable("config.fabrizoom.cinematic_multiplier"), Config.values.cinematicMultiplier)
				.setDefaultValue(4.0)
				.setMin(Double.MIN_VALUE)
				.setSaveConsumer(value -> {
					Config.values.cinematicMultiplier = value;
				})
				.setTooltip(Text.translatable("config.fabrizoom.cinematic_multiplier.tooltip"))
				.build());

		values.addEntry(entryBuilder.startDoubleField(Text.translatable("config.fabrizoom.smooth_multiplier"), Config.values.smoothMultiplier)
				.setDefaultValue(0.75)
				.setMin(Double.MIN_VALUE)
				.setMax(1.0)
				.setSaveConsumer(value -> {
					Config.values.smoothMultiplier = value;
				})
				.setTooltip(Text.translatable("config.fabrizoom.smooth_multiplier.tooltip"))
				.build());

		values.addEntry(entryBuilder.startDoubleField(Text.translatable("config.fabrizoom.minimum_linear_step"), Config.values.minimumLinearStep)
				.setDefaultValue(0.125)
				.setMin(0)
				.setSaveConsumer(value -> {
					Config.values.minimumLinearStep = value;
				})
				.setTooltip(Text.translatable("config.fabrizoom.minimum_linear_step.tooltip"))
				.build());

		values.addEntry(entryBuilder.startDoubleField(Text.translatable("config.fabrizoom.maximum_linear_step"), Config.values.maximumLinearStep)
				.setDefaultValue(0.25)
				.setMin(Double.MIN_VALUE)
				.setSaveConsumer(value -> {
					Config.values.maximumLinearStep = value;
				})
				.setTooltip(Text.translatable("config.fabrizoom.maximum_linear_step.tooltip"))
				.build());


		ConfigCategory other = builder.getOrCreateCategory(Text.translatable("config.fabrizoom.category.other"))
				.setCategoryBackground(new Identifier("minecraft:textures/block/yellow_wool.png"));

		String[] presetArray = new String[]{"None", "Default", "Classic", "Persistent"};
		other.addEntry(entryBuilder.startSelector(Text.translatable("config.fabrizoom.reset_to_preset"), presetArray, presetArray[0])
				.setSaveConsumer(value -> {
					if (value.equals("Default")) {
						Config.features.cinematicCamera = Config.FeaturesGroup.CinematicCameraOptions.OFF;
						Config.features.reduceSensitivity = true;
						Config.features.zoomTransition = Config.FeaturesGroup.ZoomTransitionOptions.SMOOTH;
						Config.FeaturesGroup.zoomMode = Config.FeaturesGroup.ZoomModes.HOLD;
						Config.features.zoomScrolling = true;
						Config.features.resetZoomWithMouse = true;
						Config.values.zoomDivisor = 4.0;
						Config.values.minimumZoomDivisor = 1.0;
						Config.values.maximumZoomDivisor = 50.0;
						Config.values.scrollStep = 1.0;
						Config.values.lesserScrollStep = 0.5;
						Config.values.cinematicMultiplier = 4.0;
						Config.values.smoothMultiplier = 0.75;
						Config.values.minimumLinearStep = 0.125;
						Config.values.maximumLinearStep = 0.25;
					} else if (value.equals("Classic")) {
						Config.features.cinematicCamera = Config.FeaturesGroup.CinematicCameraOptions.VANILLA;
						Config.features.reduceSensitivity = false;
						Config.features.zoomTransition = Config.FeaturesGroup.ZoomTransitionOptions.OFF;
						Config.FeaturesGroup.zoomMode = Config.FeaturesGroup.ZoomModes.HOLD;
						Config.features.zoomScrolling = false;
						Config.features.resetZoomWithMouse = false;
						Config.values.zoomDivisor = 4.0;
						Config.values.minimumZoomDivisor = 1.0;
						Config.values.maximumZoomDivisor = 50.0;
						Config.values.scrollStep = 1.0;
						Config.values.lesserScrollStep = 0.5;
						Config.values.cinematicMultiplier = 4.0;
						Config.values.smoothMultiplier = 0.75;
						Config.values.minimumLinearStep = 0.125;
						Config.values.maximumLinearStep = 0.25;
					} else if (value.equals("Persistent")) {
						Config.features.cinematicCamera = Config.FeaturesGroup.CinematicCameraOptions.OFF;
						Config.features.reduceSensitivity = true;
						Config.features.zoomTransition = Config.FeaturesGroup.ZoomTransitionOptions.SMOOTH;
						Config.FeaturesGroup.zoomMode = Config.FeaturesGroup.ZoomModes.PERSISTENT;
						Config.features.zoomScrolling = true;
						Config.features.resetZoomWithMouse = true;
						Config.values.zoomDivisor = 1.0;
						Config.values.minimumZoomDivisor = 1.0;
						Config.values.maximumZoomDivisor = 50.0;
						Config.values.scrollStep = 1.0;
						Config.values.lesserScrollStep = 0.5;
						Config.values.cinematicMultiplier = 4.0;
						Config.values.smoothMultiplier = 0.75;
						Config.values.minimumLinearStep = 0.125;
						Config.values.maximumLinearStep = 0.25;
					}
					value = presetArray[0];
				})
				.setNameProvider(value -> {
					if (value.equals("None")) {
						return Text.translatable("config.fabrizoom.reset_to_preset.none");
					} else if (value.equals("Default")) {
						return Text.translatable("config.fabrizoom.reset_to_preset.default");
					} else if (value.equals("Classic")) {
						return Text.translatable("config.fabrizoom.reset_to_preset.classic");
					} else if (value.equals("Persistent")) {
						return Text.translatable("config.fabrizoom.reset_to_preset.persistent");
					}
					return Text.literal("Error");
				})
				.setTooltip(new Text[]{
						Text.translatable("config.fabrizoom.reset_to_preset.tooltip"),
						Text.translatable("config.fabrizoom.reset_to_preset.tooltip.none"),
						Text.translatable("config.fabrizoom.reset_to_preset.tooltip.default"),
						Text.translatable("config.fabrizoom.reset_to_preset.tooltip.classic"),
						Text.translatable("config.fabrizoom.reset_to_preset.tooltip.persistent")
				})
				.build());

		builder.setSavingRunnable(() -> {
			// fabrizoomConfig.saveModConfig();
		});

		return builder.build();
	}
}
