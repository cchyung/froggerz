package froggerz.server;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

public class GameMessage implements Serializable {
	private Vector2 position;
	private int clientID;
	
	private static final long serialVersionUID = 1L;
	
	public GameMessage(Vector2 position) {
		this.position = position;
	}
	
	public GameMessage(Vector2 position, int clientID) {
		this.position = position;
		this.clientID = clientID;
	}
	
	public GameMessage(int clientID) {
		this.clientID = clientID;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public int getClientID() {
		return clientID;
	}
}
