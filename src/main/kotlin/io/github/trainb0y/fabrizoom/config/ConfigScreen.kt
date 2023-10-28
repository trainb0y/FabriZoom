package io.github.trainb0y.fabrizoom.config

import dev.isxander.yacl3.api.Binding
import dev.isxander.yacl3.api.ButtonOption
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.OptionGroup
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.gui.YACLScreen
import dev.isxander.yacl3.gui.controllers.BooleanController
import dev.isxander.yacl3.gui.controllers.TickBoxController
import dev.isxander.yacl3.gui.controllers.slider.DoubleSliderController
import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text


fun openConfigScreen(parent: Screen?): Screen {
	var preset = Presets.DEFAULT // currently selected preset

	// Welcome to Builder Hell:tm:
	// Have fun!

	// tbh the SpruceUI config screen was a lot cleaner
	// but it was causing too many issues.

	return YetAnotherConfigLib.createBuilder()
		.title(Text.literal("config.fabrizoom.title"))
		.category(ConfigCategory.createBuilder()
			.name(Text.translatable("category.fabrizoom.basic"))
			.group(OptionGroup.createBuilder()
				.name(Text.translatable("config.fabrizoom.general"))
				.option(Option.createBuilder<Double>()
					.name(Text.translatable("config.fabrizoom.zoomdivisor"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.zoomdivisor.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values!!.zoomDivisor,
							{ ConfigHandler.values.zoomDivisor },
							{ value -> ConfigHandler.values.zoomDivisor = value }
						)
					)
					.customController { option ->
						DoubleSliderController(
							option,
							1.5,
							8.0,
							0.1
						)
					}
					.build()
				)
				.option(Option.createBuilder<Double>()
					.name(Text.translatable("config.fabrizoom.minzoomdivisor"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.minzoomdivisor.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.minimumZoomDivisor,
							{ ConfigHandler.values.minimumZoomDivisor },
							{ value -> ConfigHandler.values.minimumZoomDivisor = value }
						)
					)
					.customController { option ->
						DoubleSliderController(
							option,
							1.5,
							4.0,
							0.1
						)
					}
					.build()
				)
				.option(Option.createBuilder<Double>()
					.name(Text.translatable("config.fabrizoom.maxzoomdivisor"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.maxzoomdivisor.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.maximumZoomDivisor,
							{ ConfigHandler.values.maximumZoomDivisor },
							{ value -> ConfigHandler.values.maximumZoomDivisor = value }
						)
					)
					.customController { option ->
						DoubleSliderController(
							option,
							4.0,
							40.0,
							0.1
						)
					}
					.build()
				)
				.build()
			)
			.group(OptionGroup.createBuilder()
				.name(Text.translatable("config.fabrizoom.preferences"))
				.option(Option.createBuilder<ZoomOverlay>()
					.name(Text.translatable("config.fabrizoom.overlay"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.overlay.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.zoomOverlay,
							{ ConfigHandler.values.zoomOverlay },
							{ value -> ConfigHandler.values.zoomOverlay = value }
						)
					)
					.controller { option ->
						EnumControllerBuilder.create(option)
							.enumClass(ZoomOverlay::class.java)
							.formatValue { e: ZoomOverlay -> Text.translatable(e.translationKey) }
					}
					.build()
				)
				.option(Option.createBuilder<Boolean>()
					.name(Text.translatable("config.fabrizoom.scrolling"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.scrolling.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.zoomScroll,
							{ ConfigHandler.values.zoomScroll },
							{ value -> ConfigHandler.values.zoomScroll = value }
						)
					)
					.customController { option ->
						TickBoxController(option)
					}
					.build()
				)
				.option(Option.createBuilder<Boolean>()
					.name(Text.translatable("config.fabrizoom.zoomsound"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.zoomsound.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.zoomSound,
							{ ConfigHandler.values.zoomSound },
							{ value -> ConfigHandler.values.zoomSound = value }
						)
					)
					.customController { option ->
						TickBoxController(option)
					}
					.build()
				)
				.build()
			)
			.group(OptionGroup.createBuilder()
				.name(Text.translatable("config.fabrizoom.presets"))
				.option(Option.createBuilder<Presets>()
					.name(Text.translatable("config.fabrizoom.preset.select"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.preset.select.tooltip")))
					.instant(true)
					.binding(
						Presets.CUSTOM,
						{ preset },
						{ value -> preset = value }
					)
					.controller { option ->
						EnumControllerBuilder.create(option)
							.enumClass(Presets::class.java)
							.formatValue { e: Presets -> Text.translatable(e.translationKey) }
					}
					.build()
				)
				.option(ButtonOption.createBuilder()
					.name(Text.translatable("config.fabrizoom.preset.apply"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.preset.apply.tooltip")))
					.action { screen: YACLScreen?, _: ButtonOption? ->
						ConfigHandler.values = preset.values?.copy() ?: ConfigHandler.values
						ConfigHandler.saveConfig()
						screen?.close()
					}
					.build())
				.build()
			)
			.build()
		)
		.category(ConfigCategory.createBuilder()
			.name(Text.translatable("category.fabrizoom.advanced"))
			.group(OptionGroup.createBuilder()
				.name(Text.translatable("config.fabrizoom.mouse.normal.title"))
				.option(Option.createBuilder<Int>()
					.name(Text.translatable("config.fabrizoom.mouse.sensitivity"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.mouse.sensitivity.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.mouseSensitivity,
							{ ConfigHandler.values.mouseSensitivity },
							{ value -> ConfigHandler.values.mouseSensitivity = value }
						)
					)
					.customController { option ->
						IntegerSliderController(
							option,
							1,
							100,
							1
						)
					}
					.build()
				)
				.build()
			)
			.group(OptionGroup.createBuilder()
				.name(Text.translatable("config.fabrizoom.mouse.cinematic.title"))
				.option(
					Option.createBuilder<Boolean>()
						.name(Text.translatable("config.fabrizoom.mouse.cinematic"))
						.description(OptionDescription.of(Text.translatable("config.fabrizoom.mouse.cinematic.tooltip")))
						.binding(
							Binding.generic(
								Presets.DEFAULT.values.cinematicCameraEnabled,
								{ ConfigHandler.values.cinematicCameraEnabled },
								{ value -> ConfigHandler.values.cinematicCameraEnabled = value }
							)
						)
						.customController { option ->
							BooleanController(option)
						}
						.build()
				)
				.option(Option.createBuilder<Double>()
					.name(Text.translatable("config.fabrizoom.mouse.cinematicmultiplier"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.mouse.cinematicmultiplier.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.cinematicCameraMultiplier,
							{ ConfigHandler.values.cinematicCameraMultiplier },
							{ value -> ConfigHandler.values.cinematicCameraMultiplier = value }
						)
					)
					.customController { option ->
						DoubleSliderController(
							option,
							0.1,
							10.0,
							0.1
						)
					}
					.build()
				)
				.build()
			)
			.group(OptionGroup.createBuilder()
				.name(Text.translatable("config.fabrizoom.transition"))
				.option(Option.createBuilder<ZoomTransition>()
					.name(Text.translatable("config.fabrizoom.transition"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.transition.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.transition,
							{ ConfigHandler.values.transition },
							{ value -> ConfigHandler.values.transition = value }
						)
					)
					.controller { option ->
						EnumControllerBuilder.create(option)
							.enumClass(ZoomTransition::class.java)
							.formatValue { e: ZoomTransition -> Text.translatable(e.translationKey) }
					}
					.build()
				)
				.option(Option.createBuilder<Double>()
					.name(Text.translatable("config.fabrizoom.linearstep.min"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.linearstep.min.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.minimumLinearStep,
							{ ConfigHandler.values.minimumLinearStep },
							{ value ->
								ConfigHandler.values.minimumLinearStep = value.coerceIn(0.0, ConfigHandler.values.maximumLinearStep)
							}
						)
					)
					.customController { option ->
						DoubleSliderController(
							option,
							0.01,
							1.0,
							0.01
						)
					}
					.build()
				)
				.option(Option.createBuilder<Double>()
					.name(Text.translatable("config.fabrizoom.linearstep.max"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.linearstep.max.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.maximumLinearStep,
							{ ConfigHandler.values.maximumLinearStep },
							{ value ->
								ConfigHandler.values.maximumLinearStep = value.coerceIn(ConfigHandler.values.minimumLinearStep, 1.0)
							}
						)
					)
					.customController { option ->
						DoubleSliderController(
							option,
							0.01,
							1.0,
							0.01
						)
					}
					.build()
				)
				.option(Option.createBuilder<Float>()
					.name(Text.translatable("config.fabrizoom.smoothmultiplier"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.smoothmultiplier.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.smoothMultiplier,
							{ ConfigHandler.values.smoothMultiplier },
							{ value -> ConfigHandler.values.smoothMultiplier = value }
						)
					)
					.customController { option ->
						FloatSliderController(
							option,
							0.1f,
							1f,
							0.1f
						)
					}
					.build()
				)
				.build()
			)
			.build()
		)
		.save(ConfigHandler::saveConfig)
		.build()
		.generateScreen(parent)
}