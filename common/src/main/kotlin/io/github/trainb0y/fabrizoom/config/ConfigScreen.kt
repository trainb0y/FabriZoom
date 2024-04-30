package io.github.trainb0y.fabrizoom.config

import dev.isxander.yacl3.api.Binding
import dev.isxander.yacl3.api.ButtonOption
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import dev.isxander.yacl3.dsl.YetAnotherConfigLib
import dev.isxander.yacl3.dsl.binding
import dev.isxander.yacl3.dsl.controller
import dev.isxander.yacl3.gui.YACLScreen
import dev.isxander.yacl3.gui.controllers.BooleanController
import dev.isxander.yacl3.gui.controllers.slider.DoubleSliderController
import dev.isxander.yacl3.gui.controllers.slider.FloatSliderController
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component


fun createConfigScreen(parent: Screen?): Screen = YetAnotherConfigLib("fabrizoom") {
	var preset = Presets.DEFAULT // currently selected preset

	title(Component.literal("config.fabrizoom.title"))

	categories.registering {
		name(Component.translatable("category.fabrizoom.basic"))
		groups.registering {
			name(Component.translatable("config.fabrizoom.general"))
			options.registering {
				name(Component.translatable("config.fabrizoom.zoomdivisor"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.zoomdivisor.tooltip")))
				binding(
					ConfigHandler.values::zoomDivisor,
					Presets.DEFAULT.values!!.zoomDivisor
				)
				customController { option ->
					DoubleSliderController(
						option,
						1.5,
						8.0,
						0.05
					)
				}
			}
			options.registering {
				name(Component.translatable("config.fabrizoom.minzoomdivisor"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.minzoomdivisor.tooltip")))
				binding(
					ConfigHandler.values::minimumZoomDivisor,
					Presets.DEFAULT.values!!.minimumZoomDivisor
				)
				customController { option ->
					DoubleSliderController(
						option,
						1.5,
						4.0,
						0.05
					)
				}
			}
			options.registering {
				name(Component.translatable("config.fabrizoom.maxzoomdivisor"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.maxzoomdivisor.tooltip")))
				binding(
					ConfigHandler.values::maximumZoomDivisor,
					Presets.DEFAULT.values!!.maximumZoomDivisor
				)
				customController { option ->
					DoubleSliderController(
						option,
						4.0,
						40.0,
						0.1
					)
				}
			}
		}
	}
	categories.registering {
		groups.registering {
			name(Component.translatable("config.fabrizoom.preferences"))
			options.registering {
				name(Component.translatable("config.fabrizoom.overlay"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.overlay.tooltip")))
				binding(
					ConfigHandler.values::zoomOverlay,
					Presets.DEFAULT.values!!.zoomOverlay
				)
				controller { option ->
					EnumControllerBuilder.create(option)
						.enumClass(ZoomOverlay::class.java)
						.formatValue { e: ZoomOverlay -> Component.translatable(e.translationKey) }
				}
			}
			options.registering {
				name(Component.translatable("config.fabrizoom.scrolling"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.scrolling.tooltip")))
				binding(
					ConfigHandler.values::zoomScroll,
					Presets.DEFAULT.values!!.zoomScroll
				)
				controller(TickBoxControllerBuilder::create) {}
			}
			options.registering {
				name(Component.translatable("config.fabrizoom.zoomsound"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.zoomsound.tooltip")))
				binding(
					ConfigHandler.values::zoomSound,
					Presets.DEFAULT.values!!.zoomSound
				)
				controller(TickBoxControllerBuilder::create) {}
			}
			group("presets") {
				name(Component.translatable("config.fabrizoom.presets"))
				options.registering {
					name(Component.translatable("config.fabrizoom.preset.select"))
					description(OptionDescription.of(Component.translatable("config.fabrizoom.preset.select.tooltip")))
					instant(true)
					binding(
						Presets.CUSTOM,
						{ preset },
						{ value -> preset = value }
					)
					controller { option ->
						EnumControllerBuilder.create(option)
							.enumClass(Presets::class.java)
							.formatValue { e: Presets -> Component.translatable(e.translationKey) }
					}
				}
				this.option("aaa") {
					ButtonOption.createBuilder()
						.name(Component.translatable("config.fabrizoom.preset.apply"))
						.description(OptionDescription.of(Component.translatable("config.fabrizoom.preset.apply.tooltip")))
						.action { screen: YACLScreen?, _: ButtonOption? ->
							ConfigHandler.values = preset.values?.copy() ?: ConfigHandler.values
							ConfigHandler.saveConfig()
							screen?.onClose()
						}
						.build()
				}
			}
		}
	}
	categories.registering {
		name(Component.translatable("category.fabrizoom.advanced"))
		groups.registering {
			name(Component.translatable("config.fabrizoom.mouse.normal.title"))
			options.registering {
				name(Component.translatable("config.fabrizoom.mouse.sensitivity"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.mouse.sensitivity.tooltip")))
				binding(
					ConfigHandler.values::mouseSensitivity,
					Presets.DEFAULT.values!!.mouseSensitivity
				)
				customController { option ->
					IntegerSliderController(
						option,
						1,
						100,
						1
					)
				}
			}
		}
		groups.registering {
			name(Component.translatable("config.fabrizoom.mouse.cinematic.title"))
			options.registering {
				name(Component.translatable("config.fabrizoom.mouse.cinematic"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.mouse.cinematic.tooltip")))
				binding(
					ConfigHandler.values::cinematicCameraEnabled,
					Presets.DEFAULT.values!!.cinematicCameraEnabled
				)
				customController { option ->
					BooleanController(option)
				}
			}
			options.registering {
				name(Component.translatable("config.fabrizoom.mouse.cinematicmultiplier"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.mouse.cinematicmultiplier.tooltip")))
				binding(
					ConfigHandler.values::cinematicCameraMultiplier,
					Presets.DEFAULT.values!!.cinematicCameraMultiplier
				)
				customController { option ->
					DoubleSliderController(
						option,
						0.1,
						10.0,
						0.05
					)
				}
			}
		}
		groups.registering {
			name(Component.translatable("config.fabrizoom.transition"))
			options.registering {
				name(Component.translatable("config.fabrizoom.transition"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.transition.tooltip")))
				binding(
					ConfigHandler.values::transition,
					Presets.DEFAULT.values!!.transition
				)
				controller { option ->
					EnumControllerBuilder.create(option)
						.enumClass(ZoomTransition::class.java)
						.formatValue { e: ZoomTransition -> Component.translatable(e.translationKey) }
				}
			}
			options.registering<Double> {
				name(Component.translatable("config.fabrizoom.linearstep.min"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.linearstep.min.tooltip")))
				binding(
					Binding.generic(
						Presets.DEFAULT.values!!.minimumLinearStep,
						{ ConfigHandler.values.minimumLinearStep },
						{ value ->
							ConfigHandler.values.minimumLinearStep =
								value.coerceIn(0.0, ConfigHandler.values.maximumLinearStep)
						}
					)
				)
				customController { option ->
					DoubleSliderController(
						option,
						0.01,
						1.0,
						0.01
					)
				}
			}
			options.registering<Double> {
				name(Component.translatable("config.fabrizoom.linearstep.max"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.linearstep.max.tooltip")))
				binding(
					Binding.generic(
						Presets.DEFAULT.values!!.maximumLinearStep,
						{ ConfigHandler.values.maximumLinearStep },
						{ value ->
							ConfigHandler.values.maximumLinearStep =
								value.coerceIn(ConfigHandler.values.minimumLinearStep, 1.0)
						}
					)
				)
				customController { option ->
					DoubleSliderController(
						option,
						0.01,
						1.0,
						0.01
					)
				}
			}
			options.registering {
				name(Component.translatable("config.fabrizoom.smoothmultiplier"))
				description(OptionDescription.of(Component.translatable("config.fabrizoom.smoothmultiplier.tooltip")))
				binding(
					ConfigHandler.values::smoothMultiplier,
					Presets.DEFAULT.values!!.smoothMultiplier
				)
				customController { option ->
					FloatSliderController(
						option,
						0.1f,
						1f,
						0.01f
					)
				}
			}
		}
	}
	save { ConfigHandler.saveConfig() }
}.generateScreen(parent)