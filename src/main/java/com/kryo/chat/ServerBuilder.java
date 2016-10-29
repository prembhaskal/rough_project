package com.kryo.chat;

import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerBuilder {

	public static final int SERVER_PORT = 9103;

	public NAPChatServer buildServer() throws IOException {
		Server server = new Server();
		Updater updater = new Updater(server);

		BlockingQueue<Event> eventQueue = new ArrayBlockingQueue<>(100);
		NetworkServerListener networkServerListener = new NetworkServerListener(eventQueue);
		NAPServerExecutor napServerExecutor = new NAPServerExecutor(eventQueue, updater);

		NAPChatServer napChatServer = new NAPChatServer(server, networkServerListener);

		new Thread(napServerExecutor).start();
		napChatServer.bindPort(SERVER_PORT);

		return napChatServer;
	}
}
