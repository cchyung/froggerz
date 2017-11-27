package froggerz.jsonobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Data to be sent from the server to the client
 */
public class GameDataJSON {
	public int command = -1;  // Any specific command from the server
	public Array<Vector2> positions = new Array<Vector2>();  // Positions of players that is not this player
	
	public GameDataJSON(){
	}
	
	/////////////////////////////////////// Setters/Getters ///////////////////////////////////////
	
	public void setCommand(int command) {
		this.command = command;
	}
	
	public void addPosition(Vector2 position) {
		positions.add(position);
	}
	
	public void setPositions(Array<Vector2> positions) {
		this.positions = positions;
	}
	
	public int getCommand() {
		return command;
	}
	
	public Array<Vector2> getPositions() {
		return positions;
	}
}
