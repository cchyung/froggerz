package froggerz.jsonobjects;

import froggerz.server.Buttons.PressableButton;

/**
 * Input from a client to update player position on the server
 */
public class ButtonsJSON {
	
	// Buttons
	public boolean left = false;
	public boolean right = false;
	public boolean up = false;
	public boolean down = false;
	public final static transient int numOfButtons = 4;
	
	public ButtonsJSON() {
	}

	public void leftPushed() {
		left = true;
	}

	public void rightPushed() {
		right = true;
	}
	
	public void upPushed() {
		up = true;
	}
	
	public void downPushed() {
		down = true;
	}
	
	/**
	 * @return Button pressed
	 */
	public PressableButton buttonPushed(){
		if(left) {
			return PressableButton.LEFT;
		}
		else if(right) {
			return PressableButton.RIGHT;
		}
		else if(up) {
			return PressableButton.UP;
		}
		else if(down) {
			return PressableButton.DOWN;
		}
		else {
			return null;
		}
	}
}
