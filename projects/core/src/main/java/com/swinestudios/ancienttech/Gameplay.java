package com.swinestudios.ancienttech;

import java.util.ArrayList;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.core.screen.transition.NullTransition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Gameplay implements GameScreen{

	public static int ID = 2;

	public static int score; 
	public static int maxScore = 0;

	public boolean paused = false;
	public boolean gameOver = false;

	//Health for computer
	public float health;
	public final float healthX = 20, healthY = 480 - 46;
	public final float maxHealth = 5000; //TODO adjust later
	public final float healthBarMaxWidth = 600;
	public final float healthBarHeight = 12;

	public ArrayList<Block> solids;
	public ArrayList<Projectile> projectiles;
	public ArrayList<BugBlock> bugBlocks;
	public ArrayList<Bug> bugs;
	public BugSpawner spawner;

	public Sprite background;
	public Sprite pauseMessage, gameOverMessage;

	public Player player;

	public static Sound pauseSound = Gdx.audio.newSound(Gdx.files.internal("pause2.wav"));
	public static Music bugsBGM = Gdx.audio.newMusic(Gdx.files.internal("bugsBGM.wav"));

	@Override
	public int getId(){
		return ID;
	}

	@Override
	public void initialise(GameContainer gc){
		solids = new ArrayList<Block>();
		bugBlocks = new ArrayList<BugBlock>();

		//Spawn solids
		solids.add(new Block(0, 387, 640, 8, this)); //Bottom border
		solids.add(new Block(0, 298, 640, 8, this)); //Top border
		solids.add(new Block(0, 0, 8, 480, this)); //Left border
		solids.add(new Block(632, 0, 8, 480, this)); //Right border

		//Spawn bug blocks
		bugBlocks.add(new BugBlock(40, 200, 500, 8, this));

		pauseMessage = new Sprite(new Texture(Gdx.files.internal("pause_quit_text.png")));
		pauseMessage.scale(1);
		gameOverMessage = new Sprite(new Texture(Gdx.files.internal("game_over_text.png")));
		gameOverMessage.scale(1);

		background = new Sprite(new Texture(Gdx.files.internal("lab_bg.png")));
		background.setOrigin(0, 0);
		background.scale(3);
	}

	@Override
	public void postTransitionIn(Transition t){

	}

	@Override
	public void postTransitionOut(Transition t){
		paused = false;
		gameOver = false;
		score = 0;
		bugsBGM.setLooping(false);
		bugsBGM.stop();
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void preTransitionIn(Transition t){
		health = maxHealth;

		paused = false;
		gameOver = false;
		score = 0;
		projectiles = new ArrayList<Projectile>();
		bugs = new ArrayList<Bug>();
		spawner = new BugSpawner(this);

		player = new Player(280, 340, this);
		Gdx.input.setInputProcessor(player);
	}

	@Override
	public void preTransitionOut(Transition t){

	}

	@Override
	public void render(GameContainer gc, Graphics g){
		g.drawSprite(background, 0, 0);

		//Debug for placement
		//int mx = Gdx.input.getX();
		//int my = Gdx.input.getY();
		//System.out.println(mx + ", " + my);

		//Score UI
		g.setColor(Color.WHITE);
		g.drawString("Score", 16, 9);
		//g.drawString("123456789", 15, 43);
		g.drawString(String.format("%09d", score), 15, 43);
		//g.drawString("123456789", 456, 43); 
		g.drawString(String.format("%09d", maxScore), 458, 43);
		g.setColor(Color.RED);
		g.drawString("High Score", 458, 9);


		//Solids rendering TODO remove later
		for(int i = 0; i < solids.size(); i++){
			solids.get(i).render(g);
		}
		for(int i = 0; i < bugBlocks.size(); i++){
			bugBlocks.get(i).render(g);
		}

		player.render(g);
		renderBugs(g);
		renderProjectiles(g);

		//Draw health bar for computer
		g.setColor(Color.RED);
		g.fillRect(healthX, healthY, healthBarMaxWidth, healthBarHeight);
		g.setColor(Color.GREEN);
		g.fillRect(healthX, healthY, healthBarMaxWidth * (health / maxHealth), healthBarHeight);

		if(paused){
			//g.setColor(Color.RED);
			//g.drawString("Are you sure you want to quit? Y or N", 220, 240);
			g.drawSprite(pauseMessage, 236, 212);
		}
		if(gameOver){
			//g.setColor(Color.RED);
			//g.drawString("Game over! Press Escape to go back to the main menu", 160, 240);
			g.drawSprite(gameOverMessage, 260, 200);
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta){
		if(!paused && !gameOver){
			player.update(delta);
			updateBugs(delta);
			updateProjectiles(delta);
			spawner.update(delta);

			//Bug damage logic
			if(player.bugCount > 0){
				health -= 5;
				if(!bugsBGM.isLooping()){
					bugsBGM.setLooping(true);
					bugsBGM.play();
				}
			}
			else{
				if(health + 1 <= maxHealth){
					health++;
					bugsBGM.setLooping(false);
					bugsBGM.stop();
				}
			}
			if(health <= 0){
				gameOver = true;
				bugsBGM.setLooping(false);
				bugsBGM.stop();
			}

			//Update max score
			if(score > maxScore){
				maxScore = score;
			}

			if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
				if(bugsBGM.isPlaying()){
					bugsBGM.pause();
				}
				pauseSound.play(0.5f);
				paused = true;
			}
		}
		else{
			if(gameOver){
				if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
					sm.enterGameScreen(MainMenu.ID, new FadeOutTransition(), new NullTransition());
				}
			}
			else if(paused){
				if(Gdx.input.isKeyJustPressed(Keys.Y)){
					sm.enterGameScreen(MainMenu.ID, new FadeOutTransition(), new NullTransition());
				}
				if(Gdx.input.isKeyJustPressed(Keys.N)){
					paused = false;
					if(bugsBGM.isLooping()){ //If bugsBGM was the one that paused
						bugsBGM.play();
					}
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
