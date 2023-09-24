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
							{ Config.values.zoomDivisor },
							{ value -> Config.values.zoomDivisor = value }
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
							{ Config.values.minimumZoomDivisor },
							{ value -> Config.values.minimumZoomDivisor = value }
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
							{ Config.values.maximumZoomDivisor },
							{ value -> Config.values.maximumZoomDivisor = value }
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
				.option(Option.createBuilder<Config.ZoomOverlay>()
					.name(Text.translatable("config.fabrizoom.overlay"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.overlay.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.zoomOverlay,
							{ Config.values.zoomOverlay },
							{ value -> Config.values.zoomOverlay = value }
						)
					)
					.controller { option ->
						EnumControllerBuilder.create(option)
							.enumClass(Config.ZoomOverlay::class.java)
							.valueFormatter { e: Config.ZoomOverlay -> Text.translatable(e.key) }
					}
					.build()
				)
				.option(Option.createBuilder<Boolean>()
					.name(Text.translatable("config.fabrizoom.scrolling"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.scrolling.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.zoomScroll,
							{ Config.values.zoomScroll },
							{ value -> Config.values.zoomScroll = value }
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
							{ Config.values.zoomSound },
							{ value -> Config.values.zoomSound = value }
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
							.valueFormatter { e: Presets -> Text.translatable(e.key) }
					}
					.build()
				)
				.option(ButtonOption.createBuilder()
					.name(Text.translatable("config.fabrizoom.preset.apply"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.preset.apply.tooltip")))
					.action { screen: YACLScreen?, _: ButtonOption? ->
						Config.values = preset.values?.copy() ?: Config.values
						Config.saveConfig()
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
							{ Config.values.mouseSensitivity },
							{ value -> Config.values.mouseSensitivity = value }
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
								{ Config.values.cinematicCameraEnabled },
								{ value -> Config.values.cinematicCameraEnabled = value }
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
							{ Config.values.cinematicCameraMultiplier },
							{ value -> Config.values.cinematicCameraMultiplier = value }
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
				.option(Option.createBuilder<Config.Transition>()
					.name(Text.translatable("config.fabrizoom.transition"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.transition.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.transition,
							{ Config.values.transition },
							{ value -> Config.values.transition = value }
						)
					)
					.controller { option ->
						EnumControllerBuilder.create(option)
							.enumClass(Config.Transition::class.java)
							.valueFormatter { e: Config.Transition -> Text.translatable(e.key) }
					}
					.build()
				)
				.option(Option.createBuilder<Double>()
					.name(Text.translatable("config.fabrizoom.linearstep.min"))
					.description(OptionDescription.of(Text.translatable("config.fabrizoom.linearstep.min.tooltip")))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.minimumLinearStep,
							{ Config.values.minimumLinearStep },
							{ value ->
								Config.values.minimumLinearStep = value.coerceIn(0.0, Config.values.maximumLinearStep)
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
							{ Config.values.maximumLinearStep },
							{ value ->
								Config.values.maximumLinearStep = value.coerceIn(Config.values.minimumLinearStep, 1.0)
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
							{ Config.values.smoothMultiplier },
							{ value -> Config.values.smoothMultiplier = value }
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
		.save(Config::saveConfig)
		.build()
		.generateScreen(parent)
}