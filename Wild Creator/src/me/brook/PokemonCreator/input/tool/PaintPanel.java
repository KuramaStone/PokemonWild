package me.brook.PokemonCreator.input.tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.graphics.style.FixedGridLayout;
import me.brook.PokemonCreator.graphics.style.ToggleIcon;
import me.brook.PokemonCreator.input.PokeMaker;
import me.brook.PokemonCreator.toolbox.Tools;

public class PaintPanel extends JPanel {
	private static final long serialVersionUID = 3787082330865865652L;

	private List<PaintButton> icons;

	public PaintPanel(PokemonCreator creator, PokeMaker maker) {
		addButtons(creator, maker);

		int columns = 1;
		int rows = 10;

		this.setLayout(new FixedGridLayout(columns, rows, 64, 64));

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
		BufferedImage sheet = Tools.readImage(creator.getSettings(), "res\\sprites\\paint_tools.png");
		BufferedImage icon;
		int size = 64;

		icon = sheet.getSubimage(0, 0, size, size);
		PaintButton pencil = new PaintButton(new PencilTool(creator), icon, maker);

		pencil.getButton().setToolTipText("<html>Left click to add a tile.<br>"
				+ "Middle click to sample highest tile.<br>"
				+ "Right click to remove highest tile.");

		maker.setCurrentTool(pencil.getTool());
		icons.add(pencil);

		icon = sheet.getSubimage(64, 0, size, size);
		PaintButton fill = new PaintButton(new FillAreaTool(creator), icon, maker);

		fill.getButton().setToolTipText("<html>Left click to add a tiles to area.<br>"
				+ "Middle click to remove highest tiles.<br>"
				+ "Right click to remove all tiles.");
		icons.add(fill);

		icon = sheet.getSubimage(0, 64, 64, 64);
		PaintButton move = new PaintButton(new MoveAreaTool(creator), icon, maker);
		move.getButton().setToolTipText("<html>Click and drag to move selected area.");
		icons.add(move);

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
