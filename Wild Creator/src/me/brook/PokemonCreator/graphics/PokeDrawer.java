package me.brook.PokemonCreator.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.input.painting.PaintTool;
import me.brook.PokemonCreator.world.PokeWorld;
import me.brook.PokemonCreator.world.area.PokeArea;
import me.brook.PokemonCreator.world.tile.Tile;

public class PokeDrawer extends JPanel {

	private static final long serialVersionUID = 604046713316255320L;

	private PokemonCreator creator;

	private boolean highlightTiles = false;
	private boolean showHitboxes = false;
	private boolean highlightAreas = true;

	private int size;
	private BufferedImage buffer;

	private int xOffset, yOffset;
	private boolean updateBuffer = true;
	private double zoom = 1.0;

	public PokeDrawer(PokemonCreator creator, int size) {
		this.creator = creator;
		this.size = size;
		buffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

		this.setFocusable(true);
	}

	public void drawTool(PaintTool currentTool) {
		currentTool.draw((Graphics2D) buffer.getGraphics());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(buffer, 0, 0, buffer.getWidth(), buffer.getHeight(),
				null);
	}

	public void clear() {
		buffer = new BufferedImage(buffer.getWidth(), buffer.getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	/*
	 * Draw the areas
	 */
	public void drawWorld(PokeWorld world) {
		if(!updateBuffer) {
			return;
		}

		Graphics2D g = (Graphics2D) buffer.getGraphics();
		for(PokeArea area : world.getAreas()) {
			if(area == null) {
				continue;
			}

			for(Tile tile : area.getTiles()) {

				int tx = (tile.getX() + xOffset) * getTileSize();
				int ty = (tile.getY() + yOffset) * getTileSize();

				drawTile(tx, ty, tile);
				// g.drawString(String.valueOf(tile.getX() + "," + tile.getY()), tx +
				// getTileSize() / 2, ty + getTileSize() / 2);
			}

		}

		if(highlightAreas) {
			for(PokeArea area : new ArrayList<>(world.getAreas())) {
				Rectangle surface = area.getAreaSurface();
				if(surface != null) {
					g.setColor(area.getColor());

					int x = surface.x;
					int y = surface.y;
					int w = surface.width;
					int h = surface.height;

					x = (x + xOffset) * getTileSize();
					y = (y + yOffset) * getTileSize();
					w = (w + xOffset + 1) * getTileSize();
					h = (h + yOffset + 1) * getTileSize();
					w -= x;
					h -= y;

					g.setStroke(new BasicStroke(area == creator.getMaker().getCurrentArea() ? 5f : 2f));
					g.drawRect(x, y, w, h);
				}
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
			if(tile.isCollidable()) {
				g.setColor(new Color(255, 0, 0, 64));
				Rectangle rect = tile.getType().getCollidingArea();
				if(rect != null) {
					g.fillRect(tx, ty - (int) ((rect.getHeight() - 1) * getTileSize()),
							(int) (rect.getWidth() * getTileSize()), (int) (rect.getHeight() * getTileSize()));
				}
			}
		}
	}

	/**
	 * 
	 * @param point
	 *            Mouse position in pixels relative to this panel
	 * @return Tile mouse position is over
	 */
	public Point getTileLocationAt(Point point) {
		// get raw x and tiles without any offset adjustment
		int x = point.x / getTileSize();
		int y = point.y / getTileSize();

		// subtract offsets
		x += -xOffset;
		y += -yOffset;

		return new Point(x, y);
	}

	public int getTileSize() {
		return (int) Math.max(1, size / 15 * zoom); // 15x15 tiles per screen
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

	public void addToZoom(double d) {
		this.zoom += d;

		this.zoom = Math.max(0, this.zoom);
	}

	public void resetZoom(double d) {
		this.zoom = 1.0;
	}

	public BufferedImage getBuffer() {
		return buffer;
	}

}
