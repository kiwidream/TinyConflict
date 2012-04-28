package com.Redream.TinyConflict;

import com.badlogic.gdx.backends.lwjgl.LwjglApplet;


public class GameApplet extends LwjglApplet {

	private static final long serialVersionUID = 1L;

	public GameApplet() {
		super(new Game(false), false);
	}

}
