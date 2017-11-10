package froggerz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class FrogMove extends MoveComponent
{
	public FrogMove(Actor owner)
	{
		super(owner);
	}
	
	public void update(float deltaTime)
	{
		
	}
	
    public void processInput()
    {
    		//Move left
    		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
    		{
    			setAngularSpeed((float)-Math.PI / 2);
        }
    		//Move right
    		else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
			setAngularSpeed((float)Math.PI / 2);
        }
    		//If neither up nor down is pushed, don't move forward
        if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            setAngularSpeed(0.0f);
        }
        
        //Move up
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            setForwardSpeed(200.0f);
        }
        
        //Move down
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            setForwardSpeed(-200.0f);
        }
        
        //If neither up nor down is pushed, don't move forward
        if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            setForwardSpeed(0.0f);
        }
    }
}
