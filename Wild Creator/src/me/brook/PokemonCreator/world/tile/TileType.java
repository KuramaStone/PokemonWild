package me.brook.PokemonCreator.world.tile;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import me.brook.PokemonCreator.toolbox.Tools;
import me.brook.PokemonCreator.world.yaml.ConfigReader;

public enum TileType {

	ERROR, GRASS, TALL_GRASS, RED_FLOWER, ORANGE_FLOWER, BLUE_FLOWER, YELLOW_FLOWER, SMALL_BUSH, LONG_BUSH, GRASS_PATH, FRAIL_ROCK,
	// Tall shit
	SHORT_TREE, TREE,
	// water
	WATER, WATER_STONE, WATER_EDGE_CORNER_LEFT, WATER_EDGE_CORNER_RIGHT, WATER_EDGE_LEFT, WATER_EDGE_RIGHT, WATER_EDGE_BOTTOM;

	private static int idCount = 0;

	private int id;
	private BufferedImage[] images;
	private int animationCycle; // The number of ticks until it shall go to the next animation frame.
	
	// Layer to render this tile
	private int layer;

	private boolean isCollidable, isAnimated;
	private Rectangle collidingArea;
	private int width, height;

	private int currentFrame = 0;
	private int ticks = 0;

	public static void loadTileData() throws IOException {
		ConfigReader config = new ConfigReader("res\\tiles\\yml\\basic.yml");

		// Load 1x1
		BufferedImage sheet = Tools.readImage("res\\tiles\\" + config.get("textures.1x1.info.file"));

		for(Object obj : config.getKeys("textures.1x1")) {
			String str = obj.toString();

			if(!str.equals("info")) {
				TileType.valueOf(str.toUpperCase()).loadBasicTile(config, sheet);
			}
		}

		for(Object obj : config.getKeys("textures.varied")) {
			String str = obj.toString();

			if(!str.equals("info")) {
				TileType.valueOf(str.toUpperCase()).loadVariedTile(config, sheet);
			}
		}

	}

	private void loadVariedTile(ConfigReader config, BufferedImage sheet) {
		id = idCount++;
		
		String name = this.toString().toLowerCase();
		config.setSection("textures.varied." + name);

		isAnimated = config.getBoolean("animated");

		layer = config.getInt("layer");
		int variants = config.getInt("variants");
		images = new BufferedImage[variants];
		animationCycle = config.getInt("cycle");

		width = config.getInt("size.width");
		height = config.getInt("size.height");

		isCollidable = config.getBoolean("collisions.collidable");
		if(isCollidable) {
			collidingArea = new Rectangle(0, 0, width, height);
		}

		// Load the BufferedImage
		Point start = config.getPoint("location.top");

		for(int i = 0; i < variants; i++) {
			int tx = (start.x + (i * width)) * 16;
			int ty = (start.y) * 16;
			try {
				images[i] = sheet.getSubimage(tx, ty, width * 16, height * 16);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void loadBasicTile(ConfigReader config, BufferedImage sheet) {
		// We load the tile data now from the basic.yml
		String name = this.toString().toLowerCase();
		int size = 16;
		config.setSection("textures.1x1." + name);
		width = 1;
		height = 1;

		layer = config.getInt("layer");
		isCollidable = config.getBoolean("collidable");
		if(isCollidable) {
			collidingArea = new Rectangle(0, 0, 1, 1);
		}
		isAnimated = config.getBoolean("animated");

		id = idCount++;
		int variants = config.getInt("variants");
		images = new BufferedImage[variants];
		animationCycle = config.getInt("cycle");

		// Load the variants using these coordinates
		Point start = config.getPoint("location.start");
		Point end = config.getPoint("location.end");

		if(end == null) {
			for(int i = 0; i < variants; i++) { // Additional pieces are in the x-axis
				int tx = (start.x + i) * 16;
				int ty = (start.y) * 16;
				try {
					images[i] = sheet.getSubimage(tx, ty, width * 16, height * 16);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		else {
			variants = (end.x - start.x + 1) * (end.y - start.y + 1);
			images = new BufferedImage[variants];
			int i = 0;
			for(int x = start.x; x <= end.x; x++) {
				for(int y = start.y; y <= end.y; y++) {
					int tx = x * size;
					int ty = y * size;

					try {
						BufferedImage image = sheet.getSubimage(tx, ty, size, size);

						if(isEmpty(image)) { // If tile is empty, then don't load it
							BufferedImage[] dest = new BufferedImage[images.length - 1];
							for(int j = 0; j < dest.length; j++) {
								dest[j] = images[j];
							}
							images = dest;
						}
						else {
							images[i++] = image;
						}

					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		config.setSection("");
	}

	private static boolean isEmpty(BufferedImage image) {
		for(int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				if(image.getRGB(x, y) != 0) { // -16777216 is the rgb value if the pixel is transparent
					return false;
				}
			}
		}
		return true;
	}

	public void tickAnimation() {
		if(!isAnimated) {
			currentFrame = 0;
			return;
		}

		int currentTransition = ticks / animationCycle;
		currentFrame = currentTransition;

		ticks++;
		ticks %= (animationCycle * images.length); // This is the total number of ticks it takes the animation to restart
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

	public BufferedImage[] getImages() {
		return images;
	}

	public BufferedImage getCurrentImage() {
		return images[currentFrame];
	}

	public Rectangle getCollidingArea() {
		return collidingArea;
	}

	public int getTileWidth() {
		return width;
	}

	public int getTileHeight() {
		return height;
	}
	
	public int getLayer() {
		return layer;
	}

}
