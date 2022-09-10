package io.github.trainb0y.fabrizoom.config

import dev.lambdaurora.spruceui.Position
import dev.lambdaurora.spruceui.screen.SpruceScreen
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget
import dev.lambdaurora.spruceui.widget.container.tabbed.SpruceTabbedWidget
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text


/**
 * Primary configuration screen
 */
class ConfigScreen(private val parent: Screen?) : SpruceScreen(Text.translatable("config.fabrizoom.title")) {

	private var tabbed: SpruceTabbedWidget? = null

	override fun init() {
		super.init()

		tabbed = SpruceTabbedWidget(Position.of(this, 0, 4), this.width, this.height - 35 - 4, this.title)
		tabbed!!.addSeparatorEntry(Text.translatable("config.fabrihud.elements"))

		tabbed!!.addTabEntry(Text.translatable("name"), null) { w, h ->
			val optionList = SpruceOptionListWidget(Position.of(4, 4), w, h)

			optionList
		}
		addDrawableChild(tabbed)
	}

	override fun close() {
		super.close()
		Config.saveConfig()
	}
}