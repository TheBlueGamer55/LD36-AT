package com.swinestudios.ancienttech;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

public class Bug { 

	public float x, y;
	public float velX, velY;

	public boolean isActive;

	public boolean isHit;
	public float hitTimer;
	public final float maxHitTimer = 1;

	public Rectangle hitbox;
	public Gameplay level;
	public String type;

	public Bug(float x, float y, Gameplay level){
		this.x = x;
		this.y = y;
		velX = 0;
		velY = -0.5f;
		isActive = true;
		isHit = false;
		hitTimer = 0;
		this.level = level;
		type = "Bug";
		hitbox = new Rectangle(x, y, 16, 24); 
	}

	public void render(Graphics g){
		g.setColor(Color.YELLOW);
		g.fillRect(x, y, hitbox.width, hitbox.height);

		if(isHit){
			//TODO flash before removed
		}
	}

	public void update(float delta){
		if(isHit){
			hitTimer += delta;
			if(hitTimer > maxHitTimer){
				hitTimer = 0;
				isHit = false;
				level.bugs.remove(this);
			}
		}
		else{
			move();
			checkProjectileCollision();

			hitbox.setX(this.x);
			hitbox.setY(this.y);
		}
	}

	public void checkProjectileCollision(){
		for(int i = 0; i < level.projectiles.size(); i++){
			Projectile temp = level.projectiles.get(i);
			if(temp != null && temp.isActive){
				if(isColliding(temp.hitbox, this.x, this.y)){ //If there is a collision
					isHit = true;
					//Decrement if this was an idle bug
					if(velY == 0){
						level.player.bugCount--;
					}
				}
			}
		}
	}

	public boolean isColliding(Rectangle other, float x, float y){
		if(other == this.hitbox){ //Make sure solid isn't stuck on itself
			return false;
		}
		if(x < other.x + other.width && x + hitbox.width > other.x && y < other.y + other.height && y + hitbox.height > other.y){
			return true;
		}
		return false;
	}

	public void move(){
		moveX();
		moveY();
	}

	/*
	 * Move horizontally in the direction of the x-velocity vector. If there is a collision in
	 * this direction, step pixel by pixel up until the player hits the solid.
	 */
	public void moveX(){
		for(int i = 0; i < level.bugBlocks.size(); i++){
			Rectangle solid = level.bugBlocks.get(i);
			if(isColliding(solid, x + velX, y)){
				while(!isColliding(solid, x + Math.signum(velX), y)){
					x += Math.signum(velX);
				}
				velX = 0;
			}
		}
		x += velX;
	}

	/*
	 * Move vertically in the direction of the y-velocity vector. If there is a collision in
	 * this direction, step pixel by pixel up until the player hits the solid.
	 */
	public void moveY(){
		for(int i = 0; i < level.bugBlocks.size(); i++){
			Rectangle solid = level.bugBlocks.get(i);
			if(isColliding(solid, x, y + velY)){
				while(!isColliding(solid, x, y + Math.signum(velY))){
					y += Math.signum(velY);
				}
				velY = 0;
				level.player.bugCount++;
			}
		}
		y += velY;
	}

}