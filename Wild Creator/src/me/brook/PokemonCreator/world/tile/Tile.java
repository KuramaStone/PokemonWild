package me.brook.PokemonCreator.world.tile;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Tile {
	
	private int x, y;
	private int width, height;
	
	private TileData data;

	public Tile(int x, int y, TileData data) {
		this.x = x;
		this.y = y;
		this.width = data.getType().getTileWidth();
		this.height = data.getType().getTileHeight();
		this.data = data;
	}

	public Tile(int x, int y, TileType type, int variant) {
		this(x, y, new TileData(type, variant));
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public TileType getType() {
		return data.getType();
	}

	public BufferedImage getCurrentImage() {
		return data.getCurrentImage();
	}
	
	public TileData getData() {
		return data;
	}

	public boolean isLocationAt(Point p) {
		return p.x == x && p.y == y;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

}