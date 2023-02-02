package main;

import java.util.Random;

import entity.Entity;
import entity.Zombie;
import object.OBJ_BarrelDump;
import object.OBJ_Fireaxe;
import object.OBJ_FlameTurret;
import object.OBJ_Flamethrower;
import object.OBJ_FuelTank;
import object.OBJ_Gate;
import object.OBJ_Key;
import object.OBJ_Medkit;
import object.OBJ_MilitaryCrate;
import object.OBJ_Palisade;
import object.OBJ_RadioactiveBarrel;
import object.OBJ_ZombieSpawner;

public class AssetSetterGetter {
	static GamePanel gp;
	Random rand = new Random();
	Entity bDump;

	public AssetSetterGetter(GamePanel gp) {
		this.gp = gp;
		
	}

	public void setObject(int map) {
		gp.obj.clear();
		gp.zombies.clear();
		gp.entityList.clear();
		gp.projectileList.clear();
		bDump = new OBJ_BarrelDump(gp);
		switch (map) {
		case 0:
			setupObject(getbDump(), 52, 29, true);
			setupObject(new OBJ_Gate(gp), 46, 30, true);
			setupObject(new OBJ_Key(gp), 42, 78, false);
			setupObject(new OBJ_Flamethrower(gp), 45, 29, false);
			setupObject(new OBJ_Fireaxe(gp), 47, 29, false);
			setupObject(new OBJ_FuelTank(gp), 44, 29, false);
			setupObject(new OBJ_FuelTank(gp), 43, 29, false);
			setupObject(new OBJ_FuelTank(gp), 42, 29, false);
			setupObject(new OBJ_FuelTank(gp), 41, 29, false);
			setupObject(new OBJ_Medkit(gp), 41, 31, false);
			setupObject(new OBJ_Medkit(gp), 42, 31, false);
			setupObject(new OBJ_Medkit(gp), 43, 31, false);
			setupObject(new OBJ_Medkit(gp), 44, 31, false);
			setupObject(new OBJ_Palisade(gp), 49, 35, false);
			setupObject(new OBJ_Palisade(gp), 50, 35, false);
			setupObject(new OBJ_FlameTurret(gp), 44, 39, false);
			setupObjectsAtRandomXY("OBJ_Medkit", 2, 40, 70, false);
			setupObjectsAtRandomXY("OBJ_Fueltank", 2, 40, 70, false);

			break;
		case 1:
			setupObject(getbDump(), 48, 45, true);

			setupObjectsAtRandomXY("OBJ_Medkit", 1, 40, 60, false);
			setupObjectsAtRandomXY("OBJ_Fueltank", 2, 40, 60, false);
			break;
		case 2:
			setupObject(getbDump(), 64, 52, true);
			setupObject(new OBJ_FlameTurret(gp), 56, 47, false);
			setupObject(new OBJ_FlameTurret(gp), 60, 49, false);
			setupObject(new OBJ_FlameTurret(gp), 61, 52, false);
			setupObject(new OBJ_FlameTurret(gp), 60, 55, false);
			setupObject(new OBJ_FlameTurret(gp), 56, 57, false);
			
			setupObject(new OBJ_Palisade(gp), 55, 49, false);
			setupObject(new OBJ_Palisade(gp), 55, 50, false);
			setupObject(new OBJ_Palisade(gp), 55, 51, false);
			
			setupObject(new OBJ_Palisade(gp), 57, 51, false);
			setupObject(new OBJ_Palisade(gp), 57, 52, false);
			setupObject(new OBJ_Palisade(gp), 57, 53, false);
			
			setupObject(new OBJ_Palisade(gp), 61, 51, false);
			setupObject(new OBJ_Palisade(gp), 61, 53, false);
			
			setupObject(new OBJ_Palisade(gp), 60, 50, false);
			setupObject(new OBJ_Palisade(gp), 60, 51, false);
			setupObject(new OBJ_Palisade(gp), 60, 52, false);
			setupObject(new OBJ_Palisade(gp), 60, 53, false);
			setupObject(new OBJ_Palisade(gp), 60, 54, false);
			
			setupObject(new OBJ_Palisade(gp), 55, 54, false);
			setupObject(new OBJ_Palisade(gp), 55, 55, false);
			
			Entity spawner1 = new OBJ_ZombieSpawner(gp, 999, rand.nextInt(300)+75);
			spawner1.spawning = true;
			setupObject(spawner1, 47, 47, false);
			Entity spawner2 = new OBJ_ZombieSpawner(gp, 999, rand.nextInt(300)+75);
			spawner2.spawning = true;
			setupObject(spawner2, 46, 49, false);
			Entity spawner3 = new OBJ_ZombieSpawner(gp, 999, rand.nextInt(300)+75);
			spawner3.spawning = true;
			setupObject(spawner3, 43, 53, false);
			Entity spawner4 = new OBJ_ZombieSpawner(gp, 999, rand.nextInt(300)+75);
			spawner4.spawning = true;
			setupObject(spawner4, 45, 55, false);
			Entity spawner5 = new OBJ_ZombieSpawner(gp, 999, rand.nextInt(300)+75);
			spawner5.spawning = true;
			setupObject(spawner5, 42, 52, false);
			break;
		case 3:
			setupObject(getbDump(), 5* gp.tileSize, 7* gp.tileSize, true);
			Zombie z = new Zombie(gp);
			z.worldX = 5 * gp.tileSize;
			z.worldY = 3 * gp.tileSize;
			gp.zombies.add(z);
		}

	}


	public void setupObjectsAtRandomXY(String obj, int quantity, int rangeStart, int rangeStop, boolean collision) {

		for (int i = 0; i < quantity; i++) {
			int x = rand.nextInt(rangeStop - rangeStart) + rangeStart;
			int y = rand.nextInt(rangeStop - rangeStart) + rangeStart;

			while (gp.tileM.tile[gp.tileM.mapTileNum[x][y]].collision) {
				x = rand.nextInt(rangeStop - rangeStart) + rangeStart;
				y = rand.nextInt(rangeStop - rangeStart) + rangeStart;
			}
			switch (obj) {
			case "OBJ_Medkit":
				setupObject(new OBJ_Medkit(gp), x, y, collision);
				break;
			case "OBJ_Fueltank":
				setupObject(new OBJ_FuelTank(gp), x, y, collision);
				break;
			case "OBJ_RadioactiveBarrel":
				setupObject(new OBJ_RadioactiveBarrel(gp), x, y, collision);
				break;
			case "OBJ_ZombieSpawner":
				setupObject(new OBJ_ZombieSpawner(gp, 20, 30), x, y, collision);
				break;
			case "OBJ_MilitaryCrate":
				setupObject(new OBJ_MilitaryCrate(gp), x, y, collision);
				break;
			}
		}

	}
	
	public void spawnWaveObjects(int barrels, int zombies, int spawners, int crates) {
		
		switch(gp.levelManager.getCurrentLevel()) {
		case 0:
			setupObjectsAtRandomXY("OBJ_MilitaryCrate", crates, 45, 70, false);
			setZombie(zombies);
			setupObjectsAtRandomXY("OBJ_ZombieSpawner",spawners, 45, 70, false);
			setupObjectsAtRandomXY("OBJ_RadioactiveBarrel", barrels, 30, 70, false);
			break;
		case 1:
			setupObjectsAtRandomXY("OBJ_MilitaryCrate", crates, 35, 40, false);
			setZombie(zombies);
			setupObjectsAtRandomXY("OBJ_ZombieSpawner", spawners, 1, 99, false);
			setupObjectsAtRandomXY("OBJ_RadioactiveBarrel", barrels, 35, 40, false);
			setupObjectsAtRandomXY("OBJ_Medkit", 1, 40, 55, false);
			setupObjectsAtRandomXY("OBJ_Fueltank", 2, 40, 55, false);
			break;
		}
		
	}

	void setupObject(Entity e, int x, int y, boolean collision) {
		e.worldX = x * gp.tileSize;
		e.worldY = y * gp.tileSize;
		e.collision = collision;
		gp.obj.add(e);
	}

	public void setZombie(int zombies) {

//		Zombie z = new Zombie(gp);
//		z.worldX = 12 * gp.tileSize;
//		z.worldY = 12 * gp.tileSize;
//		gp.zombies.add(z);
		// Debug

		spawnRandomStartingZombies(zombies);

	}

	private void spawnRandomStartingZombies(int quantity) {

		for (int i = 0; i < quantity; i++) {
			Zombie z = new Zombie(gp);

			int x = rand.nextInt(70) + 30;
			int y = rand.nextInt(70) + 30;

			while (gp.tileM.tile[gp.tileM.mapTileNum[x][y]].collision) {
				x = rand.nextInt(70) + 30;
				y = rand.nextInt(70) + 30;
			}
			z.worldX = x * gp.tileSize;
			z.worldY = y * gp.tileSize;
			gp.zombies.add(z);

		}

	}

	public Entity getObjFromCoords(int x, int y) {
		for (Entity e : gp.obj) {
			if (e.worldX / gp.tileSize == x && e.worldY / gp.tileSize == y) {
				return e;
			}
		}
		return null;
	}
	

	public Entity getbDump() {
		return bDump;
	}

	

}
