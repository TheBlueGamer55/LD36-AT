package com.swinestudios.ancienttech;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.core.screen.transition.NullTransition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MainMenu implements GameScreen{

	//public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";

	public BitmapFont font;

	public static int ID = 1;

	@Override
	public int getId(){
		return ID;
	}

	@Override
	public void initialise(GameContainer gc){
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Pixel-UniCode.ttf"));
		//font = generator.generateFont(24, MainMenu.FONT_CHARACTERS, true);
		//generator.dispose();

		//Generate a font object for font.ttf at size 40px
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 48;
		parameter.flip = true;

		//The following settings allow the font to scale smoothly
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("m5x7.ttf"));

		font = generator.generateFont(parameter);
		font.setUseIntegerPositions(false);
	}

	@Override
	public void postTransitionIn(Transition t){

	}

	@Override
	public void postTransitionOut(Transition t){

	}

	@Override
	public void preTransitionIn(Transition t){

	}

	@Override
	public void preTransitionOut(Transition t){

	}

	@Override
	public void render(GameContainer gc, Graphics g){
		g.setFont(font);
		//Score UI
		g.setColor(Color.WHITE);
		g.drawString("Score", 16, 9);
		//g.drawString("123456789", 15, 43);
		g.drawString(String.format("%09d", Gameplay.score), 15, 43);
		//g.drawString("123456789", 456, 43); 
		g.drawString(String.format("%09d", Gameplay.maxScore), 458, 43);
		g.setColor(Color.RED);
		g.drawString("High Score", 458, 9);
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta){		
		if(Gdx.input.isKeyJustPressed(Keys.ENTER)){
			sm.enterGameScreen(Gameplay.ID, new FadeOutTransition(), new NullTransition());
		}
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			Gdx.app.exit();
		}
	}

	@Override
	public void interpolate(GameContainer gc, float delta){
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResize(int width, int height){
	}

	@Override
	public void onResume() {
	}

}
