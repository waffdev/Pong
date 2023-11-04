package me.waff.games.pong;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManager implements KeyListener {

	private boolean p1Up = false;
	private boolean p1Down = false;
	private boolean p2Up = false;
	private boolean p2Down = false;
	
	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			p1Up = true;
		else if (e.getKeyCode() == KeyEvent.VK_S)
			p1Down = true;
		
		if (e.getKeyCode() == KeyEvent.VK_UP)
			p2Up = true;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			p2Down = true;
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			p1Up = false;
		else if (e.getKeyCode() == KeyEvent.VK_S)
			p1Down = false;
		
		if (e.getKeyCode() == KeyEvent.VK_UP)
			p2Up = false;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			p2Down = false;
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
			Game.theGame.stop();
		}
	}

	public int getPlayerKeyDirection(int player){ 
		if (player == 1){
			if (p1Up){
				return KeyDirection.UP;
			} else if (p1Down){
				return KeyDirection.DOWN;
			}
			
			return KeyDirection.NONE;
		} else if (player == 2){
			if (p2Up){
				return KeyDirection.UP;
			} else if (p2Down){
				return KeyDirection.DOWN;
			}
			
			return KeyDirection.NONE;
		}
		
		return KeyDirection.NONE;
	}
	
}
