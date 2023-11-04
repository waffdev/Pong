package me.waff.games.pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final String TITLE = "Pong";
	
	public static Game theGame;
	
	private Window window;
	
	private boolean isRunning = false;
	private Thread thread;
	private InputManager inputManager;
	
	private final boolean debugFPS = false;
	
	public InputManager getInputManager() { return this.inputManager; }
	
	public static void main(String[] args){
		theGame = new Game();
	}
	
	private Ball ball;
	
	private Paddle paddle1;
	private Paddle paddle2;
	private int score1;
	private int score2;
	
	private int gameState;
	private int ticksWaiting = 0;
	
	private Font scoreFont;
	
	public Game(){
		Dimension size = new Dimension(WIDTH, HEIGHT);
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		this.window = new Window(TITLE, this);
		this.inputManager = new InputManager();
		this.addKeyListener(inputManager);
		
		try {
			scoreFont = Font.createFont(Font.TRUETYPE_FONT, Game.class.getResourceAsStream("/fonts/pong-score.ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		scoreFont = scoreFont.deriveFont(Font.PLAIN, 80);
		
		this.score1 = 0;
		this.score2 = 0;
		
		start();
		
	}
	
	public void run(){
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60;
		int frames = 0;
		int ticks = 0;
		long lastTimer1 = System.currentTimeMillis();

		init();

		while (isRunning) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while (unprocessed >= 1) {
				ticks++;
				tick();
				unprocessed -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				
				if (debugFPS)
					System.out.println(ticks + " ticks, " + frames + " fps");
				
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	private synchronized void start(){
		isRunning = true;
		this.thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop(){
		isRunning = false;
		System.exit(0);
	}
	
	public void init(){
		this.paddle1 = new Paddle(5, 50, 1);
		this.paddle2 = new Paddle(Game.WIDTH - 15, 50, 2);
		this.ball = new Ball(Game.WIDTH / 2, Game.HEIGHT / 2, 1);
		this.gameState = GameState.INGAME;
	}
	
	public void tick(){
		this.requestFocus();
			
		if (gameState == GameState.INGAME){
			paddle1.tick();
			paddle2.tick();
			ball.tick();
		} else if (gameState == GameState.WAITING){
			if (ticksWaiting > 2*60){ 
				ticksWaiting = 0;
				init();
			}
			
			ticksWaiting++;
		}
		
	}
	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null){
			this.createBufferStrategy(2);
			return;
		}
		
		Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		
		g2d.setColor(Color.WHITE);
		g2d.setFont(scoreFont);
		g2d.drawString(Integer.toString(score1), (Game.WIDTH / 4) + 75, 100);
		g2d.drawString(Integer.toString(score2), Game.WIDTH - (Game.WIDTH / 4) - 120, 100);
		
		renderDivider(g2d);

		if (gameState == GameState.INGAME){
			paddle1.render(g2d);
			paddle2.render(g2d);
			ball.render(g2d);
		}
		
		g2d.dispose();
		bs.show();
	}
	
	private void renderDivider(Graphics2D g2d){
		int squareSize = 10;
		g2d.setColor(Color.WHITE);
		for (int i = 0; i < (Game.HEIGHT - 2*squareSize) / squareSize; i++){
			g2d.fillRect((Game.WIDTH / 2) - squareSize / 2, squareSize + 2*(i*squareSize), squareSize, squareSize);
		}
		
	}
	
	public Paddle getPaddle(int player){
		switch (player){
		case 1:
			return paddle1;
		case 2:
			return paddle2;
		default:
			return paddle1;
		}
		
	}
	
	public void score(int player){
		switch (player){
		case 1:
			score1++;
			break;
		case 2:
			score2++;
			break;
		default:
			break;
		}
		resetGame();
	}
	
	private void resetGame(){
		gameState = GameState.WAITING;	
		ball = null;
		paddle1 = null;
		paddle2 = null;
	}
	
}
