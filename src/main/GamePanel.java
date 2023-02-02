package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import entity.DialogueManager;
import entity.Entity;
import entity.Player;
import entity.Projectile;
import inputs.KeyHandler;
import inputs.MyMouseListener;
import levels.LevelManager;
import pathfinding2.Pathfinder;
import tile.TileManager;
import waves.WaveManager;

public class GamePanel extends JPanel implements Runnable {

	// SCREEN SETTINGS
	final int originalTileSize = 16;
	final int scale = 3;
	public final int tileSize = originalTileSize * scale;
	public final int maxScreenCol = 32;
	public final int maxScreenRow = 18;
	public int screenWidth = tileSize * maxScreenCol;
	public int screenHeight = tileSize * maxScreenRow;
	int FPS = 60;
	int FPSout;
	private final double FPS_SET = 120.0;
	private final double UPS_SET = 60.0;
	public float fullScreenOffsetFactor = 1f;

	// WORLD SETTINGS
	public final int maxWorldCol = 100;
	public final int maxWorldRow = 100;

	// ENTITIES
	public ArrayList<Entity> obj = new ArrayList<>();
	public ArrayList<Entity> zombies = new ArrayList<>();
	public ArrayList<Entity> entityList = new ArrayList<>();
	public ArrayList<Projectile> projectileList = new ArrayList<>();

	// CLASSES
	KeyHandler keyH = new KeyHandler(this);
	MyMouseListener myMouseListener = new MyMouseListener(this);
	public LevelManager levelManager = new LevelManager(this);
	public AnimationManager animManager = new AnimationManager(this);
	public Player player = new Player(this, keyH, myMouseListener);
	public TileManager tileM = new TileManager(this);
	public CollisionManager cChecker = new CollisionManager(this);
	public AssetSetterGetter aSetter = new AssetSetterGetter(this);
	public WaveManager wm = new WaveManager(this);
	public DialogueManager dialogueManager = new DialogueManager(this);
	public Pathfinder pathfinder = new Pathfinder(this);

	public UI ui = new UI(this, keyH, myMouseListener);

	Thread gameThread;
	public Sound music;
	public Sound se;

	// FULLSCREEN
	public int screenWidth2 = screenWidth;
	public int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;
	Graphics g;

	// GAMESTATES
	public int gameState;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	public final int gameOverState = 4;
	public final int craftState = 5;
	public final int startGameState = 6;
	public final int endGameState = 7;
	public final int menuState = 8;

	public GamePanel() {

		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.addKeyListener(keyH);
		this.addMouseListener(myMouseListener);
		this.addMouseMotionListener(myMouseListener);
		this.setFocusable(true);
		requestFocus();

		se = new Sound();
		se.setVolume(-25f);
		music = new Sound();
		music.setVolume(-14f);

	}

	public void reset() {
		System.out.println("Game Reset");
		player.reset();
		aSetter.setObject(levelManager.getCurrentLevel());
		wm.reset();
		gameState = menuState;

	}

	private Cursor createCursor() {
		Toolkit tkit = Toolkit.getDefaultToolkit();
		Image img1 = animManager.setup("crosshair_4", "crosshair");
		Point point = new Point(16, 16);
		Cursor cursor = tkit.createCustomCursor(img1, point, "crosshair");
		return cursor;
	}

	public void setupGame() {
		setFullScreen();
		animManager.initMiscAnims();
		Cursor cursor = createCursor();
		this.setCursor(cursor);

		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D) tempScreen.getGraphics();
		playMusic(0);
		gameState = menuState;
		levelManager.setupLevel(2);

	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();

	}

	@Override
	public void run() {
		
		
		
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long lastFrame = System.nanoTime();
		long lastUpdate = System.nanoTime();
		long lastTimeCheck = System.currentTimeMillis();
		long now;

		int frames = 0;
		int updates = 0;

		while (true) {
			now = System.nanoTime();
			// Render
			if (now - lastFrame >= timePerFrame) {
				drawToTempScreen();
				drawToScreen();
				lastFrame = now;
				frames++;
			} else {
				// we do nothing

			}
			// Update
			if (now - lastUpdate >= timePerUpdate) {
				try {
					update();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lastUpdate = now;
				updates++;

			}
			if (System.currentTimeMillis() - lastTimeCheck >= 1000) {

				System.out.println("FPS: " + frames + " UPS: " + updates);
				FPSout = updates;
				frames = 0;
				updates = 0;
				lastTimeCheck = System.currentTimeMillis();
				
			}
		}
		
		
		
		
		
		
		
		
		
		
		
//		double drawInterval = 1000000000 / FPS;
//		double delta = 0;
//		long lastTime = System.nanoTime();
//		long currentTime;
//		long timer = 0;
//		int drawCount = 0;
//
//		while (gameThread != null) {
//			currentTime = System.nanoTime();
//			delta += (currentTime - lastTime) / drawInterval;
//			timer += (currentTime - lastTime);
//			lastTime = currentTime;
//			if (delta >= 1) {
//
//				try {
//					update();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				drawToTempScreen();
//				drawToScreen();
//				delta--;
//				drawCount++;
//			}
//
//			if (timer >= 1000000000) {
//				FPSout = drawCount;
//				// System.out.println(FPSout);
//				drawCount = 0;
//				timer = 0;
//			}
//
//		}

	}

	public void update() throws IOException {

		if (gameState == playState) {
			player.update();
			tileM.update();
			wm.update();

			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i).alive) {
					projectileList.get(i).update();
				} else {
					projectileList.remove(i);
				}
			}
			for (int i = 0; i < zombies.size(); i++) {
				if (zombies.get(i).alive) {
					zombies.get(i).update();
				} else {
					zombies.remove(i);
				}
			}

			for (Entity element : obj) {
				element.update();
			}

		}
		if (gameState == dialogueState) {
			player.update();
		}
		if (gameState == gameOverState) {
			player.update();
		}
		if (gameState == menuState) {
			tileM.update();
			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i).alive) {
					projectileList.get(i).update();
				} else {
					projectileList.remove(i);
				}
			}
			for (int i = 0; i < zombies.size(); i++) {
				if (zombies.get(i).alive) {
					zombies.get(i).update();
				} else {
					zombies.remove(i);
				}
			}

			for (Entity element : obj) {
				element.update();
			}
		}

	}

	public void drawToTempScreen() {

		// Debug
		long drawStart = 0;
		drawStart = System.nanoTime();
		tileM.draw(g2);

		for (Entity e : entityList) {
			if (!entityList.isEmpty())
				e.draw(g2);
		}
		entityList.clear();

		for (int i = 0; i < projectileList.size(); i++) {
			if (projectileList.get(i).alive) {
				projectileList.get(i).draw(g2);
			} else {
				projectileList.remove(i);
			}

		}

		// Draw order of entity list
		// player
		entityList.add(player);
		// objects
		for (int i = 0; i < obj.size(); i++) {
			if (obj.get(i) != null)
				entityList.add(obj.get(i));
		}
		// zombies
		for (Entity e : zombies) {
			entityList.add(e);
		}
		// Sort
		Collections.sort(entityList, new Comparator<Entity>() {
			@Override
			public int compare(Entity e1, Entity e2) {
				int result = Integer.compare(e1.worldY, e2.worldY);
				return result;
			}
		});
		//wm.draw(g2);
		ui.draw(g2);

		long drawEnd = System.nanoTime();
		long passed = drawEnd - drawStart;
		// System.out.println("Draw to img: " + passed/1000000);

	}

	public void drawToScreen() {
		long drawStart = 0;
		drawStart = System.nanoTime();
		g = Main.window.getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();

		long drawEnd = System.nanoTime();
		long passed = drawEnd - drawStart;
		// System.out.println("Draw to screen: " + passed/1000000);
	}

	public void setFullScreen() {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		Main.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		screenWidth2 = (int) width;
		screenHeight2 = (int) height;
		fullScreenOffsetFactor = (float) screenWidth / (float) screenWidth2;
	}

	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}

	public void stopMusic() {
		music.stop();
	}

	public void playSE(int i) {
		se.setFile(i);
		se.play();
	}
}
