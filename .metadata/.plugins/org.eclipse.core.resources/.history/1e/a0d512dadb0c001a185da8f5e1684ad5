package me.brook.PokemonCreator.graphics.style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;

import me.brook.PokemonCreator.world.tile.TileData;

public class ToggleButtonDesign extends BasicButtonUI {

	private TileData data;

	public ToggleButtonDesign(TileData data) {
		this.data = data;
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		AbstractButton ab = (AbstractButton) c;
		ab.setOpaque(false);
		ab.setBorder(new EmptyBorder(5, 15, 5, 15));
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		AbstractButton ab = (AbstractButton) c;
		paintBackground(g, ab, ab.getModel().isSelected() ? 2 : 0);
		super.paint(g, c);
	}

	@Override
	protected void paintText(Graphics g, JComponent c, Rectangle rect, String text) {
		AbstractButton b = (AbstractButton) c;
		Font f = b.getFont();
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics(f);

		if(!b.isSelected())
			rect.y -= 2;

		if(b.isEnabled()) {
			g.setColor(b.getForeground());
			BasicGraphicsUtils.drawString(g, text, 0, rect.x, rect.y + fm.getAscent());
		}
		else {
			String prefix = getPropertyPrefix();
			g.setColor(UIManager.getColor(prefix + "disabledText"));
			BasicGraphicsUtils.drawString(g, text, 0, rect.x, rect.y + fm.getAscent());
		}
	}

	private void paintBackground(Graphics g, AbstractButton ab, int yOffset) {
		Dimension size = ab.getSize();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(1.5f));

		if(ab.isSelected()) {
			g.drawImage(data.getCurrentImage(), 2, 2, size.width - 2, size.height - 2, null);
			g.setColor(new Color(0, 0, 255, 200));
			g.drawRect(0, 0, size.width - 1, size.height - 1);
		}
		else {
			g.drawImage(data.getCurrentImage(), 0, 0, size.width, size.height, null);
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, size.width - 1, size.height - 1);
		}
	}

}