package com.Redream.LD23;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;



public class GameDesktop{
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
		config.title = "LD23";
		config.useGL20 = false;
		config.height = 480;
		config.width = 800;
		new LwjglApplication(new Game(), config);
	}
} 