package froggerz.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Actor 
{
	private Game mGame = null;
	private State mState = State.EActive;
	private Vector2 mPosition = Vector2.Zero;
	private float mRotation = 0.0f;
	private float mScale = 1.0f;
	
	// Components
	protected SpriteComponent mSpriteComp;
	protected MoveComponent mMoveComp;
	protected CollisionComponent mCollComp;
	
	public enum State 
	{
		EActive,
		EPaused,
		EDead
	}
	
	/**
	 * Constructor
	 * @param game
	 */
	public Actor(Game game) 
	{
		mGame = game;
		mGame.addActor(this);
		//mCollComp = new CollisionComponent(this);
	}
	
	/**
	 * Performed when the object is no longer needed, unloads any data
	 */
	public void destroy() 
	{
		if (mSpriteComp != null)
		{
			mSpriteComp.destroy();
		}
		if (mMoveComp != null)
		{
			mMoveComp.destroy();
		}
		if (mCollComp != null)
		{
			mCollComp.destroy();
		}
		mGame.removeActor(this);
	}
	
	/**
	 *  Update function called from Game
	 * @param deltaTime
	 */
	public void update(float deltaTime) 
	{
		if (mState == State.EActive)
		{
			// Update Components
			if (mSpriteComp != null)
			{
				mSpriteComp.update(deltaTime);
			}	
			if (mMoveComp != null)
			{
				mMoveComp.update(deltaTime);
			}
			updateActor(deltaTime);
		}
	}
	
	/**
	 * Any Actor-specific update code
	 * @param deltaTime
	 */
	public void updateActor(float deltaTime) 
	{
		
	}
	
	/**
	 * processInput function called from Game
	 */
	void processInput() 
	{
		if (mState == State.EActive)
		{
			// ProcessInput of components
			if (mMoveComp != null)
			{
				mMoveComp.processInput();
			}
			if (mSpriteComp != null)
			{
				mSpriteComp.processInput();
			}
			if (mCollComp != null)
			{
				mCollComp.processInput();
			}
			actorInput();
		}
	}
	
	/**
	 * Any Actor-specific update code
	 */
	public void actorInput() 
	{
	}
	
	/**
	 * Returns the forward direction vector of the Actor
	 * @return
	 */
	public Vector2 getForward() 
	{
		return new Vector2(MathUtils.cos(mRotation), MathUtils.sin(mRotation));
	}
	
	//////////////////////////////// SETTERS/GETTERS ////////////////////////////////
	
	public Vector2 getPosition() { return mPosition; }
	public void setPosition(Vector2 pos) { mPosition = pos; mSpriteComp.setPosition(mPosition); }
	public float getRotation() { return mRotation; }
	public void setRotation(float rotation) { mRotation = rotation; }
	public SpriteComponent getSprite() { return mSpriteComp; }
	public void setSprite(SpriteComponent sprite) { mSpriteComp = sprite; }
	public MoveComponent getMove() { return mMoveComp; }
	public void setMove(MoveComponent move) { mMoveComp = move; }
	public CollisionComponent getCollision() { return mCollComp; }
	public State getState() { return mState; }
	public void setState(State state) { mState = state; }
	public Game getGame() { return mGame; }
	public final float getScale() { return mScale; }
	public void setScale(float scale) { mScale = scale; }
	private Vector2 mStartPos;
	public void setStartPos(Vector2 start) { mStartPos = start; }
	public Vector2 getStartPos() { return mStartPos; }
}