package me.brook.PokemonWild;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.brook.PokemonWild.entity.Player;
import me.brook.PokemonWild.graphics.PokeWindow;
import me.brook.PokemonWild.graphics.Sprites;
import me.brook.PokemonWild.graphics.TileInventory;
import me.brook.PokemonWild.graphics.record.Recorder;
import me.brook.PokemonWild.input.InputHandler;
import me.brook.PokemonWild.world.PokeWorld;
import me.brook.PokemonWild.world.area.AreaLoader;
import me.brook.PokemonWild.world.area.PokeArea;
import me.brook.PokemonWild.world.tile.TileType;

public class PokemonWild implements Runnable {

	public static final int FPS = 30;

	// Manager classes
	private PokeWindow window;
	private PokeWorld world;
	private TileInventory inventory;
	private InputHandler keyInput;
	private Sprites sprites;
	private AreaLoader areaLoader;
	private Recorder recorder;

	// Wild classes
	private boolean isRunning = true;
	private boolean isNewGame = true;

	// Player class
	private Player player;

	public PokemonWild() throws IOException {
		TileType.loadTileData();
		loadSprites();
		createWindow();
		createPlayerInWorld();
	}

	@Override
	public void run() {
		long nanosecondsBetweenFrames = (long) (1e9 / FPS);

		long lastUpdate = System.nanoTime();
		while(isRunning) {

			if(lastUpdate + nanosecondsBetweenFrames <= System.nanoTime()) {
				
				// World tick
				player.tick();
				
				// Rendering
				window.drawWorld(world);
				window.drawPlayer(player);
				window.drawImage();
				
				// int currentFPS = (int) Math.round(1e9 / (System.nanoTime() - lastUpdate));
				// System.out.println(currentFPS);
				lastUpdate = System.nanoTime();
			}
		}

	}

	/*
	 * Setup loader
	 */

	private void loadSprites() {
		inventory = new TileInventory();
		inventory.loadTiles();

		sprites = new Sprites();
	}

	private void createPlayerInWorld() {
		player = new Player(this);
		world = new PokeWorld(this);

		areaLoader = new AreaLoader();
		if(isNewGame) {

			List<PokeArea> areas = new ArrayList<>();
			try {
				areas.addAll(areaLoader.loadAreasFrom("connection000"));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			world.setAreas(areas);

		}

	}

	private void createWindow() {
		recorder = new Recorder();
		window = new PokeWindow(this);
		keyInput = new InputHandler(window);
	}

	/*
	 * Getters
	 */

	public TileInventory getInventory() {
		return inventory;
	}

	public InputHandler getKeyInput() {
		return keyInput;
	}

	public PokeWindow getWindow() {
		return window;
	}

	public PokeWorld getWorld() {
		return world;
	}

	public Sprites getSprites() {
		return sprites;
	}
	
	public Recorder getRecorder() {
		return recorder;
	}

}