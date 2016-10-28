package com.kryo.chat;

public class NAPChatMessage implements Update {

	private String chatMessage;
	private final String sendTo;

	public NAPChatMessage(String chatMessage, String sendTo) {
		this.chatMessage = chatMessage;
		this.sendTo = sendTo;
	}

	public String getChatMessage() {
		return chatMessage;
	}

	public String getSendTo() {
		return sendTo;
	}
}
