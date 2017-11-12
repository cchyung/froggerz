package froggerz.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

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
}
