package com.bot0ff.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bot0ff.game.FlaneGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = FlaneGame.WIDTH;
		config.height = FlaneGame.HEIGHT;
		new LwjglApplication(new FlaneGame(), config);
	}
}
