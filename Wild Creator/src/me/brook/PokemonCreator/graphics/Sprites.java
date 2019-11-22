package me.brook.PokemonCreator.graphics;

import java.awt.image.BufferedImage;

import me.brook.PokemonCreator.toolbox.Tools;

public class Sprites {

	/*
	 * [3]: 0:leftF, 1:normal, 2:rightF
	 * 
	 * 
	 * [4]: 0:down, 1:up, 2:left, 3:right
	 */
	private BufferedImage[][] playerWalk = new BufferedImage[3][4];
	private BufferedImage[][] playerRun = new BufferedImage[3][4];

	public Sprites() {
		loadPlayerSprites();
	}

	private void loadPlayerSprites() {

		BufferedImage image = Tools.readImage("res\\sprites\\PlayerM.png");
		for(int mode = 0; mode < 3; mode++) {
			for(int direction = 0; direction < 4; direction++) {
				playerWalk[mode][direction] = image.getSubimage(mode * 16, direction * 20, 16, 20);
			}
		}

		for(int mode = 3; mode < 6; mode++) {
			for(int direction = 0; direction < 4; direction++) {
				playerRun[mode - 3][direction] = image.getSubimage(mode * 16, direction * 20, 16, 20);
			}
		}
	}

	public BufferedImage[][] getPlayerWalk() {
		return playerWalk;
	}

	public BufferedImage[][] getPlayerRun() {
		return playerRun;
	}

}