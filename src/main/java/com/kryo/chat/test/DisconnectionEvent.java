package com.kryo.chat.test;

public class DisconnectionEvent extends Event {

	public DisconnectionEvent(NAPNetworkConnection connection) {
		super(connection, null);
	}

}
