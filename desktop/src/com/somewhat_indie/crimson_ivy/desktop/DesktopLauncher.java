package com.somewhat_indie.crimson_ivy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.somewhat_indie.crimson_ivy.GdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width 		= 1280;
		config.height 		=  720;
		config.fullscreen 	= false;
		config.resizable 	= false;
		config.vSyncEnabled	= true;

		new LwjglApplication(new GdxGame(), config);
	}
}
