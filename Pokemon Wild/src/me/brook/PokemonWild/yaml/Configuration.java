package me.brook.PokemonWild.yaml;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

public class Configuration {

	private File yamlFile;

	private Map<?, ?> map;
	private String basePath;

	public Configuration(File yamlFile) {
		this.yamlFile = yamlFile;

		try {
			map = (Map<?, ?>) new YamlReader(new FileReader(yamlFile)).read();
		} catch (YamlException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int getInt(String path) {
		return Integer.valueOf(get(path).toString());
	}

	public double getDouble(String path) {
		return Double.valueOf(get(path).toString());
	}

	public Set<Object> getKeys(String path) {
		if (path == null || path.isEmpty()) {
			return (Set<Object>) map.keySet();
		} else {

			if (map.containsKey(path)) {
				return (Set<Object>) ((Map<?, ?>) map.get(path)).keySet();
			} else {
				return null;
			}

		}
	}

	public String getString(String path) {
		return get(path).toString();
	}

	public List<String> getStringList(String path) {
		return (List<String>) get(path);
	}

	public boolean getBoolean(String path) {

		return (boolean) get(path);
	}

	public Object get(String path) {

		String[] split = (basePath + "." + path).split("\\.");

		Map<?, ?> last = map;
		for (int i = 0; i < split.length - 1; i++) {
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
		String[] split = getString(path).split(",");
		int x = Integer.valueOf(split[0]);
		int y = Integer.valueOf(split[1]);
		
		return new Point(x, y);
	}

}