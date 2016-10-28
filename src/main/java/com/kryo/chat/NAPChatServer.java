package com.kryo.chat;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NAPChatServer {

	private final Server server;

	private volatile boolean isRunning;

	public NAPChatServer(Server server, Listener listener) {
		this.server = server;
		addReceivingListener(listener);
		initNetwork();
	}

	private void initNetwork() {
		List<Class> networkClasses = new ArrayList<>();
		networkClasses.add(String.class);
		networkClasses.add(String[].class);
		networkClasses.add(NAPChatMessage.class);
		networkClasses.add(NAPRegisterUpdate.class);
		networkClasses.add(NAPClientUpdate.class);
	}

	private void addReceivingListener(Listener listener) {
		server.addListener(listener);
	}

	public void bindPort(int serverPort) throws IOException {
		server.bind(serverPort);
	}

	public void start() {
		server.start();
	}

	public void stop() {
		server.stop();
	}
}
