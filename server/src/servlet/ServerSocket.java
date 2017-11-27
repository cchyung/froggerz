package servlet;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import game.RunningGame;

@ServerEndpoint(value = "/server")
public class ServerSocket {
	private static ConcurrentHashMap<Integer, RunningGame> runningGames = new ConcurrentHashMap<Integer, RunningGame>();
	private static RunningGame currentOpenGame = null;
	private static ConcurrentHashMap<Session, RunningGame> connectedPlayers = new ConcurrentHashMap<Session, RunningGame>();
	private static int gameNumber = 0;
	private static ExecutorService executors = Executors.newCachedThreadPool();

	// Can change method names, but cannot change annotations because that is what identifies what to call
	// These are called "Callback methods"
	@OnOpen
	public void open(Session session) {
		System.out.println("Player has connected!");
		if(currentOpenGame == null) {  // First game ever
			System.out.println("Creating game and adding player to it");
			createGame(session);
			currentOpenGame = runningGames.get(gameNumber-1);
			connectedPlayers.put(session, currentOpenGame);
		}
		else if (!currentOpenGame.gameFull()){  // Game isn't full
			System.out.println("Adding player to not full game");
			currentOpenGame.addPlayer(session);
			connectedPlayers.put(session, currentOpenGame);
		}
		else {  // Game is full, create a new game
			System.out.println("Game was full, adding player to new game");
			createGame(session);
			currentOpenGame = runningGames.get(gameNumber-1);
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		ServerSocketWorker worker = new ServerSocketWorker(message, session);
	}

	@OnClose
	public void close(Session session) {
		System.out.println("Player Disconnected!");
		connectedPlayers.remove(session);
	}

	@OnError
	public void onError(Throwable error) {
		System.out.println("Error!");
		System.out.println("Cause: " + error.getCause());
		System.out.println("Message: " + error.getMessage());
		System.out.println("Stack: " + error.getStackTrace());
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
	public RunningGame createGame(Session session) {
		System.out.println("In creating game");
		RunningGame newGame = new RunningGame(gameNumber, this);
		newGame.addPlayer(session);
		System.out.println("Put into map");
		runningGames.put(gameNumber, newGame);
		System.out.println("Game number " + gameNumber + " created!");
		++gameNumber;
		return newGame;
	}
	
	/**
	 * Used to find which player is sending the message
	 * and hand off said message
	 */
	class ServerSocketWorker extends Thread{
		String message;
		Session session;
		
		private ServerSocketWorker(String message, Session session){
			this.message = message;
			this.session = session;
			executors.execute(this);
		}
		
		public void run() {
			RunningGame runningGame = connectedPlayers.get(session);
			runningGame.passMessage(session, message);
		}
	}
}
