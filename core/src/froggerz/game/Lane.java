package froggerz.game;

import com.badlogic.gdx.utils.Array;

public class Lane 
{
	private float mSpeed;
	private float mDirection;
	private Array<Actor> mCarsInLane = new Array<Actor>();
	
	public void setSpeed(float speed)
	{
		mSpeed = speed;
	}
	
	public void setDirection(float direction)
	{
		mDirection = direction;
	}
	
	public void addVehicleToLane(Actor car)
	{
		mCarsInLane.add(car);
		car.getMove().setForwardSpeed(mDirection * mSpeed);
		
		//if the lane's direction is positive, the car is moving to the right
		//and the sprite needs to be turned around
		if (mDirection > 0)
		{
			car.getSprite().getSprite().flip(true, false);
		}
	}
}
