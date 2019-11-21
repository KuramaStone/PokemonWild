package me.brook.PokemonCreator.input;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.graphics.PokeDrawer;
import me.brook.PokemonCreator.world.PokeWorld;
import me.brook.PokemonCreator.world.area.PokeArea;
import me.brook.PokemonCreator.world.tile.Tile;
import me.brook.PokemonCreator.world.tile.TileData;
import me.brook.PokemonCreator.world.tile.TileType;

public class PokeMaker {

	private PokeWorld world;
	private PokeDrawer drawer;
	private TileScroller tileScroller;
	
	private TileData currentTileData = new TileData(TileType.GRASS, 0);
	private List<Point> tilesAdded;
	private List<Tile> undoneTiles;
	private boolean hasRemovalBeenPerformed = false;

	private int undoDelayRemaining, ticksUndoHeld;
	private int redoDelayRemaining, ticksRedoHeld;

	public PokeMaker(PokemonCreator creator) {
		world = creator.getWorld();
		drawer = creator.getDrawer();

		tilesAdded = new ArrayList<>();
		undoneTiles = new ArrayList<>();
	}

	public void tick(InputHandler input) {
		if(input == null) {
			return;
		}

		handleMouse(input);
		handleKeys(input);
		undoDelayRemaining--;
		redoDelayRemaining--;
	}

	private void handleMouse(InputHandler input) {
		Point mouse = drawer.getMousePosition();

		if(mouse != null) {

			Point tile = drawer.getTileLocationAt(mouse);

			// Add tiles during left click
			if(input.isLeftMousePressed()) {

				if(!tilesAdded.contains(tile)) {
					if(!world.getAreas().isEmpty()) {
						world.getAreas().get(0).addTile(new Tile(tile.x, tile.y, currentTileData));
						// To prevent accidentally adding another tile to the same location during a
						// click and drag,
						// we create a list of tile points that have been made since then.
						tilesAdded.add(tile);
					}
				}
			}
			else {
				tilesAdded.clear();

				if(input.isRightMousePressed()) {
					if(!hasRemovalBeenPerformed) {
						if(!world.getAreas().isEmpty()) {
							List<Tile> tiles = world.getAreas().get(0).getTiles();
							// Remove highest tile
							for(int i = tiles.size() - 1; i >= 0; i--) {
								Tile t = tiles.get(i);
								if(t.isLocationAt(tile)) {
									undoneTiles.add(tiles.remove(i));
									break;
								}
							}
							hasRemovalBeenPerformed = true;
						}
					}

				}
				else {
					hasRemovalBeenPerformed = false;

					if(input.isMiddleMouseClicked()) {
						if(!world.getAreas().isEmpty()) {
							List<Tile> tiles = world.getAreas().get(0).getTiles();
							for(int i = tiles.size() - 1; i >= 0; i--) {
								Tile t = tiles.get(i);
								if(t.isLocationAt(tile)) {
									// This is so that the player may get the tile data one tile beneath the top
									if(currentTileData.equals(t.getData())) {
										continue;
									}
									else {
										currentTileData = t.getData();
										tileScroller.unSelectOtherTiles(currentTileData);
										break;
									}
								}
							}
						}
					}
				}
			}

		}
	}

	private void handleKeys(InputHandler input) {
		if(input.isKeyPressed(KeyEvent.VK_Z)) {
			if(undoDelayRemaining <= 0) {
				if(!world.getAreas().isEmpty()) {
					List<Tile> tiles = world.getAreas().get(0).getTiles();

					if(tiles.size() > 0) {
						Tile undone = tiles.remove(tiles.size() - 1);
						undoneTiles.add(undone);
					}

					if(ticksUndoHeld >= (PokemonCreator.FPS * 6)) {
						undoDelayRemaining = PokemonCreator.FPS / 100;
					}
					else if(ticksUndoHeld >= (PokemonCreator.FPS * 3)) {
						undoDelayRemaining = PokemonCreator.FPS / 20;
					}
					else if(ticksUndoHeld >= (PokemonCreator.FPS * 1.5)) {
						undoDelayRemaining = PokemonCreator.FPS / 10;
					}
					else {
						undoDelayRemaining = PokemonCreator.FPS / 4;
					}
				}
			}

			ticksUndoHeld++;
			return;
		}
		else {
			ticksUndoHeld = 0;
		}

		if(input.isKeyPressed(KeyEvent.VK_Y)) {
			if(redoDelayRemaining <= 0) {
				if(!world.getAreas().isEmpty()) {
					List<Tile> tiles = world.getAreas().get(0).getTiles();

					if(undoneTiles.size() > 0) {
						Tile lastRemoved = undoneTiles.get(undoneTiles.size() - 1);
						tiles.add(lastRemoved);
						undoneTiles.remove(lastRemoved);
					}

					if(ticksRedoHeld >= (PokemonCreator.FPS * 6)) {
						redoDelayRemaining = PokemonCreator.FPS / 100;
					}
					else if(ticksRedoHeld >= (PokemonCreator.FPS * 3)) {
						redoDelayRemaining = PokemonCreator.FPS / 20;
					}
					else if(ticksRedoHeld >= (PokemonCreator.FPS * 1.5)) {
						redoDelayRemaining = PokemonCreator.FPS / 10;
					}
					else {
						redoDelayRemaining = PokemonCreator.FPS / 4;
					}
				}
			}

			ticksRedoHeld++;
			return;
		}
		else {
			ticksRedoHeld = 0;
		}
	}

	public JPanel addComponents(GridBagConstraints gbc) throws IOException {

		JButton addArea = new JButton("Add Area");
		addArea.setSize(100, 40);
		addArea.setLocation(25, 100);
		addArea.setPreferredSize(new Dimension(100, 40));
		addArea.setFocusable(false);
		addArea.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				world.addArea(new PokeArea(0, 0, new ArrayList<>()));
			}
		});

		JButton deleteArea = new JButton("Remove Area");
		deleteArea.setLocation(100, 50);
		deleteArea.setSize(50, 50);
		deleteArea.setFocusable(false);

		JButton save = new JButton(new ImageIcon(
				ImageIO.read(new File("C:\\Users\\Stone\\PokemonWild\\Pokemon Wild\\res\\misc\\save_icon.png"))));
		save.setPreferredSize(new Dimension(64, 64));
		save.setFocusable(false);

		JButton open = new JButton(new ImageIcon(
				ImageIO.read(new File("C:\\Users\\Stone\\PokemonWild\\Pokemon Wild\\res\\misc\\open_icon.png"))));
		open.setPreferredSize(new Dimension(64, 64));
		open.setFocusable(false);

		JToggleButton highlight = new JToggleButton("Highlight Tiles");
		highlight.setFocusable(false);
		highlight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				drawer.setHighlightTiles(highlight.isSelected());
			}
		});

		JPanel options = new JPanel();
		options.setLayout(new GridBagLayout());

		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.insets = new Insets(10, 2, 2, 2);
		gbc.gridx = 0;
		gbc.gridy = 0;
		options.add(save, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		options.add(open, gbc);

		gbc.insets = new Insets(100, 2, 2, 2);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 1;
		options.add(addArea, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		options.add(deleteArea, gbc);

		gbc.gridx = 3;
		gbc.gridy = 1;
		highlight.setPreferredSize(new Dimension(128, 40));
		options.add(highlight, gbc);

		JScrollPane tileSelector = new JScrollPane(tileScroller = new TileScroller(this),
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tileSelector.getVerticalScrollBar().setUnitIncrement(8);
		tileSelector.setFocusable(false);
		tileSelector.setPreferredSize(new Dimension(320, 300));
		gbc.gridwidth = (int) tileSelector.getPreferredSize().getWidth();
		gbc.gridheight = (int) tileSelector.getPreferredSize().getHeight();
		gbc.gridx = 0;
		gbc.gridy = 3;
		options.add(tileSelector, gbc);

		return options;
	}

	public void setCurrentType(TileData data) {
		this.currentTileData = data;
	}

	public TileData getCurrentTileData() {
		return currentTileData;
	}
	
	public TileScroller getTileScroller() {
		return tileScroller;
	}

}