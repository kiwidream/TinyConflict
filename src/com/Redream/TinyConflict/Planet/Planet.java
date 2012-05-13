package com.Redream.TinyConflict.Planet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.Redream.TinyConflict.Game;
import com.Redream.TinyConflict.Building.Building;
import com.Redream.TinyConflict.Building.Citizen;
import com.Redream.TinyConflict.Building.Housing;
import com.Redream.TinyConflict.Building.Miner;
import com.Redream.TinyConflict.Building.PlanetGyro;
import com.Redream.TinyConflict.Building.RocketBuilding;
import com.Redream.TinyConflict.Building.Turret;
import com.Redream.TinyConflict.Entity.Entity;
import com.Redream.TinyConflict.Graphics.Camera;
import com.Redream.TinyConflict.Graphics.Display;
import com.Redream.TinyConflict.Graphics.Font;
import com.Redream.TinyConflict.Graphics.Renderable;
import com.Redream.TinyConflict.Input.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public class Planet extends Entity {
	public boolean up;
	public boolean down;
	public boolean left;
	public boolean right;

	public float migratedir;

	public boolean editing;

	public static Planet[] pOrder = new Planet[]{
		new Asteroid(),
		new Moon(),
		new Pluto(),
		new Earth(),
		new GasGiant(),
		new VolcanoPlanet(),
	};

	public int pid;

	public int population;
	public int migrateCount;

	public String name = "Planet";

	public List<Building> buildings = new ArrayList<Building>();
	private Color hudColor = new Color(1,1,1,0.7f);

	private int editselect = 9999;
	private int addSelect = 9999;

	private Renderable cursor;

	public boolean isplayer;

	public float funds;
	public double migratedist;
	private int popToAdd;
	private int gc;

	private int lastX;
	private int lastY;
	private boolean touchMove;

	public Planet(){
		Input.registerListener(this);

		this.rot = new Random().nextInt(360);

		this.addBuilding(new Housing(),true);
		this.addBuilding(new Miner(),true);

		this.collisionFX = true;
	}

	public Rectangle getBounds(){
		return new Rectangle(x,y,radius*xScale*2f,radius*yScale*2f);
	}

	public boolean touchDragged(int x, int y,int pointer){
		this.touchMoved(x, y);
		lastX = x;
		lastY = y;
		this.touchMove = true;
		return true;
	}

	public boolean touchMoved(int x, int y){
		if(!Game.editing && this.getBounds().contains(x+Camera.cam.position.x, (Math.abs(y-Game.HEIGHT))+Camera.cam.position.y)){
			Game.pselected = this;
		}

		if(editing && editselect != 9999 && isplayer){
			Building pv = this.getBuildables().get(editselect);
			cursor = new Renderable();
			cursor.tex = pv.tex;
			cursor.xScale = pv.xScale;
			cursor.yScale = cursor.xScale;
			cursor.origX = pv.origX;
			cursor.origY = pv.origY;
			cursor.z = 110;

			double cangle = Math.atan2((Math.abs(Game.HEIGHT-y))-this.origY-this.y+Camera.cam.position.y, x-this.origX-this.x+Camera.cam.position.x);

			cursor.x = (float) (this.x+origX-pv.origX+Math.cos(cangle)*radius*xScale);
			cursor.y = (float) (this.y+origY-pv.origY+Math.sin(cangle)*radius*yScale);
			cursor.color = hudColor;
			cursor.rot = (float) ((Math.toDegrees(cangle)-90f)%360f);
		}
		return true;
	}

	public boolean touchUp(int pointer, int x, int y){
		this.touchMove = false;
		return true;
	}

	public boolean touchDown(int pointer, int x, int y){
		lastX = x;
		lastY = y;
		this.touchMove = true;

		if(Game.pselected == this && isplayer && new Rectangle(Game.WIDTH/2 - 96,10,96*2,64).contains(x, (Math.abs(y-Game.HEIGHT)))){
			editing = true;
			Game.editing = true;
		}

		if(Game.pselected == this && this.population == 0 && popToAdd == 0 && !isplayer && Game.migratingTo == null && !Game.editing && new Rectangle(Game.WIDTH/2 - 96,10,96*2,64).contains(x, (Math.abs(y-Game.HEIGHT)))){
			Game.migratingTo = this;
		}

		if(editing){			
			if(x<170 && y < this.getBuildables().size()*60){
				if(this.getBuildables().get(y/60).cost < funds){
					editselect = y/60;
				}
			}else if(x<170){
				if(y>Game.HEIGHT-30){
					editing = false;
					Game.editing = false;
					Camera.targetzoom = 1f;
				}
				editselect = 9999;
			}else if(editselect != 9999){
				this.addSelect = editselect;
			}
		}

		return true;
	}

	private void touchMove(int x, int y) {
		float speed = 2.5f+(gc*0.75f);
		float resp = 0.02f+(gc*0.01f);

		y = Game.HEIGHT - y;
		float mdir = (float) (Math.atan2(y-(this.y+this.origY-Camera.cam.position.y),x-(this.x+this.origX-Camera.cam.position.x)));
		float speedX = (float) (Math.cos(mdir)*speed);
		float speedY = (float) (Math.sin(mdir)*speed);

		this.vY += (speedY - vY)*resp;
		this.vX += (speedX - vX)*resp;
	}

	public void tick(){
		if(popToAdd > 0){
			population += popToAdd;
			for(int i=0;i<popToAdd;i++){
				Building b2 = new Citizen();
				b2.setPlanet(this);
				this.buildings.add(b2);
			}
			popToAdd = 0;
		}

		if(editing){
			Camera.targetzoom = 0.5f;
			if(addSelect != 9999){
				Building b = null;
				try {
					b = this.getBuildables().get(editselect).getClass().newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				if(b != null){
					this.addBuilding(b,false);
				}
				addSelect = 9999;
				editselect = 9999;
			}
		}


		if(isplayer && Game.migratingTo != null){
			Planet p = Game.migratingTo;
			float xd = (x+origX) - (p.x+p.origX);
			float yd = (y+origY) - (p.y+p.origY);
			this.migratedist = Math.sqrt(xd*xd+yd*yd)-p.radius*p.xScale-radius*xScale;
			this.migratedir = (float) Math.toDegrees(Math.atan2(p.y+p.origY-y,p.x+p.origX-x))-rot;
			if(this.migrateCount >= population){
				migrateCount = 0;
				p.isplayer = true;
				this.isplayer = false;

				p.addPopulation(population);
				this.removePopulation(population);

				up = false;
				down = false;
				left= false;
				right = false;

				Camera.target = p;

				if(Game.migratingTo.pid == 5)Game.winTime = 1;

				Game.migratingTo = null;
			}
		}

		for(Building b : buildings){
			b.tick();
		}
		this.rot += 0.15;
		if(rot>360)rot=0;
		this.vX *= 0.996;
		this.vY *= 0.996;

		if(!editing){
			float speed = 2.5f+(gc*0.75f);
			float resp = 0.02f+(gc*0.01f);
			if(down)this.vY += (-speed  - vY)*resp;
			if(up)this.vY += (speed - vY)*resp;
			if(left)this.vX += (-speed - vX)*resp;
			if(right)this.vX += (speed - vX)*resp;

			if(isplayer&& this.touchMove){
				this.touchMove(lastX, lastY);
			}

			if(Game.migratingTo == null){
				this.move();
			}
		}

		if(!isplayer && Game.migratingTo == null && Game.pselected != this){
			if(new Random().nextInt(80) == 0 && population < radius * 0.7){
				this.addBuilding(new Housing(),false);
			}else if(this.funds < 1500 && new Random().nextInt(200) == 0){
				this.addBuilding(new Miner(),false);
			}else if(new Random().nextInt(300) == 0){
				this.addBuilding(new Turret(),false);
			}else if(new Random().nextInt(400) == 0){
				this.addBuilding(new RocketBuilding(),false);
			}
		}
	}

	private void addBuilding(Building b,boolean free) {
		if(free || (funds - b.cost >= 0 && (isplayer || this.buildings.size()-population<radius/3))){
			if(cursor != null){
				b.pX = (cursor.rot+85f-this.rot)%360;
			}else{
				b.pX = new Random().nextInt(360);
			}
			b.setPlanet(this);
			this.buildings.add(b);
			if(b instanceof PlanetGyro){
				this.gc++;
			}
			if(!free)funds -= b.cost;
		}
	}

	public void addPopulation(int count) {
		popToAdd += count;
	}

	public List<Building> getBuildables(){
		List<Building> bl = new ArrayList<Building>();

		bl.add(Building.ROCKET);
		bl.add(Building.TURRET);
		bl.add(Building.HOUSING);
		bl.add(Building.MINER);
		bl.add(Building.GYRO);

		return bl;
	}

	public int getPopulation(){
		return new Random().nextInt((int)(radius/4))+radius/6;
	}

	public void queueRender(Display display) {
		for(Building b : buildings){
			b.queueRender(display);
		}
			
		if(Game.pselected == this && !editing){
			Renderable ch = new Renderable();
			ch.tex = 1;
			ch.xScale = (this.origX * this.xScale)/14;
			ch.color = this.hudColor;
			ch.yScale = ch.xScale;
			ch.x = this.x - (this.origX*(this.xScale-1)) - ch.xScale*16*0.125f;
			ch.y = this.y - (this.origY*(this.yScale-1)) - ch.yScale*16*0.125f;
			ch.z = 400;
			ch.queueRender(display);

			if(isplayer){
				Renderable b1 = new Renderable();
				b1.tex = 2;
				b1.xScale = 2;
				b1.yScale = 2;
				b1.y = 10;
				b1.applyCam = false;
				b1.x = Game.WIDTH/2 - (96*b1.xScale)/2;

				b1.queueRender(display);

				new Font("Upgrade", Game.WIDTH/2, b1.y+20, Font.POS_CENTER, false, Color.DARK_GRAY, 3).queueRender(display);
			}else if(population == 0 && Game.migratingTo == null){
				Renderable b1 = new Renderable();
				b1.tex = 2;
				b1.xScale = 2;
				b1.yScale = 2;
				b1.y = 10;
				b1.applyCam = false;
				b1.x = Game.WIDTH/2 - (96*b1.xScale)/2;

				b1.queueRender(display);

				new Font("Migrate", Game.WIDTH/2, b1.y+20, Font.POS_CENTER, false, Color.DARK_GRAY, 3).queueRender(display);
			}
		}

		if(isplayer){
			Renderable coin = new Renderable();
			coin.tex = 20;
			coin.xScale = 2;
			coin.yScale = 2;
			coin.x = Game.WIDTH-30;
			coin.y = Game.HEIGHT -30;
			coin.z = 300;
			coin.applyCam = false;
			coin.queueRender(display);
			new Font(String.valueOf(Math.round(funds)), Game.WIDTH-35, Game.HEIGHT-36, Font.POS_RIGHT,false,Color.WHITE,2).queueRender(display);


			if(!editing){

				int id = Math.min(pOrder.length-1,pid+1);
				Planet tp = Game.planets.get(id);
				double tdir = Math.toDegrees(Math.atan2((tp.y+tp.origY*tp.yScale)-(y+origY*yScale),(tp.x+tp.origX*tp.xScale)-(x+origX*xScale)));
				double tdir2 = Math.toRadians(tdir);

				Renderable point = new Renderable();
				point.tex = 28;
				point.xScale = 1;
				point.yScale = 1;
				point.origX = 10;
				point.origY = 10;
				point.z = 305;
				point.x = (float) (x+origX-point.origX+Math.cos(tdir2)*(radius*xScale+20));
				point.y = (float) (y+origY-point.origY+Math.sin(tdir2)*(radius*yScale+20));
				point.rot = (float) ((tdir-90)%360);

				point.queueRender(display);
			}
		}

		if(editing){
			Renderable p1 = new Renderable();
			p1.tex = 11;
			p1.yScale = Game.HEIGHT;
			p1.applyCam = false;
			p1.z = 99;
			p1.xScale = 170;
			p1.queueRender(display);
			p1.color = hudColor;

			new Font("Upgrade "+name, Game.WIDTH/2, Game.HEIGHT - 40, Font.POS_CENTER,false,Color.WHITE,3).queueRender(display);
			new Font("Exit Editor", 30,0, Font.POS_LEFT,false,Color.BLACK,2).queueRender(display);
			int i = 0;
			for(Building b : this.getBuildables()){
				if(i == editselect){
					Renderable p2 = new Renderable();
					p2.tex = 11;
					p2.yScale = 60;
					p2.applyCam = false;
					p2.z = 99;
					p2.y = (Game.HEIGHT - i*60)-60;
					p2.xScale = 170;
					p2.queueRender(display);
					p2.color = Color.WHITE;

					if(cursor != null)cursor.queueRender(display);
				}
				new Font(b.name, 10, (Game.HEIGHT - i*60)-35, Font.POS_LEFT,false,Color.BLACK,1).queueRender(display);
				new Font(b.desc, 10, (Game.HEIGHT - i*60)-50, Font.POS_LEFT,false,Color.DARK_GRAY,1).queueRender(display);

				String ctxt = "Cost: "+b.cost;
				if(b.cost > funds)ctxt="Cannot afford!";
				new Font(ctxt, 10, (Game.HEIGHT - i*60)-70, Font.POS_LEFT,false,Color.BLACK,1).queueRender(display);
				i++;
			}
		}

		super.queueRender(display);
	}

	public boolean keyDown(int keycode){
		if(isplayer){
			if(keycode == Keys.W)up=true;
			if(keycode == Keys.A)left=true;
			if(keycode == Keys.S)down=true;
			if(keycode == Keys.D)right=true;
		}
		return true;
	}

	public boolean keyUp(int keycode){
		if(isplayer){
			if(keycode == Keys.W)up=false;
			if(keycode == Keys.A)left=false;
			if(keycode == Keys.S)down=false;
			if(keycode == Keys.D)right=false;
		}
		return true;
	}

	public void removePopulation(int count) {
		int counter = count;
		for(int j=0;j<buildings.size() && population > 0 && counter > 0;j++){
			if(buildings.get(j) instanceof Citizen){
				buildings.remove(j--);
				this.population--;
				counter--;
			}
		}
		counter = count - counter;
		for(int j=0;j<buildings.size() && counter > 0;j++){
			if(buildings.get(j) instanceof Housing){
				buildings.remove(j--);
				counter--;
			}else if(new Random().nextInt(20) == 0 && !(buildings.get(j) instanceof Citizen) && !(buildings.get(j) instanceof Miner)){
				if(buildings.get(j) instanceof PlanetGyro){
					this.gc--;
				}
				buildings.remove(j--);
			}
		}
	}
}
