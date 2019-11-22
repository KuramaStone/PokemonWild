package me.brook.PokemonCreator.toolbox;

import java.io.File;

public class Settings {
	
	private File mainFolder;

	public Settings(File mainFolder) {
		this.mainFolder = mainFolder;
	}

	public File getFolder() {
		return mainFolder;
	}

}
