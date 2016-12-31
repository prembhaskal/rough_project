package com.kryo.chat.test;

import java.util.Arrays;

public class NAPClientUpdateListener implements NAPListener {
	@Override
	public void receiveUpdate(Update update) {
		if (!(update instanceof NAPClientUpdate)) {
			throw new IllegalArgumentException("Cannot process " + update.getClass());
		}

		NAPClientUpdate clientUpdate = (NAPClientUpdate) update;
		System.out.println("received client update " + Arrays.toString(clientUpdate.getConnectedClients()));
	}
}
