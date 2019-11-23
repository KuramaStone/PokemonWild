package me.brook.PokemonCreator.input.tool;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.input.InputHandler;
import me.brook.PokemonCreator.world.area.PokeArea;
import me.brook.PokemonCreator.world.tile.Tile;

public class FillAreaTool extends SelectAreaPaintTool {

	public FillAreaTool(PokemonCreator creator) {
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

			g.setColor(Color.RED);
			g.drawRect(x1, y1, x2 - x1, y2 - y1);
		}
	}

	@Override
	public void handleInput(InputHandler input, Point mouse) {

		if(maker.getCurrentArea() == null) {
			return;
		}

		if(input.getMousePressed()[clickType]) {
			mouseEnd = drawer.getTileLocationAt(mouse);
		}
		else if(mouseStart != null && mouseEnd != null) {
			Point start = constructTopLeft(), end = constructBottomRight();

			List<Point> points = new ArrayList<>();
			for(int x = start.x; x < end.x; x++) {
				for(int y = start.y; y < end.y; y++) {
					points.add(new Point(x, y));
				}
			}

			if(clickType == 1) {
				fillTiles(points);
			}
			else if(clickType == 3) {
				removeTiles(points);
			}

			mouseStart = null;
			mouseEnd = null;
		}

	}

//	private void removeSurfaceTiles(List<Point> points) {
//		PokeArea area = maker.getCurrentArea();
//
//		// Get highest tile in selection
//		Tile highest = null;
//		for(Point p : points) {
//
//			for(Tile t : area.getTilesAt(p)) {
//				if(highest == null) {
//					highest = t;
//					continue;
//				}
//
//				if(t.getLayer() > highest.getLayer()) {
//					highest = t;
//				}
//			}
//
//		}
//
//		for(Point p : points) {
//
//			for(Tile t : area.getTilesAt(p)) {
//				if(t.getLayer() == highest.getLayer()) {
//					area.remove(t);
//					continue;
//				}
//			}
//		}
//
//	}

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
