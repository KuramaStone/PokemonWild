package me.brook.PokemonCreator.world;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import me.brook.PokemonCreator.toolbox.Settings;
import me.brook.PokemonCreator.world.area.PokeArea;

public class PokeConnection {
	
	/*
	 * All the areas that should be rendered together
	 */
	private List<PokeArea> areas;
	
	public PokeConnection() {
	}
	
	public PokeConnection(List<PokeArea> areas) {
		this.areas = areas;
	}

	public List<PokeArea> getAreas() {
		return areas;
	}
	
	public void setAreas(List<PokeArea> areas) {
		this.areas = areas;
	}

	/*
	 * We'll test for this by just checking if the file already exists 
	 */
	public boolean doesConnectionsExist(Settings settings, String connectionName) {
		File parent = new File(settings.getFolder(), "areas");
		File file = new File(parent, connectionName + ".yml");
		return file.exists();
	}
	
	/*
	 * Loading and saving
	 */

	public void save(Settings settings, String connectionName) {
		
		try {
			File parent = new File(settings.getFolder(), "areas");
			File file = new File(parent, connectionName + ".yml");
			YamlWriter writer = new YamlWriter(new FileWriter(file));
			writer.getConfig().writeConfig.setAutoAnchor(false);
			writer.write(this);
			writer.close();
			System.out.println("Area file saved at '" + file.getAbsolutePath() + "'.");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static PokeConnection open(File file) {
		
		try {
			YamlReader reader = new YamlReader(new FileReader(file));
			PokeConnection connections = reader.read(PokeConnection.class);
			
			// Reform PokeArea construction
			connections.getAreas().forEach(pa -> pa.construct());
			
			return connections;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static class LoadingFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			if(f.isDirectory()) {
				return true;
			}
			
			if(f.getName().endsWith(".yml")) {
				return true;
			}
			
			return false;
		}

		@Override
		public String getDescription() {
			return null;
		}

	}

}
