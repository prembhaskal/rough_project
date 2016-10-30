package com.kryo.chat;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NAPChatClient {

	private String connectionName;
	private final Client client;

	private volatile ClientState state;

	public enum ClientState {
		CONNECTING,
		CONNECTED,
		DISCONNECTING,
		DISCONNECTED
	}

	public NAPChatClient(Client client, Listener listener) {
		this.client = client;
		addReceivingListener(listener);
		initNetwork();
	}

	private void addReceivingListener(Listener listener) {
		client.addListener(listener);
	}

	private void initNetwork() {
		List<Class> networkClasses = new ArrayList<>();
		networkClasses.add(String.class);
		networkClasses.add(String[].class);
		networkClasses.add(NAPChatMessage.class);
		networkClasses.add(NAPRegisterUpdate.class);
		networkClasses.add(NAPClientUpdate.class);
		for (Class networkClass : networkClasses) {
			client.getKryo().register(networkClass);
		}

	}

	public void connect(String serverAddress, int serverPort) throws IOException {
		state = ClientState.CONNECTING;

		client.start();

		try {
			client.connect(10000, serverAddress, serverPort);
		}
		catch (IOException e) {
			throw new IOException("Error connecting to server at " + serverAddress + " : " + serverPort, e);
		}

		state = ClientState.CONNECTED;
	}

	public void disconnect() {
		state = ClientState.DISCONNECTING;

		client.stop();

		state = ClientState.DISCONNECTED;
	}

	public void sendTCPCommand(Object object) {
		client.sendTCP(object);
	}

	public void sendChatMessage(String chatMessage, String sender, String recipient) {
		sendTCPCommand(new NAPChatMessage(chatMessage, sender, recipient));
	}

	public ClientState getState() {
		return state;
	}
}
