package default_package;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
public class ChatServer {
	static int port_number = 9800;
	static ArrayList<String> userNames = new ArrayList<String>();
	static ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();
	static AppConstants constants = AppConstants.getInstance();
	public static void main(String[] args) throws IOException {
		System.out.println("Waiting for client...");
		ServerSocket ss = new ServerSocket(constants.port_number);
		while (true){
			System.out.println("Waiting...");
			Socket soc = ss.accept();
			System.out.println("Connection established!");
			ConversationHandler handler = new ConversationHandler(soc);
			handler.start();
			System.out.println("continue");
		}
	}
}


class ConversationHandler extends Thread{
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	String name;
	PrintWriter log_writer;
	AppConstants consts = AppConstants.getInstance();
	static FileWriter log_file_writer;
	static BufferedWriter buffered_writer;
	public ConversationHandler(Socket socket) throws IOException {
		this.socket = socket;
		log_file_writer = new FileWriter("src/default_package/server_logs.txt", true);
		buffered_writer = new BufferedWriter(log_file_writer);
		log_writer = new PrintWriter(buffered_writer, true);
		
		   File directory = new File("src/default_package/server_logs.txt");
		   System.out.println(directory.getAbsolutePath());
	}
	private void get_username(BufferedReader in) throws IOException {
		int count = 0;
		while (true) {
			if (count > 0) {
				out.println(consts.NAME_ALREADY_EXISTS);
			}
			else {
				out.println(consts.NAME_REQUIRED);
			}
			name = in.readLine();
			if (name == null) {
				return;
			}
			if (!ChatServer.userNames.contains(name)) {
				ChatServer.userNames.add(name);
				break;
			}
			count++;
		}
		out.println(consts.NAME_ACCEPTED +":"+ name);
		ChatServer.writers.add(out);

	}
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			get_username(in);
			while (true) {
				String message = in.readLine();
				if (message == null) {
					return;
				}
				log_writer.println(name + ": " + message);
				for (PrintWriter writer: ChatServer.writers) {
					writer.println(name + ": " + message);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}