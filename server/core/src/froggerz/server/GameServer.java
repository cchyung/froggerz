package froggerz.server;

import java.io.IOException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.Server;

import froggerz.jsonobjects.ButtonsJSON;
import froggerz.jsonobjects.GameDataJSON;

public class GameServer extends ApplicationAdapter {
	private SpriteBatch batch;
	private BitmapFont font;
	private Server server;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		server = new Server(262144,65536);
		server.start();
		try {
			server.bind(25565,25565);
		} catch (IOException e) {
			System.out.println("Issue binding server to port");
		}
		
		Kryo kryo = server.getKryo();
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
		kryo.register(PositionPacket.class, 1);
	    kryo.register(ButtonsJSON.class, 2);
	    kryo.register(GameDataJSON.class, 3);
	    /*kryo.register(GameDataJSON.class, new Serializer<GameDataJSON>() {
	    	{
	    		setAcceptsNull(true);
	    	}

	    	public void write (Kryo kryo, Output output, GameDataJSON data) {
	    		output.writeInt(data.getCommand());
	    		
	    		// Write vectors
	    		int length = data.getPositions().size;
	    		output.writeInt(length);
	    		//if (length == 0) return;
	    		for (int i = 0, n = data.getPositions().size; i < n; i++) {
	    			output.writeFloat(data.getPositions().get(i).x);
					output.writeFloat(data.getPositions().get(i).y);
	    		}
	    	}

	    	public GameDataJSON read (Kryo kryo, Input input, Class<GameDataJSON> type) {
	    		System.out.println("Starting to deserialize");
	    		GameDataJSON data = new GameDataJSON();
	    		int command = input.readInt();
	    		data.setCommand(command);
	    		
	    		Array<Vector2> array = new Array<Vector2>();
	    		kryo.reference(array);
	    		int length = input.readInt();
	    		array.ensureCapacity(length);
	    		for (int i = 0; i < length; i++) {
	    			Vector2 vector = new Vector2();
	    			vector.x = input.readFloat();
					vector.y = input.readFloat();
	    			array.add(vector);
	    		}
	    		
	    		data.setPositions(array);
	    		return data;
	    	}
	    }, 3);*/
		
		server.addListener(new GameListener(server));
		
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.draw(batch, "Froggerz Server is running ", 640/2, 480/2);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
