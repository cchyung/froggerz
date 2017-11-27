package froggerz.server;
import java.io.IOException;
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
	private Vector2 position = Vector2.Zero;
	
	public Player(int playerNum, Connection connection) {
		this.playerNum = playerNum;
		this.buttonsPressed = new ArrayList<PressableButton>();
		buttonsPressed = Collections.synchronizedList(buttonsPressed);
		this.connection = connection;
	}
	
	/**
	 * Sends game data to the player
	 * @param message data to send
	 */
	public void writeMessage(GameDataJSON data) {
		System.out.println("Sending player: " + playerNum + " data");
		connection.sendTCP(data);
	}
	
	/**
	 * Processes a JSON string to get which button a player pressed
	 * @param message JSON string
	 */
	public void processMessage(ButtonsJSON message) {
		PressableButton button = message.buttonPushed();
		buttonsPressed.add(button);
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
		position.x = x;
		position.y = y;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public int getPlayerNum() {
		return playerNum;
	}
}