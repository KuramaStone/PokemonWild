package me.brook.PokemonCreator.input.tool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.input.InputHandler;
import me.brook.PokemonCreator.world.area.PokeArea;
import me.brook.PokemonCreator.world.tile.Tile;

public class FillAreaTool extends PaintTool {

	private Point pressStart, pressEnd;

	// If should fill, add tiles. If not, then remove them.
	private boolean shouldFill;

	public FillAreaTool(PokemonCreator creator) {
		super(creator);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		if(pressStart != null && pressEnd != null) {
			Point start = new Point(), end = new Point();

			if(pressStart.x < pressEnd.x) {
				start.x = pressStart.x;
				end.x = pressEnd.x;
			}
			else {
				start.x = pressEnd.x;
				end.x = pressStart.x;
			}

			if(pressStart.y < pressEnd.y) {
				start.y = pressStart.y;
				end.y = pressEnd.y;
			}
			else {
				start.y = pressEnd.y;
				end.y = pressStart.y;
			}

			// Add padding to end to get bottom right of tile
			end.x++;
			end.y++;

			start.x *= drawer.getTileSize();
			start.y *= drawer.getTileSize();
			end.x *= drawer.getTileSize();
			end.y *= drawer.getTileSize();

			g.drawRect(start.x, start.y, end.x - start.x, end.y - start.y);
		}
	}

	@Override
	public void handleInput(InputHandler input) {

		Point mouse = drawer.getMousePosition();

		if(mouse == null) {
			return;
		}
		
		
		if(maker.getCurrentArea() == null) {
			return;
		}

		pressEnd = drawer.getTileLocationAt(mouse);

		if(pressStart == null) {

			if(input.isLeftMousePressed()) {
				pressStart = drawer.getTileLocationAt(mouse);
				shouldFill = true;
			}
			else if(input.isRightMousePressed()) {
				pressStart = drawer.getTileLocationAt(mouse);
				shouldFill = false;
			}
		}

		if((shouldFill && input.isLeftMouseReleased()) ||
				(!shouldFill && input.isRightMouseReleased())) {
			// Get top left and bottom right corners
			Point start = new Point(), end = new Point();

			if(pressStart.x < pressEnd.x) {
				start.x = pressStart.x;
				end.x = pressEnd.x;
			}
			else {
				start.x = pressEnd.x;
				end.x = pressStart.x;
			}

			if(pressStart.y < pressEnd.y) {
				start.y = pressStart.y;
				end.y = pressEnd.y;
			}
			else {
				start.y = pressEnd.y;
				end.y = pressStart.y;
			}
			end.x++;
			end.y++;

			// Get all tiles the two points
			List<Point> points = new ArrayList<>();

			for(int x = start.x; x < end.x; x++) {
				for(int y = start.y; y < end.y; y++) {
					Point point = new Point(x, y);
					points.add(point);
				}
			}

			// Either add or remove the tiles
			if(shouldFill) {
				fillTiles(points);
			}
			else {
				removeTiles(points);
			}

			pressStart = null;
		}

	}

	private void fillTiles(List<Point> points) {

		PokeArea area = maker.getCurrentArea();
		for(Point p : points) {
			Tile tile = new Tile(p.x, p.y, maker.getCurrentTileData());
			area.add(tile);
		}
	}

	private void removeTiles(List<Point> points) {

		PokeArea area = maker.getCurrentArea();
		for(Point p : points) {
			for(Tile t : area.getTilesAt(p)) {
				area.remove(t);
			}
		}
	}

}
