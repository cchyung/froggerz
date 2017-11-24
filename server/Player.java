package froggerz.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.websocket.Session;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

import froggerz.game.Buttons.PressableButton;
import froggerz.jsonobjects.ButtonsJSON;
import froggerz.jsonobjects.GameDataJSON;

public class Player{
	private int playerNum;
	private Session session;
	private List<PressableButton> buttonsPressed;
	private Json json;
	private Vector2 position = Vector2.Zero;
	
	public Player(int playerNum, Session session) {
		this.playerNum = playerNum;
		this.buttonsPressed = new ArrayList<PressableButton>();
		buttonsPressed = Collections.synchronizedList(buttonsPressed);
		json = new Json();
		this.session = session;
	}
	
	/**
	 * Sends game data to the player
	 * @param message data to send
	 */
	public void writeMessage(String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException ioe) {
			System.out.println("ioe in Player::writeMessage(): " + ioe.getMessage());
		}
	}
	
	/**
	 * Processes a JSON string to get which button a player pressed
	 * @param message JSON string
	 */
	public void processMessage(String message) {
		ButtonsJSON clientData = json.fromJson(ButtonsJSON.class, message);
		PressableButton button = clientData.buttonPushed();
		synchronized(buttonsPressed) {
			buttonsPressed.add(button);
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
		position.x = x;
		position.y = y;
	}
	
	public Vector2 getPosition() {
		return position;
	}
}