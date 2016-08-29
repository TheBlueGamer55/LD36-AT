package com.swinestudios.ancienttech;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Bug { 

	public float x, y;
	public float velX, velY;

	public boolean isActive;
	public boolean stuck;

	public boolean isHit;
	public float hitTimer;
	public final float maxHitTimer = 0.5f;

	public boolean flashing;
	public float flashTimer;
	public final float maxFlashTimer = 0.04f;

	public Sprite frame0, frame1, frame2, idle;
	public Animation<Sprite> bugAnim;
	public float animationSpeed = 0.05f; //How many seconds a frame lasts

	public Rectangle hitbox;
	public Gameplay level;
	public String type;
	
	public final int pointsValue = 25;
	
	public static Sound dieSound = Gdx.audio.newSound(Gdx.files.internal("bug_die.wav"));

	public Bug(float x, float y, Gameplay level){
		this.x = x;
		this.y = y;
		velX = 0;
		velY = -0.5f;
		isActive = true;
		isHit = false;
		stuck = false;
		flashing = false;
		hitTimer = 0;
		this.level = level;
		type = "Bug";

		frame0 = new Sprite(new Texture(Gdx.files.internal("moth00.png")));
		frame1 = new Sprite(new Texture(Gdx.files.internal("moth01.png")));
		frame2 = new Sprite(new Texture(Gdx.files.internal("moth02.png")));
		idle = new Sprite(new Texture(Gdx.files.internal("moth_idle.png")));
		adjustSprite(frame0, frame1, frame2, idle);
		resizeSprite(frame0, frame1, frame2, idle);

		bugAnim = new Animation<Sprite>();
		bugAnim.addFrame(frame0, animationSpeed);
		bugAnim.addFrame(frame1, animationSpeed);
		bugAnim.addFrame(frame2, animationSpeed);
		bugAnim.flip(false, true);
		bugAnim.setLooping(true);

		this.hitbox = new Rectangle(x, y, frame0.getWidth(), frame0.getHeight());
	}

	public void render(Graphics g){
		if(!flashing){
			if(bugAnim != null){
				if(stuck){
					g.drawSprite(idle, x, y);
				}
				else{
					bugAnim.draw(g, x, y);
				}
			}
			else{
				g.setColor(Color.YELLOW);
				g.fillRect(x, y, hitbox.width, hitbox.height);
			}
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
			
			flashTimer += delta;
			if(flashTimer > maxFlashTimer){
				flashing = !flashing;
				flashTimer = 0;
			}
		}
		else{
			bugAnim.update(delta);
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
					dieSound.play(0.6f);
					Gameplay.score += this.pointsValue;
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
				stuck = true;
			}
		}
		y += velY;
	}

	public void adjustSprite(Sprite... s){
		for(int i = 0; i < s.length; i++){
			if(s != null){
				s[i].setOrigin(0, 0);
				//s[i].flip(false, true);
			}
		}
	}

	public void resizeSprite(Sprite... s){
		for(int i = 0; i < s.length; i++){
			if(s != null){ 
				s[i].setSize(s[i].getWidth()*2, s[i].getHeight()*2);
			}
		}
	}

}