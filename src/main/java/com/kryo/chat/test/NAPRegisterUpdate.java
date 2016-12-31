package com.kryo.chat.test;

public class NAPRegisterUpdate implements Update {
	private String clientName;

	private NAPRegisterUpdate(){};

	public NAPRegisterUpdate(String clientName) {
		this.clientName = clientName;
	}

	public String getClientName() {
		return clientName;
	}
}