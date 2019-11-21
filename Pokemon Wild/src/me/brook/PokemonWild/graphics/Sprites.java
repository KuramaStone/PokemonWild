package me.brook.PokemonWild.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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

		try {
			BufferedImage image = ImageIO.read(new File("res\\sprites\\PlayerM.png"));
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
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage[][] getPlayerWalk() {
		return playerWalk;
	}

	public BufferedImage[][] getPlayerRun() {
		return playerRun;
	}

}