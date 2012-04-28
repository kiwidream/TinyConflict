package com.Redream.TinyConflict.Entity;

import java.util.Random;

import com.Redream.TinyConflict.Game;
import com.Redream.TinyConflict.Planet.Planet;
import com.badlogic.gdx.graphics.Color;

public class Bullet extends Particle {
	public Planet home;

	public Bullet(float x, float y, float speed, float dir, int lifespan,Planet home) {
		super(x, y, speed, dir, lifespan);
		this.color = Color.YELLOW;
		this.home = home;
	}
	
	public void tick(){
		this.move((float) Math.toRadians(rot), speed);
		speed *= 0.999;
		
		if(age>lifespan)this.remove = true;
		age++;
	}
	
	public boolean move(){
		this.x += vX;
		this.y += vY;

		for(Planet p : Game.planets){
			if(p == home)continue;
			float xd = (x+origX) - (p.x+p.origX);
			float yd = (y+origY) - (p.y+p.origY);
			double dist = Math.sqrt(xd*xd+yd*yd);
			if(dist < this.radius*xScale+p.radius*p.xScale){
				this.remove = true;
				for(int i=0;i<10;i++)Game.entities.add(new Particle((float)(x+new Random().nextGaussian()*5f),(float)(y+new Random().nextGaussian()*5f),0.5f,new Random().nextFloat()*360,new Random().nextInt(50)+15));
				if(new Random().nextInt(20) == 0)p.removePopulation(1);
				return false;
			}
		}
		return true;

	}

}
