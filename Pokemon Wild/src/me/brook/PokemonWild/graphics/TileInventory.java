package me.brook.PokemonWild.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class TileInventory {

	private static final String path = "C:\\Users\\Stone\\workspace\\Pokemon Wild\\res\\tiles";

	private Map<Integer, BufferedImage> inventory;

	public TileInventory() {
		inventory = new HashMap<>();
	}
	
	public BufferedImage getTile(int tileID) {
		return inventory.get(tileID);
	}
	
	public Map<Integer, BufferedImage> getInventory() {
		return inventory;
	}

	/*
	 * Load each fileset and assign an id to each one.
	 */
	public void loadTiles() {
		File parent = new File(path);

		int tileID = 0;
		for(File child : parent.listFiles()) {
			if(!child.getName().endsWith(".png")) {
				continue;
			}
			
			try {
				BufferedImage image = ImageIO.read(child);

				if(image.getWidth() % 16 != 0) {
					System.err.println(String.format("File %s's width is not divisible by 16", child.getName()));
					continue;
				}
				else if(image.getHeight() % 16 != 0) {
					System.err.println(String.format("File %s's height is not divisible by 16", child.getName()));
					continue;
				}

				for(int y = 0; y < image.getHeight(); y += 16) {
					for(int x = 0; x < image.getWidth(); x += 16) {
						
						BufferedImage tile = image.getSubimage(x, y, 16, 16);
						
						inventory.put(tileID++, tile);
					}
				}
				
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
			
		}

	}

}
