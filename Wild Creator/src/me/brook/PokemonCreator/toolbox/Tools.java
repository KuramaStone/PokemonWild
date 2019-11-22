package me.brook.PokemonCreator.toolbox;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

public class Tools {

	public static Color getUniqueColor(long seed) {
		Random rnd = new Random(seed);
		float hue = rnd.nextFloat() * 360;
		float saturation = 0.5f + rnd.nextFloat() * 0.5f;
		
		return Color.getHSBColor(hue, saturation, 1.0f);
	}

	public static BufferedImage readImage(String path) {
		File file = new File("C:\\Users\\Stone\\PokemonWild\\Pokemon Wild\\" + path);
		
		try {
			return ImageIO.read(file);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
