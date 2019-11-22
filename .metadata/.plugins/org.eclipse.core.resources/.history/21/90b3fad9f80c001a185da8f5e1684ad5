package me.brook.PokemonCreator.world.tile;

import java.awt.image.BufferedImage;

public class TileData {

	private TileType type;
	private int variant;

	public TileData(TileType type, int variant) {
		this.type = type;
		this.variant = variant;
	}

	public TileType getType() {
		return type;
	}

	public int getVariant() {
		return variant;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof TileData) {
			TileData data = (TileData) obj;
			return data.type == type && data.variant == variant;
		}

		return false;
	}

	public BufferedImage getCurrentImage() {
		if(this.getType().isAnimated()) {
			return this.getType().getCurrentImage();
		}
		else {
			return this.getType().getImages()[this.getVariant()];
		}
	}
}
