package froggerz.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private GameServer server;
	private int clientID;
	
	public ServerThread(Socket s, GameServer server, int clientID) {
		this.server = server;
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			this.clientID = clientID;
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			while(true) {
				GameMessage m = (GameMessage)ois.readObject(); // get message from readObject
				server.handleGameMessage(m); // Send to server's handleMessage dispatcher
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		}		
	}
	
	// Takes a message object and writes it to the object output stream
	public void sendMessage(GameMessage m) {
		try {
			oos.writeObject(m);
		} catch (IOException e) {
			System.err.println("Error in ServerThread " + clientID + " " + e.getMessage());
		}
	}
	
	
}
