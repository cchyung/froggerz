package froggerz.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class AnimatedSprite extends SpriteComponent
{
	private float mAnimSpeed = 15.0f;
	private float mAnimTimer = 0.0f;
	private Array<Texture> mImages = new Array<Texture>();
	
	public AnimatedSprite(Actor owner, int drawOrder) 
	{
		super(owner, drawOrder);
	}
	
	public void addImage(Texture texture)
	{
		mImages.add(texture);
	}
	
	public void update(float deltaTime)
	{
		mAnimTimer += mAnimSpeed * deltaTime;
	    
	    //If mAnimTimer gets too high, it needs to wrap back around (should never be higher than number of frames)
	    if (mAnimTimer >= mImages.size)
	    {
	        mAnimTimer = 0;
	    }
	    
	    //Cast mAnimTimer to an integer to get the current frame of the animation
	    int currentFrame = (int) mAnimTimer;
	    	    
	    setTexture(mImages.get(currentFrame));
	}
	
	public void setSpeed(float speed)
	{
		mAnimSpeed = speed;
	}
}
