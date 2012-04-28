package com.Redream.TinyConflict.Planet;

public class Earth extends Planet{

	public Earth() {
		this.origX = 33;
		this.origY = 33;
		this.tex = 26;
		this.radius = 33;
		this.xScale = 1.5f;
		this.yScale = 1.5f;
		this.name = "Earth";
		
		pid = 3;
		
		funds = radius * 70;
	}

}
