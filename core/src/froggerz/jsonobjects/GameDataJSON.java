package froggerz.jsonobjects;

import com.badlogic.gdx.utils.Array;

import froggerz.game.Actor;
import froggerz.game.SortSprite;
import froggerz.game.SpriteComponent;

public class GameDataJSON {
	private Array<Actor> mActors;
	private Array<Actor> mDeadActors; 
	private Array<SpriteComponent> mSprites;
	private float deltaTime;
	
	/**
	 * Adds an Actor to mActors
	 * @param actor Actor to add
	 */
	public void addActor(Actor actor) 
	{
		mActors.add(actor);
	}
	
	/**
	 * Adds an Actor to mDeadActors
	 * @param actor Actor to add
	 */
	public void addDeadActor(Actor actor) 
	{
		mDeadActors.add(actor);
	}
	
	/**
	 * Adds an Sprite to mSprites
	 * @param sprite Sprite to add
	 */
	public void addSprite(SpriteComponent sprite) 
	{
		mSprites.add(sprite);
		mSprites.sort(new SortSprite());
	}
	
	/////////////////////////////////////// Setters/Getters ///////////////////////////////////////
	
	public void setDeltaTime(float deltaTime) {
		this.deltaTime = deltaTime;
	}
	
	
}
