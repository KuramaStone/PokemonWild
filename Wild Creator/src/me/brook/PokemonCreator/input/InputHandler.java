package me.brook.PokemonCreator.input;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputHandler {

	public Point mouse;

	private boolean isLeftMousePressed, isMiddleMousePressed, isRightMousePressed;
	private boolean isLeftClicked, isMiddleClicked, isRightClicked;
	private boolean[] isKeyPressed;
	
	public InputHandler(Component component) {
		component.addKeyListener(new KeyInput());
		component.addMouseListener(new MouseInput());
		
		isKeyPressed = new boolean[1000];
	}
	
	public void update() {
		isLeftClicked = false;
		isMiddleClicked = false;
		isRightClicked = false;
	}
	
	public class MouseInput extends MouseAdapter {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			mouse = e.getPoint();
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				isLeftClicked = true;
			}
			else if(e.getButton() == MouseEvent.BUTTON3) {
				isRightClicked = true;
			}
			else if(e.getButton() == MouseEvent.BUTTON2) {
				isMiddleClicked = true;
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				isLeftMousePressed = true;
			}
			else if(e.getButton() == MouseEvent.BUTTON3) {
				isRightMousePressed = true;
			}
			else if(e.getButton() == MouseEvent.BUTTON2) {
				isMiddleMousePressed = true;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				isLeftMousePressed = false;
			}
			else if(e.getButton() == MouseEvent.BUTTON3) {
				isRightMousePressed = false;
			}
			else if(e.getButton() == MouseEvent.BUTTON2) {
				isMiddleMousePressed = false;
			}
		}
	}

	public class KeyInput implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			isKeyPressed[e.getKeyCode()] = true;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			isKeyPressed[e.getKeyCode()] = false;
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	}

	public boolean isKeyPressed(int keycode) {
		return isKeyPressed[keycode];
	}

	public boolean isMiddleMousePressed() {
		return isMiddleMousePressed;
	}
	
	public boolean isLeftMousePressed() {
		return isLeftMousePressed;
	}
	
	public boolean isRightMousePressed() {
		return isRightMousePressed;
	}

	public boolean isLeftMouseClicked() {
		return isLeftClicked;
	}

	public boolean isMiddleMouseClicked() {
		return isMiddleClicked;
	}

	public boolean isRightMouseClicked() {
		return isRightClicked;
	}

}