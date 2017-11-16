package froggerz.game;

import com.badlogic.gdx.utils.Array;

public class River 
{
	private float mSpeed;
	private float mDirection;
	private Array<Actor> mThingsInRiver = new Array<Actor>();
	
	public void setSpeed(float speed)
	{
		mSpeed = speed;
	}
	
	public void setDirection(float direction)
	{
		mDirection = direction;
	}
	
	public void addThingToRiver(Actor floater)
	{
		mThingsInRiver.add(floater);
		floater.getMove().setForwardSpeed(mDirection * mSpeed);
	}
}
