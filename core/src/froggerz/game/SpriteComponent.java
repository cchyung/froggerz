package froggerz.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class SpriteComponent extends Component
{
	private Sprite mSprite = null;
	private int mDrawOrder = 100;
	
	/**
	 * Constructor
	 * @param owner
	 * @param drawOrder
	 */
	public SpriteComponent(Actor owner, int drawOrder) 
	{
		super(owner);
		mDrawOrder = drawOrder;
		mSprite = new Sprite();
		mOwner.getGame().addSprite(this);
	}
	
	/**
	 * Removes this sprite from mOwner's game
	 */
	public void destroy() 
	{
		mOwner.getGame().removeSprite(this);
	}
	
	/**
	 * @param texture
	 */
	public void setTexture(Texture texture) 
	{
		mSprite.setRegion(texture);
	}
	
	/**
	 * @param sprite Sprite to set
	 */
	public void setSprite(Sprite sprite) 
	{
		mSprite = sprite;
	}
	
	/**
	 * @param position Position of the sprite
	 */
	public void setPosition(Vector2 position) {
		mSprite.setPosition(position.x, position.y);
	}
	
	/**
	 * @param width Width of the sprite image
	 * @param height Height of the sprite image
	 */
	public void setSize(float width, float height) {
		mSprite.setSize(width, height);
	}
	
	/**
	 * @return Sprite that belongs to the SpriteComponent
	 */
	public Sprite getSprite() 
	{
		return mSprite;
	}
	
	/**
	 * @return mDrawOrder
	 */
	public int getDrawOrder() 
	{
		return mDrawOrder;
	}
	
	/**
	 * @param drawOrder
	 */
	public void setDrawOrder(int drawOrder) 
	{
		mDrawOrder = drawOrder;
	}
	
	public void update(float deltaTime)
	{
		mSprite.setPosition(mOwner.getPosition().x, mOwner.getPosition().y);
	}
	
	protected int mCurrentFrame = -1;
	public int getCurrentFrame() { return mCurrentFrame; }

}