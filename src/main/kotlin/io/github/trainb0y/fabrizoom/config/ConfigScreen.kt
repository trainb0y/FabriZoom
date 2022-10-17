package io.github.trainb0y.fabrizoom.config

import dev.isxander.yacl.api.Binding
import dev.isxander.yacl.api.ConfigCategory
import dev.isxander.yacl.api.Option
import dev.isxander.yacl.api.OptionGroup
import dev.isxander.yacl.api.YetAnotherConfigLib
import dev.isxander.yacl.gui.controllers.slider.DoubleSliderController
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

fun openConfigScreen(parent: Screen?): Screen =
	YetAnotherConfigLib.createBuilder()
		.title(Text.literal("config.fabrizoom.title"))
		.category(ConfigCategory.createBuilder()
			.name(Text.translatable("category.fabrizoom.general"))
			.tooltip(Text.translatable("category.fabrizoom.general.tooltip"))
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
							2.0,
							8.0,
							0.1
						)
					}
					.build()
				)
				.build()
			)
			.build()
		)
		.category(ConfigCategory.createBuilder()
			.name(Text.translatable("category.fabrizoom.mouse"))
			.tooltip(Text.translatable("category.fabrizoom.mouse.tooltip"))
			.build()
		)
		.category(ConfigCategory.createBuilder()
			.name(Text.translatable("category.fabrizoom.transition"))
			.tooltip(Text.translatable("category.fabrizoom.transition.tooltip"))
			.build()
		)
		.save(Config::saveConfig)
		.build()
		.generateScreen(parent)