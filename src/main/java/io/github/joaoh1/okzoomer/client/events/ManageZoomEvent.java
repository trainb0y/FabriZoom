package io.github.joaoh1.okzoomer.client.events;

import io.github.joaoh1.okzoomer.client.config.Config;
import io.github.joaoh1.okzoomer.client.keybinds.ZoomKeybinds;
import io.github.joaoh1.okzoomer.client.utils.ZoomUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

//This event is responsible for managing the zoom signal.
public class ManageZoomEvent {
	//Used internally in order to make zoom toggling possible.
	private static boolean lastZoomPress = false;

	//Used internally in order to make persistent zoom less buggy.
	private static boolean persistentZoom = false;

	public static void registerEvent() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			//Handle zoom mode changes.
			if (!Config.FeaturesGroup.zoomMode.equals(Config.FeaturesGroup.ZoomModes.HOLD)) {
				if (!persistentZoom) {
					persistentZoom = true;
					lastZoomPress = true;
					ZoomUtils.zoomDivisor = Config.values.zoomDivisor;
				}
			} else {
				if (persistentZoom) {
					persistentZoom = false;
					lastZoomPress = true;
				}
			}

			//If the press state is the same as the previous tick's, cancel the rest. Makes toggling usable and the zoom divisor adjustable.
			if (ZoomKeybinds.zoomKey.isPressed() == lastZoomPress) {
				return;
			}

			if (Config.FeaturesGroup.zoomMode.equals(Config.FeaturesGroup.ZoomModes.HOLD)) {
				//If the zoom needs to be held, then the zoom signal is determined by if the key is pressed or not.
				ZoomUtils.zoomState = ZoomKeybinds.zoomKey.isPressed();
				ZoomUtils.zoomDivisor = Config.values.zoomDivisor;
			} else if (Config.FeaturesGroup.zoomMode.equals(Config.FeaturesGroup.ZoomModes.TOGGLE)) {
				//If the zoom needs to be toggled, toggle the zoom signal instead.
				if (ZoomKeybinds.zoomKey.isPressed()) {
					ZoomUtils.zoomState = !ZoomUtils.zoomState;
					ZoomUtils.zoomDivisor = Config.values.zoomDivisor;
				}
			} else if (Config.FeaturesGroup.zoomMode.equals(Config.FeaturesGroup.ZoomModes.PERSISTENT)) {
				//If persistent zoom is enabled, just keep the zoom on.
				ZoomUtils.zoomState = true;
			}

			//Manage the post-zoom signal.
			ZoomUtils.lastZoomState = !ZoomUtils.zoomState && lastZoomPress;

			//Set the previous zoom signal for the next tick.
			lastZoomPress = ZoomKeybinds.zoomKey.isPressed();
		});
	}
}
