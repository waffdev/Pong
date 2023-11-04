package me.waff.games.pong;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Paddle {

	private int x,y;
	private int width, height;
	private float velY;
	
	private Rectangle bounds;
	private Game game;
	
	private int playerNum; // either player 1 or player 2 (1 or 2)
	
	public Paddle(int x, int y, int playerNum){
		this.x = x;
		this.y = y;
		this.width = 10;
		this.height = 50;
		this.velY = 0f;
		this.playerNum = playerNum;
		
		this.game = Game.theGame;
		
		bounds = new Rectangle(x, y, width, height);
	}
	
	public void tick(){
		if (game.getInputManager().getPlayerKeyDirection(playerNum) == KeyDirection.UP)
			velY = -5f;
		else if (game.getInputManager().getPlayerKeyDirection(playerNum) == KeyDirection.DOWN)
			velY = 5f;
		else if (game.getInputManager().getPlayerKeyDirection(playerNum) == KeyDirection.NONE)
			velY = 0f;
		
		y += velY;
		
		bounds.x = x;
		bounds.y = y;
		
		if (y < 0)
			y = 0;
		
		if ((y + bounds.height) > Game.HEIGHT)
			y = Game.HEIGHT - bounds.height;
	}
	
	public void render(Graphics2D g2d){
		g2d.setColor(Color.WHITE);
		g2d.fill(bounds);
	}
	
	public Rectangle getBounds() { return this.bounds; } 
}
