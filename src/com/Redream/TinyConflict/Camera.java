package com.Redream.TinyConflict;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;


public class Camera {
	public static OrthographicCamera cam;
	public static OrthographicCamera HUDcam;
	
	public static Renderable target;
	
	public static float targetzoom = 1f;
	
	
	public static void tick(){
		if(target != null){
			final float moveX = (float) (((target.x + target.origX - Game.WIDTH / 2) - cam.position.x) * 0.05);
			final float moveY = (float) (((target.y + target.origY - Game.HEIGHT / 2) - cam.position.y) * 0.05);
			cam.translate(moveX, moveY, 0);
		}
		
		Camera.cam.zoom += (targetzoom - Camera.cam.zoom) * 0.01f;
		
		cam.update();
	}


	public static Rectangle getBounds() {
		return new Rectangle(Camera.cam.position.x,Camera.cam.position.y,Game.WIDTH,Game.HEIGHT);
	}
}

