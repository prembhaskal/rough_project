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
		}

		try {
			Event event = new Event((NAPNetworkConnection)connection, (Update)object);
			updateQueue.put(event);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disconnected(Connection connection) {
		try {
			DisconnectionEvent disconnectionEvent = new DisconnectionEvent((NAPNetworkConnection) connection);
			updateQueue.put(disconnectionEvent);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
