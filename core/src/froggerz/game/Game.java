/**
 * Should use these data structures instead of java ones:
 * http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/utils/package-summary.html
 */

package froggerz.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import froggerz.game.Actor.State;

public class Game extends ApplicationAdapter {
	// Constants
	public static final int WIDTH = 512;
	public static final int HEIGHT = 384;
	public final float TARGETFPS = 1.0f/60.0f;
	
	
	private final int mLevelWidth = 512;
	private final int mLevelHeight = 1920;
	
	// Delta Time related
	private float deltaTime = 0.0f;
	
	// Holds all actors in the game
	private Array<Actor> mActors;
	private Array<SpriteComponent> mSprites;
	
	private SpriteBatch batch;
	private AssetManager manager;  // Keeps track of assests
	
	/**
	 * Like a constructor, ran when an instance of Game is created
	 */
	@Override
	public void create () {
		mActors = new Array<Actor>();
		mSprites = new Array<SpriteComponent>();
		
		batch = new SpriteBatch();
		manager = new AssetManager();
		
		loadData();
	}

	/**
	 * Ran when rendering needs to be done
	 */
	@Override
	public void render () {
		
		// Process input and update the gameworld before rendering
		processInput();
		updateGame();
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		// Draw all the sprites
		for(SpriteComponent spriteComp: mSprites) {
			spriteComp.getSprite().draw(batch);
		}
		
		batch.end();
	}
	
	/**
	 * Like destructor, ran when game cleanup happens
	 */
	@Override
	public void dispose () {
		// Unload data before disposing of libGDX objects
		unloadData();
		
		batch.dispose();
		manager.dispose();
	}
	
	/**
	 * Processes input from the player
	 */
	public void processInput() {
		
		// Process input for all actors
		for(Actor actor : mActors) {
			actor.processInput();
		}
	}
	
	/**
	 * Update game variables and states
	 */
	public void updateGame() {
		
		// Update deltaTime and continue if it meets TARGETFPS
		deltaTime += Gdx.graphics.getDeltaTime();
		if(deltaTime < TARGETFPS) {
			return;
		}
		else if(deltaTime > .05f) {  // Limit deltaTime
			deltaTime = 0.05f;
		}

		// Update actors
		Array<Actor> copyActors = mActors;
		for (Actor actor : copyActors)
		{
			actor.update(deltaTime);
		}

		// Get dead actors
		Array<Actor> deadActors = new Array<Actor>();
		for (Actor actor : mActors)
		{
			if (actor.getState() == State.EDead)
			{
				deadActors.add(actor);
			}
		}

		// Make sure the dead don't get up
		for (Actor actor : deadActors)
		{
			actor.destroy();
		}
		
		deltaTime = 0.0f;
	}
	
	
	/**
	 * Loads data relevant to the game
	 */
	private void loadData() {
		// manager.load("path/texture.png", Texture.class);
		// manager.finishLoading()  // Make sure all assets are loaded
	}
	
	/**
	 * Unloads data relevant to the game
	 */
	private void unloadData() {
		manager.clear();
	}
	
	//////////////////////////////// SETTERS/GETTERS ////////////////////////////////
	
	/**
	 * Adds an Actor to mActors
	 * @param actor Actor to add
	 */
	public void addActor(Actor actor) {
		mActors.add(actor);
	}
	
	/**
	 * Removes an Actor from mActors
	 * @param actor Actor to remove
	 */
	public void removeActor(Actor actor) {
		mActors.removeValue(actor, true);
	}
	
	/**
	 * Adds an Sprite to mSprites
	 * @param sprite Sprite to add
	 */
	public void addSprite(SpriteComponent sprite) {
		mSprites.add(sprite);
		mSprites.sort(new SortSprite());
	}
	
	/**
	 * Removes an Sprite from mSprites
	 * @param sprite Sprite to remove
	 */
	public void removeSprite(SpriteComponent sprite) {
		mSprites.removeValue(sprite, true);
	}
	
}
