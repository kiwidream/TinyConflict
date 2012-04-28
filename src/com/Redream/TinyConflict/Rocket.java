package com.Redream.TinyConflict;

import java.util.Random;

public class Rocket extends Renderable {
	public float dir;
	public float speed;
	
	public float rotspeed;
	public float rstarget;
	
	public boolean launched;
	private Planet target;
	
	public Rocket(){
		this.tex = 14;
		this.origX = 5;
		this.origY = 5;
		this.xScale = 0.5f;
		this.yScale = 0.5f;
	}
	
	public void move(){
		double dr = Math.toRadians(dir+90);	
		this.x += Math.cos(dr) * speed;
		this.y += Math.sin(dr) * speed;
		this.dir += rotspeed;
		rot = dir;
	}
	
	public void tick(){
		if(launched){
			double tdir = Math.toDegrees(Math.atan2(target.y+target.origY-y,target.x+target.origX-x))-90;
			
			double diff = tdir - dir;
			
			if(Math.abs(diff)> 180){
				dir = (float) tdir;
			}else{
				dir += diff * 0.06f;
			}
			
			if(new Random().nextInt(1) == 0)Game.entities.add(new Particle(x+5,y,1,new Random().nextFloat()*360,new Random().nextInt(30)+15));
			
			float xd = (x+origX) - (target.x+target.origX);
			float yd = (y+origY) - (target.y+target.origY);
			double dist = Math.sqrt(xd*xd+yd*yd);
			
			if(dist <= target.radius*target.xScale){
				this.remove = true;
				target.removePopulation(new Random().nextInt(3)+1);
				if(Camera.getBounds().contains(x, y))Resources.xplode.play();
				for(int i=0;i<70;i++)Game.entities.add(new Particle((float)(x+new Random().nextGaussian()*5f),(float)(y+new Random().nextGaussian()*5f),2,new Random().nextFloat()*360,new Random().nextInt(50)+15));
			}
			
			move();
		}
	}
	
	public void launch(Planet target){
		dir = rot;
		this.target = target;
		this.launched = true;
		this.speed = 2f;
	}
}
