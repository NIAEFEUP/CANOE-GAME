package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.TestApplication;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "hello-world";

		// Full screen window ///////////////////////////////////////
		DisplayMode screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		cfg.width = screen.getWidth();
		cfg.height = screen.getHeight();
		cfg.fullscreen = true;
		/////////////////////////////////////////////////////////////


		new LwjglApplication(new TestApplication(), cfg);
	}
}
