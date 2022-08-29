package io.github.trainb0y.fabrizoom.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

//The event that makes sure to load the config and puts any load-once options in effect if enabled through the config file.
public class LoadConfigEvent {
	public static void registerEvent() {
		ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
			/*
			//Attempt to load the config if it hasn't been loaded yet, which is unlikely due to extra keybinds.
			if (!fabrizoomConfig.isConfigLoaded) {
				fabrizoomConfig.loadModConfig();
			}

			//This handles the hijacking of the "Save Toolbar Activator" key.
			if (fabrizoomConfigPojo.tweaks.unbindConflictingKey) {
				ZoomUtils.unbindConflictingKey(client, false);
				fabrizoomConfigPojo.tweaks.unbindConflictingKey = false;
				fabrizoomConfig.saveModConfig();
			}
			 */
		});
	}
}
