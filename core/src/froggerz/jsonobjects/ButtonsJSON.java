package froggerz.jsonobjects;

import com.badlogic.gdx.utils.Array;

public class ButtonsJSON {
	
	/**
	 * Buttons and their index 
	 */
	private enum Buttons{
		LEFT, RIGHT, UP, DOWN;
		
		private transient int totalButtons = 0;
		private transient int idx;
		
		Buttons(){
			idx = totalButtons;
			++totalButtons;
		}
		
		public int index() { return idx; }
	}
	
	// Buttons
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;
	private final static transient int numOfButtons = 4;

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
	 * @return Array of the 
	 */
	public Array<Boolean> getButtonStates(){
		// Add buttons in the order they were created in the enum Buttons
		Array<Boolean> pushed = new Array<Boolean>(numOfButtons);
		pushed.add(left);
		pushed.add(right);
		pushed.add(up);
		pushed.add(down);
		
		return pushed;
	}
}
