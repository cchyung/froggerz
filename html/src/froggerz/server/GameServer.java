package froggerz.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server for a Black Jack game
 */
public class GameServer {
	private ConcurrentHashMap<Integer, RunningGame> runningGames;
	private RunningGame currentOpenGame = null;
	private int gameNumber = 0;
	
	/**
	 * @param serversocket ServerSocket that listens for clients
	 */
	public GameServer(ServerSocket serversocket) {
		ServerSocket ss = serversocket;
		runningGames = new ConcurrentHashMap<Integer, RunningGame>();
		try {
			while(true) {
				Socket s = ss.accept();
				
				if(currentOpenGame == null) {  // First game ever
					createGame(s);
					currentOpenGame = runningGames.get(gameNumber-1);
				}
				else if (!currentOpenGame.gameFull()){  // Game isn't full
					currentOpenGame.addPlayer(s);
				}
				else {  // Game is full, create a new game
					createGame(s);
					currentOpenGame = runningGames.get(gameNumber-1);
				}
			}
		} catch (IOException ioe) {
			System.out.println("ioe in GameServer constructor: " + ioe.getMessage());
		}
	}
	
	public void gameOver(int gameNum) {
		runningGames.remove(gameNum);
	}
	
	public RunningGame createGame(Socket socket) {
		RunningGame newGame = new RunningGame(gameNumber, this);
		newGame.addPlayer(socket);
		runningGames.put(gameNumber, newGame);
		System.out.println("Game number " + gameNumber + " created!");
		++gameNumber;
		return newGame;
	}
	
	/**
	 * Main method to run the Black Jack server
	 */
	public static void main(String[] args) {
		Integer port = 8080;
		ServerSocket ss = null;
		System.out.println("Welcome to the Froggerz server Server!");
		
		try {
			ss = new ServerSocket(port);
			System.out.println("Successfully started the Froggerz on port " + ss.getLocalPort());
		} catch(IOException ioe) {
			System.out.println("Unable to connect to server with port " + port);
		}
		
		GameServer gameServer = new GameServer(ss);
	}
}

