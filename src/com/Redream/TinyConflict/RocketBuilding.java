package com.Redream.TinyConflict;

import java.util.Random;

public class RocketBuilding extends Building {
	public boolean launched;
	public int launchtime = 0;
	public Rocket rocket = new Rocket();
	private int waittime;
	
	public RocketBuilding() {
		this.tex = 13;
		this.origX = 20;
		this.origY = 11;
		this.xScale = 0.5f;
		this.yScale = 0.5f;
		Input.registerListener(this);
		
		this.name = "Rocket Launch Facility";
		this.desc = "Launches powerful rockets!";
		this.cost = 750;
	}
	
	public void queueRender(Display display) {
		if(home == null)return;
		double pos = Math.toRadians(pX+home.rot);
		this.x = (float) (home.x+home.origX-origX+Math.cos(pos)*home.radius);
		this.y = (float) (home.y+home.origY-origY+Math.sin(pos)*home.radius);
		this.rot = pX - 90 + home.rot;
		
		if(!launched && rocket != null){
			rocket.x = (float) (home.x+home.origX-rocket.origX + Math.cos(pos)*(home.radius*home.xScale+2));
			rocket.y = (float) (home.y+home.origX-rocket.origX + Math.sin(pos)*(home.radius*home.yScale+2));
			rocket.rot = this.rot;
			rocket.z = z + 1;
		}
		
		if(rocket != null)rocket.queueRender(display);
		super.queueRender(display);
	}
	
	public void tick(){
		if(home == null)return;
		if(!launched && !home.editing && rocket != null){
			for(Planet p : Game.planets){
				if(p == home || p.population == 0 || home.population == 0)continue;
				float xd = home.x - p.x;
				float yd = home.y - p.y;
				double dist = Math.sqrt(xd*xd+yd*yd);
				if(dist < 300){
					rocket.launch(p);
					this.launched = true;
				}
			}
		}else{
			if(rocket != null && rocket.remove)rocket = null;
		}
		
		if(rocket == null){
			if(waittime > 1400){
				launched = false;
				rocket = new Rocket();
				waittime = 0;
			}
		}else{
			if(waittime > 800){
				for(int i=0;i<70;i++)Game.entities.add(new Particle((float)(rocket.x+new Random().nextGaussian()*5f),(float)(rocket.y+new Random().nextGaussian()*5f),2,new Random().nextFloat()*360,new Random().nextInt(50)+15));
				if(Camera.getBounds().contains(rocket.x, rocket.y))Resources.xplode.play();
				rocket = null;
				waittime = 0;
			}
		}
		if(launched)waittime++;
		
		if(rocket != null)rocket.tick();
		super.tick();
	}

}
