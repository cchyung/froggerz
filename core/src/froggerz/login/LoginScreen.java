package froggerz.login;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import froggerz.game.Froggerz;
import froggerz.game.FroggerzGame;

public class LoginScreen implements Screen {
	final Froggerz game;
	TextField username;
	TextField password;
	Stage stage;
	SpriteBatch batch;

	OrthographicCamera camera;

	public LoginScreen(final Froggerz game) {
		this.game = game;
		this.stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		batch = new SpriteBatch();
		
		username = new TextField("", new Skin(Gdx.files.internal("uiskin.json")));
		username.setPosition(200, 300);
		username.setSize(300, 40);
		
		password = new TextField("", new Skin(Gdx.files.internal("uiskin.json")));
		password.setPosition(200, 200);
		password.setSize(300, 40);

	    
	    stage.addActor(username);    
	    stage.addActor(password);    
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
	    // do other rendering ...
	    batch.end();

	    stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
	    
	    if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
			game.setScreen(new FroggerzGame(game));
			dispose();
		}
	}

	@Override
	public void dispose () {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void hide () {

	}

	@Override 
	public void show () {	
	}
}
