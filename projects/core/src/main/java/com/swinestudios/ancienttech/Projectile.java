package com.swinestudios.ancienttech;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import com.badlogic.gdx.graphics.Color;

public class Projectile{

	public float x, y, velX;
	public final float maxSpeedX = 0.25f;

	public boolean isActive;
	
	public float lifeTimer;
	public final float maxLifeTimer = 0.1f;

	public Rectangle hitbox;
	public Gameplay level;
	public String type;
	public Sprite projectileSprite;

	public Projectile(float x, float y, float velX, Gameplay level){
		this.x = x;
		this.y = y;
		this.velX = velX;
		if(Math.abs(velX) > maxSpeedX){
			velX = Math.signum(velX) * maxSpeedX;
		}
		isActive = true;
		this.level = level;
		type = "Projectile";
		//projectileSprite = new Sprite(new Texture(Gdx.files.internal("______.png")));
		//adjustSprite(projectileSprite);
		hitbox = new Rectangle(x, y, 16, 16); //Temporary
	}

	public void render(Graphics g){
		if(isActive){
			if(projectileSprite != null){
				g.drawSprite(projectileSprite, x, y);
			}
			else{ //Temporary shape placeholder
				g.setColor(Color.WHITE);
				g.fillRect(x, y, hitbox.width, hitbox.height);
			}
		}
	}

	public void update(float delta){
		if(isActive){
			x += velX;
			
			if(lifeTimer < maxLifeTimer){
				lifeTimer += delta;
				if(lifeTimer > maxLifeTimer){
					//lifeTimer = 0;
					level.projectiles.remove(this);
				}
			}

			hitbox.setX(x);
			hitbox.setY(y);
		}
	}

}
