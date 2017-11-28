package froggerz.game;

import com.badlogic.gdx.math.Vector2;

public class MoveComponent extends Component
{
	private float mForwardSpeed;
	
	public MoveComponent(Actor owner)
	{
		super(owner);
	}
	
	public void update(float deltaTime)
	{
	    mOwner.setPosition(new Vector2(mOwner.getPosition().x + mOwner.getForward().x * mForwardSpeed * deltaTime,
	    		mOwner.getPosition().y + mOwner.getForward().y * mForwardSpeed * deltaTime));
	}
	
	public float getForwardSpeed()
	{
		return mForwardSpeed;
	}
	
	public void setForwardSpeed(float speed)
	{
		mForwardSpeed = speed;
	}
}