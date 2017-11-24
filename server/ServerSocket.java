package froggerz.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/server")
public class ServerSocket {
	private static ConcurrentHashMap<Integer, RunningGame> runningGames;
	private RunningGame currentOpenGame = null;
	private ConcurrentHashMap<Session, RunningGame> connectedPlayers = new ConcurrentHashMap<Session, RunningGame>();
	private int gameNumber = 0;
	ExecutorService executors = Executors.newCachedThreadPool();

	// Can change method names, but cannot change annotations because that is what identifies what to call
	// These are called "Callback methods"
	@OnOpen
	public void open(Session session) {
		if(currentOpenGame == null) {  // First game ever
			createGame(session);
			currentOpenGame = runningGames.get(gameNumber-1);
			connectedPlayers.put(session, currentOpenGame);
		}
		else if (!currentOpenGame.gameFull()){  // Game isn't full
			currentOpenGame.addPlayer(session);
			connectedPlayers.put(session, currentOpenGame);
		}
		else {  // Game is full, create a new game
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
		RunningGame newGame = new RunningGame(gameNumber, this);
		newGame.addPlayer(session);
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
