package com.swinestudios.ancienttech;

import java.util.ArrayList;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeInTransition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

public class Gameplay implements GameScreen{

	public static int ID = 2;

	public boolean paused = false;

	public ArrayList<Block> solids;
	public ArrayList<Projectile> projectiles;
	public ArrayList<BugBlock> bugBlocks;
	public ArrayList<Bug> bugs;
	public BugSpawner spawner;

	public Player player;

	@Override
	public int getId(){
		return ID;
	}

	@Override
	public void initialise(GameContainer gc){
		solids = new ArrayList<Block>();
		bugBlocks = new ArrayList<BugBlock>();
		
		//Spawn solids
		solids.add(new Block(10, 10, 16, 64, this));
		solids.add(new Block(64, 10, 32, 32, this));
		
		//Spawn bug blocks
		bugBlocks.add(new BugBlock(40, 60, 320, 8, this));
	}

	@Override
	public void postTransitionIn(Transition t){

	}

	@Override
	public void postTransitionOut(Transition t){
		paused = false;
	}

	@Override
	public void preTransitionIn(Transition t){
		paused = false;
		projectiles = new ArrayList<Projectile>();
		bugs = new ArrayList<Bug>();
		spawner = new BugSpawner(this);
		
		player = new Player(16, 200, this);
		Gdx.input.setInputProcessor(player);
	}

	@Override
	public void preTransitionOut(Transition t){
		
	}

	@Override
	public void render(GameContainer gc, Graphics g){
		//Solids rendering TODO remove later
		for(int i = 0; i < solids.size(); i++){
			solids.get(i).render(g);
		}
		for(int i = 0; i < bugBlocks.size(); i++){
			bugBlocks.get(i).render(g);
		}

		g.drawString("Bugs: " + player.bugCount, 4, 4);
		player.render(g);
		renderBugs(g);
		renderProjectiles(g);

		if(paused){
			g.setColor(Color.RED);
			g.drawString("Are you sure you want to quit? Y or N", 220, 240);
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta){
		if(!paused){
			player.update(delta);
			updateBugs(delta);
			updateProjectiles(delta);
			spawner.update(delta);

			if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
				paused = true;
			}
		}
		else{
			if(paused){
				if(Gdx.input.isKeyJustPressed(Keys.Y)){
					sm.enterGameScreen(MainMenu.ID, new FadeOutTransition(), new FadeInTransition());
				}
				if(Gdx.input.isKeyJustPressed(Keys.N)){
					paused = false;
				}
			}
		}
	}
	
	public void renderBugs(Graphics g){
		for(int i = 0; i < bugs.size(); i++){
			bugs.get(i).render(g);
		}
	}

	public void updateBugs(float delta){
		for(int i = 0; i < bugs.size(); i++){
			bugs.get(i).update(delta);
		}
	}
	
	public void renderProjectiles(Graphics g){
		for(int i = 0; i < projectiles.size(); i++){
			projectiles.get(i).render(g);
		}
	}

	public void updateProjectiles(float delta){
		for(int i = 0; i < projectiles.size(); i++){
			projectiles.get(i).update(delta);
		}
}

	@Override
	public void interpolate(GameContainer gc, float delta){
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResize(int width, int height) {
	}

	@Override
	public void onResume() {
	}

}
