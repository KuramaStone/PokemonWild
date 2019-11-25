package me.brook.PokemonCreator.world.tile;

import java.io.IOException;
import java.util.List;

import me.brook.PokemonCreator.PokemonCreator;

public class TileManager {
		
	private List<TileType> tileTypes;
	
	public TileManager(PokemonCreator creator) {
		try {
			tileTypes = TileType.loadTileData(creator.getSettings());
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
//		tileTypes.sort(new Comparator<TileType>() {
//
//			@Override
//			public int compare(TileType o1, TileType o2) {
//
//				char[] c1 = o1.getName().toCharArray();
//				char[] c2 = o2.getName().toCharArray();
//				for(int i = 0; i < Math.min(c1.length, c2.length); i++) {
//					int value = c1[i] - c2[i];
//
//					if(value == 0) {
//						continue;
//					}
//					else {
//						return value;
//					}
//				}
//				
//				return 0;
//			}
//		});
	}
	
	public List<TileType> getTileTypes() {
		return tileTypes;
	}

	public TileType getTileByName(String string) {
		
		for(TileType tt : tileTypes) {
			if(tt.getName().equalsIgnoreCase(string)) {
				return tt;
			}
		}
		return null;
	}

}
