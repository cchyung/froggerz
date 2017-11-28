package froggerz.game;

import com.badlogic.gdx.math.Vector2;

public class CollisionComponent extends Component {
	private float mWidth;
	private float mHeight;
	
	public CollisionComponent(Actor owner) {
		super(owner);
	}
	
	
	/**
	 * Sets the width and height of the collision box
	 * @param width
	 * @param height
	 */
	public void SetSize(float width, float height)
	{
		mWidth = width;
		mHeight = height;
	}
	
	/**
	 * Detects if two collison boxes intersect
	 * @param other
	 * @return
	 */
	public boolean Intersect(final CollisionComponent other) {
		boolean allFalse = true;

		if (getMax().x < other.getMin().x)  // Left
		{
			allFalse = false;
		}
		if (other.getMax().x < getMin().x)  // Right
		{
			allFalse = false;
		}
		if (other.getMax().y < getMin().y)  // Above
		{
			allFalse = false;
		}
		if (getMax().y < other.getMin().y)  // Below
		{
			allFalse = false;
		}

		return allFalse;
	}
	
	public final Vector2 getMin()
  	{
  		Vector2 min = mOwner.getPosition();
  		return min;
  	}

	public final Vector2 getMax()
	{
		Vector2 max = new Vector2(mOwner.getPosition().x + mWidth, mOwner.getPosition().y + mHeight);
		return max;
	}

	public final Vector2 getCenter() {
		return mOwner.getPosition();
	}
	
	public final float getWidth() {
		return mWidth; 
	}
	
	public final float getHeight() {
		return mHeight;
	}
}
