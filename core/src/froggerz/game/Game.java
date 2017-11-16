/**
 * Should use these data structures instead of java ones:
 * http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/utils/package-summary.html
 */

package froggerz.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	private AssetManager manager;  // Keeps track of assets
	public TextureAtlas atlas = new TextureAtlas();
	public Animation<TextureRegion> gatorAnimation;
	
	public enum TileType 
	{
		GRASS, WATER, ROAD, FINISH, ROADTOP
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
		manager.load(Gdx.files.internal("gator_0.png").path(), Texture.class);
		manager.load(Gdx.files.internal("gator_1.png").path(), Texture.class);
		manager.load(Gdx.files.internal("gator_2.png").path(), Texture.class);
		manager.load(Gdx.files.internal("gator_3.png").path(), Texture.class);
		manager.load(Gdx.files.internal("gator_4.png").path(), Texture.class);
		manager.load(Gdx.files.internal("grass.png").path(), Texture.class);
		manager.load(Gdx.files.internal("log.png").path(), Texture.class);
		manager.load(Gdx.files.internal("police car.png").path(), Texture.class);
		manager.load(Gdx.files.internal("red car.png").path(), Texture.class);
		manager.load(Gdx.files.internal("road.png").path(), Texture.class);
		manager.load(Gdx.files.internal("water.png").path(), Texture.class);
		manager.load(Gdx.files.internal("roadtop.png").path(), Texture.class);
		// TODO Get the players skins from server
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
			
			//Load in the types of tiles
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
			else if (text.charAt(i) == 'T')
			{
				tileType = TileType.ROADTOP;
			}
			
			//Load in the actors (vehicles, logs, alligators, frogs)
			else if (text.charAt(i) == '1')
			{
				Texture texture = manager.get("frog classic.png", Texture.class);
				Actor frog = new Frog(this);
				
				SpriteComponent sc = new SpriteComponent(frog, 150);
				sc.setTexture(texture);
				sc.setSize(30, 23);
				frog.setSprite(sc);
				
				FrogMove move = new FrogMove(frog);
				frog.setMove(move);
				
				frog.setPosition(new Vector2(k * 32, j * 32 + 5));
				tileType = TileType.GRASS;
			}
			else if (text.charAt(i) == '2')
			{
				Texture texture = manager.get("frog orange.png", Texture.class);
				Actor frog = new Frog(this);
				
				SpriteComponent sc = new SpriteComponent(frog, 150);
				sc.setTexture(texture);
				sc.setSize(30, 23);
				frog.setSprite(sc);
				
				frog.setPosition(new Vector2(k * 32, j * 32 + 5));
				tileType = TileType.GRASS;
			}
			else if (text.charAt(i) == '3')
			{
				Texture texture = manager.get("frog black.png", Texture.class);
				Actor frog = new Frog(this);
				
				SpriteComponent sc = new SpriteComponent(frog, 150);
				sc.setTexture(texture);
				sc.setSize(30, 23);
				frog.setSprite(sc);
				
				frog.setPosition(new Vector2(k * 32, j * 32 + 5));
				tileType = TileType.GRASS;
			}
			else if (text.charAt(i) == '4')
			{
				Texture texture = manager.get("frog red.png", Texture.class);
				Actor frog = new Frog(this);

				SpriteComponent sc = new SpriteComponent(frog, 150);
				sc.setTexture(texture);
				sc.setSize(30, 23);
				frog.setSprite(sc);
				
				frog.setPosition(new Vector2(k * 32, j * 32 + 5));
				tileType = TileType.GRASS;
			}
			else if (text.charAt(i) == 'V')
			{
				Texture texture = manager.get("blue car.png", Texture.class);
				Actor car = new Vehicle(this);
				
				SpriteComponent sc = new SpriteComponent(car, 100);
				sc.setTexture(texture);
				sc.setSize(43, 28);
				car.setSprite(sc);
				
				car.setPosition(new Vector2(k * 32, j * 32 + 5));
				tileType = TileType.ROAD;
			}
			else if (text.charAt(i) == 'C')
			{
				Texture texture = manager.get("red car.png", Texture.class);
				Actor car = new Vehicle(this);

				SpriteComponent sc = new SpriteComponent(car, 100);
				sc.setTexture(texture);
				sc.setSize(43, 28);
				car.setSprite(sc);
				
				car.setPosition(new Vector2(k * 32, j * 32 + 5));
				tileType = TileType.ROAD;
			}
			else if (text.charAt(i) == 'P')
			{
				Texture texture = manager.get("police car.png", Texture.class);
				Actor car = new PoliceCar(this);
				
				SpriteComponent sc = new SpriteComponent(car, 100);
				sc.setTexture(texture);
				sc.setSize(65, 28);
				car.setSprite(sc);
				
				car.setPosition(new Vector2(k * 32, j * 32 + 5));
				tileType = TileType.ROADTOP;
			}
			else if (text.charAt(i) == 'L')
			{
				Texture texture = manager.get("log.png", Texture.class);
				Actor log = new Log(this);
				
				SpriteComponent sc = new SpriteComponent(log, 100);
				sc.setTexture(texture);
				sc.setSize(60, 19);
				log.setSprite(sc);
				
				log.setPosition(new Vector2(k * 32, j * 32 + 5));
				tileType = TileType.WATER;
			}
			else if (text.charAt(i) == 'A')
			{
				Texture texture = manager.get("gator_0.png", Texture.class);
				Texture texture1 = manager.get("gator_1.png", Texture.class);
				Texture texture2 = manager.get("gator_2.png", Texture.class);
				Texture texture3 = manager.get("gator_3.png", Texture.class);
				Texture texture4 = manager.get("gator_4.png", Texture.class);
				Actor gator = new Alligator(this);

				AnimatedSprite sc = new AnimatedSprite(gator, 100);
				sc.setTexture(texture); //just need to initialize the texture to something
				
				//Add images to the animation...need a lot of copies of the mouth in the closed state
				sc.addImage(texture);
				sc.addImage(texture);
				sc.addImage(texture);
				sc.addImage(texture);
				sc.addImage(texture);
				sc.addImage(texture);
				sc.addImage(texture);
				sc.addImage(texture);
				sc.addImage(texture);
				sc.addImage(texture);
				sc.addImage(texture);
				sc.addImage(texture1);
				sc.addImage(texture2);
				sc.addImage(texture3);
				sc.addImage(texture4);
				//Mouth is open all the way, now it needs to gradually close
				sc.addImage(texture4);
				sc.addImage(texture3);
				sc.addImage(texture2);
				sc.addImage(texture1);
				sc.addImage(texture);
				
				sc.setSize(100, 19);
				sc.setSpeed(10.0f);
				gator.setSprite(sc);
				
				gator.setPosition(new Vector2(k * 32, j * 32 + 5));
				tileType = TileType.WATER;
			}
			else if (text.charAt(i) == '\n')
			{
				j++;	
				
				//k should always have the column's index (0 - 19)
				//each row has 20 chars plus the endl char
				k = k - 21;
			}

			switch(tileType) 
			{
			case GRASS:
				Texture texture1 = manager.get("grass.png", Texture.class);
				Actor grass = new Actor(this);
				
				SpriteComponent sc1 = new SpriteComponent(grass, 50);
				sc1.setTexture(texture1);
				sc1.setSize(32, 32);
				grass.setSprite(sc1);
				
				grass.setPosition(new Vector2(k * 32, j * 32));
				break;
				
			case WATER:
				Texture texture2 = manager.get("water.png", Texture.class);
				Actor water = new Actor(this);

				SpriteComponent sc2 = new SpriteComponent(water, 50);
				sc2.setTexture(texture2);
				sc2.setSize(32, 32);
				water.setSprite(sc2);
				
				water.setPosition(new Vector2(k * 32, j * 32));
				break;
			case ROAD:
				Texture texture3 = manager.get("road.png", Texture.class);
				Actor road = new Actor(this);
				SpriteComponent sc3 = new SpriteComponent(road, 50);
				sc3.setTexture(texture3);
				sc3.setSize(32, 32);
				road.setSprite(sc3);
				
				road.setPosition(new Vector2(k * 32, j * 32));
				break;
			case FINISH:
				// TODO if player collides with this, end the game
				Texture texture4 = manager.get("finish.png", Texture.class);
				Actor finish = new Actor(this);
				
				SpriteComponent sc4 = new SpriteComponent(finish, 50);
				sc4.setTexture(texture4);
				sc4.setSize(32, 32);
				finish.setSprite(sc4);
				
				finish.setPosition(new Vector2(k * 32, j * 32));
				break;
			case ROADTOP:
				Texture texture5 = manager.get("roadtop.png", Texture.class);
				Actor top = new Actor(this);
				
				SpriteComponent sc5 = new SpriteComponent(top, 50);
				sc5.setTexture(texture5);
				sc5.setSize(32, 32);
				top.setSprite(sc5);
				
				top.setPosition(new Vector2(k * 32, j * 32));
				break;
			}	
		}
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
