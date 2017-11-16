package froggerz.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class TestClient {
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private int clientID;
	
	public TestClient() {
		String host = "localhost";
		int port = 55665;
		try {
			Socket socket = new Socket(host, port);
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			
			GameMessage firstMessage = receiveMessage();
			
			clientID = firstMessage.getClientID();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Waits until a message is sent from the server and returns it
	public GameMessage receiveMessage() {
		GameMessage message;
		try {
			message = (GameMessage) ois.readObject();
			return message;
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Error in receiveMessage " + e.getMessage());
		}
		return null;
	}

	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			System.err.println("ioe in Client sendMessage(): " + e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		ArrayList<TestClient> clients = new ArrayList<TestClient>();
		for(int i = 0; i < 4; i++) {
			TestClient tc = new TestClient();
			clients.add(tc);
			for(int j = 0; j < 4; j++) {
				tc.sendMessage(new GameMessage(new Vector2(j, i)));
			}
		}

	}

}
