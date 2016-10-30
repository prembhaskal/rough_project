package com.kryo.chat;

public class NAPChatMessage implements Update {

	private String chatMessage;
	private String sender;
	private String recipient;

	private NAPChatMessage(){}

	public NAPChatMessage(String chatMessage, String sender, String recipient) {
		this.chatMessage = chatMessage;
		this.sender = sender;
		this.recipient = recipient;
	}

	public String getChatMessage() {
		return chatMessage;
	}

	public String getSender() {
		return sender;
	}

	public String getRecipient() {
		return recipient;
	}
}
