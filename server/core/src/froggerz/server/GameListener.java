package froggerz.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import froggerz.jsonobjects.ButtonsJSON;

public class GameListener extends Listener {
	private static ConcurrentHashMap<Integer, RunningGame> runningGames = new ConcurrentHashMap<Integer, RunningGame>();
	private static RunningGame currentOpenGame = null;
	private static ConcurrentHashMap<Connection, RunningGame> connectedPlayers = new ConcurrentHashMap<Connection, RunningGame>();
	private static int gameNumber = 0;
	private static ExecutorService executors = Executors.newCachedThreadPool();
	private Server server;

	public GameListener(Server server) {
		this.server = server;
	}
	
	public void connected(Connection connection) {
		System.out.println("Player has connected! " + connection.getID());
		if(currentOpenGame == null) {  // First game ever
			//System.out.println("Creating game and adding player to it");
			createGame(connection);
			currentOpenGame = runningGames.get(gameNumber-1);
			connectedPlayers.put(connection, currentOpenGame);
		}
		else if (!currentOpenGame.gameFull()){  // Game isn't full
			//System.out.println("Adding player to not full game");
			connectedPlayers.put(connection, currentOpenGame);
			currentOpenGame.addPlayer(connection);
		}
		else {  // Game is full, create a new game
			//System.out.println("Game was full, adding player to new game");
			createGame(connection);
			currentOpenGame = runningGames.get(gameNumber-1);
		}
	}


	// TODO
	public void received (Connection connection, Object object) {
		if (object instanceof ButtonsJSON) {
			connectedPlayers.get(connection).passMessage(connection, (ButtonsJSON)object);
		}
	}

	public void disconnected(Connection c){
		connectedPlayers.remove(c);
		System.out.println("Connection dropped.");
	}

	/**
	 * Game is no longer running, remove it
	 * @param gameNum Number associated with the game instance
	 */
	public void gameOver(int gameNum) {
		runningGames.remove(gameNum);
	}

	/**
	 * Creates a new Froggerz game
	 * @param session First player in the game
	 * @return Instance of RunningGame created
	 */
	public RunningGame createGame(Connection connection) {
		//System.out.println("In creating game");
		RunningGame newGame = new RunningGame(gameNumber, server, this);
		//System.out.println("Put into map");
		runningGames.put(gameNumber, newGame);
		newGame.addPlayer(connection);
		System.out.println("Game number " + gameNumber + " created!");
		++gameNumber;
		return newGame;
	}

	/**
	 * Used to find which player is sending the message
	 * and hand off said message
	 */
	class ServerSocketWorker extends Thread{
		ButtonsJSON message;
		Connection connection;

		private ServerSocketWorker(ButtonsJSON message, Connection connection){
			this.message = message;
			this.connection = connection;
			executors.execute(this);
		}
		
		public void run() {
			RunningGame runningGame = connectedPlayers.get(connection);
			runningGame.passMessage(connection, message);
		}
	}
}
