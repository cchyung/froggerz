package froggerz.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import froggerz.game.Froggerz;
import froggerz.game.FroggerzGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = FroggerzGame.WIDTH;
		config.height = FroggerzGame.HEIGHT;
		config.resizable = false;
		new LwjglApplication(new Froggerz(), config);
	}
}
