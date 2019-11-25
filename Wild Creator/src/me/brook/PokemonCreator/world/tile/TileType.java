package me.brook.PokemonCreator.world.tile;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.brook.PokemonCreator.toolbox.Settings;
import me.brook.PokemonCreator.toolbox.Tools;
import me.brook.PokemonCreator.world.yaml.ConfigReader;

public class TileType {

	private int id;
	private String name;

	private BufferedImage[] images;
	private int animationCycle; // The number of ticks until it shall go to the next animation frame.

	// Layer to render this tile
	private int layer;

	private boolean[] collidableVariants;
	private boolean isAnimated;
	private Rectangle collidingArea;
	private int width, height;
	private Point start, end;

	private boolean isStructure;
	private BufferedImage fullIcon;
	private int structureWidth, structureHeight;

	private int currentFrame = 0;
	private int ticks = 0;

	public static List<TileType> loadTileData(Settings settings) throws IOException {
		ConfigReader config = new ConfigReader(settings, "res\\tiles\\yml\\basic.yml");

		// Load 1x1
		BufferedImage sheet = Tools.readImage(settings, "res\\tiles\\" + config.get("textures.1x1.info.file"));

		int id = 0;
		List<TileType> tileTypes = new ArrayList<>();
		for(Object obj : config.getKeys("textures.1x1")) {
			String str = obj.toString();

			if(!str.equals("info")) {
				TileType type = new TileType();
				type.loadBasicTile(config, sheet, str, id++);
				tileTypes.add(type);
			}
		}

		// Load bulk tiles
//		sheet = Tools.readImage(settings, "res\\tiles\\" + config.get("textures.bulk.info.file"));
//		int size = 16;
//		for(int x = 0; x < sheet.getWidth(); x += size) {
//			for(int y = 0; y < sheet.getHeight(); y += size) {
//
//				TileType type = new TileType();
//				type.loadBulkTile(config, sheet.getSubimage(x, y, size, size), String.valueOf(id), id++, size);
//				tileTypes.add(type);
//			}
//		}

		for(Object obj : config.getKeys("textures.varied")) {
			String str = obj.toString();

			if(!str.equals("info")) {
				TileType type = new TileType();
				type.loadVariedTile(config, sheet, str, id++);
				tileTypes.add(type);
			}
		}

		sheet = Tools.readImage(settings, "res\\tiles\\" + config.get("textures.structures.info.file"));
		for(Object obj : config.getKeys("textures.structures")) {
			String str = obj.toString();

			if(!str.equals("info")) {
				TileType type = new TileType();
				type.loadStructureTiles(config, sheet, str, id++);
				tileTypes.add(type);
			}
		}

		return tileTypes;
	}

	private void loadBulkTile(ConfigReader config, BufferedImage image, String name, int id, int size) {
		this.id = id;
		this.name = name;

		images = new BufferedImage[0];
		images[0] = image;

		isStructure = false;
		layer = 0;
		animationCycle = 0;

		width = 1;
		height = 1;

		collidableVariants = new boolean[1];
		for(int j = 0; j < collidableVariants.length; j++) {
			collidableVariants[j] = true;
		}

		collidingArea = new Rectangle(0, 0, width, height);
	}

	private void loadStructureTiles(ConfigReader config, BufferedImage sheet, String name, int id) {
		this.id = id;
		this.name = name;

		isStructure = true;
		config.setSection("textures.structures." + name);

		layer = config.getInt("layer");
		animationCycle = config.getInt("cycle");

		width = 1;
		height = 1;

		// Load the BufferedImage
		start = config.getPoint("location.start");
		end = config.getPoint("location.end");

		structureWidth = end.x - start.x + 1;
		structureHeight = end.y - start.y + 1;

		final int size = 16;
		int variants = (end.x - start.x + 1) * (end.y - start.y + 1);
		images = new BufferedImage[variants];
		int i = 0;
		fullIcon = sheet.getSubimage(start.x * size, start.y * size, (end.x - start.x + 1) * size,
				(end.y - start.y + 1) * size);
		for(int y = start.y; y <= end.y; y++) {
			for(int x = start.x; x <= end.x; x++) {
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

		boolean isCollidable = config.getBoolean("collisions.collidable");
		collidableVariants = new boolean[variants];
		if(isCollidable) {
			for(int j = 0; j < collidableVariants.length; j++) {
				collidableVariants[j] = true;
			}
			for(int j : config.getIntList("collisions.nonCollidableTiles")) {
				collidableVariants[j] = false;
			}

			collidingArea = new Rectangle(0, 0, 1, 1);
		}

		config.setSection("");
	}

	private void loadVariedTile(ConfigReader config, BufferedImage sheet, String name, int id) {
		this.id = id;
		this.name = name;

		config.setSection("textures.varied." + name);

		isAnimated = config.getBoolean("animated");

		layer = config.getInt("layer");
		int variants = config.getInt("variants");
		images = new BufferedImage[variants];
		animationCycle = config.getInt("cycle");

		width = config.getInt("size.width");
		height = config.getInt("size.height");

		boolean isCollidable = config.getBoolean("collisions.collidable");
		collidableVariants = new boolean[variants];
		if(isCollidable) {
			for(int j = 0; j < collidableVariants.length; j++) {
				collidableVariants[j] = true;
			}
			collidingArea = new Rectangle(0, 0, config.getInt("collisions.width"), config.getInt("collisions.height"));
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

		config.setSection("");
	}

	private void loadBasicTile(ConfigReader config, BufferedImage sheet, String name, int id) {
		this.id = id;
		this.name = name;

		// We load the tile data now from the basic.yml
		int size = 16;
		config.setSection("textures.1x1." + name);
		width = 1;
		height = 1;

		int variants = config.getInt("variants");
		images = new BufferedImage[variants];

		layer = config.getInt("layer");
		boolean isCollidable = config.getBoolean("collidable");
		collidableVariants = new boolean[variants];
		if(isCollidable) {
			for(int j = 0; j < collidableVariants.length; j++) {
				collidableVariants[j] = true;
			}

			collidingArea = new Rectangle(0, 0, width, height);
		}
		isAnimated = config.getBoolean("animated");

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
			for(int y = start.y; y <= end.y; y++) {
				for(int x = start.x; x <= end.x; x++) {
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

	public boolean isCollidable(int variant) {
		return collidableVariants[variant];
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

	public boolean isStructure() {
		return isStructure;
	}

	public BufferedImage getFullIcon() {
		return fullIcon;
	}

	public Point getStart() {
		return start;
	}

	public Point getEnd() {
		return end;
	}

	public int getStructureWidth() {
		return structureWidth;
	}

	public int getStructureHeight() {
		return structureHeight;
	}

	public String getName() {
		return name;
	}

}
