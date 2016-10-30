package com.kryo.chat;

public class NAPChatListener implements NAPListener {

	@Override
	public void receiveUpdate(Update update) {
		if (! (update instanceof NAPChatMessage )) {
			throw new IllegalArgumentException("cannot process the update of type: " + update.getClass());
		}

		NAPChatMessage napChatMessage = (NAPChatMessage) update;

		System.out.println("received chat - From: " + napChatMessage.getSender()
				+ " with message: " + napChatMessage.getChatMessage());
	}
}
