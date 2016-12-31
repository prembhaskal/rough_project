package com.kryo.chat.test;

public class NAPClientUpdate implements Update {

	private String[] connectedClients;

	private NAPClientUpdate(){};

	public NAPClientUpdate(String[] connectedClients) {
		this.connectedClients = connectedClients;
	}

	public String[] getConnectedClients() {
		return connectedClients;
	}
}
