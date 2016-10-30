package com.kryo.chat;

import com.esotericsoftware.kryonet.Connection;

public class NAPNetworkConnection extends Connection {

	private String connectionName;

	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	@Override
	public String toString() {
		return super.toString() + " -- " + connectionName;
	}
}
