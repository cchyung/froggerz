package froggerz.server;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import froggerz.jsonobjects.ButtonsJSON;
import froggerz.jsonobjects.GameDataJSON;
import froggerz.server.Buttons.PressableButton;

/**
 * Contains methods and variables needed for a running game
 */
public class RunningGame extends Thread {
	private final int maxPlayers = 2;
	private final float DELTATIME = 1.0f/60.0f;
	private long timePassed = 0L;
	private final int gameNumber;
	private GameListener gameServer;
	private ConcurrentHashMap<Integer, Player> players;
	private int numPlayers = 0;
	private ConcurrentHashMap<Integer, Vector2> startingPosition;
	private boolean gameOver = false;
	private long currentTime = 0L;
	private Server server;
	
	public RunningGame(int gameNumber, Server server, GameListener gameServer) {
		//System.out.println("Entering the constructor");
		this.gameServer = gameServer;
		this.gameNumber = gameNumber;	
		this.server = server;
		this.players = new ConcurrentHashMap<Integer, Player>();
		this.startingPosition = new ConcurrentHashMap<Integer, Vector2>();
		//System.out.println("Exiting the constructor");
	}
	
	public void run() {
		// Send player start position
		for (Map.Entry<Integer, Player> sendToPlayer : players.entrySet()) {
			// Send position to the player
			PositionPacket data = new PositionPacket();
			data.playerNum = sendToPlayer.getValue().getPlayerNum();
			data.vector2 = sendToPlayer.getValue().getPosition().toString();
			System.out.println("Sending starting position to player: " + sendToPlayer.getValue().getPlayerNum());
			System.out.println("Vector: " + data.vector2);
			System.out.println("Sending update frogs to: " + sendToPlayer.getValue().getPlayerNum());
			server.sendToTCP(sendToPlayer.getKey(), data);
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Send the frogs start positions
		updateGame();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Send signal to start the game
		GameDataJSON data = new GameDataJSON();
		data.setCommand(1);
		broadcast(data);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//currentTime += Gdx.graphics.getDeltaTime();
		System.out.println("About to enter gameOver loop");
		while(!gameOver) {
			
			updateGame();
		}
		
		// TODO Send the close websocket command
		
		// Game is no longer running
		gameServer.gameOver(gameNumber);
	}
	
	public void updateGame() {
		
		// Wait 1 second
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Game " + gameNumber + ": Updating positions of players");
		// Iterate through all players
		for (Map.Entry<Integer, Player> entry : players.entrySet()) { 
			Player player = entry.getValue();
			Vector2 position = player.getPosition();
			
			// Process all buttons the player pressed
			ArrayList<PressableButton> buttonSequence = player.getButtonsPressed();
			for(PressableButton button : buttonSequence) {
				if(button == PressableButton.LEFT) {
					position.x += -32.0f;
				}
				else if(button == PressableButton.RIGHT) {
					position.x += 32.0f;
				}
				else if(button == PressableButton.UP) {
					position.y += 32.0f;
				}
				else if(button == PressableButton.DOWN) {
					position.y += -32.0f;
				}
				
				player.setPosition(position.x, position.y);
				
				// TODO Check to see if finishline was hit at this move
//				if() {
//					gameOver = true;
//					return;
//				}
			}
		}

		// Send positions of players to other players
		for (Map.Entry<Integer, Player> sendToPlayer : players.entrySet()) {
			GameDataJSON data = new GameDataJSON();
			data.setCommand(3);
			for (Map.Entry<Integer, Player> playerToSend : players.entrySet()) { 
				// Don't send the position of this player to 
				if(sendToPlayer.getKey() != playerToSend.getKey()) {
					data.addPosition(playerToSend.getValue().getPosition());
				}	
			}
			System.out.println("Sending update frogs to: " + sendToPlayer.getValue().getPlayerNum());
			server.sendToTCP(sendToPlayer.getKey(), data);
		}
	}

	/**
	 * Passes data from another source to a player
	 * @param session The player's session
	 * @param message Message to pass
	 */
	public void passMessage(Connection connection, ButtonsJSON message) {
		players.get(connection.getID()).processMessage(message);
	}
	
	/**
	 * 
	 * @param message Send a message to all players
	 */
	public void broadcast(GameDataJSON message) {
		for (Map.Entry<Integer, Player> entry : players.entrySet()) { 
			server.sendToTCP(entry.getKey(), message);
		}
	}
	
	public void addPlayer(Connection connection) {	
		//System.out.println("Adding player to game");
		Player newPlayer = new Player(numPlayers, connection);
		newPlayer.setPosition((numPlayers+1) * 160.0f, 32.0f);
		players.put(connection.getID(), newPlayer);
		numPlayers++;
		//System.out.println("Added player");
		if(gameFull()) {
			System.out.println("Last player added, starting game");
			this.start();  // Start the game
		}
	}
	
	public boolean gameFull() {
		return numPlayers == maxPlayers;
	}
}
