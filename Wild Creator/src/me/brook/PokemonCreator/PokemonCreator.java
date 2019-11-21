package me.brook.PokemonCreator;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.brook.PokemonCreator.graphics.PokeDrawer;
import me.brook.PokemonCreator.graphics.PokeWindow;
import me.brook.PokemonCreator.input.PokeMaker;
import me.brook.PokemonCreator.world.PokeWorld;
import me.brook.PokemonCreator.world.tile.TileType;

public class PokemonCreator implements Runnable {

	public static int FPS = 60;

	private PokeMaker maker;
	private PokeWorld world;
	private PokeWindow window;
	private PokeDrawer drawer;

	public PokemonCreator() throws IOException {
		drawer = new PokeDrawer(PokeWindow.SIZE);
		TileType.loadTileData();
		world = new PokeWorld(this);
		maker = new PokeMaker(this);
		window = new PokeWindow(this);
	}

	@Override
	public void run() {

		try {
			for(int i = 0; i < TileType.RED_FLOWER.getImages().length; i++)
				ImageIO.write(TileType.RED_FLOWER.getImages()[i], "png",
						new File("C:\\Users\\Stone\\PokemonWild\\" + i + ".png"));
		}
		catch(Exception e) {
		}

		long lastGlobalUpdate = System.currentTimeMillis();
		long lastWorldUpdate = System.currentTimeMillis();
		while(true) {

			if(lastWorldUpdate + (1000 / FPS) <= System.currentTimeMillis()) {
				world.tick();
				lastWorldUpdate = System.currentTimeMillis();
			}

			if(lastGlobalUpdate + (1000 / FPS) <= System.currentTimeMillis()) {
				maker.tick(window.getInput());
				window.getInput().update();

				// Rendering
				drawer.drawWorld(world);
				drawer.repaint();

				lastGlobalUpdate = System.currentTimeMillis();
			}

		}

	}

	public PokeWindow getWindow() {
		return window;
	}

	public PokeWorld getWorld() {
		return world;
	}

	public PokeMaker getMaker() {
		return maker;
	}

	public PokeDrawer getDrawer() {
		return drawer;
	}

}
