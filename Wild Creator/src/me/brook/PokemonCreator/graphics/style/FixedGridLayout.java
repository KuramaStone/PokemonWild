package me.brook.PokemonCreator.graphics.style;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.List;

public class FixedGridLayout implements LayoutManager2 {

	private int gridwidth, gridheight;
	private int gridSizeWidth, gridSizeHeight;

	private List<Component> components;

	public FixedGridLayout(int gridwidth, int gridheight, int gridSizeWidth, int gridSizeHeight) {
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
		this.gridSizeWidth = gridSizeWidth;
		this.gridSizeHeight = gridSizeHeight;

		components = new ArrayList<>();
	}

	private void rebuild() {

		int i = 0;
		for(Component comp : components) {
			int x = (i % gridwidth) * gridSizeWidth;
			int y = (i / gridwidth) * gridSizeHeight;
			comp.setLocation(x, y);
			comp.setSize(gridSizeWidth, gridSizeHeight);
			i++;
		}

	}

	@Override
	public void layoutContainer(Container parent) {

	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(gridwidth * gridSizeWidth, gridheight * gridSizeHeight);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(gridwidth * gridSizeWidth, gridheight * gridSizeHeight);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		components.remove(comp);
		rebuild();
	}

	@Override
	public void addLayoutComponent(Component comp, Object arg1) {
		components.add(comp);
		rebuild();
	}

	@Override
	public float getLayoutAlignmentX(Container arg0) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container arg0) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container arg0) {

	}

	@Override
	public Dimension maximumLayoutSize(Container arg0) {
		return null;
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		addLayoutComponent(comp, name);
	}

}
