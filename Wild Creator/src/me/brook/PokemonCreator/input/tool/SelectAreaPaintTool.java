package me.brook.PokemonCreator.input.tool;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.input.InputHandler;
import me.brook.PokemonCreator.world.tile.Tile;

public abstract class SelectAreaPaintTool extends PaintTool {

	protected Point mouseStart, mouseEnd;
	protected int clickType;

	public SelectAreaPaintTool(PokemonCreator creator) {
		super(creator);
	}

	@Override
	public void handleInput(InputHandler input) {
		Point mouse = drawer.getMousePosition();

		if(mouse == null || mouse.x > drawer.getBuffer().getWidth()) {
			return;
		}

		if(maker.getCurrentArea() == null) {
			return;
		}

		int mousePressed = getMousePressed(input);
		if(mousePressed >= 0 && mouseStart == null) {
			mouseStart = drawer.getTileLocationAt(mouse);
			clickType = mousePressed;
		}

		handleInput(input, mouse);
	}

	public List<Tile> getAllTilesWithinSelection() {
		List<Tile> tiles = new ArrayList<>();

		for(int x = mouseStart.x; x < mouseEnd.x; x++) {
			for(int y = mouseStart.y; y < mouseEnd.y; y++) {
				tiles.addAll(world.getTilesAt(x, y));
			}
		}

		return tiles;
	}

	private int getMousePressed(InputHandler input) {

		for(int i = 0; i < input.getMousePressed().length; i++) {
			if(input.getMousePressed()[i]) {
				return i;
			}
		}
		return -1;
	}

	protected Point constructBottomRight() {
		Point end = new Point();

		if(mouseStart.x < mouseEnd.x) {
			end.x = mouseEnd.x;
		}
		else {
			end.x = mouseStart.x;
		}

		if(mouseStart.y < mouseEnd.y) {
			end.y = mouseEnd.y;
		}
		else {
			end.y = mouseStart.y;
		}

		// Buffer to select edge tiles
		end.x++;
		end.y++;
		
		return end;
	}

	protected Point constructTopLeft() {
		Point start = new Point();
		if(mouseStart.x < mouseEnd.x) {
			start.x = mouseStart.x;
		}
		else {
			start.x = mouseEnd.x;
		}

		if(mouseStart.y < mouseEnd.y) {
			start.y = mouseStart.y;
		}
		else {
			start.y = mouseEnd.y;
		}
		return start;
	}

}