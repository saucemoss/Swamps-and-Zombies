package levels;

import main.GamePanel;

public class LevelManager {
	GamePanel gp;
	int currentLevel = 0;
	public int mapDimension = 0;
	private final static String[] maps = new String[10];
	
	public LevelManager(GamePanel gp) {
		this.gp = gp;
		
		maps[0] = "/maps/mapkaDemo2.csv";
		maps[1] = "/maps/map2.csv";
		maps[2] = "/maps/menumap.csv";
		maps[3] = "/maps/smalltest.csv";

	}
	
	public void setupLevel(int levelNo) {
		currentLevel = levelNo;
		setConfig(levelNo);
		gp.tileM.loadMap(maps[levelNo], mapDimension);
		gp.aSetter.setObject(levelNo);
		gp.wm.reset();
		
		
	}

	private void setConfig(int levelNo) {
		switch(levelNo) {
		case 0:
			gp.player.invisible = false;
			gp.player.worldX = gp.tileSize * 48;
			gp.player.worldY = gp.tileSize * 30;
			mapDimension = 100;
			break;
		case 1:
			gp.player.invisible = false;
			gp.player.worldX = gp.tileSize * 50;
			gp.player.worldY = gp.tileSize * 50;
			gp.player.hasFireAxe = true;
			gp.player.setPlayerWithFireAxeAnimationFrames();
			gp.player.hasFireAxeEquipped = true;
			gp.player.hasFlameThrower = true;
			mapDimension = 100;
			break;
		case 2:
			gp.wm.night = false;
			gp.player.worldX = 54 * gp.tileSize;
			gp.player.worldY = 51 * gp.tileSize;
			gp.player.invisible = true;
			mapDimension = 100;
			break;
		case 3:
			gp.player.worldX = 1 * gp.tileSize;
			gp.player.worldY = 1 * gp.tileSize;
			gp.player.invisible = false;
			gp.player.hasFireAxe = true;
			gp.player.setPlayerWithFireAxeAnimationFrames();
			gp.player.hasFireAxeEquipped = true;
			gp.player.hasFlameThrower = true;
			mapDimension = 10;
			break;
		}
		
	}

	public int getCurrentLevel() {
		return currentLevel;
		
	}

}
