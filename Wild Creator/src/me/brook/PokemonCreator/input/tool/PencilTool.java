package me.brook.PokemonCreator.input.tool;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.input.InputHandler;
import me.brook.PokemonCreator.input.PokeMaker.DrawingMode;
import me.brook.PokemonCreator.world.area.PokeArea;
import me.brook.PokemonCreator.world.tile.StructureData;
import me.brook.PokemonCreator.world.tile.Tile;
import me.brook.PokemonCreator.world.tile.TileData;

public class PencilTool extends PaintTool {

	private List<Point> tilesAdded;

	private boolean hasRemovalBeenPerformed;
	private boolean canAddStructure = true;

	public PencilTool(PokemonCreator creator) {
		super(creator);
		tilesAdded = new ArrayList<>();
	}

	@Override
	public void draw(Graphics2D g) {
		if(maker.getDrawingMode() == DrawingMode.GROUP) {
			StructureData structureData = maker.getCurrentStructureData();

			if(structureData != null) {
				Point mouse = drawer.getMousePosition();

				if(mouse != null) {
					mouse = drawer.getTileLocationAt(mouse);

					int width = structureData.getType().getStructureWidth();
					int height = structureData.getType().getStructureHeight();
					width *= drawer.getTileSize();
					height *= drawer.getTileSize();

					int tx = mouse.x * drawer.getTileSize();
					int ty = mouse.y * drawer.getTileSize();

					g.setColor(Color.BLUE);
					g.drawRect(tx, ty, width, height);

				}
			}
		}
	}

	@Override
	public void handleInput(InputHandler input, Point mouse) {

		PokeArea currentArea = maker.getCurrentArea();

		if(currentArea == null) {
			return;
		}
		TileData currentTileData = maker.getCurrentTileData();
		StructureData structureData = maker.getCurrentStructureData();

		Point tile = drawer.getTileLocationAt(mouse);

		// Add tiles during left click
		if(input.isLeftMousePressed()) {

			if(!tilesAdded.contains(tile)) {

				if(maker.getDrawingMode() == DrawingMode.SINGLE && currentTileData != null) {
					currentArea.add(new Tile(tile.x, tile.y, currentTileData));
					// To prevent accidentally adding another tile to the same location during a
					// click and drag,
					// we create a list of tile points that have been made since then.
					tilesAdded.add(tile);
				}
				else if(maker.getDrawingMode() == DrawingMode.GROUP && structureData != null &&
						canAddStructure) {

					structureData.addTo(currentArea, tile);

					canAddStructure = false;
				}
			}
		}
		else {
			canAddStructure = true;
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
			}
		}
	}

}
