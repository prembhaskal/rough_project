package com.kryo.chat;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class ClientBuilder {

	private final String serverAddress;
	private final int serverPort;

	private final NetworkClientListener networkClientListener;

	public ClientBuilder(String serverAddress, int serverPort) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		networkClientListener = new NetworkClientListener();
	}

	public void setChatListener(NAPListener napListener) {
		networkClientListener.setChatListener(napListener);
	}

	public void setClientUpdateListener(NAPListener napListener) {
		networkClientListener.setClientUpdateListener(napListener);
	}


	public NAPChatClient startClient() throws IOException {
		Client client = new Client();
		NAPChatClient napChatClient = new NAPChatClient(client, networkClientListener);
		napChatClient.connect(serverAddress, serverPort);
		return napChatClient;
	}
}
