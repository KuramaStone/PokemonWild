package me.brook.PokemonCreator.input.painting;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.input.InputHandler;
import me.brook.PokemonCreator.input.PokeMaker.DrawingMode;
import me.brook.PokemonCreator.world.area.PokeArea;
import me.brook.PokemonCreator.world.tile.StructureData;
import me.brook.PokemonCreator.world.tile.Tile;
import me.brook.PokemonCreator.world.tile.data.TileData;

public class PencilTool extends PaintTool {

	// This is to ensure no repetition of adding tiles to a point per click
	private List<Point> tilesAdded, tilesRemoved;

	// This is for easy removal of a structure by just clicking it
	private Map<Rectangle, StructureData> structuresAdded;

	private boolean canAddStructure = true;

	public PencilTool(PokemonCreator creator) {
		super(creator);
		tilesAdded = new ArrayList<>();
		tilesRemoved = new ArrayList<>();

		structuresAdded = new HashMap<>();
	}

	@Override
	public void draw(Graphics2D g) {
		if(maker.getDrawingMode() == DrawingMode.STRUCTURE) {
			StructureData structureData = maker.getCurrentStructureData();

			if(structureData != null) {
				Point mouse = drawer.getMousePosition();

				if(mouse != null) {
					mouse = drawer.getTileLocationAt(mouse);

					int width = structureData.getType().getStructureWidth();
					int height = structureData.getType().getStructureHeight();
					width *= drawer.getTileSize();
					height *= drawer.getTileSize();

					int tx = (mouse.x + drawer.getxOffset()) * drawer.getTileSize();
					int ty = (mouse.y + drawer.getyOffset()) * drawer.getTileSize();

					g.setColor(Color.BLUE);
					g.drawRect(tx, ty - height + drawer.getTileSize(), width, height);

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

				if(maker.getDrawingMode() == DrawingMode.TILE && currentTileData != null) {
					currentArea.add(new Tile(tile.x, tile.y, currentTileData));
					// To prevent accidentally adding another tile to the same location during a
					// click and drag,
					// we create a list of tile points that have been made since then.
					tilesAdded.add(tile);
				}
				else if(maker.getDrawingMode() == DrawingMode.STRUCTURE && structureData != null &&
						canAddStructure) {
					int w = structureData.getType().getStructureWidth() + 1;
					int h = structureData.getType().getStructureHeight() + 1;
					tile.y -= h - 2;
					Rectangle rect = new Rectangle(tile.x, tile.y, w, h);

					structureData.addTo(currentArea, tile);
					structuresAdded.put(rect, structureData);

					canAddStructure = false;
				}
			}
		}
		else {
			canAddStructure = true;
			tilesAdded.clear();

			if(input.isRightMousePressed()) {
				if(maker.getDrawingMode() == DrawingMode.TILE) {
					if(!tilesRemoved.contains(tile)) {

						// Remove highest tile
						Tile highest = null;
						for(Tile t : currentArea.getTilesAt(tile)) {
							if(highest == null) {
								highest = t;
								continue;
							}

							if(t.getLayer() > highest.getLayer()) {
								highest = t;
							}
						}

						currentArea.remove(highest);
						tilesRemoved.add(tile);
					}

				}
				else if(maker.getDrawingMode() == DrawingMode.STRUCTURE) {
						Rectangle added = doesStructuresContain(tile);
						if(added != null) {
							StructureData struct = structuresAdded.get(added);
							struct.removeFrom(currentArea, new Point(added.x, added.y));
					}

				}

			}
			else {
				tilesRemoved.clear();
			}
		}
	}

	private Rectangle doesStructuresContain(Point tile) {
		for(Rectangle rect : structuresAdded.keySet()) {
			if(rect.contains(tile)) {
				return rect;
			}
		}

		return null;
	}

}
