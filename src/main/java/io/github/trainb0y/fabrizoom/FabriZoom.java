package io.github.trainb0y.fabrizoom;

import io.github.trainb0y.fabrizoom.events.LoadConfigEvent;
import io.github.trainb0y.fabrizoom.events.ManageZoomEvent;
import net.fabricmc.api.ClientModInitializer;

//This class is responsible for registering the events and packets.
public class FabriZoom implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		//Load the events.
		LoadConfigEvent.registerEvent();
		ManageZoomEvent.registerEvent();
	}
}
