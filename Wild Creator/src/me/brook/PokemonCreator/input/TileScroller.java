package me.brook.PokemonCreator.input;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import me.brook.PokemonCreator.graphics.style.ToggleButtonDesign;
import me.brook.PokemonCreator.world.tile.TileData;
import me.brook.PokemonCreator.world.tile.TileType;

public class TileScroller extends JPanel {

	private static final long serialVersionUID = 2040291925460760780L;

	private PokeMaker maker;

	private List<TileToggle> tiles;

	public TileScroller(PokeMaker maker) {
		this.maker = maker;
		addTiles();
	}

	private void addTiles() {
		tiles = new ArrayList<>();

		int columns = 5;
		int total = 0;
		for(TileType type : TileType.values()) {
			total += type.isAnimated() ? 1 : type.getImages().length;
		}
		int rows = total / columns;

		GridLayout grid = new GridLayout(rows, columns, 1, 2);
		this.setLayout(grid);

		for(TileType type : TileType.values()) {
			for(int variant = 0; variant < (type.isAnimated() ? 1 : type.getImages().length); variant++) {
				BufferedImage image = type.getImages()[variant];
				TileData data = new TileData(type, variant);

				JToggleButton icon = new JToggleButton();
				icon.setUI(new ToggleButtonDesign(data));
				icon.setSelected(maker.getCurrentTileData().getType() == type &&
						maker.getCurrentTileData().getVariant() == variant);
				icon.addActionListener(getTileChanger(data));
				icon.setFocusable(false);
				icon.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

				this.add(icon);
				tiles.add(new TileToggle(icon, data));
			}
		}
	}

	private ActionListener getTileChanger(TileData data) {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				unSelectOtherTiles(data);
				TileScroller.this.maker.setCurrentType(data);
			}
		};
	}

	public void unSelectOtherTiles(TileData data) {
		for(TileToggle tt : tiles) {
			tt.getButton().setSelected(tt.getData().equals(data));
		}
	}

	private static class TileToggle {

		private JToggleButton button;
		private TileData data;

		public TileToggle(JToggleButton button, TileData data) {
			this.button = button;
			this.data = data;
		}

		public JToggleButton getButton() {
			return button;
		}

		public TileData getData() {
			return data;
		}

	}

}