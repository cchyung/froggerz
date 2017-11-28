package froggerz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.esotericsoftware.kryonet.Client;

import froggerz.game.Buttons.PressableButton;
import froggerz.jsonobjects.ButtonsJSON;

/**
 * 
 */
public class FrogMove extends MoveComponent
{
	Json json;
	
	//Keep track of which button was pushed last, so we know which direction we need to turn
	boolean leftPushed = false;
	boolean rightPushed = false;
	boolean upPushed = true;
	boolean downPushed = false;
	PressableButton buttonPushed = null;
	PressableButton buttonPushedLast = null;
	PressableButton buttonToUpdate = null;
	
	public FrogMove(Actor owner)
	{
		super(owner);
		json = new Json();
	}
	
	@Override
	public void processInput()
	{
		// Get what button was pushed
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			buttonPushed = PressableButton.LEFT;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			buttonPushed = PressableButton.RIGHT;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			buttonPushed = PressableButton.UP;
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			buttonPushed = PressableButton.DOWN;
		}
		else {
			buttonPushed = null;
		}
		
		// Do not let the user continually move in one direction
		if(buttonPushedLast == buttonPushed) {
			buttonToUpdate = null;
			return;
		}
		buttonPushedLast = buttonPushed;
		buttonToUpdate = buttonPushed;
		
		//Move left
		if (buttonPushed == PressableButton.LEFT)
		{
			mOwner.setRotation((float)Math.PI);
			//Need to turn 180 if we were just facing right
			if (rightPushed)
			{
				mOwner.getSprite().getSprite().rotate90(true);
				mOwner.getSprite().getSprite().rotate90(true);
			}

			//Turn counterclockwise if we were just facing up
			if (upPushed)
			{
				mOwner.getSprite().getSprite().rotate90(false);
			}

			//Turn clockwise if we were just facing down
			else if (downPushed)
			{
				mOwner.getSprite().getSprite().rotate90(true);
			}	

			//Update which button was pushed most recently
			leftPushed = true;
			downPushed = false;
			upPushed = false;
			rightPushed = false;
		}

		//Move right
		else if(buttonPushed == PressableButton.RIGHT)
		{
			mOwner.setRotation(0.0f);
			//Need to turn 180 if we were just facing left
			if (leftPushed)
			{
				mOwner.getSprite().getSprite().rotate90(true);
				mOwner.getSprite().getSprite().rotate90(true);
			}

			//Turn clockwise if we were just facing up
			if (upPushed)
			{
				mOwner.getSprite().getSprite().rotate90(true);
			}

			//Turn counterclockwise if we were just facing down
			else if (downPushed)
			{
				mOwner.getSprite().getSprite().rotate90(false);
			}

			//Update which button was pushed most recently
			rightPushed = true;
			leftPushed = false;
			upPushed = false;
			downPushed = false;
		}

		//Move up
		if(buttonPushed == PressableButton.UP)
		{
			mOwner.setRotation((float)Math.PI/2);
			//Need to turn 180 if we were just facing up
			if (downPushed)
			{
				mOwner.getSprite().getSprite().rotate90(true);
				mOwner.getSprite().getSprite().rotate90(true);
			}

			//Turn clockwise if we were just facing left
			if (leftPushed)
			{
				mOwner.getSprite().getSprite().rotate90(true);
			}

			//Turn counterclockwise if we were just facing right
			else if (rightPushed)
			{
				mOwner.getSprite().getSprite().rotate90(false);
			}

			//Update which button was pushed most recently
			upPushed = true;
			rightPushed = false;
			leftPushed = false;
			downPushed = false;
		}

		//Move down
		else if(buttonPushed == PressableButton.DOWN)
		{
			mOwner.setRotation(-(float)Math.PI/2);
			//Need to turn 180 if we were just facing up
			if (upPushed)
			{
				mOwner.getSprite().getSprite().rotate90(false);
				mOwner.getSprite().getSprite().rotate90(false);
			}

			//Turn counterclockwise if we were just facing left
			if (leftPushed)
			{
				mOwner.getSprite().getSprite().rotate90(false);
			}

			//Turn clockwise if we were just facing right
			else if (rightPushed)
			{
				mOwner.getSprite().getSprite().rotate90(true);
			}

			//Update which button was pushed most recently
			downPushed = true;
			rightPushed = false;
			leftPushed = false;
			upPushed = false;
		}

		// Get game socket to send input to server
		Client gameSocket = mOwner.getGame().getClient();
		ButtonsJSON buttons = new ButtonsJSON();
		
		// If no direction are pushed, do not move
		if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)
				&& !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			buttonPushed = null;
		}
		else {  // Button pressed, send to server
			if(leftPushed) {
				buttons.leftPushed();
			}
			else if(rightPushed) {
				buttons.rightPushed();
			}
			else if(upPushed) {
				buttons.upPushed();
			}
			else if(downPushed) {
				buttons.downPushed();
			}
			
			gameSocket.sendTCP(buttons);
		}
	}
	
	@Override
	public void update(float deltaTime)
	{
		// How much the frog should leap
		float changeX = 0.0f;
		float changeY = 0.0f;
		
		// Direction that was pressed
		if(buttonToUpdate == null) {
			
		}
		if(buttonToUpdate == PressableButton.LEFT) {
			changeX = -32.0f;
		}
		else if(buttonToUpdate == PressableButton.RIGHT) {
			changeX = 32.0f;
		}
		else if(buttonToUpdate == PressableButton.UP) {
			changeY = 32.0f;
		}
		else if(buttonToUpdate  == PressableButton.DOWN) {
			changeY = -32.0f;
		}
		
		Vector2 newPosition = mOwner.getPosition();
		newPosition.x += changeX;
		newPosition.y += changeY;
		mOwner.setPosition(newPosition);
	}
}
