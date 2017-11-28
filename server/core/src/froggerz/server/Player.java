package froggerz.server;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;

import froggerz.jsonobjects.ButtonsJSON;
import froggerz.jsonobjects.GameDataJSON;
import froggerz.server.Buttons.PressableButton;

public class Player{
	private int playerNum;
	private Connection connection;
	private List<PressableButton> buttonsPressed;
	private Vector2 position;
	
	public Player(int playerNum, Connection connection) {
		this.playerNum = playerNum;
		this.buttonsPressed = new ArrayList<PressableButton>();
		buttonsPressed = Collections.synchronizedList(buttonsPressed);
		this.connection = connection;
		position = new Vector2();
	}
	
	/**
	 * Sends game data to the player
	 * @param message data to send
	 */
	public void writeMessage(Object data) {
		System.out.println("Sending player: " + playerNum + " data");
		if (data instanceof GameDataJSON) {
			connection.sendTCP((GameDataJSON)data);

        }
		else if (data instanceof PositionPacket) {
			connection.sendTCP((PositionPacket)data);

        }
		
	}
	
	/**
	 * Processes a JSON string to get which button a player pressed
	 * @param message JSON string
	 */
	public void processMessage(ButtonsJSON message) {
		PressableButton button = message.buttonPushed();
		System.out.println(button);
		synchronized(position) {
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
		}
	}
	
	/**
	 * @return The sequence of buttons the player has pressed
	 */
	public ArrayList<PressableButton> getButtonsPressed() {
		// Ensure only the current sequence of buttons are returned
		ArrayList<PressableButton> buttons = null;
		synchronized(buttonsPressed) {
			buttons = new ArrayList<PressableButton>(buttonsPressed);
			buttonsPressed.clear();
		}
		return buttons;
	}
	
	public void setPosition(float x, float y) {
		synchronized(position) {
			position.x = x;
			position.y = y;
		}
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public int getPlayerNum() {
		return playerNum;
	}
}