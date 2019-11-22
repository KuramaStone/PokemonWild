package me.brook.PokemonCreator.input.tool;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.graphics.style.ToggleIcon;
import me.brook.PokemonCreator.input.PokeMaker;
import me.brook.PokemonCreator.toolbox.Tools;

public class PaintPanel extends JPanel {
	private static final long serialVersionUID = 3787082330865865652L;
	
	private List<PaintButton> icons;
	
	public PaintPanel(PokemonCreator creator, PokeMaker maker) {
		addButtons(creator, maker);

		int columns = 2;
		int rows = 10;

		GridLayout grid = new GridLayout(rows, columns, 1, 2);
		this.setLayout(grid);

		for(PaintButton icon : icons) {
			this.add(icon.button);
		}
	}
	
	public void unSelectOthers(PaintButton button) {
		for(PaintButton pb : icons) {
			if(pb != button) {
				pb.button.setSelected(false);
			}
			else {
				pb.button.setSelected(true);
			}
		}
	}

	private void addButtons(PokemonCreator creator, PokeMaker maker) {
		icons = new ArrayList<>();
		BufferedImage sheet = Tools.readImage("res\\sprites\\paint_tools.png");
		BufferedImage icon;
		int size = 64;
		
		icon = sheet.getSubimage(0, 0, size, size);
		PaintButton pencil = new PaintButton(new PencilTool(creator), icon, maker);
		maker.setCurrentTool(pencil.getTool());
		icons.add(pencil);

		icon = sheet.getSubimage(64, 0, size, size);
		PaintButton fill = new PaintButton(new FillAreaTool(creator), icon, maker);
		icons.add(fill);

		unSelectOthers(pencil);
	}

	public class PaintButton {

		private PaintTool tool;
		private JToggleButton button;

		public PaintButton(PaintTool tool, BufferedImage icon, PokeMaker maker) {
			this.tool = tool;

			button = new JToggleButton();
			button.setFocusable(false);
			button.setUI(new ToggleIcon(icon));
			button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					maker.setCurrentTool(tool);
					unSelectOthers(PaintButton.this);
				}
			});

		}
		
		public PaintTool getTool() {
			return tool;
		}
		
		public JToggleButton getButton() {
			return button;
		}
	}

}
