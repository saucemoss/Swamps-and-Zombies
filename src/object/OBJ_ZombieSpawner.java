package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import entity.Zombie;
import main.AnimationManager;
import main.GamePanel;
import pathfinding.ExampleFactory;
import pathfinding.Map;

public class OBJ_ZombieSpawner extends Entity{

	public int spawnIntervalCounter, zombieCounter = 0, quantity, spawnInterval, delayStartTimer = 0;
	public boolean depleated, alive, pathFound;
	String target;
	Entity bDump;
	Map myMap4x;
	public OBJ_ZombieSpawner(GamePanel gp, int quantity, int spawnInterval) {
		super(gp);
		bDump = gp.aSetter.getbDump();
		pathFound = false;
		type = "object";
		name = "Zombie Spawner";
		alive = true;
		this.quantity = quantity;
		this.spawnInterval = spawnInterval;

		try {
			image = ImageIO.read(getClass().getResource("/objects/groundSpawner.png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private void spawnZombies() {

		spawnIntervalCounter++;
		if (spawnIntervalCounter >= spawnInterval && !depleated) {
			int x = worldX;
			int y = worldY;
			Zombie z = new Zombie(gp, zPath);
			z.worldX = x;
			z.worldY = y;
			if(pathFound && zPath != null  && zPath.size() > 0)
				z.zPath = this.zPath;

			gp.zombies.add(z);
			spawnIntervalCounter = 0;
			zombieCounter++;
		}

		if (zombieCounter >= quantity) {
			depleated = true;
			alive = false;
		}
	}

	@Override
	public void update() {
		if(!pathFound) {
			
			getMap();
			
			zPath = myMap4x.findPath(worldX/gp.tileSize, worldY/gp.tileSize, 
					bDump.worldX/gp.tileSize, bDump.worldY/gp.tileSize);
			pathFound = true;

		}
		if(pathFound && zPath.size() == 0)
		pathFound = false;
		if (spawning) {
			spawnZombies();

		}



	}
	
	

	private void getMap() {
		myMap4x = new Map<>(400, 400, new ExampleFactory());
		int col = 0;
		int row = 0;
		while (col < 400 && row < 400) {
			int tileNum = gp.tileM.mapTileNum[col/4][row/4];

			if (gp.tileM.tile[tileNum].collision) {
				myMap4x.setWalkable(col, row, false);
				
			}
			col++;
			if (col == gp.maxWorldCol) {
				col = 0;
				row++;
			}
		}
		
		
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
