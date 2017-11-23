package froggerz.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

import froggerz.game.Buttons.PressableButton;
import froggerz.jsonobjects.GameDataJSON;

/**
 * Contains methods and variables needed for a running game
 */
public class RunningGame extends Thread {
	private final int maxPlayers = 4;
	private final float DELTATIME = 1.0f/60.0f;
	private final int gameNumber;
	private ServerSocket gameServer;
	private ConcurrentHashMap<Session, Player> players;
	private boolean gameOver = false;
	private Json json;
	
	public RunningGame(int gameNumber, ServerSocket gameServer) {
		this.gameServer = gameServer;
		this.gameNumber = gameNumber;	
		this.players = new ConcurrentHashMap<Session, Player>(maxPlayers);
		this.json = new Json();
	}
	
	public void run() {
		// Send signal to start the game
		GameDataJSON data = new GameDataJSON();
		data.setCommand("start");
		broadcast(json.toJson(data));
		
		while(!gameOver) {
			updateGame();
		}
		
		// TODO Send the close websocket command
		
		// Game is no longer running
		gameServer.gameOver(gameNumber);
	}
	
	public void updateGame() {
		System.out.println("Game " + gameNumber + ": Updating positions of players");
		// Iterate through all players
		for (Map.Entry<Session, Player> entry : players.entrySet()) { 
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
		for (Map.Entry<Session, Player> sendToPlayer : players.entrySet()) {
			GameDataJSON data = new GameDataJSON();
			
			for (Map.Entry<Session, Player> playerToSend : players.entrySet()) { 
				// Don't send the position of this player to 
				if(sendToPlayer.getValue() == playerToSend.getValue()) {
					continue;
				}
				
				data.addPosition(playerToSend.getValue().getPosition());
			}
			
			sendToPlayer.getValue().writeMessage(json.toJson(data));
		}
	}

	/**
	 * Passes data from another source to a player
	 * @param session The player's session
	 * @param message Message to pass
	 */
	public void passMessage(Session session, String message) {
		players.get(session).processMessage(message);
	}
	
	/**
	 * 
	 * @param message Send a message to all players
	 */
	public void broadcast(String message) {
		for (Map.Entry<Session, Player> entry : players.entrySet()) { 
			entry.getValue().writeMessage(message);
		}
	}
	
	public synchronized void addPlayer(Session session) {	
		
		Player newPlayer = new Player(players.size(), session);
		newPlayer.setPosition((players.size()+1) * 160.0f, 32.0f);
		players.put(session, newPlayer);
		
		if(gameFull()) {
			this.start();  // Start the game
		}
	}
	
	public boolean gameFull() {
		return players.size() == maxPlayers;
	}
}
