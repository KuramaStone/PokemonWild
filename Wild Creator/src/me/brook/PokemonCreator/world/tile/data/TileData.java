package me.brook.PokemonCreator.world.tile.data;

import java.awt.image.BufferedImage;

import me.brook.PokemonCreator.world.tile.TileType;

public class TileData {

	public TileType type;
	public int variant;
	
	public TileData() {
	}

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
			return data.type == this.type && data.variant == this.variant;
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

	public boolean isCollidable() {
		return type.isCollidable(variant);
	}
}
