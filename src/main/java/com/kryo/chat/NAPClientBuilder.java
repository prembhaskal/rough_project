package com.kryo.chat;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class NAPClientBuilder {

	public static final String SERVER_ADDRESS = "104.199.208.37";
//	public static final String SERVER_ADDRESS = "localhost";
	public static final int SERVER_PORT = 9013;

	public NAPClientBuilder() {
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		new NAPClientBuilder().setUPClient("test1");
	}

	public NAPChatClient setUPClient(String connectionName) throws IOException, InterruptedException {
		NAPChatClient napChatClient = new NAPChatClient(new Client());
		NetworkClientListener networkClientListener = new NetworkClientListener(napChatClient, connectionName);
		networkClientListener.setChatListener(new NAPChatListener());
		networkClientListener.setClientUpdateListener(new NAPClientUpdateListener());

		napChatClient.addReceivingListener(networkClientListener);
		napChatClient.connect(SERVER_ADDRESS, SERVER_PORT);

		return napChatClient;
	}

}
