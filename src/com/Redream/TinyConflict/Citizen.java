package com.Redream.TinyConflict;

import java.util.Random;

public class Citizen extends Building {
	public float moveamt = 0.15f;
	public boolean migrated;
	public int walkTime = new Random().nextInt(60);

	public Citizen() {
		this.tex = 8;
		if(new Random().nextBoolean())moveamt *= -1;
		pX = new Random().nextFloat() * 360;
		this.xScale = 1;
		this.yScale = 1;
		this.origX = 4;
		this.origY = 2;
	}
	
	public void setPlanet(Planet p){
		this.home = p;
		this.z = home.z + 2;
	}

	public void tick(){
		if(home == null)return;
		if(Game.migratingTo != null){
			pX = home.migratedir;
			
			if(!migrated){
				home.migrateCount++;
				migrated = true;
			}
		}else{
			if(elevation == 0)this.pX += moveamt;

			if(new Random().nextInt(150) == 0){
				moveamt *= -1;
				this.pX += moveamt*2;
			}
		}
		if(walkTime <= 0){
			if(tex == 8){
				tex = 9;
			}else{
				tex = 8;
			}
			walkTime = 60;
		}
		walkTime--;
	}

}
