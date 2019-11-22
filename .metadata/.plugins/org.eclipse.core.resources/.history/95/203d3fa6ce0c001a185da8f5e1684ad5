package me.brook.PokemonWild.world.tile;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.brook.PokemonWild.yaml.Configuration;

public enum TileType {

	ERROR, GRASS, TALL_GRASS, flower;

	private static int idCount = 0;

	private int id;
	private BufferedImage[] images;

	private boolean isCollidable, isAnimated;

	public static void loadTileData() throws IOException {
		Configuration config = new Configuration(new File("res\\tiles\\yml\\basic.yml"));
		BufferedImage sheet = ImageIO.read(new File("res\\tiles\\basic.png"));
		for(TileType type : TileType.values()) {
			type.loadTile(config, sheet);
		}
		
	}

	private void loadTile(Configuration config, BufferedImage sheet) {
		// We load the tile data now from the basic.yml
		String name = this.toString().toLowerCase();
		int size = config.getInt("textures.info.size");
		config.setSection("textures.1x1." + name);
		
		isCollidable = config.getBoolean("collidable");
		isAnimated = config.getBoolean("animated");
		
		id = idCount++;
		int variants = config.getInt("variants");
		images = new BufferedImage[variants];

		// Load the variants using these coordinates
		Point start = config.getPoint("location.start");
		for(int i = 0; i < variants; i++) { // Additional pieces are in the x-axis
			int tx = (i + start.x) * size;
			int ty = (start.y) * size;
			
			try {
			images[i] = sheet.getSubimage(tx, ty, size, size);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int getID() {
		return id;
	}

	public boolean isCollidable() {
		return isCollidable;
	}
	
	public boolean isAnimated() {
		return isAnimated;
	}

}
