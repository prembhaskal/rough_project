package com.kryo.chat;

import com.esotericsoftware.kryonet.Server;

public class ServerBuilder {

	public void build() {
		Server server = new Server();
		NAPChatServer napChatServer = new NAPChatServer(server, );
	}
}
