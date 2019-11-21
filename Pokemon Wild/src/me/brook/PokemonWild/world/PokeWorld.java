package me.brook.PokemonWild.world;

import java.util.ArrayList;
import java.util.List;

import me.brook.PokemonWild.PokemonWild;
import me.brook.PokemonWild.world.area.PokeArea;

/*
 * This class will deal with the player's location in the world, the areas visible, and the animation cycles involved.
 * 
 * Some areas connect to multiple others. An example of this is Route 1 connecting Pallet Town and Viridian City without any map transitions.
 */
public class PokeWorld {

	// Constructor
	private PokemonWild wild;
	
	private List<PokeArea> areas;
	
	public PokeWorld(PokemonWild wild) {
		this.wild = wild;
		areas = new ArrayList<>();
	}

	/*
	 * Getters and setters
	 */
	
	public void setAreas(List<PokeArea> areas) {
		this.areas = areas;
	}
	
	public List<PokeArea> getAreas() {
		return areas;
	}

}