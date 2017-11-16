package froggerz.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GameServer {
	private int connectionCount; 
	ArrayList<ServerThread> serverThreads;
	
	public GameServer(int port) {
		connectionCount = 0; // initialize to connections to 0
		try {
			System.out.println("Binding server to port " + port);
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Bound to port " + port);
			
			// Initialize maps to track current games and server threads
			serverThreads = new ArrayList<ServerThread>();
			
			while(true) {
				Socket socket = ss.accept();
				System.out.println("Connection from: " + socket.getInetAddress());
				int clientID = connectionCount; // use number of connections for clientID
				
				// Create new server thread
				ServerThread thread = new ServerThread(socket, this, clientID);
				// add to list of serverThreads
				serverThreads.add(thread);
				// Send new client id back to client
				thread.sendMessage(new GameMessage(clientID)); 
				
				connectionCount++; // increment connection count
				
				System.out.println("Total connections: " + connectionCount);
			}
			
		} catch (IOException ioe) {
			System.out.println("ioe in Server constructor: " + ioe.getMessage());
		}
	}
	
	public void handleGameMessage(GameMessage gm) {
		System.out.println("Got message from " + gm.getClientID() + ": " + gm.getPosition().toString());
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to Froggerz Server!");
		int port = 55665;
		
		while(port == 0) {
			System.out.println("Please enter a port number:");
			try {
				port = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Please input a valid port number");
				scanner.nextLine();
				continue;
			}
		}
		
		// Start a server binded to the given port
		GameServer server = new GameServer(port);
		scanner.close();
	}
}
