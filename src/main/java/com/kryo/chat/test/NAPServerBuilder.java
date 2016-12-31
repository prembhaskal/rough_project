package com.kryo.chat.test;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class NAPServerBuilder {

	public final int serverPort;

	public NAPServerBuilder(int serverPort) {
		this.serverPort = serverPort;
	}

	public NAPChatServer startServer() throws IOException {
		Server server = new Server() {
			@Override
			public Connection newConnection() {
				return new NAPNetworkConnection();
			}
		};
		Updater updater = new Updater(server);

		BlockingQueue<Event> eventQueue = new ArrayBlockingQueue<>(100);
		NetworkServerListener networkServerListener = new NetworkServerListener(eventQueue);
		NAPServerExecutor napServerExecutor = new NAPServerExecutor(eventQueue, updater);

		NAPChatServer napChatServer = new NAPChatServer(server, networkServerListener);

		new Thread(napServerExecutor).start();
		napChatServer.bindPort(serverPort);
		napChatServer.start();

		return napChatServer;
	}

	public static void main(String[] args) {
		NAPServerBuilder napServerBuilder = new NAPServerBuilder(9013);
		try {
			napServerBuilder.startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			new CountDownLatch(1).await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
