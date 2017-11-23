package froggerz.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.badlogic.gdx.utils.Json;

import froggerz.game.Buttons.PressableButton;

public class Player{
	private int playerNum;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private List<PressableButton> buttonsPressed;
	private boolean quit = false;
	private Lock lock;
	private Json json;
	
	public Player(int playerNum, Socket socket) {
		this.playerNum = playerNum;
		this.buttonsPressed = new ArrayList<PressableButton>();
		buttonsPressed = Collections.synchronizedList(buttonsPressed);
		lock = new ReentrantLock();
		json = new Json();
		try {
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			
			// Receive button input
			while(!quit) {
				PressableButton button = json.fromJson(PressableButton.class ,(String)(inputStream.readObject()));
				if(button == null) {
					continue;
				}
				
				lock.lock();
				buttonsPressed.add(button);
				lock.unlock();
			}
		} catch (IOException ioe) {
			System.out.println("ioe in Player constructor: " + ioe.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe in Player constructor: " + cnfe.getMessage());
		}	
	}
	
	/**
	 * Sends a GameMessage to the player
	 * @param gameMessage message to send
	 */
	public void writeMessage(String message) {
		try {
			outputStream.writeObject(message);
			outputStream.flush();
		} catch (IOException ioe) {
			System.out.println("ioe in Player::writeMessage(): " + ioe.getMessage());
		}
	}
	
	/**
	 * @return The sequence of buttons the player has pressed
	 */
	public List<PressableButton> getButtonsPressed() {
		// Ensure only the current sequence of buttons are returned
		lock.lock();
		ArrayList<PressableButton> buttons = new ArrayList<PressableButton>(buttonsPressed);
		buttonsPressed.clear();
		lock.unlock();
		return buttons;
	}
}