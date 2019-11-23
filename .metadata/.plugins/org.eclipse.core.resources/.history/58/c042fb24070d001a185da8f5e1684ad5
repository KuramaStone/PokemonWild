package me.brook.PokemonCreator.input.tool;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.input.InputHandler;
import me.brook.PokemonCreator.world.area.PokeArea;
import me.brook.PokemonCreator.world.tile.Tile;
import me.brook.PokemonCreator.world.tile.TileData;

public class PencilTool extends PaintTool {

	private List<Point> tilesAdded;

	private boolean hasRemovalBeenPerformed;

	public PencilTool(PokemonCreator creator) {
		super(creator);
		tilesAdded = new ArrayList<>();
	}

	@Override
	public void handleInput(InputHandler input) {

		Point mouse = drawer.getMousePosition();
		if(mouse == null) {
			return;
		}

		PokeArea currentArea = maker.getCurrentArea();
		TileData currentTileData = maker.getCurrentTileData();
		
		if(currentArea == null || currentTileData == null) {
			return;
		}

		Point tile = drawer.getTileLocationAt(mouse);

		// Add tiles during left click
		if(input.isLeftMousePressed()) {

			if(!tilesAdded.contains(tile)) {
				currentArea.add(new Tile(tile.x, tile.y, currentTileData));
				// To prevent accidentally adding another tile to the same location during a
				// click and drag,
				// we create a list of tile points that have been made since then.
				tilesAdded.add(tile);
			}
		}
		else {
			tilesAdded.clear();

			if(input.isRightMousePressed()) {
				if(!hasRemovalBeenPerformed) {
					List<Tile> tiles = currentArea.getTiles();
					// Remove highest tile
					for(int i = tiles.size() - 1; i >= 0; i--) {
						Tile t = tiles.get(i);
						if(t.isLocationAt(tile)) {
							currentArea.remove(t);
							break;
						}
					}
					hasRemovalBeenPerformed = true;
				}

			}
			else {
				hasRemovalBeenPerformed = false;

				if(input.isMiddleMouseReleased()) {
					List<Tile> tiles = currentArea.getTiles();
					for(int i = tiles.size() - 1; i >= 0; i--) {
						Tile t = tiles.get(i);
						if(t.isLocationAt(tile)) {
							// This is so that the player may get the tile data one tile beneath the top
							if(currentTileData.equals(t.getData())) {
								continue;
							}
							else {
								currentTileData = t.getData();
								maker.getTileScroller().unSelectOtherTiles(currentTileData);
								break;
							}
						}
					}
				}
			}
		}
	}

}
