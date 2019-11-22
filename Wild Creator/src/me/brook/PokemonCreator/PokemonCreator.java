package me.brook.PokemonCreator;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import me.brook.PokemonCreator.graphics.PokeDrawer;
import me.brook.PokemonCreator.graphics.PokeWindow;
import me.brook.PokemonCreator.input.PokeMaker;
import me.brook.PokemonCreator.toolbox.Settings;
import me.brook.PokemonCreator.world.PokeWorld;
import me.brook.PokemonCreator.world.tile.TileType;

public class PokemonCreator implements Runnable {

	public static int FPS = 60;

	private PokeMaker maker;
	private PokeWorld world;
	private PokeWindow window;
	private PokeDrawer drawer;
	
	private Settings settings;

	public PokemonCreator() throws IOException {
		loadSettings();
		drawer = new PokeDrawer(this, PokeWindow.SIZE);
		TileType.loadTileData();
		world = new PokeWorld(this);
		maker = new PokeMaker(this);
		window = new PokeWindow(this);
	}

	private void loadSettings() {
		JFileChooser fr = new JFileChooser();
		FileSystemView fw = fr.getFileSystemView();
		this.settings = new Settings(new File(fw.getDefaultDirectory(), "Pokemon Wild"));
	}

	@Override
	public void run() {

		long lastGlobalUpdate = System.currentTimeMillis();
		long lastWorldUpdate = System.currentTimeMillis();
		while(true) {

			if(lastWorldUpdate + (1000 / 30) <= System.currentTimeMillis()) {
				world.tick();
				lastWorldUpdate = System.currentTimeMillis();
			}

			if(lastGlobalUpdate + (1000 / FPS) <= System.currentTimeMillis()) {
				maker.tick(window.getInput());

				// Rendering
				drawer.clear();
				drawer.drawWorld(world);
				drawer.drawTool(maker.getCurrentTool());
				drawer.repaint();


				// Update last to reset
				window.getInput().update();
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
	
	public Settings getSettings() {
		return settings;
	}

}
