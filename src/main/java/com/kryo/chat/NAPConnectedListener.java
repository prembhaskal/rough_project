package com.kryo.chat;

public class NAPConnectedListener implements NAPListener {

	private final NAPChatClient napChatClient;
	private final String clientName;

	public NAPConnectedListener(NAPChatClient napChatClient, String clientName) {
		this.napChatClient = napChatClient;
		this.clientName = clientName;
	}

	@Override
	public void receiveUpdate(Update update) {
		napChatClient.sendTCPCommand(new NAPRegisterUpdate(clientName));
	}
}
