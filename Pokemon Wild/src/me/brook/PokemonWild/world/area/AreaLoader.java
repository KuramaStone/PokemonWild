package me.brook.PokemonWild.world.area;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import me.brook.PokemonWild.yaml.Configuration;

public class AreaLoader {

	private File baseFolder = new File("res\\areas");
	private File areasFolder = new File(baseFolder, "areas");
	private Configuration config;

	public AreaLoader() {
		config = new Configuration(new File(baseFolder, "area_connections.yml"));
	}

	/*
	 * This loads the tile map data from the pattern.txt inside the parent file
	 */
	public List<PokeArea> loadAreasFrom(String connectionName) throws Exception {
		List<PokeArea> connectedAreas = new ArrayList<>();

		List<String> areasInConnection = config.getStringList(connectionName);

		FileReader fr;
		BufferedReader br;

		for(String areaName : areasInConnection) {
			int offsetX = 0;
			int offsetY = 0;
			int width = 0;
			int[] tiles = null;

			fr = new FileReader(new File(areasFolder, areaName + ".txt"));
			br = new BufferedReader(fr);

			String line;
			int count = 0;

			int arrayMark = 0;
			while((line = br.readLine()) != null) {
				if(line.startsWith("##")) {
					continue;
				}

				if(count == 0) { // offsets
					String[] split = line.split(",");
					offsetX = parseInt(split[0]);
					offsetY = parseInt(split[1]);
				}

				else if(count == 1) { // Area size
					String[] split = line.split("x");
					width = parseInt(split[0]);
					int height = parseInt(split[0]);
					tiles = new int[width * height];
				}

				else { // The route data
					for(String str : line.split(",")) {
						tiles[arrayMark++] = parseInt(str);
					}
				}

				count++;
			}

			connectedAreas.add(new PokeArea(offsetX, offsetY, width, tiles));

			br.close();
			fr.close();
		}

		return connectedAreas;
	}

	private int parseInt(String string) {
		return Integer.parseInt(string);
	}

}