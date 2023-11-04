package me.waff.games.pong;

import javax.swing.JFrame;

public class Window {

	private JFrame frame;
	
	public Window(String title, Game game){
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}
