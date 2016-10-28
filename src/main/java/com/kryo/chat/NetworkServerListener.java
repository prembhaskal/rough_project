package com.kryo.chat;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkServerListener extends Listener {
	private Block

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
	}
}
