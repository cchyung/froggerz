package froggerz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class FrogMove extends MoveComponent
{
	public FrogMove(Actor owner)
	{
		super(owner);
	}

	//Keep track of which button was pushed last, so we know which direction we need to turn
	boolean leftPushed = false;
	boolean rightPushed = false;
	boolean upPushed = true;
	boolean downPushed = false;
	
    public void processInput()
    {
    		//Move left
    		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
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
        		setForwardSpeed(150.0f);
        		
        		//Update which button was pushed most recently
        		leftPushed = true;
    			downPushed = false;
            upPushed = false;
            rightPushed = false;
        }
    		
    		//Move right
    		else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
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
			setForwardSpeed(150.0f);
			
    			//Update which button was pushed most recently
        		rightPushed = true;
            leftPushed = false;
            upPushed = false;
            downPushed = false;
        }
        
        //Move up
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
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
        		setForwardSpeed(150.0f);
        		
        		//Update which button was pushed most recently
        		upPushed = true;
            rightPushed = false;
            leftPushed = false;
            downPushed = false;
        }
        
        //Move down
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
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
        		setForwardSpeed(150.0f);
        		
        		//Update which button was pushed most recently
        		downPushed = true;
            rightPushed = false;
            leftPushed = false;
            upPushed = false;
        }
        
        //If neither up nor down is pushed, don't move forward/backward
        if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)
        		&& !Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            setForwardSpeed(0.0f);
        }
    }
}
