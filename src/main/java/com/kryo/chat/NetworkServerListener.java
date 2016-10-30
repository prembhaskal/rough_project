package com.kryo.chat;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.concurrent.BlockingQueue;

public class NetworkServerListener extends Listener {
	private final BlockingQueue<Event> updateQueue;

	public NetworkServerListener(BlockingQueue<Event> updateQueue) {
		this.updateQueue = updateQueue;
	}

	@Override
	public void received(Connection connection, Object object) {
		if (! (object instanceof Update)) {
			System.out.println("unknown object received: " + object);
			super.received(connection, object);
			return;
		}

		try {
			System.out.println("received object : " + object + " from: " + connection);
			Event event = new Event((NAPNetworkConnection)connection, (Update)object);
			updateQueue.put(event);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connected(Connection connection) {
		System.out.println("received a connection : " + connection);
		super.connected(connection);
	}

	@Override
	public void disconnected(Connection connection) {
		System.out.println("disconnected client: " + connection);
		try {
			DisconnectionEvent disconnectionEvent = new DisconnectionEvent((NAPNetworkConnection) connection);
			updateQueue.put(disconnectionEvent);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
