/**
 * Should use these data structures instead of java ones:
 * http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/utils/package-summary.html
 */

package froggerz.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import froggerz.game.Actor.State;

public class Game extends ApplicationAdapter 
{
	// Constants
	public static final int WIDTH = 640;  //512?
	public static final int HEIGHT = 480;   //384?
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
	
	public enum TileType 
	{
		GRASS, WATER, ROAD, FINISH
	}
	
	
	/**
	 * Like a constructor, ran when an instance of Game is created
	 */
	@Override
	public void create () 
	{
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
	public void render () 
	{
		
		// Process input and update the gameworld before rendering
		processInput();
		updateGame();
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		// Draw all the sprites
		for(SpriteComponent spriteComp: mSprites) 
		{
			spriteComp.getSprite().draw(batch);
		}	
		
		batch.end();
	}
	
	/**
	 * Like destructor, ran when game cleanup happens
	 */
	@Override
	public void dispose () 
	{
		// Unload data before disposing of libGDX objects
		unloadData();
		
		batch.dispose();
		manager.dispose();
	}
	
	/**
	 * Processes input from the player
	 */
	public void processInput() 
	{
		
		// Process input for all actors
		for(Actor actor : mActors) 
		{
			actor.processInput();
		}
	}
	
	/**
	 * Update game variables and states
	 */
	public void updateGame() 
	{
		
		// Update deltaTime and continue if it meets TARGETFPS
		deltaTime += Gdx.graphics.getDeltaTime();
		if(deltaTime < TARGETFPS) 
		{
			return;
		}
		else if(deltaTime > .05f) 
		{  // Limit deltaTime
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
	private void loadData() 
	{
		//Load all of the assets
		manager.load(Gdx.files.internal("blue car.png").path(), Texture.class);
		manager.load(Gdx.files.internal("finish.png").path(), Texture.class);
		manager.load(Gdx.files.internal("frog black.png").path(), Texture.class);
		manager.load(Gdx.files.internal("frog blue.png").path(), Texture.class);
		manager.load(Gdx.files.internal("frog classic.png").path(), Texture.class);
		manager.load(Gdx.files.internal("frog orange.png").path(), Texture.class);
		manager.load(Gdx.files.internal("frog purple.png").path(), Texture.class);
		manager.load(Gdx.files.internal("frog red.png").path(), Texture.class);
		manager.load(Gdx.files.internal("gator0.png").path(), Texture.class);
		manager.load(Gdx.files.internal("gator1.png").path(), Texture.class);
		manager.load(Gdx.files.internal("gator2.png").path(), Texture.class);
		manager.load(Gdx.files.internal("gator3.png").path(), Texture.class);
		manager.load(Gdx.files.internal("gator4.png").path(), Texture.class);
		manager.load(Gdx.files.internal("grass.png").path(), Texture.class);
		manager.load(Gdx.files.internal("log.png").path(), Texture.class);
		manager.load(Gdx.files.internal("police car.png").path(), Texture.class);
		manager.load(Gdx.files.internal("red car.png").path(), Texture.class);
		manager.load(Gdx.files.internal("road.png").path(), Texture.class);
		manager.load(Gdx.files.internal("water.png").path(), Texture.class);
		manager.load(Gdx.files.internal("road top.png").path(), Texture.class);
		// TODO Get the players skins form server
		// Texture playerSkin = manager.load(Gdx.files.internal("playerskin.png").path(), Texture.class);
		
		manager.finishLoading();  // Block until all assets are loaded
		
		
		
		// Load level from file
		FileHandle file = Gdx.files.internal("Level.txt");
		
		String text = file.readString();
		
		//Parse the file into individual chars
		TileType tileType = null;
		int j = 0;
		int k = -1;
		for (int i = 0; i < text.length(); i++)
		{
			k++;
			if (text.charAt(i) == '.') 
			{
				tileType = TileType.GRASS;
			}
			else if (text.charAt(i) == 'W') 
			{
				tileType = TileType.WATER;
			}
			else if (text.charAt(i) == 'R') 
			{
				tileType = TileType.ROAD;
			}
			else if (text.charAt(i) == 'F') 
			{
				tileType = TileType.FINISH;
			}
			else if (text.charAt(i) == 'V')
			{
				Texture texture = manager.get("blue car.png", Texture.class);
				Actor car = new Actor(this);
				car.setPosition(new Vector2(k * 32, j * 32 + 5));
				SpriteComponent sc1 = new SpriteComponent(car, 100, texture);
				sc1.setTexture(texture);
				car.setSprite(sc1);
			}
			else if (text.charAt(i) == 'T')
			{
				Texture texture = manager.get("road top.png", Texture.class);
				Actor top = new Actor(this);
				top.setPosition(new Vector2(k * 32, j * 32 + 5));
				SpriteComponent sc1 = new SpriteComponent(top, 55, texture);
				sc1.setTexture(texture);
				top.setSprite(sc1);
			}
			else if (text.charAt(i) == '\n')
			{
				j++;	
				
				//k should always have the column's index (0 - 7)
				//each row has 8 chars plus the endl char
				k = k - 9;
			}

			
			switch(tileType) 
			{
			case GRASS:
				Texture texture1 = manager.get("grass.png", Texture.class);
				Actor grass = new Actor(this);
				grass.setPosition(new Vector2(k * 32, j * 32));
				SpriteComponent sc1 = new SpriteComponent(grass, 50, texture1);
				sc1.setTexture(texture1);
				grass.setSprite(sc1);
				break;
				
			case WATER:
				Texture texture2 = manager.get("water.png", Texture.class);
				Actor water = new Actor(this);
				water.setPosition(new Vector2(k * 32, j * 32));
				SpriteComponent sc2 = new SpriteComponent(water, 50, texture2);
				water.setSprite(sc2);
				break;
				
			case ROAD:
				Texture texture3 = manager.get("road.png", Texture.class);
				Actor road = new Actor(this);
				road.setPosition(new Vector2(k * 32, j * 32));
				SpriteComponent sc3 = new SpriteComponent(road, 50, texture3);
				road.setSprite(sc3);
				break;
				
			case FINISH:
				// TODO if player collides with this, end the game
				Texture texture4 = manager.get("finish.png", Texture.class);
				Actor finish = new Actor(this);
				finish.setPosition(new Vector2(k * 32, j * 32));
				SpriteComponent sc4 = new SpriteComponent(finish, 50, texture4);
				finish.setSprite(sc4);
				break;
			}
			
			
		}


		/* 
		//The file wasn't being found for me with this approach

		BufferedReader br = null;
		
		try 
		{
			br = new BufferedReader(new FileReader(Gdx.files.internal("Level.txt").path()));
		} catch (FileNotFoundException fnfe) 
		{
			System.out.println("Cannot load level file. Quiting game: " + fnfe.getMessage());
			exitGame();
		}
		
		
		if(br == null) 
		{
			exitGame();
		}
		*/
		
		/*
		
		// Loop through the Level.txt file
		String currLine = null;
		float xPos = 32.0f;
		float yPos = 32.0f;
		for (int j = 0; j < mLevelHeight; ++j, yPos+=32.0f)  // Vertical
		{
			xPos = 32.0f;
			try 
			{
				currLine = br.readLine();
			} catch(IOException ioe) 
			{
				System.out.println("Could not read from level file: " + ioe.getMessage());
				exitGame();
			}
			
			for (int i=0; i < currLine.length(); ++i, xPos+=32.0f)  // Horizontal
			{
				TileType tileType = null;
				Actor actor = null;
				SpriteComponent sc = null;
				Texture texture = null;
				boolean notTile = true;
				
				// TODO replace Actor with the appropriate child class when they are created
				// Player 1
				if (currLine.charAt(i) == '1') 
				{
					// TODO replace which texture is got based on skins gotten from server
					texture = manager.get("player1.png", Texture.class);
					actor = new Actor(this);	
					sc = new SpriteComponent(actor, 100);
					
					tileType = TileType.GRASS;
				}
				// Player 2
				else if (currLine.charAt(i) == '2') 
				{
					texture = manager.get("player2.png", Texture.class);
					actor = new Actor(this);
					sc = new SpriteComponent(actor, 100);

					tileType = TileType.GRASS;
				}
				// Player 3
				else if (currLine.charAt(i) == '3') 
				{
					texture = manager.get("player3.png", Texture.class);
					actor = new Actor(this);
					sc = new SpriteComponent(actor, 100);

					tileType = TileType.GRASS;
				}
				// Player 4
				else if (currLine.charAt(i) == '4') 
				{
					texture = manager.get("player4.png", Texture.class);
					actor = new Actor(this);
					sc = new SpriteComponent(actor, 100);

					tileType = TileType.GRASS;
				}
				// Vehicle
				else if(currLine.charAt(i) == 'V') 
				{
					//TODO Set direction the vehicle is going in based on x value (32 or mLevelWidth-32)
					texture = manager.get("vehicle.png", Texture.class);
					actor = new Actor(this);
					sc = new SpriteComponent(actor, 100);

					tileType = TileType.ROAD;
				}
				// Log
				else if(currLine.charAt(i) == 'L') 
				{
					//TODO Set direction the log is going in based on x value (32 or mLevelWidth-32)
					texture = manager.get("log.png", Texture.class);
					actor = new Actor(this);
					sc = new SpriteComponent(actor, 100);
					
					tileType = TileType.WATER;
				}
				else 
				{
					notTile = false;
				}
				
				if(notTile) 
				{
					actor.setPosition(new Vector2(xPos, yPos));
					sc.setTexture(texture);
					actor.setSprite(sc);
				}
				
				// Background tile type
				if(currLine.charAt(i) == '.') 
				{
					tileType = TileType.GRASS;
				}
				else if(currLine.charAt(i) == 'W') 
				{
					tileType = TileType.WATER;
				}
				else if(currLine.charAt(i) == 'R') 
				{
					tileType = TileType.ROAD;
				}
				else if(currLine.charAt(i) == 'F') 
				{
					tileType = TileType.FINISH;
				}
				
				// Select the bass tile
				switch(tileType) 
				{
					case GRASS:
						Texture texture1 = manager.get("grass.png", Texture.class);
						Actor grass = new Actor(this);
						grass.setPosition(new Vector2(xPos, yPos));
						SpriteComponent sc1 = new SpriteComponent(grass, 50);
						sc1.setTexture(texture1);
						grass.setSprite(sc1);
						break;
					case WATER:
						Texture texture2 = manager.get("water.png", Texture.class);
						Actor water = new Actor(this);
						water.setPosition(new Vector2(xPos, yPos));
						SpriteComponent sc2 = new SpriteComponent(water, 50);
						sc2.setTexture(texture2);
						water.setSprite(sc2);
						break;
					case ROAD:
						Texture texture3 = manager.get("road.png", Texture.class);
						Actor road = new Actor(this);
						road.setPosition(new Vector2(xPos, yPos));
						SpriteComponent sc3 = new SpriteComponent(road, 50);
						sc3.setTexture(texture3);
						road.setSprite(sc3);
						break;
					case FINISH:
						// TODO if player collides with this, end the game
						Texture texture4 = manager.get("finish.png", Texture.class);
						Actor finish = new Actor(this);
						finish.setPosition(new Vector2(xPos, yPos));
						SpriteComponent sc4 = new SpriteComponent(finish, 50);
						sc4.setTexture(texture4);
						finish.setSprite(sc4);
						break;
				} // end switch
			}
		}
		
		// Close input streams
		try 
		{
			if(br != null) 
			{
				br.close();
			}
		} catch (IOException ioe) 
		{
			System.out.println("Problem closing files in loadData(): " + ioe.getMessage());
		}
		*/
	}
	
	
	/**
	 * Unloads data relevant to the game
	 */
	private void unloadData() 
	{
		manager.clear();
	}
	
	/**
	 * Run tasks to kill this game
	 *
	 */
	private void exitGame() 
	{
		// TODO
		dispose();
		Gdx.app.exit();
	}
	
	//////////////////////////////// SETTERS/GETTERS ////////////////////////////////
	
	/**
	 * Adds an Actor to mActors
	 * @param actor Actor to add
	 */
	public void addActor(Actor actor) 
	{
		mActors.add(actor);
	}
	
	/**
	 * Removes an Actor from mActors
	 * @param actor Actor to remove
	 */
	public void removeActor(Actor actor) 
	{
		mActors.removeValue(actor, true);
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
	
	/**
	 * Removes an Sprite from mSprites
	 * @param sprite Sprite to remove
	 */
	public void removeSprite(SpriteComponent sprite) 
	{
		mSprites.removeValue(sprite, true);
	}
	
}
