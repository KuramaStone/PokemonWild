package me.brook.PokemonWild.entity;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import me.brook.PokemonWild.PokemonWild;
import me.brook.PokemonWild.input.InputHandler;

public class Player extends Entity {

	/*
	 * Movement
	 */
	private int direction = 0, mode = 1;
	private int movementFrame = 0;

	private boolean isRunning, finishedMoving;

	private boolean isLeftFoot; // This variable checks which foot should be animated to move
	private int turningFrame = 0;

	/*
	 * Input
	 */
	private InputHandler input;

	public Player(PokemonWild wild) {
		super(wild);
		input = wild.getKeyInput();
	}

	public void tick() {
		move();

		super.tick();
	}

	private void move() {
		finishedMoving = false; // Reset finishedMoving

		if(!isMoving()) {
			if(turningFrame == 0) {
				detectMovement();
			}
		}
		else {
			// Do movement animation
			updateMovementAnimation();
		}

		if(turningFrame > 0) {
			if(--turningFrame == 0) {
				mode = 1;
			}
			else {
				changeFoot();
			}
		}
	}

	private void detectMovement() {
		int d = -1;

		if(input.isKeyDown(KeyEvent.VK_W)) {
			d = 1;
		}
		else if(input.isKeyDown(KeyEvent.VK_S)) {
			d = 0;
		}
		else if(input.isKeyDown(KeyEvent.VK_A)) {
			d = 2;
		}
		else if(input.isKeyDown(KeyEvent.VK_D)) {
			d = 3;
		}
		else {
			return;
		}

		isRunning = input.isKeyDown(KeyEvent.VK_SHIFT);

		// If different direction, don't move immediately unless running
		if(this.direction != d && !isRunning) {
			turningFrame = 2;
			this.direction = d;
			return;
		}

		this.direction = d;

		movementFrame = isRunning ? 4 : 8;
	}

	private void changeFoot() {
		mode = isLeftFoot ? 0 : 2;
		isLeftFoot = !isLeftFoot;
	}

	private void updateMovementAnimation() {
		if(movementFrame >= 0) {
			movePlayer(isRunning ? 12 : 6);

			if(movementFrame == (isRunning ? 3 : 6)) {
				changeFoot();
			}

			movementFrame--;
		}

		if(movementFrame == 0) {
			finishedMoving = true;
		}

		if(finishedMoving) {
			mode = 1;
			isRunning = false;
		}
	}

	private boolean isMoving() {
		return movementFrame > 0;
	}

	private void movePlayer(int i) {
		if(direction == 1) {
			window.addYOffset(i);
		}
		else if(direction == 0) {
			window.addYOffset(-i);
		}
		else if(direction == 2) {
			window.addXOffset(i);
		}
		else if(direction == 3) {
			window.addXOffset(-i);
		}
	}

	public BufferedImage[][] getCurrentAnimation() {
		return isRunning ? wild.getSprites().getPlayerRun() : wild.getSprites().getPlayerWalk();
	}

	public int getDirection() {
		return direction;
	}

	public int getMode() {
		return mode;
	}

}
