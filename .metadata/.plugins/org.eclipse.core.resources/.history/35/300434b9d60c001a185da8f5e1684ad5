package me.brook.PokemonCreator.world.area;

import java.awt.Point;
import java.util.List;

import me.brook.PokemonCreator.world.tile.Tile;

/*
 * This is a single area alone. For multiple routes joining, look at PokeWorld
 */
public class PokeArea {

	private int offsetX, offsetY;
	private List<Tile> tiles;

	public PokeArea(int offsetX, int offsetY, List<Tile>tiles) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.tiles = tiles;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public void addTile(Tile tile) {
		tiles.add(tile);
	}

	public Tile getTileAt(Point p) {
		for(Tile tile : tiles) {
			if(tile.isLocationAt(p)) {
				return tile;
			}
		}
		
		return null;
	}

}
