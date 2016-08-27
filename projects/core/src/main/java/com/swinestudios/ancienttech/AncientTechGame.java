package com.swinestudios.ancienttech;

import org.mini2Dx.core.game.ScreenBasedGame;

public class AncientTechGame extends ScreenBasedGame {
	
	public static final String GAME_IDENTIFIER = "com.swinestudios.ancienttech";

	@Override
	public void initialise() {
		this.addScreen(new MainMenu());
		this.addScreen(new Gameplay());
	}	

	@Override
	public int getInitialScreenId() {
		return MainMenu.ID;
	}
	
}