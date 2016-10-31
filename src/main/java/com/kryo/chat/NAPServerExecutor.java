package com.kryo.chat;

import java.util.concurrent.BlockingQueue;

public class NAPServerExecutor implements Runnable {

	private final BlockingQueue<Event> blockingQueue;
	private final Updater updater;

	public NAPServerExecutor(BlockingQueue<Event> blockingQueue, Updater updater) {
		this.blockingQueue = blockingQueue;
		this.updater = updater;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Event event = blockingQueue.take();
				handleEvent(event);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleEvent(Event event) {

		if (event instanceof DisconnectionEvent) {
			updater.removeDisconnectedConnection(event.getConnection());
			return;
		}

		// Handle other events.
		NAPNetworkConnection napNetworkConnection = event.getConnection();
		Update update = event.getUpdate();

		if (update instanceof NAPRegisterUpdate) {
			System.out.println("new NAP Register update received : " + ((NAPRegisterUpdate) update).getClientName());
			napNetworkConnection.setConnectionName(((NAPRegisterUpdate) update).getClientName());
			updater.updateNewConnection(napNetworkConnection, (NAPRegisterUpdate) update);

			String[] connectedClients = updater.getConnectedClients().toArray(new String[0]);
			NAPClientUpdate napClientUpdate = new NAPClientUpdate(connectedClients);
			updater.broadCastUpdate(napClientUpdate);
		}
		else if (update instanceof NAPChatMessage) {
			System.out.println("chat received from " + napNetworkConnection.getConnectionName());
			NAPChatMessage napChatMessage = (NAPChatMessage) update;

			updater.broadCastUpdate(napChatMessage);
		}
	}
}
