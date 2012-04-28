package com.Redream.TinyConflict.Entity;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;

public class Particle extends Entity {
	protected float speed;
	protected int age;
	protected int lifespan;
	
	public Particle(float x, float y, float speed, float dir, int lifespan){
		this.tex=11;
		this.xScale = 2;
		this.yScale = 2;
		this.x = x;
		this.y = y;
		this.z = 200;
		this.rot = dir;
		this.speed = speed;
		this.lifespan = lifespan;
		this.collides = false;
		
		float col = new Random().nextFloat()*0.6f + 0.4f;
		
		this.color = new Color(col,col,col,1);
	}
	
	public void tick(){
		this.move((float) Math.toRadians(rot), speed);
		speed *= 0.96;
		
		if(age>lifespan)this.remove = true;
		age++;
	}
}
