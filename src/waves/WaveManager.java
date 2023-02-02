package waves;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.Entity;
import main.AssetSetterGetter;
import main.GamePanel;

public class WaveManager {
	GamePanel gp;
	Graphics2D g2;
	BufferedImage image;
	BufferedImage imageLight;
	public boolean night, allZombiesCleared, tToDay, tToNight;
	public int barrelsThisWave;
	public int barrelsDeposited;
	private int tCounter = 0;
	public int currentWave = 0;
	float opacity;
	public ArrayList<Wave> waves;

	public WaveManager(GamePanel gp) {
		this.gp = gp;
		try {
			image = ImageIO.read(getClass().getResource("/night/night4.png"));
			image = gp.player.uTool.scaleImage(image, gp.screenWidth, gp.screenHeight);
			imageLight = ImageIO.read(getClass().getResource("/night/light1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		opacity = 1f;
		configureWaves();
		setupWaves();
		night = true;
		tToNight = false;

	}

	private void setupWaves() {

		tToNight = false;
		tCounter = 0;
		waves.get(currentWave).spawnObjects();
		barrelsThisWave = waves.get(currentWave).barrels;

	}

	private void configureWaves() {
		waves = new ArrayList<>();
		gp.animManager.initMiscAnims();

		for (int i = 0; i < 50; i++) {
			Wave wave = new Wave(i+1, i+1, i+1*10, i * 2, i + 1);
			waves.add(wave);
		}


	}

	public void update() {

		nightTransitions();
		if (barrelsDeposited == barrelsThisWave && barrelsDeposited > 0) {
			if (!waves.get(currentWave).dayStarted) {
				waves.get(currentWave).startDayWave();
			}
		}

		if (allZombiesCleared && !night) {
			if (currentWave < waves.size()) {
				currentWave++;
				setupWaves();
				allZombiesCleared = false;
			} else {
				gp.gameState = gp.endGameState;
			}
		}

		if (gp.zombies.size() == 0 && barrelsDeposited == barrelsThisWave && barrelsDeposited > 0
				&& waves.get(currentWave).dayStarted) {
			allZombiesCleared = true;
		}

	}

	private void nightTransitions() {

		tCounter++;
		if (night && tToDay && tCounter < 100 && opacity > 0.01) {
			// transition to day
			tToNight = false;
			opacity -= 0.01;
			if (tCounter == 99) {
				night = false;
				gp.playSE(11);
			}

		} else if (!night && tToNight && tCounter < 100 && opacity < 1) {
			// transition to night
			tToDay = false;
			opacity += 0.01f;
			if (tCounter == 99) {
				night = true;
				gp.playSE(11);
			}
		}

	}

	public void draw(Graphics2D g2) {

		if (opacity > 0.1f) {
			//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g2.drawImage(image, 0, 0, null);
			//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}

	}

	public class Wave {
		public int waveNo;
		public int zombies;
		public int barrels;
		public int spawners;
		public int crates;
		public boolean dayStarted;

		Wave(int waveNo, int barrels, int zombies, int spawners, int crates) {
			this.waveNo = waveNo;
			this.zombies = zombies;
			this.barrels = barrels;
			this.spawners = spawners;
			this.crates = crates;
		}

		public void spawnObjects() {
			gp.aSetter.spawnWaveObjects(barrels, zombies, spawners, crates);

		}

		public void startDayWave() {
			tCounter = 0;
			tToDay = true;
			dayStarted = true;
			allZombiesCleared = false;
			for (Entity e : gp.zombies) {
				e.agroRange = 5000;
			}
			for (Entity o : gp.obj) {
				if (o.name.equals("Zombie Spawner")) {
					o.spawning = true;
				}
			}

		}

	}

	public void reset() {
		currentWave = 0;
		waves.clear();
		configureWaves();
		setupWaves();
		tCounter = 0;
		barrelsDeposited = 0;
		opacity = 1f;
		tToNight = true;

	}
}
