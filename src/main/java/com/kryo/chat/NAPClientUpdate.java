package com.kryo.chat;

public class NAPClientUpdate implements Update {

	private String[] connectedClients;

	public NAPClientUpdate(String[] connectedClients) {
		this.connectedClients = connectedClients;
	}

	public String[] getConnectedClients() {
		return connectedClients;
	}
}
