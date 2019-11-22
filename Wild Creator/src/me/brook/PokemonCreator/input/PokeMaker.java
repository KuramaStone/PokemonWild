package me.brook.PokemonCreator.input;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

import me.brook.PokemonCreator.PokemonCreator;
import me.brook.PokemonCreator.graphics.PokeDrawer;
import me.brook.PokemonCreator.input.tool.PaintPanel;
import me.brook.PokemonCreator.input.tool.PaintTool;
import me.brook.PokemonCreator.toolbox.Tools;
import me.brook.PokemonCreator.world.PokeConnection;
import me.brook.PokemonCreator.world.PokeConnection.LoadingFilter;
import me.brook.PokemonCreator.world.PokeWorld;
import me.brook.PokemonCreator.world.area.PokeArea;
import me.brook.PokemonCreator.world.tile.TileData;
import me.brook.PokemonCreator.world.tile.TileType;

public class PokeMaker {

	private PokemonCreator creator;
	private PokeWorld world;
	private PokeDrawer drawer;
	private TileScroller tileScroller;

	private PaintTool currentTool;
	private TileData currentTileData = new TileData(TileType.GRASS, 0);
	private PokeArea currentArea;

	public PokeMaker(PokemonCreator creator) {
		this.creator = creator;
		world = creator.getWorld();
		drawer = creator.getDrawer();
	}

	public void tick(InputHandler input) {
		if(input == null) {
			return;
		}

		handleMouse(input);
		handleKeys(input);
	}

	private void handleMouse(InputHandler input) {

		if(currentTool != null) {
			currentTool.handleInput(input);
		}

		if(input.getMouseWheel() != 0) {
			boolean zoomIn = input.getMouseWheel() > 0;

			double zoomAmount = 0.05;
			if(zoomIn) {
				drawer.addToZoom(zoomAmount);
			}
			else {
				drawer.addToZoom(-zoomAmount);
			}
		}
	}

	private void handleKeys(InputHandler input) {
		if(input.isKeyPressed(KeyEvent.VK_W)) {
			drawer.addYOffset(1);
		}
		else if(input.isKeyPressed(KeyEvent.VK_S)) {
			drawer.addYOffset(-1);
		}
		else if(input.isKeyPressed(KeyEvent.VK_A)) {
			drawer.addXOffset(1);
		}
		else if(input.isKeyPressed(KeyEvent.VK_D)) {
			drawer.addXOffset(-1);
		}
	}

	private JComboBox<PokeArea> areaSelector;

	public void updateAreaSelector() {
		areaSelector.removeAllItems();
		for(PokeArea area : world.getAreas()) {
			areaSelector.addItem(area);
		}
	}

	public JPanel addOptionComponents(GridBagConstraints gbc) throws IOException {

		JButton addArea = new JButton("Add Area");
		addArea.setPreferredSize(new Dimension(100, 40));
		addArea.setFocusable(false);
		addArea.addActionListener(addAreaListener);

		JButton deleteArea = new JButton("Remove Area");
		deleteArea.setPreferredSize(new Dimension(128, 40));
		deleteArea.setFocusable(false);
		deleteArea.addActionListener(deleteAreaListener);

		JLabel selectArea = new JLabel("Current Area: ");
		selectArea.setFocusable(false);
		this.areaSelector = new JComboBox<>();
		this.areaSelector.setFocusable(false);
		areaSelector.addActionListener(areaSelectorListener);

		JButton save = new JButton(new ImageIcon(Tools.readImage("res\\misc\\save_icon.png")));
		save.setPreferredSize(new Dimension(64, 64));
		save.setFocusable(false);
		save.addActionListener(saveConnectionsListener);

		JButton open = new JButton(new ImageIcon(Tools.readImage("res\\misc\\open_icon.png")));
		open.setPreferredSize(new Dimension(64, 64));
		open.setFocusable(false);
		open.addActionListener(openAreaListener);

		JToggleButton highlight = new JToggleButton("Highlight Tiles");
		highlight.setFocusable(false);
		highlight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				drawer.setHighlightTiles(highlight.isSelected());
			}
		});

		JPanel options = new JPanel();
		options.setLayout(new GridBagLayout());

		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.insets = new Insets(10, 2, 2, 2);
		gbc.gridx = 0;
		gbc.gridy = 0;
		options.add(save, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		options.add(open, gbc);

		gbc.insets = new Insets(50, 50, 2, 2);
		gbc.gridx = 0;
		gbc.gridy = 1;
		options.add(selectArea, gbc);

		gbc.insets = new Insets(50, 2, 2, 2);
		gbc.gridx = 1;
		gbc.gridy = 1;
		options.add(areaSelector, gbc);

		gbc.insets = new Insets(10, 2, 2, 2);

		gbc.gridx = 0;
		gbc.gridy = 2;
		options.add(addArea, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		options.add(deleteArea, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		highlight.setPreferredSize(new Dimension(128, 40));
		options.add(highlight, gbc);

		gbc.insets = new Insets(50, 2, 2, 2);

		JScrollPane tileSelector = new JScrollPane(tileScroller = new TileScroller(this),
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tileSelector.getVerticalScrollBar().setUnitIncrement(8);
		tileSelector.setFocusable(false);
		tileSelector.setPreferredSize(new Dimension(320, 300));
		gbc.gridwidth = (int) tileSelector.getPreferredSize().getWidth();
		gbc.gridheight = (int) tileSelector.getPreferredSize().getHeight();
		gbc.gridx = 0;
		gbc.gridy = 4;
		options.add(tileSelector, gbc);

		return options;
	}

	public JPanel addPaintComponents(GridBagConstraints gbc) {

		JScrollPane selector = new JScrollPane(new PaintPanel(creator, this),
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		selector.setFocusable(false);
		selector.setPreferredSize(new Dimension(128, 640));

		JPanel paint = new JPanel();
		paint.add(selector, BorderLayout.CENTER);

		return paint;
	}

	private ActionListener openAreaListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(world.getAreas().size() > 0) {
				int value = JOptionPane.showConfirmDialog(creator.getWindow(),
						"Are you certain you want to do this?\n"
								+ "Doing this will cause currently open areas to be permanently lost if they are not saved.",
						"Confirmation", JOptionPane.YES_NO_OPTION);

				if(value != JOptionPane.OK_OPTION) {
					return;
				}
			}

			JPanel input = new JPanel();

			JTextField areaName = new JTextField("Area name", 32);
			input.add(areaName);

			JFileChooser chooser = new JFileChooser(creator.getSettings().getFolder());
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(new LoadingFilter());

			int result = chooser.showOpenDialog(creator.getWindow());

			if(result == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				world.getConnections();
				world.setConnections(PokeConnection.open(file));
			}
		}
	};

	private ActionListener saveConnectionsListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			JPanel input = new JPanel();

			JTextField areaName = new JTextField("Connection name", 32);
			input.add(areaName);

			int result = JOptionPane.showConfirmDialog(creator.getWindow(), input, "Connection name",
					JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION) {
				String name = areaName.getText();

				if(world.isConnectionNameTaken(name)) {
					result = JOptionPane.showConfirmDialog(creator.getWindow(),
							"That name already exists! Would you like to overwrite it?",
							"Confirmation", JOptionPane.YES_NO_OPTION);
					if(result == JOptionPane.YES_OPTION) {
						actionPerformed(e);
					}
				}
				else if(name.isEmpty()) {
					JOptionPane.showMessageDialog(creator.getWindow(), "You gotta type a name, silly goose!");
					actionPerformed(e);
				}
				else {
					world.getConnections().save(creator.getSettings(), name);
					JOptionPane.showMessageDialog(creator.getWindow(), "Connections and areas have been saved!");
				}

			}

		}
	};

	private ActionListener deleteAreaListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentArea == null) {
				return;
			}

			String message = String.format(
					"Are you sure you want to remove '%s'? If you wish to keep it, then save it first.",
					currentArea.getAreaName());
			int result = JOptionPane.showConfirmDialog(creator.getWindow(), message, "Confirmation",
					JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.OK_OPTION) {
				world.removeArea(currentArea);
				currentArea = null;
			}
		}
	};

	private ActionListener addAreaListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JPanel input = new JPanel();

			JTextField areaName = new JTextField("Area name", 32);
			input.add(areaName);

			int result = JOptionPane.showConfirmDialog(creator.getWindow(), input, "Area data",
					JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION) {
				String name = areaName.getText();

				if(world.isAreaNameTaken(name)) {
					JOptionPane.showMessageDialog(creator.getWindow(), "That name already exists! :(");
					actionPerformed(e);
				}
				else if(!name.isEmpty()) {
					world.addArea(new PokeArea(name, new ArrayList<>()));
				}
			}
		}
	};

	private ActionListener areaSelectorListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			PokeArea selected = (PokeArea) ((JComboBox<PokeArea>) e.getSource()).getSelectedItem();

			PokeMaker.this.currentArea = selected;
		}
	};

	public void setCurrentType(TileData data) {
		this.currentTileData = data;
	}

	public TileData getCurrentTileData() {
		return currentTileData;
	}

	public TileScroller getTileScroller() {
		return tileScroller;
	}

	public PaintTool getCurrentTool() {
		return currentTool;
	}

	public PokeArea getCurrentArea() {
		return currentArea;
	}

	public void setCurrentArea(PokeArea currentArea) {
		this.currentArea = currentArea;
	}

	public void setCurrentTool(PaintTool currentTool) {
		this.currentTool = currentTool;
	}

}
