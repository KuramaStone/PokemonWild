package me.brook.PokemonCreator.input.tool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.input.InputHandler;
import me.brook.PokemonCreator.world.tile.Tile;

public class MoveAreaTool extends SelectAreaPaintTool {

	private List<Tile> selectedTiles;
	private Point origin;

	public MoveAreaTool(PokemonCreator creator) {
		super(creator);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		if(mouseStart != null && mouseEnd != null) {
			Point start = constructTopLeft(), end = constructBottomRight();

			int x1 = (start.x + drawer.getxOffset()) * drawer.getTileSize();
			int y1 = (start.y + drawer.getyOffset()) * drawer.getTileSize();

			int x2 = (end.x + drawer.getxOffset()) * drawer.getTileSize();
			int y2 = (end.y + drawer.getyOffset()) * drawer.getTileSize();

			g.setColor(Color.BLUE);
			g.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0));
			g.drawRect(x1, y1, x2 - x1, y2 - y1);
		}
	}

	@Override
	protected void handleInput(InputHandler input, Point mouse) {
		if(maker.getCurrentArea() == null) {
			return;
		}

		// Get area to gather tiles
		if(input.getMousePressed()[clickType] && selectedTiles == null) {
			mouseEnd = drawer.getTileLocationAt(mouse);
		}
		// Get selected tiles
		else if(mouseStart != null && mouseEnd != null && selectedTiles == null) {
			Point start = constructTopLeft(), end = constructBottomRight();

			selectedTiles = new ArrayList<>();
			for(int x = start.x; x < end.x; x++) {
				for(int y = start.y; y < end.y; y++) {
					selectedTiles.addAll(world.getTilesAt(x, y));
				}
			}

		}

		// Move the selected tiles
		if(input.getMousePressed()[clickType] && selectedTiles != null) {
			Point current = drawer.getTileLocationAt(mouse);
			if(origin == null) {
				origin = current;
			}
			Point add = subtract(current, origin);
			if(Math.abs(add.x) + Math.abs(add.y) > 0) {

				for(Tile tile : selectedTiles) {
					tile.x += add.x;
					tile.y += add.y;
				}

				mouseStart.x += add.x;
				mouseStart.y += add.y;
				mouseEnd.x += add.x;
				mouseEnd.y += add.y;

				origin = current;

				maker.getCurrentArea().recalculateSurface();
			}
		}

		// After done moving a section, reset the data to be ready for the next try
		if(selectedTiles != null && input.isKeyPressed(KeyEvent.VK_SPACE)) {
			selectedTiles = null;
			mouseStart = null;
			mouseEnd = null;
			origin = null;
		}

	}

	private Point subtract(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}

}
