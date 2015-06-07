package com.mygdx.game.desktop;

import Engine.GameEngine;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
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
		cfg.fullscreen = false;
		/////////////////////////////////////////////////////////////


		new LwjglApplication(new GameEngine(), cfg);
	}
}
