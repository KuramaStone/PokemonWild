package me.brook.PokemonCreator.input;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class InputHandler {

	public Point mousere;

	// Pressed is if it is currently held down
	// Clicked is if it has been clicked since last tick
	private boolean[] mousePressed, mouseReleased;
	private int mouseWheel = 0;
	private boolean[] isKeyPressed;
	
	private boolean hasMouseMovedSincePressed = false;
	private Point startClickPoint;
	
	public InputHandler(Component component) {
		
		MouseInput mouse = new MouseInput();
		component.addKeyListener(new KeyInput());
		component.addMouseListener(mouse);
		component.addMouseWheelListener(mouse);
		
		isKeyPressed = new boolean[1000];
		mousePressed = new boolean[10];
		mouseReleased = new boolean[10];
	}
	
	public void update() {
		mouseWheel = 0;
		
		for(int i = 0; i < mouseReleased.length; i++) {
			mouseReleased[i] = false;
		}
	}
	
	public class MouseInput extends MouseAdapter {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			mousere = e.getPoint();
			
			if(mousere.distance(startClickPoint) > 5) {
				hasMouseMovedSincePressed = true;
			}
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			mouseWheel = e.getUnitsToScroll();
		}
		

		@Override
		public void mousePressed(MouseEvent e) {
			mousePressed[e.getButton()] = true;
			startClickPoint = e.getPoint();
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			mouseReleased[e.getButton()] = true;
			mousePressed[e.getButton()] = false;
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
	
	public int getMouseWheel() {
		return mouseWheel;
	}
	
	public boolean hasMouseMovedSincePressed() {
		return hasMouseMovedSincePressed;
	}

	public boolean isLeftMousePressed() {
		return mousePressed[MouseEvent.BUTTON1];
	}

	public boolean isMiddleMousePressed() {
		return mousePressed[MouseEvent.BUTTON2];
	}

	public boolean isRightMousePressed() {
		return mousePressed[MouseEvent.BUTTON3];
	}
	
	public boolean isLeftMouseReleased() {
		return mouseReleased[MouseEvent.BUTTON1];
	}
	
	public boolean isMiddleMouseReleased() {
		return mouseReleased[MouseEvent.BUTTON2];
	}
	
	public boolean isRightMouseReleased() {
		return mouseReleased[MouseEvent.BUTTON3];
	}

}
