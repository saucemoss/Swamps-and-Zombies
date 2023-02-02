package main;

import javax.swing.JFrame;
import javax.swing.WindowConstants;


public class Main{
public static JFrame window;
	public static void main(String[] args) {
		window = new JFrame();
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Swamp&Zombies");
		window.setUndecorated(true);
		
		GamePanel gamePanel = new GamePanel();
		window.add(gamePanel);
		window.pack();		
		gamePanel.setupGame();
		gamePanel.startGameThread();
		//window.setLocationRelativeTo(null);
		window.setVisible(true);

	}

}
