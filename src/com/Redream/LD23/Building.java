package com.Redream.LD23;

public class Building extends Renderable {
	public Planet home;
	public static Building ROCKET = new RocketBuilding();
	public static Building TURRET = new Turret();
	public static Building HOUSING = new Housing();
	public static Building MINER = new Miner();
	public static Building GYRO = new PlanetGyro();
	
	public String name = "Building";
	public String desc = "Can be built";
	public int cost = 100;
	public int pvtex = 0;

	public double elevation;
	
	public float pX = 0;

	public Building(){
		this.pX = 100;
	}

	public void setPlanet(Planet p){
		this.home = p;
		this.z = home.z + 1;
	}

	public void queueRender(Display display) {
		if(home == null)return;
		double pos = Math.toRadians(pX+home.rot);
		this.x = (float) (home.x+home.origX-origX+Math.cos(pos)*((home.radius*home.xScale)+elevation));
		this.y = (float) (home.y+home.origY-origY+Math.sin(pos)*((home.radius*home.yScale)+elevation));
		this.rot = (pX - 90 + home.rot)%360;
		super.queueRender(display);
	}
}
