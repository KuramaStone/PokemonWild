package me.brook.PokemonWild.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

import me.brook.PokemonWild.PokemonWild;
import me.brook.PokemonWild.entity.Player;
import me.brook.PokemonWild.world.PokeWorld;
import me.brook.PokemonWild.world.area.PokeArea;

/*
 * This class deals with the JFrame
 */
public class PokeWindow extends JFrame {

	public static final int SIZE = 720;
	private static final long serialVersionUID = -887579245455944705L;
	private boolean record = false;

	// Constructor
	private PokemonWild wild;
	private TileInventory inventory;

	// Misc
	private BufferedImage buffer;
	private boolean highlightTiles = false;

	// Current Screen location
	private int xOffset, yOffset;

	public PokeWindow(PokemonWild wild) {
		this.wild = wild;
		createWindow();
		this.inventory = wild.getInventory();

		buffer = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
	}

	private void createWindow() {
		this.setTitle("Pokemon Wild");
		this.setSize(PokeWindow.SIZE, PokeWindow.SIZE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Saving!");
				record = false;
				try {
					wild.getRecorder().saveImages();
				}
				catch(IOException e1) {
					e1.printStackTrace();
				}
				PokeWindow.this.dispose();
				System.exit(0);
			}
		});
	}

	/*
	 * Draw the areas
	 */
	public void drawWorld(PokeWorld world) {

		for(PokeArea area : world.getAreas()) {

			int offsetX = area.getOffsetX() * getTileSize() + xOffset;
			int offsetY = area.getOffsetY() * getTileSize() + yOffset;

			int width = area.getWidth();

			int[] tiles = area.getTiles();
			for(int i = 0; i < tiles.length; i++) {
				int tileID = tiles[i];

				int x = (i % width) * getTileSize();
				int y = (i / width) * getTileSize();

				int tx = x + offsetX;
				int ty = y + offsetY;
				drawTile(tx, ty, tileID);
			}

		}
		// for(int i = 0; i < inventory.getInventory().size(); i++) {
		// drawTile(0, 0, i);
		// }

	}

	private void drawTile(int tx, int ty, int tileID) {
		Graphics g = buffer.getGraphics();
		BufferedImage tile = inventory.getTile(tileID);

		g.drawImage(tile, tx, ty, getTileSize(), getTileSize(), null);

		if(highlightTiles) {
			g.setColor(Color.RED);
			g.drawRect(tx, ty, getTileSize(), getTileSize());
		}
	}

	private int getTileSize() {
		return SIZE / 15; // 15x15 tiles per screen
	}

	public void drawImage() {
		Insets insets = getInsets();
		getGraphics().drawImage(buffer, insets.left, insets.top, getWidth() - insets.left, getHeight() - insets.top,
				null);

		// Save each frame to save
		if(record) {
			wild.getRecorder().addImage(buffer);
		}
		
		buffer = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
	}

	public void addXOffset(int offset) {
		xOffset += offset;
	}

	public void addYOffset(int offset) {
		yOffset += offset;
	}

	public void drawPlayer(Player player) {
		Graphics g = buffer.getGraphics();

		g.drawImage(player.getCurrentAnimation()[player.getMode()][player.getDirection()],
				SIZE / 2 - (getTileSize() / 2), (int) (SIZE / 2 - (getTileSize() / 1.6)), getTileSize(),
				(int) (getTileSize() * 1.25), null);
	}

}
