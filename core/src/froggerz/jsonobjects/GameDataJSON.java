package froggerz.jsonobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Data to be sent from the server to the client
 */
public class GameDataJSON {
	private String command = "";  // Any specific command from the server
	private Array<Vector2> positions = new Array<Vector2>();  // Positions of players that is not this player
	private String data = "";
	
	/////////////////////////////////////// Setters/Getters ///////////////////////////////////////
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public void addPosition(Vector2 position) {
		position.add(position);
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getCommand() {
		return command;
	}
	
	public Array<Vector2> getPositions() {
		return positions;
	}
	
	public String getData() {
		return data;
	}
}
