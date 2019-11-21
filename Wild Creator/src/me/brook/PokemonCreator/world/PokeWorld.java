package me.brook.PokemonCreator.world;

import java.util.ArrayList;
import java.util.List;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.world.area.PokeArea;
import me.brook.PokemonCreator.world.tile.TileType;

/*
 * This class will deal with the player's location in the world, the areas visible, and the animation cycles involved.
 * 
 * Some areas connect to multiple others. An example of this is Route 1 connecting Pallet Town and Viridian City without any map transitions.
 */
public class PokeWorld {

	private PokemonCreator creator;
	
	private List<PokeArea> areas;

	public PokeWorld(PokemonCreator creator) {
		this.creator = creator;
		areas = new ArrayList<>();
	}

	public void tick() {

		updateAnimatedTiles();
		
	}

	private void updateAnimatedTiles() {
		for(TileType type : TileType.values()) {
			type.tickAnimation();
		}
		
		// We update the TileScroller to make the tiles inside the scrollpane be animated as well
		creator.getMaker().getTileScroller().repaint();
	}

	/*
	 * Getters and setters
	 */

	public void setAreas(List<PokeArea> areas) {
		this.areas = areas;
	}

	public void addArea(PokeArea area) {
		this.areas.add(area);
	}

	public void removeArea(PokeArea area) {
		this.areas.remove(area);
	}

	public List<PokeArea> getAreas() {
		return areas;
	}

}
