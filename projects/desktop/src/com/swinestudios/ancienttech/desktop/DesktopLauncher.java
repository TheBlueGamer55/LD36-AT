package com.swinestudios.ancienttech.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;

import com.swinestudios.ancienttech.AncientTechGame;

public class DesktopLauncher {
	public static void main(String[] arg){
		DesktopMini2DxConfig config = new DesktopMini2DxConfig(AncientTechGame.GAME_IDENTIFIER);
		config.vSyncEnabled = true;
		config.width = 640;
		config.height = 480;
		config.resizable = false; 
		config.foregroundFPS = 30;
		config.backgroundFPS = 30;
		config.title = "TO BE NAMED";
		new DesktopMini2DxGame(new AncientTechGame(), config);
	}
}
