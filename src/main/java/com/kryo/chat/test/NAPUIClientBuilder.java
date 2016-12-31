package com.kryo.chat.test;

import com.esotericsoftware.kryonet.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class NAPUIClientBuilder {

	public NAPUIClientBuilder() {
	}


	public static void main(String[] args) throws InterruptedException, IOException {
		createClientUI();
//		testClientConnetion();
	}

	private static NAPChatClient setUPClient(String connectionName) throws IOException, InterruptedException {
		Client client = new Client();
		NAPChatClient napChatClient = new NAPChatClient(client);
		NetworkClientListener networkClientListener = new NetworkClientListener(napChatClient, connectionName);
		networkClientListener.setChatListener(new NAPChatListener());
		networkClientListener.setClientUpdateListener(new NAPClientUpdateListener());

		napChatClient.addReceivingListener(networkClientListener);
//		napChatClient.connect("localhost", 9013);
		napChatClient.connect("104.199.208.37", 9013);

		return napChatClient;
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
					napChatClient = setUPClient(connectionName);
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
