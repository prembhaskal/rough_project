package com.kryo.chat;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class Updater {

	private final Server server;

	public Updater(Server server) {
		this.server = server;
	}

	public void broadCastUpdate(Update update, Connection[] connections) {
		for (Connection connection : connections) {
			connection.sendTCP(update);
		}

	}

	public void sendUpdate(Update update, String connectionID) {

	}

}
