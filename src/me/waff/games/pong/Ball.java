package me.waff.games.pong;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Ball {

	private int x,y;
	private int width, height;
	private float velX, velY;
	
	private final float moveSpeed = 5f;
	
	private int turn;
	private Rectangle bounds;
	
	private Game game;
	
	private boolean scoredYet = false;
	
	public Ball(int x, int y, int turn){
		this.x = x;
		this.y = y;
		this.width = 10;
		this.height = 10;
		this.velY = 0f;
		this.game = Game.theGame;
		
		this.turn = turn;
		
		switch (turn){
		case 1:
			this.velX = -moveSpeed;
			break;
		case 2:
			this.velX = moveSpeed;
			break;
		default:
			this.velX = moveSpeed;
			break;
		}
		
		this.bounds = new Rectangle(x, y, width, height);
		
	}
	
	public void tick(){
		
		Rectangle paddle1Bounds = game.getPaddle(1).getBounds();
		Rectangle paddle2Bounds = game.getPaddle(2).getBounds();
		
		if (paddle1Bounds.intersects(bounds) || bounds.intersects(paddle1Bounds))
			calculateReflectionVelocity(1);
		if (paddle2Bounds.intersects(bounds) || bounds.intersects(paddle2Bounds))
			calculateReflectionVelocity(2);
		
		x += Math.round(velX);
		y += Math.round(velY);
		
		bounds.x = x;
		bounds.y = y;
		
		if (!scoredYet){
			if (x > Game.WIDTH){
				game.score(1);
				scoredYet = true;
			}
			else if (x < 0) {
				game.score(2);
				scoredYet = true;
			}
				
			if (y > Game.HEIGHT){
				if (x > (Game.WIDTH / 2))
					game.score(1);
				else if (x < (Game.WIDTH / 2))
					game.score(2);
				scoredYet = true;
			} else if (y < 0){
				if (x > (Game.WIDTH / 2))
					game.score(1);
				else if (x < (Game.WIDTH / 2))
					game.score(2);
				scoredYet = true;
			}
		}
		
	}
	
	public void render(Graphics2D g2d){
		g2d.setColor(Color.WHITE);
		g2d.fill(bounds);
	}
	
	private void calculateReflectionVelocity(int paddle){
		
		Rectangle paddleBounds = game.getPaddle(paddle).getBounds(); 
		
		int paddleY = paddleBounds.y;
		int paddleHeight = paddleBounds.height;
		
		int relativeHeight = y - paddleY;
		float relation = (float)relativeHeight / (float)paddleHeight;			
		
		int angle = 0;
		
		if (relation >= 0 && relation < 0.1f)
			angle = 35;
		else if (relation >= 0.1f && relation < 0.2f)
			angle = 30;
		else if (relation >= 0.2f && relation < 0.3f)
			angle = 25;
		else if (relation >= 0.3f && relation < 0.4f)
			angle = 20;
		else if (relation >= 0.4f && relation < 0.5f)
			angle = 15;
		else if (relation >= 0.5f && relation < 0.6f)
			angle = 0;
		else if (relation >= 0.6f && relation < 0.7f)
			angle = -15;
		else if (relation >= 0.7f && relation < 0.8f)
			angle = -20;
		else if (relation >= 0.8f && relation < 0.9f)
			angle = -25;
		else if (relation >= 0.9f && relation < 1f)
			angle = -30;
		else if (relation >= 1f)
			angle = -35;
		
		velX = ((float)Math.cos(Math.toRadians(angle)))*moveSpeed;
		if (paddle == 2)
			velX *= -1;
		
		velY = (-(float)Math.sin(Math.toRadians(angle)))*moveSpeed;
					
	}
	
}
