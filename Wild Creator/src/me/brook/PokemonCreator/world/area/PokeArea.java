package me.brook.PokemonCreator.world.area;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import me.brook.PokemonCreator.toolbox.Tools;
import me.brook.PokemonCreator.world.tile.Tile;

/*
 * This is a single area alone. For multiple routes joining, look at PokeWorld
 */
public class PokeArea {

	public String areaName;
	public List<Tile> tiles;

	/*
	 * This is a randomly generated color to get a unique identification for showing
	 * the surface area of an area. It is created by taking the area name and
	 * turning it into a unique int, and using that as a seed for the random.
	 */
	private Color color, transparentColor;

	/*
	 * The corners of all tiles on the extremities to get a box that contains all
	 * tiles.
	 */
	private Rectangle surface;

	public PokeArea(String name, List<Tile> tiles) {
		this.areaName = name;
		this.tiles = tiles;

		long seed = 0;
		for(char c : name.toCharArray()) {
			seed += c * 852377;
		}
		color = Tools.getUniqueColor(seed);
		transparentColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 50);

		recalculateSurface();
	}

	public PokeArea() {
	}

	public void construct() {
		long seed = 0;
		for(char c : areaName.toCharArray()) {
			seed += c * 852377;
		}
		color = Tools.getUniqueColor(seed);
		transparentColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 50);

		recalculateSurface();
	}

	public void recalculateSurface() {
		if(tiles.isEmpty()) {
			this.surface = null;
			return;
		}

		Rectangle area = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

		// Get the four corners and put them on the Rectangle
		for(Tile t : tiles) {
			if(t.getX() < area.x) {
				area.x = t.getX();
			}
			if(t.getX() > area.width) {
				area.width = t.getX();
			}

			if(t.getY() < area.y) {
				area.y = t.getY();
			}
			if(t.getY() + 1 > area.height) {
				area.height = t.getY();
			}
		}

		this.surface = area;
	}

	public String getAreaName() {
		return areaName;
	}

	public Rectangle getAreaSurface() {
		return surface;
	}

	public void add(Tile tile) {
		tiles.add(tile);
		recalculateSurface();
		sort();
	}

	private void sort() {
		tiles.sort(SORTER);
	}

	public boolean remove(Tile tile) {
		boolean boo = tiles.remove(tile);
		recalculateSurface();

		return boo;
	}

	public Tile remove(int index) {
		Tile t = tiles.remove(index);
		recalculateSurface();

		return t;
	}

	public List<Tile> getTilesAt(Point p) {
		List<Tile> list = new ArrayList<>();
		for(Tile tile : tiles) {
			if(tile.isLocationAt(p)) {
				list.add(tile);
			}
		}

		return list;
	}

	public Color getColor() {
		return color;
	}

	public Color getTransparentColor() {
		return transparentColor;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	@Override
	public String toString() {
		return areaName;
	}

	public static Comparator<Tile> SORTER = new Comparator<Tile>() {

		@Override
		public int compare(Tile t1, Tile t2) {

			int x = t1.x - t2.x;
			int y = t1.y - t2.y;
			int layer = t1.getLayer() - t2.getLayer();

			if(y == 0) {

				if(x == 0) {
					return layer;
				}
				else {
					return x;
				}

			}
			else {
				return y;
			}
		}

	};

}
