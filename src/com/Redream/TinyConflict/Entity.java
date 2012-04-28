package com.Redream.TinyConflict;

import java.util.Random;

public class Entity extends Renderable {
	public float vX;
	public float vY;
	public int radius;
	protected boolean collides = true;
	public boolean collisionFX = false;
	
	public boolean move(float dir,float speed){
		vX = (float) (Math.cos(dir)*speed);
		vY = (float) (Math.sin(dir)*speed);
		return this.move();
	}
	
	public boolean move(){
		this.x += vX;
		this.y += vY;
		if(!collides || (vX == 0 && vY == 0))return true;
		
		for(int i=0;i<Game.planets.size();i++){
			Planet p = Game.planets.get(i);
			if(p == this)continue;
			
			float xd = (x+origX) - (p.x+p.origX);
			float yd = (y+origY) - (p.y+p.origY);
			double dist = Math.sqrt(xd*xd+yd*yd);
			if(dist < this.radius*xScale+p.radius*p.xScale){
				this.x -= vX;
				this.y -= vY;
				double moveangle = Math.toDegrees(Math.atan2(vY, vX));
				double normal = Math.toDegrees(Math.atan2(y-p.y, x-p.x))+90;
				double origv = Math.sqrt(vX*vX+vY*vY);
				
				moveangle = (float) Math.toRadians(2*normal-moveangle);
				vX = (float) (Math.cos(moveangle)*origv*(1-radius/65f));
				vY = (float) (Math.sin(moveangle)*origv*(1-radius/65f));
				
				moveangle = (float) Math.toRadians(2*normal-Math.toDegrees(moveangle));
				p.vX = (float) (Math.cos(moveangle)*origv*(1-p.radius/65f));
				p.vY = (float) (Math.sin(moveangle)*origv*(1-p.radius/65f));
				
				if(collisionFX){
					float py = (float) (y+origY+Math.sin(Math.toRadians(normal+90))*radius*yScale);
					float px = (float) (x+origX+Math.cos(Math.toRadians(normal+90))*radius*xScale);
					
					if(Camera.getBounds().contains(px, py))Resources.xplode.play();
					
					for(int j=0;j<origv*10;j++)Game.entities.add(new Particle((float)(px+new Random().nextGaussian()*5f),(float)(py+new Random().nextGaussian()*5f),(float) origv/4f,new Random().nextFloat()*360,new Random().nextInt(50)+15));
				}
				return false;
			}
		}
		return true;

	}
}
