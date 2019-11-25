package me.brook.PokemonCreator.world.tile;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import me.brook.PokemonCreator.world.area.PokeArea;
import me.brook.PokemonCreator.world.tile.data.TileData;

public class StructureData {

	private TileType type;
	private List<TileData> tiles;

	public StructureData(TileType type) {
		if(!type.isStructure()) {
			throw new IllegalArgumentException("TileType '" + type + "' is not a structure.");
		}
		this.type = type;

		tiles = new ArrayList<>();
		for(int i = 0; i < type.getImages().length; i++) {
			tiles.add(new TileData(type, i));
		}

	}

	public List<TileData> getTileData() {
		return tiles;
	}

	public TileType getType() {
		return type;
	}

	public void addTo(PokeArea area, Point origin) {
		int width = this.getType().getStructureWidth();

		for(int i = 0; i < this.getTileData().size(); i++) {
			TileData data = this.getTileData().get(i);
			int x = (i % width) + origin.x;
			int y = (i / width) + origin.y;
			Tile t = new Tile(x, y, data);

			area.add(t);
		}
	}

	public void removeFrom(PokeArea area, Point origin) {
		int width = this.getType().getStructureWidth();

		for(int i = 0; i < this.getTileData().size(); i++) {
			int x = (i % width) + origin.x;
			int y = (i / width) + origin.y;
			List<Tile> tiles = area.getTilesAt(new Point(x, y));

			TileData data = getTileData().get(i);
			for(Tile tile : tiles) {
				if(tile.getData().equals(data)) {
					area.remove(tile);
				}
			}
		}
	}

}
