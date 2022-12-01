package io.github.trainb0y.fabrizoom.config

import dev.isxander.yacl.api.Binding
import dev.isxander.yacl.api.ButtonOption
import dev.isxander.yacl.api.ConfigCategory
import dev.isxander.yacl.api.Option
import dev.isxander.yacl.api.OptionGroup
import dev.isxander.yacl.api.YetAnotherConfigLib
import dev.isxander.yacl.gui.YACLScreen
import dev.isxander.yacl.gui.controllers.ActionController
import dev.isxander.yacl.gui.controllers.BooleanController
import dev.isxander.yacl.gui.controllers.cycling.EnumController
import dev.isxander.yacl.gui.controllers.TickBoxController
import dev.isxander.yacl.gui.controllers.slider.DoubleSliderController
import dev.isxander.yacl.gui.controllers.slider.FloatSliderController
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController
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
				.option(Option.createBuilder(Double::class.java)
					.name(Text.translatable("config.fabrizoom.zoomdivisor"))
					.tooltip(Text.translatable("config.fabrizoom.zoomdivisor.tooltip"))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values!!.zoomDivisor,
							{ Config.values.zoomDivisor },
							{ value -> Config.values.zoomDivisor = value }
						)
					)
					.controller { option ->
						DoubleSliderController(
							option,
							1.5,
							8.0,
							0.1
						)
					}
					.build()
				)
				.option(Option.createBuilder(Double::class.java)
					.name(Text.translatable("config.fabrizoom.minzoomdivisor"))
					.tooltip(Text.translatable("config.fabrizoom.minzoomdivisor.tooltip"))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.minimumZoomDivisor,
							{ Config.values.minimumZoomDivisor },
							{ value -> Config.values.minimumZoomDivisor = value }
						)
					)
					.controller { option ->
						DoubleSliderController(
							option,
							1.5,
							4.0,
							0.1
						)
					}
					.build()
				)
				.option(Option.createBuilder(Double::class.java)
					.name(Text.translatable("config.fabrizoom.maxzoomdivisor"))
					.tooltip(Text.translatable("config.fabrizoom.maxzoomdivisor.tooltip"))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.maximumZoomDivisor,
							{ Config.values.maximumZoomDivisor },
							{ value -> Config.values.maximumZoomDivisor = value }
						)
					)
					.controller { option ->
						DoubleSliderController(
							option,
							4.0,
							10.0,
							0.1
						)
					}
					.build()
				)
				.build()
			)
			.group(OptionGroup.createBuilder()
				.name(Text.translatable("config.fabrizoom.preferences"))
				.option(Option.createBuilder(Boolean::class.java)
					.name(Text.translatable("config.fabrizoom.overlay"))
					.tooltip(Text.translatable("config.fabrizoom.overlay.tooltip"))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.zoomOverlayEnabled,
							{ Config.values.zoomOverlayEnabled },
							{ value -> Config.values.zoomOverlayEnabled = value }
						)
					)
					.controller { option ->
						TickBoxController(option)
					}
					.build()
				)
				.option(Option.createBuilder(Boolean::class.java)
					.name(Text.translatable("config.fabrizoom.scrolling"))
					.tooltip(Text.translatable("config.fabrizoom.scrolling.tooltip"))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.zoomScroll,
							{ Config.values.zoomScroll },
							{ value -> Config.values.zoomScroll = value }
						)
					)
					.controller { option ->
						TickBoxController(option)
					}
					.build()
				)
				.option(Option.createBuilder(Boolean::class.java)
					.name(Text.translatable("config.fabrizoom.zoomsound"))
					.tooltip(Text.translatable("config.fabrizoom.zoomsound.tooltip"))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.zoomSound,
							{ Config.values.zoomSound },
							{ value -> Config.values.zoomSound = value }
						)
					)
					.controller { option ->
						TickBoxController(option)
					}
					.build()
				)
				.build()
			)
			.group(OptionGroup.createBuilder()
				.name(Text.translatable("config.fabrizoom.presets"))
				.option(Option.createBuilder(Presets::class.java)
					.name(Text.translatable("config.fabrizoom.preset.select"))
					.tooltip(Text.translatable("config.fabrizoom.preset.select.tooltip"))
					.instant(true)
					.binding(
						Presets.CUSTOM,
						{ preset },
						{ value -> preset = value }
					)
					.controller { option ->

						EnumController(option) { e: Presets -> Text.translatable(e.key) }
					}
					.build()
				)
				.option(ButtonOption.createBuilder()
					.name(Text.translatable("config.fabrizoom.preset.apply"))
					.tooltip(Text.translatable("config.fabrizoom.preset.apply.tooltip"))
					.action { screen: YACLScreen?, _: ButtonOption? ->
						Config.values = preset.values?.copy() ?: Config.values
						Config.saveConfig()
						screen?.close()
					}
					.controller { option: ButtonOption? ->
						ActionController(option)
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
				.option(Option.createBuilder(Int::class.java)
					.name(Text.translatable("config.fabrizoom.mouse.sensitivity"))
					.tooltip(Text.translatable("config.fabrizoom.mouse.sensitivity.tooltip"))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.mouseSensitivity,
							{ Config.values.mouseSensitivity },
							{ value -> Config.values.mouseSensitivity = value }
						)
					)
					.controller { option ->
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
					Option.createBuilder(Boolean::class.java)
						.name(Text.translatable("config.fabrizoom.mouse.cinematic"))
						.tooltip(Text.translatable("config.fabrizoom.mouse.cinematic.tooltip"))
						.binding(
							Binding.generic(
								Presets.DEFAULT.values.cinematicCameraEnabled,
								{ Config.values.cinematicCameraEnabled },
								{ value -> Config.values.cinematicCameraEnabled = value }
							)
						)
						.controller { option ->
							BooleanController(option)
						}
						.build()
				)
				.option(Option.createBuilder(Double::class.java)
					.name(Text.translatable("config.fabrizoom.mouse.cinematicmultiplier"))
					.tooltip(Text.translatable("config.fabrizoom.mouse.cinematicmultiplier.tooltip"))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.cinematicCameraMultiplier,
							{ Config.values.cinematicCameraMultiplier },
							{ value -> Config.values.cinematicCameraMultiplier = value }
						)
					)
					.controller { option ->
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
				.option(Option.createBuilder(Config.Transition::class.java)
					.name(Text.translatable("config.fabrizoom.transition"))
					.tooltip(Text.translatable("config.fabrizoom.transition.tooltip"))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.transition,
							{ Config.values.transition },
							{ value -> Config.values.transition = value }
						)
					)
					.controller { option ->
						EnumController(option) { e: Config.Transition -> Text.translatable(e.key) }
					}
					.build()
				)
				.option(Option.createBuilder(Double::class.java)
					.name(Text.translatable("config.fabrizoom.linearstep.min"))
					.tooltip(Text.translatable("config.fabrizoom.linearstep.min.tooltip"))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.minimumLinearStep,
							{ Config.values.minimumLinearStep },
							{ value ->
								Config.values.minimumLinearStep = value.coerceIn(0.0, Config.values.maximumLinearStep)
							}
						)
					)
					.controller { option ->
						DoubleSliderController(
							option,
							0.01,
							1.0,
							0.01
						)
					}
					.build()
				)
				.option(Option.createBuilder(Double::class.java)
					.name(Text.translatable("config.fabrizoom.linearstep.max"))
					.tooltip(Text.translatable("config.fabrizoom.linearstep.max.tooltip"))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.maximumLinearStep,
							{ Config.values.maximumLinearStep },
							{ value ->
								Config.values.maximumLinearStep = value.coerceIn(Config.values.minimumLinearStep, 1.0)
							}
						)
					)
					.controller { option ->
						DoubleSliderController(
							option,
							0.01,
							1.0,
							0.01
						)
					}
					.build()
				)
				.option(Option.createBuilder(Float::class.java)
					.name(Text.translatable("config.fabrizoom.smoothmultiplier"))
					.tooltip(Text.translatable("config.fabrizoom.smoothmultiplier.tooltip"))
					.binding(
						Binding.generic(
							Presets.DEFAULT.values.smoothMultiplier,
							{ Config.values.smoothMultiplier },
							{ value -> Config.values.smoothMultiplier = value }
						)
					)
					.controller { option ->
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