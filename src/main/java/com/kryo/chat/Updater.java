package com.kryo.chat;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Updater {

	private final Server server;
	private final Map<String, NAPNetworkConnection> connectionMap = new ConcurrentHashMap<>();

	public Updater(Server server) {
		this.server = server;
	}

	public void broadCastUpdate(Update update/*, Connection[] connections*/) {
		Connection[] connections = server.getConnections();
		if (connections != null) {
			for (Connection connection : connections) {
				connection.sendTCP(update);
			}
		}
	}

	public void sendUpdate(Update update, String connectionName) {
		NAPNetworkConnection napNetworkConnection = connectionMap.get(connectionName);
		if (napNetworkConnection == null) {
			throw new IllegalStateException("No such connection connected " + connectionName);
		}

		napNetworkConnection.sendTCP(update);
	}

	public void updateNewConnection(NAPNetworkConnection napNetworkConnection, NAPRegisterUpdate napRegisterUpdate) {
		if (napRegisterUpdate.getClientName() != null) {
			connectionMap.put(napRegisterUpdate.getClientName(), napNetworkConnection);
		}
	}

	public void removeDisconnectedConnection(NAPNetworkConnection napNetworkConnection) {
		if (napNetworkConnection.getConnectionName() != null) {
			connectionMap.remove(napNetworkConnection.getConnectionName());
		}
	}

	public Collection<String> getConnectedClients() {
		return connectionMap.keySet();
	}

}
