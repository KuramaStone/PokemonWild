package me.brook.PokemonCreator.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.world.area.PokeArea;
import me.brook.PokemonCreator.world.tile.Tile;
import me.brook.PokemonCreator.world.tile.TileType;

/*
 * This class will deal with the player's location in the world, the areas visible, and the animation cycles involved.
 * 
 * Some areas connect to multiple others. An example of this is Route 1 connecting Pallet Town and Viridian City without any map transitions.
 */
public class PokeWorld {

	private PokemonCreator creator;

	private PokeConnection connections;

	public PokeWorld(PokemonCreator creator) {
		this.creator = creator;

		connections = new PokeConnection(new ArrayList<>());
	}

	public void tick() {

		updateAnimatedTiles();
	}

	private void updateAnimatedTiles() {
		for(TileType type : TileType.values()) {
			type.tickAnimation();
		}

		// We update the TileScroller to make the tiles inside the scrollpane be
		// animated as well
		creator.getMaker().getTileScroller().repaint();
	}

	public boolean isAreaNameTaken(String areaName) {
		for(PokeArea area : connections.getAreas()) {
			if(area.getAreaName().equalsIgnoreCase(areaName)) {
				return true;
			}
		}

		return false;
	}

	/*
	 * Getters and setters
	 */

	public void setAreas(List<PokeArea> areas) {
		this.connections.setAreas(areas);
	}

	public void addArea(PokeArea area) {
		this.connections.getAreas().add(area);
		creator.getMaker().updateAreaSelector();
	}

	public void removeArea(PokeArea area) {
		this.connections.getAreas().remove(area);
		creator.getMaker().updateAreaSelector();
	}

	public List<PokeArea> getAreas() {
		return connections.getAreas();
	}

	public PokeConnection getConnections() {
		return connections;
	}

	public void setConnections(PokeConnection connections) {
		this.connections = connections;
		creator.getMaker().updateAreaSelector();
	}

	/*
	 * We'll test for this by just checking if the file already exists
	 */
	public boolean isConnectionNameTaken(String name) {
		return connections.doesConnectionsExist(creator.getSettings(), name);
	}

	public Collection<Tile> getTilesAt(int x, int y) {
		Collection<Tile> tiles = new ArrayList<>();

		Point p = new Point(x, y);
		for(PokeArea area : getAreas()) {

			tiles.addAll(area.getTilesAt(p));

		}

		return tiles;
	}

}
