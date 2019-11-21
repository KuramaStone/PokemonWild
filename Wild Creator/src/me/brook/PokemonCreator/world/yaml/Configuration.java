package me.brook.PokemonCreator.world.yaml;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

public class Configuration {

	private File yamlFile;

	private Map<?, ?> map;
	private String basePath = "";

	public Configuration(File yamlFile) {
		this.yamlFile = yamlFile;

		try {
			map = (Map<?, ?>) new YamlReader(new FileReader(yamlFile)).read();
		}
		catch(YamlException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int getInt(String path) {
		Object get = get(path);
		if(get == null) {
			return 0;
		}
		
		return Integer.valueOf(get.toString());
	}

	public double getDouble(String path) {
		return Double.valueOf(get(path).toString());
	}

	public List<String> getKeys(String path) {
		if(path == null || path.isEmpty()) {
			return (List<String>) map.keySet();
		}
		else {

			String[] split = (basePath.isEmpty() ? path : basePath + "." + path).split("\\.");

			Map<?, ?> last = map;
			for(int i = 0; i < split.length; i++) {
				last = (Map<?, ?>) last.get(split[i]);
			}
			
			List<String> keys = new ArrayList<>();

			for(Entry<?, ?> set : last.entrySet()) {
				keys.add(set.getKey().toString());
			}
			
			return keys;
		}
	}

	public String getString(String path) {
		Object get = get(path);
		if(get == null) {
			return null;
		}
		return get.toString();
	}

	public List<String> getStringList(String path) {
		return (List<String>) get(path);
	}

	public boolean getBoolean(String path) {
		Object obj = get(path);
		return obj == null ? false : Boolean.valueOf(obj.toString());
	}

	public Object get(String path) {

		String[] split = (basePath.isEmpty() ? path : basePath + "." + path).split("\\.");

		Map<?, ?> last = map;
		for(int i = 0; i < split.length - 1; i++) {
			last = (Map<?, ?>) last.get(split[i]);
		}

		return last.get(split[split.length - 1]);

	}

	public File getYAMLFile() {
		return yamlFile;
	}

	public void setSection(String path) {
		this.basePath = path;
	}

	public Point getPoint(String path) {
		String str = getString(path);
		if(str == null) {
			return null;
		}
		String[] split = str.split(",");
		int x = Integer.valueOf(split[0]);
		int y = Integer.valueOf(split[1]);

		return new Point(x, y);
	}

}