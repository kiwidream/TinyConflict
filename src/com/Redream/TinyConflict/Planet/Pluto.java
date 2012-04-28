package com.Redream.TinyConflict.Planet;

public class Pluto extends Planet {

	public Pluto() {
		this.origX = 28;
		this.origY = 28;
		this.tex = 25;
		this.radius = 28;
		this.xScale = 1;
		this.yScale = 1;
		this.name = "Pluto";
		
		pid = 2;
		
		funds = radius * 70;
	}

}
