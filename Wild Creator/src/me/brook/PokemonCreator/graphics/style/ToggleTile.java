package me.brook.PokemonCreator.graphics.style;

import java.awt.Graphics;

import javax.swing.JComponent;

import me.brook.PokemonCreator.world.tile.TileData;

public class ToggleTile extends ToggleIcon {

	private TileData data;

	public ToggleTile(TileData data) {
		super(null);
		this.data = data;
	}


	@Override
	public void paint(Graphics g, JComponent c) {
		// Change icon to currentimage
		image = data.getCurrentImage();
		super.paint(g, c);
	}

}