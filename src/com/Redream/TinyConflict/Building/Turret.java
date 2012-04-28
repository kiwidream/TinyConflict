package com.Redream.TinyConflict.Building;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.Redream.TinyConflict.Game;
import com.Redream.TinyConflict.Entity.Bullet;
import com.Redream.TinyConflict.Graphics.Camera;
import com.Redream.TinyConflict.Graphics.Display;
import com.Redream.TinyConflict.Graphics.Renderable;
import com.Redream.TinyConflict.Input.Input;
import com.Redream.TinyConflict.Planet.Planet;
import com.Redream.TinyConflict.Resources.Resources;

public class Turret extends Building {
	private Renderable gun= new Renderable();
	private List<Bullet> bullets = new ArrayList<Bullet>();
	private float gunangle = 0;
	
	private int shootTime = 20;
	
	public Turret() {
		this.tex = 18;
		this.origX = 15;
		this.origY = 5;
		this.xScale = 0.5f;
		this.yScale = 0.5f;
		Input.registerListener(this);
		
		this.name = "Anti-Planet Turret";
		this.desc = "Shoots lasers at foes";
		this.cost = 400;
		gun.tex = 19;
		gun.xScale = 1;
		gun.yScale = 1;
		gun.origX = 3;
		gun.origY = 6;
	}
	
	public void tick(){
		double mindist = 200;
		for(Planet p : Game.planets){
			if(p == home || p.population == 0 || home.population == 0)continue;
			float xd = (p.x+p.origX)-(gun.x);
			float yd = (p.y+p.origY)-(gun.y);
			double dist = Math.abs(Math.sqrt(xd*xd+yd*yd));
			
			if(dist < mindist){
				mindist = dist;
				gunangle = (float) (Math.toDegrees(Math.atan2(p.y+p.origY-y,p.x+p.origX-x))-90-rot);
			}
		}
		
		if(mindist == 200 || home.editing){
			gunangle = 90;
		}
		
		if(shootTime<=0 && mindist != 200 && !home.editing){
			bullets.add(new Bullet(gun.x, gun.y, 3, (float) (gunangle+rot+90+new Random().nextGaussian()*6), 200, home));
			if(new Random().nextInt(4)==0 && Camera.getBounds().contains(gun.x, gun.y))Resources.pew.play();
			shootTime = 20;
		}
		shootTime--;
		for(int i=0;i<bullets.size();i++){
			bullets.get(i).tick();
			if(bullets.get(i).remove)bullets.remove(i--);
		}
	}
	
	public void queueRender(Display display){
		if(home == null)return;
		double pos = Math.toRadians(pX+home.rot);
		this.x = (float) (home.x+home.origX-origX+Math.cos(pos)*home.radius);
		this.y = (float) (home.y+home.origY-origY+Math.sin(pos)*home.radius);
		this.rot = pX - 90 + home.rot;
		gun.x = (float) (home.x+home.origX-gun.origX + Math.cos(pos)*(home.radius*home.xScale+2));
		gun.y = (float) (home.y+home.origX-gun.origX + Math.sin(pos)*(home.radius*home.yScale+2));
		gun.rot = this.rot+gunangle;
		gun.z = z + 1;
		
		super.queueRender(display);
		gun.queueRender(display);
		for(int i=0;i<bullets.size();i++){
			bullets.get(i).queueRender(display);
		}
	}
}
