package com.kryo.chat.test;

public class Event {

	private final NAPNetworkConnection connection;
	private final Update update;

	public Event(NAPNetworkConnection connection, Update update) {
		this.connection = connection;
		this.update = update;
	}

	public NAPNetworkConnection getConnection() {
		return connection;
	}

	public Update getUpdate() {
		return update;
	}
}
