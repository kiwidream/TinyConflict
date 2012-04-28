package com.Redream.TinyConflict;

public class Asteroid extends Planet {
	public Asteroid(){
		this.origX = 12;
		this.origY = 12;
		this.tex = 4;
		this.radius = 14;
		this.xScale = 1;
		this.yScale = 1;
		this.name = "Asteroid";
		
		pid = 0;
		
		funds = radius * 100;
	}
}
