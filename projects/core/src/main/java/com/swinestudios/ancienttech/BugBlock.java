package com.swinestudios.ancienttech;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

public class BugBlock extends Rectangle{

	public boolean isActive;
	public String type;
	private Gameplay level;

	public BugBlock(float x, float y, float width, float height, Gameplay level){
		super(x, y, width, height);
		isActive = true;
		type = "BugBlock";
		this.level = level;
	}

	public void render(Graphics g){
		g.setColor(Color.CHARTREUSE);
		g.drawRect(x, y, width, height);
	}

	public void update(float delta){
		//Empty for now
	}

}
