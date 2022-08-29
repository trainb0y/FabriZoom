package io.github.trainb0y.fabrizoom.events

import io.github.trainb0y.fabrizoom.config.Config
import io.github.trainb0y.fabrizoom.keybinds.ZoomKeybinds
import io.github.trainb0y.fabrizoom.utils.ZoomUtils
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient

//This event is responsible for managing the zoom signal.
object ManageZoomEvent {
	//Used internally in order to make zoom toggling possible.
	private var lastZoomPress = false

	//Used internally in order to make persistent zoom less buggy.
	private var persistentZoom = false
	@JvmStatic
	fun registerEvent() {
		ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient? ->
			//Handle zoom mode changes.
			if (Config.FeaturesGroup.zoomMode != Config.FeaturesGroup.ZoomModes.HOLD && !persistentZoom) {
				persistentZoom = true
				lastZoomPress = true
				ZoomUtils.zoomDivisor = Config.values.zoomDivisor
			} else if (persistentZoom) {
				persistentZoom = false
				lastZoomPress = true
			}

			//If the press state is the same as the previous tick's, cancel the rest. Makes toggling usable and the zoom divisor adjustable.
			if (ZoomKeybinds.zoomKey.isPressed == lastZoomPress) return@EndTick
			if (Config.FeaturesGroup.zoomMode == Config.FeaturesGroup.ZoomModes.HOLD) {
				//If the zoom needs to be held, then the zoom signal is determined by if the key is pressed or not.
				ZoomUtils.zoomState = ZoomKeybinds.zoomKey.isPressed
				ZoomUtils.zoomDivisor = Config.values.zoomDivisor
			} else if (Config.FeaturesGroup.zoomMode == Config.FeaturesGroup.ZoomModes.TOGGLE) {
				//If the zoom needs to be toggled, toggle the zoom signal instead.
				if (ZoomKeybinds.zoomKey.isPressed) {
					ZoomUtils.zoomState = !ZoomUtils.zoomState
					ZoomUtils.zoomDivisor = Config.values.zoomDivisor
				}
			} else if (Config.FeaturesGroup.zoomMode == Config.FeaturesGroup.ZoomModes.PERSISTENT) {
				//If persistent zoom is enabled, just keep the zoom on.
				ZoomUtils.zoomState = true
			}

			//Manage the post-zoom signal.
			ZoomUtils.lastZoomState = !ZoomUtils.zoomState && lastZoomPress

			//Set the previous zoom signal for the next tick.
			lastZoomPress = ZoomKeybinds.zoomKey.isPressed
		})
	}
}