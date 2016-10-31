package com.kryo.chat;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NetworkClientListener extends Listener {
	private final NAPChatClient napChatClient;
	private final String clientName;
	private NAPListener chatListener;
	private NAPListener clientUpdateListener;

	private final Executor executor;

	public NetworkClientListener(NAPChatClient napChatClient, String clientName) {
		this.napChatClient = napChatClient;
		this.clientName = clientName;

		executor = new ThreadPoolExecutor(1, 1,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>(100));
	}

	public void setChatListener(NAPListener chatListener) {
		this.chatListener = chatListener;
	}

	public void setClientUpdateListener(NAPListener clientUpdateListener) {
		this.clientUpdateListener = clientUpdateListener;
	}


	@Override
	public void received(Connection connection, Object object) {
		if (object instanceof NAPChatMessage) {
			notifyListener(chatListener, (Update) object);
		}
		else if (object instanceof NAPClientUpdate) {
			notifyListener(clientUpdateListener, (Update) object);
		}
		else {
			super.received(connection, object);
		}
	}

	private void notifyListener(NAPListener napListener, Update update) {
		executor.execute(() -> napListener.receiveUpdate(update));
	}

	@Override
	public void connected(Connection connection) {
		System.out.println("client connected to server");
		napChatClient.sendTCPCommand(new NAPRegisterUpdate(clientName));
	}

	@Override
	public void disconnected(Connection connection) {
		System.out.println("client disconnected to server");
	}
}
