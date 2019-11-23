package me.brook.PokemonCreator.input.tool;

import java.awt.Graphics2D;
import java.awt.Point;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.graphics.PokeDrawer;
import me.brook.PokemonCreator.input.InputHandler;
import me.brook.PokemonCreator.input.PokeMaker;
import me.brook.PokemonCreator.world.PokeWorld;

public abstract class PaintTool {
	
	protected PokemonCreator creator;
	protected PokeDrawer drawer;
	protected PokeWorld world;
	protected PokeMaker maker;
	
	public PaintTool(PokemonCreator creator) {
		this.creator = creator;
		
		drawer = creator.getDrawer();
		world = creator.getWorld();
		maker = creator.getMaker();
	}
	
	public void handleInput(InputHandler input) {
		Point mouse = drawer.getMousePosition();

		if(mouse == null || mouse.x > drawer.getBuffer().getWidth()) {
			return;
		}
		
		handleInput(input, mouse);
	}

	protected abstract void handleInput(InputHandler input, Point mouse);
	
	public void draw(Graphics2D g) {
		
	}
	
}
