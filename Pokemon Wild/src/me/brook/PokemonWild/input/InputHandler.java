package me.brook.PokemonWild.input;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {
	
	private boolean keys[] = new boolean[256];
	
	public void setKey(int key, boolean bool) {
		keys[key] = bool;
	}
	
	public InputHandler(Component c) {
		c.addKeyListener(this);
	}

	public boolean isKeyDown(int keyCode) {
		if(keyCode > 0 && keyCode < 256) {
			return keys[keyCode];
		}

		return false;
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() > 0 && e.getKeyCode() < 256) {
			keys[e.getKeyCode()] = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() > 0 && e.getKeyCode() < 256) {
			keys[e.getKeyCode()] = false;
		}
	}

}