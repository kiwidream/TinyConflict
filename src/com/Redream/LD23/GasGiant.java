package com.Redream.LD23;

public class GasGiant extends Planet {

	public GasGiant() {
		this.origX = 52;
		this.origY = 52;
		this.tex = 27;
		this.radius = 52;
		this.xScale = 1;
		this.yScale = 1;
		this.name = "Gas Giant";
		
		pid = 4;
		
		funds = radius * 70;
	}

}
