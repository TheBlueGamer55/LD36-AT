package com.swinestudios.ancienttech;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Player implements InputProcessor{ 

	public float x, y;
	public float velX, velY;

	public final float moveSpeedX = 2.0f;
	public final float moveSpeedY = 1.0f;
	
	public final float sprayOffsetX = 16f;
	public final float sprayOffsetY = 51f;

	public boolean isActive;
	public boolean facingLeft, facingRight;
	public boolean walking;

	public Rectangle hitbox;
	public Gameplay level;
	public String type;

	public int bugCount;

	public Sprite left, left1, left2, left3, right, right1, right2, right3;
	public Animation<Sprite> playerLeft, playerRight, playerCurrent;
	public final float animationSpeed = 0.025f; 

	//Controls/key bindings
	public final int LEFT = Keys.A;
	public final int RIGHT = Keys.D;
	public final int UP = Keys.W;
	public final int DOWN = Keys.S;

	public static Sound spraySound = Gdx.audio.newSound(Gdx.files.internal("spray1.wav"));

	public Player(float x, float y, Gameplay level){
		this.x = x;
		this.y = y;
		velX = 0;
		velY = 0;
		bugCount = 0;
		isActive = true;
		facingRight = true;
		facingLeft = false;
		walking = false;
		this.level = level;
		type = "Player";
		
		right = new Sprite(new Texture(Gdx.files.internal("player_right.png")));
		right1 = new Sprite(new Texture(Gdx.files.internal("player_right1.png")));
		right2 = new Sprite(new Texture(Gdx.files.internal("player_right2.png")));
		right3 = new Sprite(new Texture(Gdx.files.internal("player_right3.png")));
		setSpriteOrigin(right, right1, right2, right3);
		right.scale(1);
		right1.scale(1);
		right2.scale(1);
		right3.scale(1);
		
		/*left = new Sprite(new Texture(Gdx.files.internal("player_left.png")));
		left1 = new Sprite(new Texture(Gdx.files.internal("player_left1.png")));
		left2 = new Sprite(new Texture(Gdx.files.internal("player_left2.png")));
		left3 = new Sprite(new Texture(Gdx.files.internal("player_left2.png")));*/
		left = new Sprite(right);
		left.flip(true, false);
		left1 = new Sprite(right1);
		//left1.flip(true, false);
		left2 = new Sprite(right2);
		//left2.flip(true, false);
		left3 = new Sprite(right3);
		//left3.flip(true, false);
		
		playerLeft = new Animation<Sprite>();
		playerRight = new Animation<Sprite>();
		
		playerLeft.addFrame(left, animationSpeed);
		playerLeft.addFrame(left1, animationSpeed);
		playerLeft.addFrame(left2, animationSpeed);
		playerLeft.addFrame(left3, animationSpeed);
		playerLeft.setLooping(false);
		playerLeft.flip(true, true);
		
		playerRight.addFrame(right, animationSpeed);
		playerRight.addFrame(right1, animationSpeed);
		playerRight.addFrame(right2, animationSpeed);
		playerRight.addFrame(right3, animationSpeed);
		playerRight.setLooping(false);
		playerRight.flip(false, true);
		
		playerCurrent = playerRight;
		
		hitbox = new Rectangle(x, y, 28, 20); 
	}

	public void render(Graphics g){
		if(velX != 0 || velY != 0){ //if moving, draw animated sprites
			playerCurrent.draw(g, x, y);
		}
		else{ //draw still images if not moving, with appropriate direction
			if(facingRight){
				g.drawSprite(right, x, y);
			}
			else if(facingLeft){
				g.drawSprite(playerLeft.getFrame(0), x, y);
			}
		}
		//g.setColor(Color.GREEN);
		//g.drawRect(x, y, hitbox.width, hitbox.height);
	}

	public void update(float delta){
		playerMovement();
		updateSprite(delta);

		if(velX != 0 || velY != 0){
			if(!walking){
				walking = true;
				//TODO resume music
			}
		}
		else{
			if(walking){
				walking = false;
				//TODO pause music
			}
		}

		//Stop x-movement if not pressing LEFT nor RIGHT
		if(!Gdx.input.isKeyPressed(this.LEFT) && !Gdx.input.isKeyPressed(this.RIGHT)){
			velX = 0;
		}
		//Stop y-movement if not pressing UP nor DOWN
		if(!Gdx.input.isKeyPressed(this.UP) && !Gdx.input.isKeyPressed(this.DOWN)){
			velY = 0;
		}

		hitbox.setX(this.x);
		hitbox.setY(this.y);
	}
	
	public void updateSprite(float delta){
		//change the direction the player is facing
		if(facingRight){
			playerCurrent = playerRight;
		}
		else{
			playerCurrent = playerLeft;
		}
		//check if the player is moving or not to set up static or animated frames
		if(velX != 0 || velY != 0){
			playerLeft.setLooping(true);
			playerRight.setLooping(true);
		}
		else{
			playerLeft.setLooping(false);
			playerRight.setLooping(false);
		}
		playerCurrent.update(delta);
	}

	public void playerMovement(){
		//Move Left
		if(Gdx.input.isKeyPressed(this.LEFT)){
			velX = -moveSpeedX;
			facingLeft = true;
			facingRight = false;
		}
		//Move Right
		if(Gdx.input.isKeyPressed(this.RIGHT)){
			velX = moveSpeedX;
			facingLeft = false;
			facingRight = true;
		}
		//Move Up
		if(Gdx.input.isKeyPressed(this.UP)){
			velY = -moveSpeedY;
		}
		//Move Down
		if(Gdx.input.isKeyPressed(this.DOWN)){
			velY = moveSpeedY;
		}
		move();
	}

	/*
	 * Checks if there is a collision if the player was at the given position.
	 */
	public boolean isColliding(Rectangle other, float x, float y){
		if(other == this.hitbox){ //Make sure solid isn't stuck on itself
			return false;
		}
		if(x < other.x + other.width && x + hitbox.width > other.x && y < other.y + other.height && y + hitbox.height > other.y){
			return true;
		}
		return false;
	}

	/*
	 * Helper method for checking whether there is a collision if the player moves at the given position
	 */
	public boolean collisionExistsAt(float x, float y){
		for(int i = 0; i < level.solids.size(); i++){
			Rectangle solid = level.solids.get(i);
			if(isColliding(solid, x, y)){
				return true;
			}
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
		for(int i = 0; i < level.solids.size(); i++){
			Rectangle solid = level.solids.get(i);
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
		for(int i = 0; i < level.solids.size(); i++){
			Rectangle solid = level.solids.get(i);
			if(isColliding(solid, x, y + velY)){
				while(!isColliding(solid, x, y + Math.signum(velY))){
					y += Math.signum(velY);
				}
				velY = 0;
			}
		}
		y += velY;
	}

	public void attack(){
		spraySound.play();
		if(facingLeft){
			Projectile spray = new Projectile(this.x - sprayOffsetX, this.y - sprayOffsetY, -1, level);
			level.projectiles.add(spray);
		}
		else if(facingRight){
			Projectile spray = new Projectile(this.x + sprayOffsetX + 16, this.y - sprayOffsetY, 1, level);
			level.projectiles.add(spray);
		}
	}
	
	public void setSpriteOrigin(Sprite... s){
		for(int i = 0; i < s.length; i++){
			if(s != null){
				s[i].setOrigin(0, s[i].getHeight()*2-20);
			}
		}
	}

	//========================================Input Methods==============================================

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.SPACE){
			if(!level.paused && !level.gameOver){
				attack();
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}