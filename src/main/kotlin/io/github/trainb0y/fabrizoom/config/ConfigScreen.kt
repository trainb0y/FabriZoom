package io.github.trainb0y.fabrizoom.config

import dev.lambdaurora.spruceui.Position
import dev.lambdaurora.spruceui.option.SpruceCyclingOption
import dev.lambdaurora.spruceui.option.SpruceDoubleInputOption
import dev.lambdaurora.spruceui.option.SpruceFloatInputOption
import dev.lambdaurora.spruceui.option.SpruceIntegerInputOption
import dev.lambdaurora.spruceui.option.SpruceSeparatorOption
import dev.lambdaurora.spruceui.option.SpruceSimpleActionOption
import dev.lambdaurora.spruceui.option.SpruceToggleBooleanOption
import dev.lambdaurora.spruceui.screen.SpruceScreen
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget
import dev.lambdaurora.spruceui.widget.container.tabbed.SpruceTabbedWidget
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text


/**
 * Primary configuration screen
 */
class ConfigScreen(private val parent: Screen?) : SpruceScreen(Text.translatable("config.fabrizoom.title")) {


	override fun init() {
		super.init()

		val tabbed = SpruceTabbedWidget(Position.of(this, 0, 4), this.width, this.height - 35 - 4, this.title)


		tabbed.addTabEntry(Text.translatable("category.fabrizoom.general"), null) { w, h ->
			val optionList = SpruceOptionListWidget(Position.of(4, 4), w, h)
			optionList.addSingleOptionEntry(SpruceSeparatorOption("config.fabrizoom.general", true, null))
			optionList.addSmallSingleOptionEntry(
				SpruceDoubleInputOption(
					"config.fabrizoom.zoomdivisor",
					{ Config.zoomDivisor },
					{ value -> Config.zoomDivisor = value.coerceIn(Config.minimumZoomDivisor, Config.maximumZoomDivisor) },
					Text.translatable("config.fabrizoom.zoomdivisor.tooltip")
				)
			)

			optionList.addOptionEntry(
				SpruceDoubleInputOption(
					"config.fabrizoom.minzoomdivisor",
					{ Config.minimumZoomDivisor },
					{ value -> Config.minimumZoomDivisor = value },
					Text.translatable("config.fabrizoom.minzoomdivisor.tooltip")
				), SpruceDoubleInputOption(
					"config.fabrizoom.maxzoomdivisor",
					{ Config.maximumZoomDivisor },
					{ value -> Config.maximumZoomDivisor = value },
					Text.translatable("config.fabrizoom.maxzoomdivisor.tooltip")
				)
			)

			optionList.addSingleOptionEntry(SpruceSeparatorOption("", false, null))
			optionList.addOptionEntry(
				SpruceSimpleActionOption.of(
					"config.fabrizoom.reset",
					{
						Config.applyDefaultConfig()
						Config.saveConfig()
						this.client!!.setScreen(ConfigScreen(parent)) // values don't automatically update, so...
					},
					Text.translatable("config.fabrizoom.reset.tooltip")
				), SpruceSimpleActionOption.of(
					"config.fabrizoom.cancel",
					{
						Config.loadConfig()
						this.client!!.setScreen(parent)
					},
					Text.translatable("config.fabrizoom.cancel.tooltip")
				)
			)
			optionList.addSmallSingleOptionEntry(
				SpruceSimpleActionOption.of(
					"config.fabrizoom.apply",
					{
						Config.saveConfig()
						this.client!!.setScreen(parent)
					},
					Text.translatable("config.fabrizoom.apply.tooltip")
				)
			)
			optionList
		}

		tabbed.addTabEntry(
			Text.translatable("category.fabrizoom.mouse"),
			Text.translatable("category.fabrizoom.mouse.description")
		) { w, h ->
			val optionList = SpruceOptionListWidget(Position.of(4, 4), w, h)
			optionList.addSingleOptionEntry(SpruceSeparatorOption("config.fabrizoom.mouse.normal.title", true, null))
			optionList.addSingleOptionEntry(
				SpruceIntegerInputOption(
					"config.fabrizoom.mouse.sensitivity",
					{ Config.mouseSensitivity },
					{ value -> Config.mouseSensitivity = value },
					Text.translatable("config.fabrizoom.mouse.sensitivity.tooltip")
				)
			)
			optionList.addSingleOptionEntry(SpruceSeparatorOption("config.fabrizoom.mouse.cinematic.title", true, null))
			optionList.addOptionEntry(
				SpruceToggleBooleanOption(
					"config.fabrizoom.mouse.cinematic",
					{ Config.cinematicCameraEnabled },
					{ value -> Config.cinematicCameraEnabled = value },
					Text.translatable("config.fabrizoom.mouse.cinematic.tooltip")
				), SpruceDoubleInputOption(
					"config.fabrizoom.mouse.cinematicmultiplier",
					{ Config.cinematicCameraMultiplier },
					{ value -> Config.cinematicCameraMultiplier = value.coerceIn(0.1, 10.0) },
					Text.translatable("config.fabrizoom.mouse.cinematicmultiplier.tooltip")
				)
			)
			optionList
		}

		tabbed.addTabEntry(
			Text.translatable("category.fabrizoom.transition"),
			Text.translatable("category.fabrizoom.transition.description")
		) { w, h ->
			val optionList = SpruceOptionListWidget(Position.of(4, 4), w, h)
			optionList.addSingleOptionEntry(SpruceSeparatorOption("config.fabrizoom.transition", true, null))
			optionList.addSingleOptionEntry(
				SpruceCyclingOption(
					"config.fabrizoom.transition",
					{ Config.transition = Config.transition.next() },
					{ Text.translatable(Config.transition.key) },
					Text.translatable("config.fabrizoom.transition.tooltip")
				)
			)
			optionList.addSingleOptionEntry(SpruceSeparatorOption("config.fabrizoom.transition.linear", true, null))
			optionList.addOptionEntry(
				SpruceDoubleInputOption(
					"config.fabrizoom.linearstep.min",
					{ Config.minimumLinearStep },
					{ value -> Config.maximumLinearStep = value },
					Text.translatable("config.fabrizoom.linearstep.min.tooltip")
				), SpruceDoubleInputOption(
					"config.fabrizoom.linearstep.max",
					{ Config.maximumLinearStep },
					{ value -> Config.maximumLinearStep = value },
					Text.translatable("config.fabrizoom.linearstep.max.tooltip")
				)
			)
			optionList.addSingleOptionEntry(SpruceSeparatorOption("config.fabrizoom.transition.smooth", true, null))
			optionList.addSmallSingleOptionEntry(
				SpruceFloatInputOption(
					"config.fabrizoom.smoothmultiplier",
					{ Config.smoothMultiplier },
					{ value ->
						Config.smoothMultiplier = value.coerceIn(0.1f, 1f)
					},
					Text.translatable("config.fabrizoom.smoothmultiplier.tooltip")
				)
			)

			optionList
		}
		addDrawableChild(tabbed)
	}

	override fun close() {
		super.close()
		Config.saveConfig()
	}
}