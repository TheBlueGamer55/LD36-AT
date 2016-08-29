package com.swinestudios.ancienttech;

import java.util.Random;

import org.mini2Dx.core.graphics.Graphics;

public class BugSpawner{

	public boolean isActive;
	
	public final float minSpawnX = 16, maxSpawnX = 600; 
	
	public float spawnTimer;
	public float maxSpawnTimer;
	public final float initialSpawnTimer = 0.75f; //Starting difficulty
	
	/*
	public float difficultyTimer;
	public final float maxDifficultyTimer = 10f; //Duration of current difficulty 
	public final float difficultyRate = 1f; //How much faster each difficulty gets
	*/

	public Gameplay level;
	public String type;
	
	public Random random = new Random();

	public BugSpawner(Gameplay level){
		isActive = true;
		this.level = level;
		spawnTimer = 0;
		//difficultyTimer = 0;
		maxSpawnTimer = initialSpawnTimer; 
		type = "BugSpawner";
	}

	public void render(Graphics g){
		//Empty
	}

	public void update(float delta){
		if(isActive){
			if(spawnTimer <= maxSpawnTimer){
				spawnTimer += delta;
				if(spawnTimer > maxSpawnTimer){
					spawnTimer = 0;
					spawnBug();
				}
			}
			/*
			if(difficultyTimer < maxDifficultyTimer){
				difficultyTimer += delta;
				if(difficultyTimer > maxDifficultyTimer){
					difficultyTimer = 0;
					if(maxSpawnTimer - difficultyRate > spawnTimer){
						maxSpawnTimer -= difficultyRate;
					}
				}
			}
			*/
		}
	}
	
	public void spawnBug(){
		float spawnY = 400;
		float spawnX = random.nextInt((int) ((maxSpawnX - minSpawnX) + 1)) + minSpawnX;
		
		Bug bug = new Bug(spawnX, spawnY, level);
		level.bugs.add(bug);
	}

}
