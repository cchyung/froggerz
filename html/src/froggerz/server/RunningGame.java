package froggerz.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.websocket.Session;

import com.badlogic.gdx.math.Vector2;

import froggerz.jsonobjects.GameDataJSON;

/**
 * Contains methods and variables needed for a running game
 */
public class RunningGame extends Thread {
	Vector<Vector2> playerPositions = null;
	private final int maxPlayers = 4;
	private final float DELTATIME = 1.0f/60.0f;
	private final int gameNumber;
	private GameServer gameServer;
	private List<Player> players;
	
	
	public RunningGame(int gameNumber, GameServer gameServer) {
		this.gameServer = gameServer;
		this.gameNumber = gameNumber;	
		this.players = new ArrayList<Player>(maxPlayers);
		players = Collections.synchronizedList(players);
	}
	
	public void run() {
		boolean gameOver = false;
		while(!gameOver) {
			updateGame();
		}
	}
	
	public void updateGame() {
//		System.out.println("Received message from player");
//		
//		String message = "";
//		GameDataJSON data;
//		data.setDeltaTime(DELTATIME);
//		
//		try {
//			for(Session s : players) {
//				
//
//				s.getBasicRemote().sendText(message);
//			}
//		} catch(IOException ioe) {
//			System.out.println("ioe: " + ioe.getMessage());
//			close(session);
//		}
	}

	public void broadcast(String message) {
		for(Player player : players) {
				player.writeMessage(message);
		}
	}
	
	public synchronized void addPlayer(Socket socket) {	
		
		Player newPlayer = new Player(players.size(), socket);
		players.add(newPlayer);
		
		if(gameFull()) {
			this.start();  // Start the game
		}
	}
	
	public boolean gameFull() {
		return players.size() == maxPlayers;
	}
}
