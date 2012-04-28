package com.Redream.TinyConflict.Building;

import java.util.Random;

import com.Redream.TinyConflict.Planet.Planet;

public class Miner extends Building {
	private float moveamt = 0.1f;

	public Miner() {
		this.tex = 23;
		if(new Random().nextBoolean())moveamt  *= -1;
		this.xScale = 0.5f;
		this.yScale = 0.5f;
		this.origX = 14;
		this.origY = 7;
		this.name = "Miner Bot";
		this.desc = "Mines valuable resources";
		this.cost = 500;

		this.pX = new Random().nextFloat()*360;
	}

	public void setPlanet(Planet p){
		this.home = p;
		this.z = home.z + 2;
	}

	public void tick(){
		if(home == null)return;

		if(!home.editing){
			this.pX += moveamt;

			if(new Random().nextInt(60) == 0)home.funds += new Random().nextInt(15)+5;

			if(new Random().nextInt(150) == 0){
				moveamt *= -1;
				this.pX += moveamt*2;
			}
		}
	}
}
