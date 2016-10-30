package com.kryo.chat;

import com.esotericsoftware.kryonet.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class NAPClientBuilder {

	private final String serverAddress;
	private final int serverPort;

	private final NetworkClientListener networkClientListener;
	private NAPChatClient napChatClient;

	public NAPClientBuilder(String serverAddress, int serverPort) {
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

	public void setConnectedListener(NAPConnectedListener napConnectedListener) {
		networkClientListener.setConnectedListener(napConnectedListener);
	}


	public NAPChatClient buildClient() throws IOException {
		Client client = new Client();
		napChatClient = new NAPChatClient(client, networkClientListener);
		return napChatClient;
	}

	public void startClient() throws IOException {
		napChatClient.connect(serverAddress, serverPort);
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		createClientUI();
//		testClientConnetion();
	}

	private static NAPChatClient testClientConnection(String connectionName) throws IOException, InterruptedException {
		NAPClientBuilder napClientBuilder = new NAPClientBuilder("localhost", 9013);
		napClientBuilder.setChatListener(new NAPChatListener());
		napClientBuilder.setClientUpdateListener(new NAPClientUpdateListener());

		NAPChatClient napChatClient;
		try {
			napChatClient = napClientBuilder.buildClient();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		napClientBuilder.setConnectedListener(new NAPConnectedListener(napChatClient, connectionName));

		napClientBuilder.startClient();

		return napChatClient;
//
//		// wait for init
//		Thread.sleep(10000);
//
//		// send registration update
//		Random random = new Random();
//		int i = random.nextInt(10) + 1;
//		napChatClient.sendTCPCommand(new NAPRegisterUpdate("test" + i));
//
//		try {
//			new CountDownLatch(1).await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	public static void createClientUI() {
		ChatFrame chatFrame = new ChatFrame();
		chatFrame.setVisible(true);

	}

	private static class ChatFrame extends JFrame {

		public CardLayout cardLayout;

		public NAPChatClient napChatClient;

		public String connectionName = "test7";

		public ChatFrame() {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setSize(640, 400);
			setLocationRelativeTo(null);

			Container contentPane = getContentPane();
			cardLayout = new CardLayout();
			contentPane.setLayout(cardLayout);

			// enter information
			JPanel connectionPanel = new JPanel();
			connectionPanel.setLayout(new GridLayout(3, 2));
			contentPane.add(connectionPanel, "connection");

			connectionPanel.add(new JLabel("Server Address"));
			JTextField serverField = new JTextField("localhost");
			connectionPanel.add(serverField);
			connectionPanel.add(new JLabel("Server Port"));
			JTextField portField = new JTextField("9010");
			connectionPanel.add(portField);
			JButton jButton = new JButton("Connect");
			connectionPanel.add(jButton);

			jButton.addActionListener(e -> {
				String serverAddress1 = serverField.getText();
				int port = Integer.parseInt(portField.getText());

				try {
					napChatClient = testClientConnection(connectionName);
				}
				catch (Exception e1) {
					e1.printStackTrace();
					throw new RuntimeException("error connecting");
				}

				cardLayout.show(contentPane, "chat");
			});

			JPanel chatPanel = new JPanel();
			chatPanel.setLayout(new BorderLayout());

			JPanel topChatPanel = new JPanel();
			topChatPanel.setLayout(new GridLayout(1, 2));
			contentPane.add(chatPanel, "chat");

			JList<String> messageList = new JList<>();
			messageList.setModel(new DefaultListModel<>());
			JScrollPane msgPane = new JScrollPane(messageList);

			JList<String> nameList = new JList<>();
			messageList.setModel(new DefaultListModel<>());
			JScrollPane namePane = new JScrollPane(nameList);

			topChatPanel.add(msgPane);
			topChatPanel.add(namePane);

			JPanel bottomChatPanel = new JPanel();
			bottomChatPanel.setLayout(new GridBagLayout());
			JTextField chatField = new JTextField();
			JButton sendButton = new JButton("Send");

			sendButton.addActionListener(e -> {
				napChatClient.sendTCPCommand(new NAPChatMessage(chatField.getText(), connectionName, ""));
			});

			bottomChatPanel.add(chatField, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			bottomChatPanel.add(sendButton , new GridBagConstraints(1, 0, 1, 1, 0, 0,
					GridBagConstraints.CENTER, 0, new Insets(0, 0, 0, 0), 0, 0));

			chatPanel.add(topChatPanel);
			chatPanel.add(bottomChatPanel, BorderLayout.SOUTH);
		}
	}


}
