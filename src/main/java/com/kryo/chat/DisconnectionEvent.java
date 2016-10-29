package com.kryo.chat;

public class DisconnectionEvent extends Event {

	public DisconnectionEvent(NAPNetworkConnection connection) {
		super(connection, null);
	}

}
