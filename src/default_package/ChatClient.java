package default_package;

import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
public class ChatClient {
	static JFrame chatWindow = new JFrame("Chat Application");
	static AppConstants consts = AppConstants.getInstance();
	static JTextArea chatArea = new JTextArea(consts.CHAT_AREA_WIDTH,
			consts.CHAT_AREA_HEIGHT);
	static JTextField textField = new JTextField(consts.CHAT_AREA_HEIGHT);
	static JLabel blankLabel = new JLabel("        ");
	static JButton sendButton = new JButton("Send");
	static JLabel nameLabel = new JLabel("        ");
	static BufferedReader in;
	static PrintWriter out;
	private void prepare_layout() {
		chatWindow.setLayout(new FlowLayout());
		chatWindow.add(new JScrollPane(chatArea));
		chatWindow.add(blankLabel);
		chatWindow.add(textField);
		chatWindow.add(sendButton);
		chatWindow.add(nameLabel);
		chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatWindow.setSize(consts.WINDOW_WIDTH, consts.WINDOW_HEIGHT);
		chatWindow.setVisible(true);
		
		textField.setEditable(false);
		chatArea.setEditable(false);
	}
	private void add_listeners() {
		sendButton.addActionListener(new Listener());
		textField.addActionListener(new Listener());
	}
	private Socket create_connection() throws UnknownHostException, IOException {
		String ipAddress = JOptionPane.showInputDialog(
				chatWindow, "Enter IP Address:", "IP Address Required!!",
				JOptionPane.PLAIN_MESSAGE);
		Socket soc = new Socket(ipAddress, consts.port_number);;
		System.out.println("Created socket!");
		return soc;
	}
	ChatClient(){
		prepare_layout();
		add_listeners();
	}
	void startChat() throws UnknownHostException, IOException {
		Socket soc = create_connection();
		in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		out = new PrintWriter(soc.getOutputStream(), true);
		while (true) {
			String message = in.readLine();
			if (message.equals(consts.NAME_REQUIRED)) {
				String name = JOptionPane.showInputDialog(chatWindow,
						"Enter a unique name: ", "Name Required!!",
						JOptionPane.PLAIN_MESSAGE);
				out.println(name);
			}
			else if(message.equals(consts.NAME_ALREADY_EXISTS)) {
				String name = JOptionPane.showInputDialog(chatWindow,
						"Enter a unique name: ", "Name Already Exists!!",
						JOptionPane.WARNING_MESSAGE);
				out.println(name);
			}
			else if (message.startsWith(consts.NAME_ACCEPTED)) {
				System.out.println(message);
				textField.setEditable(true);
				nameLabel.setText("You are logged in as " + message.substring(4));
			}
			else {
				chatArea.append(message + "\n");
			}
		}
	}
	public static void main(String[] args) throws Exception{
		ChatClient client = new ChatClient();
		client.startChat();
	}
}

class Listener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		ChatClient.out.println(ChatClient.textField.getText());
		ChatClient.textField.setText("");
	}
	
}
