package com.Redream.TinyConflict.Planet;

import com.Redream.TinyConflict.Graphics.Display;
import com.Redream.TinyConflict.Graphics.Renderable;


public class VolcanoPlanet extends Planet{
	public VolcanoPlanet(){
		
		this.origX = 51;
		this.origY = 50;
		this.xScale = 1.5f;
		this.yScale = 1.5f;
		radius = 52;
		
		pid = 5;
		
		name = "Volcanitus";
		
		funds = radius * 80;
	}
	
	public void queueRender(Display display){
		Renderable ring = new Renderable();
		ring.tex = 15;
		ring.xScale = xScale;
		ring.yScale = yScale;
		ring.x = x-origX;
		ring.y = y-origY;
		ring.z = z+2;
		ring.queueRender(display);
		super.queueRender(display);
	}
}
