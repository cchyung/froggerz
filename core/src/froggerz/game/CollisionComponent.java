package froggerz.game;

import com.badlogic.gdx.math.Vector2;

public class CollisionComponent extends Component {
	private float mWidth;
	private float mHeight;
	
	public CollisionComponent(Actor owner) {
		super(owner);
	}
	
	
	// Set width/height of this box
	
	void SetSize(float width, float height)
	{
		mWidth = width;
		mHeight = height;
	}
	
	/**
	 * Detects if two collison boxes intersect
	 * @param other
	 * @return
	 */
	boolean Intersect(final CollisionComponent other) {
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
	
	final Vector2 getMin()
	{
		Vector2 min = mOwner.getPosition();
		min.x = (mWidth * mOwner.getScale()) / 2.0f;
		min.y = (mHeight * mOwner.getScale()) / 2.0f;

		return min;
	}

	final Vector2 getMax()
	{
		Vector2 max = mOwner.getPosition();
		max.x = (mWidth * mOwner.getScale()) / 2.0f;
		max.y = (mHeight * mOwner.getScale()) / 2.0f;
		return max;
	}

	final Vector2 getCenter() {
		return mOwner.getPosition();
	}
	
	final float getWidth() {
		return mWidth; 
	}
	
	final float getHeight() {
		return mHeight;
	}
}
