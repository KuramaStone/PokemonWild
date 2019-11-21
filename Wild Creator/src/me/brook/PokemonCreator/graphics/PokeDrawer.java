package me.brook.PokemonCreator.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import me.brook.PokemonCreator.world.PokeWorld;
import me.brook.PokemonCreator.world.area.PokeArea;
import me.brook.PokemonCreator.world.tile.Tile;

public class PokeDrawer extends JPanel {

	private static final long serialVersionUID = 604046713316255320L;

	private boolean highlightTiles = false;
	private boolean showHitboxes = true;
	private BufferedImage buffer;

	private int xOffset, yOffset;
	private boolean updateBuffer = true;

	public PokeDrawer(int size) {
		buffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

		this.setFocusable(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(buffer, 0, 0, buffer.getWidth(), buffer.getHeight(),
				null);
	}

	/*
	 * Draw the areas
	 */
	public void drawWorld(PokeWorld world) {
		if(!updateBuffer) {
			return;
		}
		buffer = new BufferedImage(getWidth(), getWidth(), BufferedImage.TYPE_INT_RGB);

		for(PokeArea area : world.getAreas()) {

			int offsetX = area.getOffsetX() * getTileSize() + xOffset;
			int offsetY = area.getOffsetY() * getTileSize() + yOffset;

			for(Tile tile : area.getTiles()) {

				int x = tile.getX() * getTileSize();
				int y = tile.getY() * getTileSize();

				int tx = x + offsetX;
				int ty = y + offsetY;
				drawTile(tx, ty, tile);
				// g.drawString(String.valueOf(tile.getX() + "," + tile.getY()), tx +
				// getTileSize() / 2, ty + getTileSize() / 2);
			}

		}

	}

	private void drawTile(int tx, int ty, Tile tile) {
		Graphics g = buffer.getGraphics();
		BufferedImage image = tile.getCurrentImage();

		g.drawImage(image, tx, ty - getTileSize() * (tile.getHeight() - 1),
				getTileSize() * tile.getWidth(), getTileSize() * (tile.getHeight()), null);

		if(highlightTiles) {
			g.setColor(Color.RED);
			g.drawRect(tx, ty, getTileSize() * tile.getWidth(), getTileSize() * tile.getHeight());
		}

		if(showHitboxes) {
			g.setColor(new Color(255, 0, 0, 100));
			Rectangle rect = tile.getType().getCollidingArea();
			if(rect != null) {
				g.fillRect(tx, ty - (int) ((rect.getHeight() - 1) * getTileSize()),
						(int) (rect.getWidth() * getTileSize()), (int) (rect.getHeight() * getTileSize()));
			}
		}
	}

	public Point getTileLocationAt(Point point) {
		int x = point.x + xOffset;
		int y = point.y + yOffset;
		x /= getTileSize();
		y /= getTileSize();

		return new Point(x, y);
	}

	private int getTileSize() {
		return getWidth() / 15; // 15x15 tiles per screen
	}

	public boolean highlightTiles() {
		return highlightTiles;
	}

	public void addXOffset(int offset) {
		xOffset += offset;
		updateBuffer = true;
	}

	public void addYOffset(int offset) {
		yOffset += offset;
		updateBuffer = true;
	}

	public int getxOffset() {
		return xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public boolean shouldHighlightTiles() {
		return highlightTiles;
	}

	public void setHighlightTiles(boolean highlightTiles) {
		this.highlightTiles = highlightTiles;
		updateBuffer = true;
	}

}