/**
 * Should use these data structures instead of java ones:
 * http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/utils/package-summary.html
 */

package froggerz.game;

import java.io.IOException;

// TODO Test if the WebSocket wrapper works
// TODO Fix the positioning of the camera and implement scrolling
// TODO Add Collision detection
// TODO Delete actors when they go off screen
// TODO Add bounds for the frogs

/**
 * IDEA
 * For the web side, have the user choose skins and sign up. When they want to join a game
 * give them a unique code that connects them to a game. Create 4 unique codes per game.
 * The code marks the game and also marks which player so you can give them the skin
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import froggerz.game.Actor.State;
import froggerz.jsonobjects.ButtonsJSON;
import froggerz.jsonobjects.GameDataJSON;

public class Game extends ApplicationAdapter 
{
	// Constants
	public static final int WIDTH = 640;  //512?
	public static final int HEIGHT = 960;   //384?
	public final float TARGETFPS = 1.0f/60.0f;
	
	private final int mLevelWidth = 640;
	private final int mLevelHeight = 960;
	
	// Delta Time related
	private float deltaTime = 0.0f;
	
	// Holds all actors in the game
	private Array<Actor> mActors;
	private Array<Actor> mPlayers;  // Players in the game exluding this player
	private Array<SpriteComponent> mSprites;
	
	private SpriteBatch batch;
	private AssetManager manager;  // Keeps track of assets
	public TextureAtlas atlas = new TextureAtlas();  // Why public?
	public Animation<TextureRegion> gatorAnimation;  // Why public?
	
	//private Viewport viewport;
	private OrthographicCamera camera;
	
	// Server related variables
	private Client client;
	private static Queue<GameDataJSON> messageFromServer;
	
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
		mPlayers = new Array<Actor>();
		mSprites = new Array<SpriteComponent>();
		
		batch = new SpriteBatch();
		manager = new AssetManager();
		
		client = new Client(262144,65536);
		client.start();
		try {
			client.connect(5000, "localhost", 25565, 25565);
		} catch (IOException e) {
			System.out.println("Problem with connecting client");
		}
		
		Kryo kryo = client.getKryo();
//		kryo.register(Vector2.class, new Serializer<Vector2>() {
//			{
//				setAcceptsNull(true);
//			}
//
//			public void write (Kryo kryo, Output output, Vector2 vector) {
//				output.writeFloat(vector.x);
//				output.writeFloat(vector.y);
//			}
//
//			public Vector2 read (Kryo kryo, Input input, Class<Vector2> type) {
//				Vector2 vector = new Vector2();
//				kryo.reference(vector);
//				vector.x = input.readFloat();
//				vector.y = input.readFloat();
//				return vector;
//			}
//		});
//		
	    kryo.register(String.class);
//	    kryo.register(com.badlogic.gdx.utils.Array.class);
//	    kryo.register(Object[].class);
//	    kryo.register(Array[].class);
	    kryo.register(ButtonsJSON.class);
	    kryo.register(GameDataJSON.class, new Serializer<GameDataJSON>() {
	    	{
	    		setAcceptsNull(true);
	    	}

	    	public void write (Kryo kryo, Output output, GameDataJSON data) {
	    		output.writeInt(data.getCommand(), true);
	    		
	    		// Write vectors
	    		int length = data.getPositions().size;
	    		output.writeInt(length, true);
	    		//if (length == 0) return;
	    		for (int i = 0, n = data.getPositions().size; i < n; i++) {
	    			output.writeFloat(data.getPositions().get(i).x);
					output.writeFloat(data.getPositions().get(i).y);
	    		}
	    		
	    		output.writeInt(data.getData(), true);
	    	}

	    	public GameDataJSON read (Kryo kryo, Input input, Class<GameDataJSON> type) {
	    		GameDataJSON data = new GameDataJSON();
	    		int command = input.readInt(true);
	    		data.setCommand(command);
	    		
	    		Array<Vector2> array = new Array<Vector2>();
	    		kryo.reference(array);
	    		int length = input.readInt(true);
	    		array.ensureCapacity(length);
	    		for (int i = 0; i < length; i++) {
	    			Vector2 vector = new Vector2();
	    			vector.x = input.readFloat();
					vector.y = input.readFloat();
	    			array.add(vector);
	    		}
	    		
	    		int dataCommand = input.readInt(true);
	    		data.setData(dataCommand);
	    		
	    		data.setPositions(array);
	    		return data;
	    	}
	    });
		
	    // Listener for the server
		client.addListener(new Listener() {
		       public void received (Connection connection, Object object) {
		          if (object instanceof GameDataJSON) {
		        	  GameDataJSON response = (GameDataJSON)object;
		        	  addQueue(response);
		             System.out.println("Received: " + response.getCommand());
		          }
		       }
		    });
		
		messageFromServer = new Queue<GameDataJSON>();
		
		loadData();
		
		// Game setup is over, process frogPositions
		createServerProcesser();
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
		
		//camera.update();
		
		batch.begin();
		//batch.setProjectionMatrix(camera.combined);
		
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
	private void processInput() 
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
	private void updateGame() 
	{

		// Update deltaTime and continue if it meets TARGETFPS
		deltaTime += Gdx.graphics.getDeltaTime();
		if(deltaTime < TARGETFPS) 
		{
			return;
		}
		else if (deltaTime > TARGETFPS) 
		{  // Limit deltaTime
			deltaTime = TARGETFPS;
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
		//Declare lanes and their speeds/directions
		Lane lane1 = new Lane();
		lane1.setDirection(-1.0f);
		lane1.setSpeed(40.0f);

		Lane lane2 = new Lane();
		lane2.setDirection(1.0f);
		lane2.setSpeed(70.0f);

		Lane lane3 = new Lane();
		lane3.setDirection(1.0f);
		lane3.setSpeed(55.0f);

		Lane lane4 = new Lane();
		lane4.setDirection(-1.0f);
		lane4.setSpeed(100.0f);

		River river1 = new River();
		river1.setDirection(-1.0f);
		river1.setSpeed(20.0f);

		River river2 = new River();
		river2.setDirection(1.0f);
		river2.setSpeed(25.0f);

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

		String level = file.readString();
		String[] levelRows = level.split("\n");

		//Parse the file into individual chars
		TileType tileType = null;

		//Keep track of how many logs have been made
		int logCount = 0;

		for(int j=0; j<levelRows.length; ++j) {
			String levelRow = levelRows[j];
			for(int k=0; k<levelRow.length(); ++k) {
				char levelChar = levelRow.charAt(k);

				//Load in the types of tiles
				if (levelChar == '.') 
				{
					tileType = TileType.GRASS;
				}
				else if (levelChar == 'W') 
				{
					tileType = TileType.WATER;
				}
				else if (levelChar == 'R') 
				{
					tileType = TileType.ROAD;
				}
				else if (levelChar == 'F') 
				{
					tileType = TileType.FINISH;
				}
				else if (levelChar == 'T')
				{
					tileType = TileType.ROADTOP;
				}

				//Load in the actors (vehicles, logs, alligators, frogs)
				else if (levelChar == '1')
				{
					tileType = TileType.GRASS;
				}
				else if (levelChar == '2')
				{
					tileType = TileType.GRASS;
				}
				else if (levelChar == '3')
				{
					tileType = TileType.GRASS;
				}
				else if (levelChar == '4')
				{
					tileType = TileType.GRASS;
				}
				else if (levelChar == 'V')
				{
					Texture texture = manager.get("blue car.png", Texture.class);
					Actor car = new Vehicle(this);

					SpriteComponent sc = new SpriteComponent(car, 100);
					sc.setTexture(texture);
					sc.setSize(43, 28);
					car.setSprite(sc);

					car.setPosition(new Vector2(k * 32, j * 32 + 5));
					tileType = TileType.ROAD;

					//Assign cars to lanes
					if (car.getPosition().y >= 128 && car.getPosition().y <= 140)
					{
						lane1.addVehicleToLane(car);
					}

					else if (car.getPosition().y >= 160 && car.getPosition().y <= 172)
					{
						lane2.addVehicleToLane(car);
					}

					else if (car.getPosition().y >= 384 && car.getPosition().y <= 396)
					{
						lane3.addVehicleToLane(car);
					}
					else
					{
						lane4.addVehicleToLane(car);
					}
				}
				else if (levelChar == 'C')
				{
					Texture texture = manager.get("red car.png", Texture.class);
					Actor car = new Vehicle(this);

					SpriteComponent sc = new SpriteComponent(car, 100);
					sc.setTexture(texture);
					sc.setSize(43, 28);
					car.setSprite(sc);

					car.setPosition(new Vector2(k * 32, j * 32 + 5));
					tileType = TileType.ROAD;

					//Assign cars to lanes
					if (car.getPosition().y >= 128 && car.getPosition().y <= 140)
					{
						lane1.addVehicleToLane(car);
					}

					else if (car.getPosition().y >= 160 && car.getPosition().y <= 172)
					{
						lane2.addVehicleToLane(car);
					}

					else if (car.getPosition().y >= 384 && car.getPosition().y <= 396)
					{
						lane3.addVehicleToLane(car);
					}
					else
					{
						lane4.addVehicleToLane(car);
					}
				}
				else if (levelChar == 'P')
				{
					Texture texture = manager.get("police car.png", Texture.class);
					Actor car = new PoliceCar(this);

					SpriteComponent sc = new SpriteComponent(car, 100);
					sc.setTexture(texture);
					sc.setSize(65, 28);
					car.setSprite(sc);

					car.setPosition(new Vector2(k * 32, j * 32 + 5));
					tileType = TileType.ROADTOP;

					//Assign cars to lanes
					if (car.getPosition().y >= 128 && car.getPosition().y <= 140)
					{
						lane1.addVehicleToLane(car);
					}

					else if (car.getPosition().y >= 160 && car.getPosition().y <= 172)
					{
						lane2.addVehicleToLane(car);
					}

					else if (car.getPosition().y >= 384 && car.getPosition().y <= 396)
					{
						lane3.addVehicleToLane(car);
					}
					else
					{
						lane4.addVehicleToLane(car);
					}
				}
				else if (levelChar == 'L')
				{
					Texture texture = manager.get("log.png", Texture.class);
					Actor log = new Log(this);

					SpriteComponent sc = new SpriteComponent(log, 100);
					sc.setTexture(texture);

					//Evenly distribute the log sizes
					if (logCount % 3 == 0)
					{
						sc.setSize(60, 19);
					}

					else if (logCount % 3 == 1)
					{
						sc.setSize(90, 19);
					}

					else if (logCount % 3 == 2)
					{
						sc.setSize(120, 19);
					}

					log.setSprite(sc);

					log.setPosition(new Vector2(k * 32, j * 32 + 5));
					tileType = TileType.WATER;
					logCount++;

					//Assign logs to rivers
					if (log.getPosition().y >= 256 && log.getPosition().y <= 268)
					{
						river1.addThingToRiver(log);
					}

					else
					{
						river2.addThingToRiver(log);
					}
				}
				else if (levelChar == 'A')
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

					//Assign alligators to rivers
					if (gator.getPosition().y >= 256 && gator.getPosition().y <= 268)
					{
						river1.addThingToRiver(gator);
					}

					else
					{
						river2.addThingToRiver(gator);
					}
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
		}  // End level loading from file

		System.out.println("Waiting for first postion");
		// Wait to receive this players position from the server
		while(messageFromServer.size == 0){
		}
		System.out.println("Got frist position");
		GameDataJSON dataFromServer = messageFromServer.removeFirst();
		
		// Process what was sent from the server
		Vector2 playerPos = dataFromServer.getPositions().get(0);

		// TODO put the skin of the player here
		Texture texture = manager.get("frog orange.png", Texture.class);
		Actor frog = new Frog(this);

		SpriteComponent sc = new SpriteComponent(frog, 150);
		sc.setTexture(texture);
		sc.setSize(30, 23);
		frog.setSprite(sc);
		frog.setPosition(playerPos);

		System.out.println("Waiting for other frogs positions");
		// Wait to receive the positions of other frogs
		while(messageFromServer.size == 0){
		}

		dataFromServer = messageFromServer.removeFirst();

		// Create a new frog for each Vector2 in the JsonValue
		Array<Vector2> frogPositions = dataFromServer.getPositions();
		for (Vector2 position : frogPositions) {
			texture = manager.get("frog classic.png", Texture.class);
			frog = new Frog(this);

			// Non player frogs should be drawn under the player
			sc = new SpriteComponent(frog, 125);
			sc.setTexture(texture);
			sc.setSize(30, 23);
			frog.setSprite(sc);
			frog.setMove(null);  // Frog does not move based off of this players input
			frog.setPosition(position);

			mPlayers.add(frog);
		}
		
		System.out.println("Waiting for the game to start");
		// Wait for the command to start the game
		while(messageFromServer.size == 0){
			System.out.println(messageFromServer.size);
		}
		messageFromServer.removeFirst();
		
		System.out.println("Reached the end of load data");
	}

	/**
	 * Unloads data relevant to the game
	 */
	private void unloadData() 
	{
		manager.clear();
		
	}
	
	/**
	 * This thread manages updating
	 */
	private void createServerProcesser() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// Always update game variables based on queue of server data
				while(true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					GameDataJSON dataFromServer = null;
					// What kind of data was sent from the server
					synchronized(messageFromServer) {
						if(messageFromServer.size != 0 && messageFromServer.first().getCommand() == 3) {
							dataFromServer = messageFromServer.removeFirst();
						}
						// TODO What to do when the game is over
//						else if(dataFromServer.getCommand().equals("gameOver")) {
//				
//						}
					}
					
					// Positions for frogs
					Array<Vector2> frogPositions;
					if(dataFromServer != null && dataFromServer.getCommand() == 3) {
						frogPositions = dataFromServer.getPositions();
						int currentFrog = 0;
						for (Vector2 position : frogPositions) {
							// Make sure the game isn't trying to draw the this frog
							synchronized(mPlayers.get(currentFrog)) {
								mPlayers.get(currentFrog).setPosition(position);
							}
							++currentFrog;
						}
					}
					
					// this will be run on the application listener thread
					// before the next call to ApplicationListener#render()
//					Gdx.app.postRunnable(new Runnable() {
//						@Override
//						public void run() {
//							
//							Gdx.app.log("ThreadingExample", "i come in peace");
//						}
//					});
				}
			}
		}).start();
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
	
	public Client getClient() {
		return client;
	}
	
	public static void addQueue(GameDataJSON data) {
		//Json json = new Json();
		//GameDataJSON data = json.fromJson(GameDataJSON.class, message);
		synchronized(messageFromServer) {
			messageFromServer.addLast(data);
		}
	}
	
}
