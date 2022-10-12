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
import io.github.trainb0y.fabrizoom.config.Config.values
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text


/**
 * Primary mod configuration screen
 */
class ConfigScreen(private val parent: Screen?) : SpruceScreen(Text.translatable("config.fabrizoom.title")) {


	override fun init() {
		super.init()
		var preset = Presets.CUSTOM

		val tabbed = SpruceTabbedWidget(Position.of(this, 0, 4), this.width, this.height - 35 - 4, this.title)
		tabbed.addSeparatorEntry(Text.translatable("category.fabrizoom.basic"))

		tabbed.addTabEntry(Text.translatable("category.fabrizoom.general"), null) { w, h ->
			val optionList = SpruceOptionListWidget(Position.of(4, 4), w, h)
			optionList.addSingleOptionEntry(SpruceSeparatorOption("config.fabrizoom.general", true, null))
			optionList.addSmallSingleOptionEntry(
				SpruceDoubleInputOption(
					"config.fabrizoom.zoomdivisor",
					{ values.zoomDivisor },
					{ value ->
						values.zoomDivisor = value.coerceIn(values.minimumZoomDivisor, values.maximumZoomDivisor)
					},
					Text.translatable("config.fabrizoom.zoomdivisor.tooltip")
				)
			)

			optionList.addOptionEntry(
				SpruceDoubleInputOption(
					"config.fabrizoom.minzoomdivisor",
					{ values.minimumZoomDivisor },
					{ value -> values.minimumZoomDivisor = value.coerceIn(1.0, values.maximumZoomDivisor) },
					Text.translatable("config.fabrizoom.minzoomdivisor.tooltip")
				), SpruceDoubleInputOption(
					"config.fabrizoom.maxzoomdivisor",
					{ values.maximumZoomDivisor },
					{ value -> values.maximumZoomDivisor = value.coerceIn(values.minimumZoomDivisor, 20.0) },
					Text.translatable("config.fabrizoom.maxzoomdivisor.tooltip")
				)
			)

			optionList.addOptionEntry(
				SpruceToggleBooleanOption(
					"config.fabrizoom.overlay",
					{ values.zoomOverlayEnabled },
					{ value -> values.zoomOverlayEnabled = value },
					Text.translatable("config.fabrizoom.overlay.tooltip")
				), SpruceToggleBooleanOption(
					"config.fabrizoom.zoomsound",
					{ values.zoomSound},
					{ value -> values.zoomSound = value },
					Text.translatable("config.fabrizoom.zoomsound.tooltip")
				)
			)

			optionList.addSmallSingleOptionEntry(
				SpruceToggleBooleanOption(
					"config.fabrizoom.scrolling",
					{ values.zoomScroll },
					{ value -> values.zoomScroll = value },
					Text.translatable("config.fabrizoom.scrolling.tooltip")
				)
			)

			optionList.addSingleOptionEntry(SpruceSeparatorOption("category.fabrizoom.preset", true, null))

			optionList.addOptionEntry(
				SpruceCyclingOption(
					"config.fabrizoom.preset.select",
					{ preset = preset.next() },
					{ Text.translatable(preset.key) },
					Text.translatable("config.fabrizoom.preset.select.tooltip")
				), SpruceSimpleActionOption.of(
					"config.fabrizoom.preset.apply",
					{
						values = preset.values?.copy() ?: values
						close()
					},
					Text.translatable("config.fabrizoom.preset.apply.tooltip")
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

		tabbed.addSeparatorEntry(Text.translatable("category.fabrizoom.advanced"))

		tabbed.addTabEntry(
			Text.translatable("category.fabrizoom.mouse"),
			Text.translatable("category.fabrizoom.mouse.description")
		) { w, h ->
			val optionList = SpruceOptionListWidget(Position.of(4, 4), w, h)
			optionList.addSingleOptionEntry(SpruceSeparatorOption("config.fabrizoom.mouse.normal.title", true, null))
			optionList.addSingleOptionEntry(
				SpruceIntegerInputOption(
					"config.fabrizoom.mouse.sensitivity",
					{ values.mouseSensitivity },
					{ value -> values.mouseSensitivity = value.coerceIn(10, 40) },
					Text.translatable("config.fabrizoom.mouse.sensitivity.tooltip")
				)
			)
			optionList.addSingleOptionEntry(SpruceSeparatorOption("config.fabrizoom.mouse.cinematic.title", true, null))
			optionList.addOptionEntry(
				SpruceToggleBooleanOption(
					"config.fabrizoom.mouse.cinematic",
					{ values.cinematicCameraEnabled },
					{ value -> values.cinematicCameraEnabled = value },
					Text.translatable("config.fabrizoom.mouse.cinematic.tooltip")
				), SpruceDoubleInputOption(
					"config.fabrizoom.mouse.cinematicmultiplier",
					{ values.cinematicCameraMultiplier },
					{ value -> values.cinematicCameraMultiplier = value.coerceIn(0.1, 10.0) },
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
					{ values.transition = values.transition.next() },
					{ Text.translatable(values.transition.key) },
					Text.translatable("config.fabrizoom.transition.tooltip")
				)
			)
			optionList.addSingleOptionEntry(SpruceSeparatorOption("config.fabrizoom.transition.linear", true, null))
			optionList.addOptionEntry(
				SpruceDoubleInputOption(
					"config.fabrizoom.linearstep.min",
					{ values.minimumLinearStep },
					{ value -> values.maximumLinearStep = value },
					Text.translatable("config.fabrizoom.linearstep.min.tooltip")
				), SpruceDoubleInputOption(
					"config.fabrizoom.linearstep.max",
					{ values.maximumLinearStep },
					{ value -> values.maximumLinearStep = value },
					Text.translatable("config.fabrizoom.linearstep.max.tooltip")
				)
			)
			optionList.addSingleOptionEntry(SpruceSeparatorOption("config.fabrizoom.transition.smooth", true, null))
			optionList.addSmallSingleOptionEntry(
				SpruceFloatInputOption(
					"config.fabrizoom.smoothmultiplier",
					{ values.smoothMultiplier },
					{ value ->
						values.smoothMultiplier = value.coerceIn(0.1f, 1f)
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