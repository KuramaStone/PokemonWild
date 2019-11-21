package me.brook.PokemonWild.entity;

import me.brook.PokemonWild.PokemonWild;
import me.brook.PokemonWild.graphics.PokeWindow;

public abstract class Entity {

	protected PokemonWild wild;
	protected PokeWindow window;
	
	protected int ticks;
	
	public Entity(PokemonWild wild) {
		this.wild = wild;
		this.window = wild.getWindow();
	}
	
	public void tick() {
		ticks++;
		ticks %= 30;
	}

}
