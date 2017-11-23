package froggerz.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Manages direct connection between the client and server for game setup
 */
public class ServerThread extends Thread {
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private GameServer gameServer;
	private RunningGame game = null;  // Game this client is in
	private Socket socket;
	
	/**
	 * Constructor
	 * @param s Socket that connects the server and the client
	 * @param gr Instance of the server
	 */
	public ServerThread(Socket s, GameServer gs) {
		socket = s;
		// Set up socket input and output streams
		try {
			gameServer = gs;
			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.start();
		} catch(IOException ioe) {
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
		}
	}
	
	public void run() {
		try {
			// Loop to process ClientRequest during game setup
			boolean canKillThread = false;
			while(true) {
				// Break out of game setup processing, let Game take over processing
				if(canKillThread) {
					// Wait for all players to be in game
					while(!game.gameFull()) {
						Thread.yield();
					}
					gameRoom.removeThread(this);
					return;
				}
				
				// Get input from client
				GameRequest<String> gameRequest = (GameRequest<String>)(inputStream.readObject());
				if(gameRequest == null) {
					continue;
				}
				
				// Process Request
				String data = gameRequest.getData();
				RequestType request = gameRequest.getRequest();
				if(request == RequestType.GAMENAME) {  // Creating a new game, checking name
					ServerResponse<Boolean> response = new ServerResponse<Boolean>(gameRoom.gameNameTaken(data));
					outputStream.writeObject(response);
					outputStream.flush();
				}
				else if(request == RequestType.CREATEGAME) {  // Creating a new game
					String[] args = data.split(",");
					game = gameRoom.createGame(args[0], Integer.parseInt(args[1]), args[2], socket, outputStream, inputStream);
					canKillThread = true;
					
				}
				else if(request == RequestType.JOINGAME) {  // Joining a game
					ServerResponse<Boolean> response;
					// Sends a Game instance if joinable, otherwise send null
					if(gameRoom.gameIsWaiting(data)) {				
						game = gameRoom.getGame(data);
						
						response = new ServerResponse<Boolean>(true);
					}
					else {
						response = new ServerResponse<Boolean>(false);
					}
					outputStream.writeObject(response);
					outputStream.flush();
				}
				else if(request == RequestType.SETUSERNAME && game != null) {
					ServerResponse<Boolean> response;
					// Checks if username is available, if so, add the player
					if(game.containsUsername(data)) {  // Taken
						response = new ServerResponse<Boolean>(false);
						outputStream.writeObject(response);
						outputStream.flush();
					}
					else {  // Not taken
						response = new ServerResponse<Boolean>(true);
						outputStream.writeObject(response);
						outputStream.flush();
						game.addPlayer(socket, data, outputStream, inputStream);
						canKillThread = true;
					}
				}
			}  // end while
		} catch(IOException ioe) {
			System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
		} catch(ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
}
