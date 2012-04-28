package com.Redream.TinyConflict;

public class Moon extends Planet {
	public Moon(){
		this.origX = 34;
		this.origY = 35;
		this.tex = 21;
		this.radius = 35;
		this.xScale = 1;
		this.yScale = 1;
		this.name = "Moon";
		
		pid = 1;
		
		funds = radius * 60;
	}
}
