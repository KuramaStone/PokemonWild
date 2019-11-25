package me.brook.PokemonCreator.input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.graphics.style.FixedGridLayout;
import me.brook.PokemonCreator.graphics.style.ToggleIcon;
import me.brook.PokemonCreator.world.tile.StructureData;
import me.brook.PokemonCreator.world.tile.TileType;

public class StructureScroller extends JPanel {

	private static final long serialVersionUID = 1275918524454747478L;

	private PokemonCreator creator;
	private PokeMaker maker;

	private List<StructureIcon> icons;

	public StructureScroller(PokemonCreator creator) {
		this.creator = creator;
		this.maker = creator.getMaker();

		addIcons();
		setIconsInPanel("");
	}

	public void setIconsInPanel(String string) {
		icons.forEach(t -> this.remove(t.button));

		
		for(StructureIcon tt : icons) {
			if(tt.getType().toString().toLowerCase().contains(string.toLowerCase())) {
				this.add(tt.button);
			}
		}

	}

	private void addIcons() {
		icons = new ArrayList<>();

		int columns = 2;
		int total = 0;
		for(TileType type : creator.getTileManager().getTileTypes()) {
			total += type.isStructure() ? 1 : 0;
		}
		int rows = total / columns + 1;

		int size = 150;
		this.setLayout(new FixedGridLayout(columns, rows, size, size));
		
		for(TileType type : creator.getTileManager().getTileTypes()) {
			if(type.isStructure()) {
				
				StructureIcon icon = new StructureIcon(type);
				icons.add(icon);
				
			}
		}
		
	}

	public class StructureIcon {

		private TileType type;
		private JToggleButton button;
		
		private StructureData structureData;

		public StructureIcon(TileType type) {
			this.type = type;
			
			structureData = new StructureData(type);
			
			button = new JToggleButton();
			button.setFocusable(false);
			button.setUI(new ToggleIcon(type.getFullIcon()));
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					maker.setCurrentStructureData(structureData);
				}
			});
		}

		public TileType getType() {
			return type;
		}

	}

}
