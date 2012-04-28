package com.Redream.TinyConflict;

import java.util.Random;

public class Housing extends Building {
	public Housing(){
		this.tex = 22;
		this.xScale = 0.5f;
		this.yScale = 0.5f;
		this.origX = 11;
		this.origY = 9;
		
		name = "House";
		desc = "Increases population";
		this.pX = new Random().nextFloat()*360;
	}
	
	public void setPlanet(Planet p){
		this.home = p;
		this.z = home.z + 1;
		p.addPopulation(1);
	}
}
