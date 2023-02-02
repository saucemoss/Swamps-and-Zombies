package main;

import java.awt.Rectangle;
import java.util.ArrayList;

import entity.Entity;

public class CollisionManager {

	GamePanel gp;

	public CollisionManager(GamePanel gp) {
		this.gp = gp;
	}

	public String checkTileCollision(Entity entity) {

		double entityLeftWorldX = entity.worldX + entity.solidArea.x;
		double entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
		double entityTopWorldY = entity.worldY + entity.solidArea.y;
		double entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

		double entityLeftCol = entityLeftWorldX / gp.tileSize;
		double entityRightCol = entityRightWorldX / gp.tileSize;
		double entityTopRow = entityTopWorldY / gp.tileSize;
		double entityBottomRow = entityBottomWorldY / gp.tileSize;

		int tileNum1, tileNum2;

		switch (entity.direction) {
		case "up":
			entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[(int) entityLeftCol][(int) entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[(int) entityRightCol][(int) entityTopRow];
			determineTileCollisions(entity, tileNum1, tileNum2);
			break;
		case "upRight":
			entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[(int) entityLeftCol][(int) entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[(int) entityRightCol][(int) entityTopRow];
			determineTileCollisions(entity, tileNum1, tileNum2);
			entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[(int) entityRightCol][(int) entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[(int) entityRightCol][(int) entityBottomRow];
			determineTileCollisions(entity, tileNum1, tileNum2);
			break;
		case "upLeft":
			entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[(int) entityLeftCol][(int) entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[(int) entityRightCol][(int) entityTopRow];
			determineTileCollisions(entity, tileNum1, tileNum2);
			entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[(int) entityLeftCol][(int) entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[(int) entityLeftCol][(int) entityBottomRow];
			determineTileCollisions(entity, tileNum1, tileNum2);
			break;
		case "down":
			entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[(int) entityLeftCol][(int) entityBottomRow];
			tileNum2 = gp.tileM.mapTileNum[(int) entityRightCol][(int) entityBottomRow];
			determineTileCollisions(entity, tileNum1, tileNum2);
			break;
		case "downLeft":
			entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[(int) entityLeftCol][(int) entityBottomRow];
			tileNum2 = gp.tileM.mapTileNum[(int) entityRightCol][(int) entityBottomRow];
			determineTileCollisions(entity, tileNum1, tileNum2);
			entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[(int) entityLeftCol][(int) entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[(int) entityLeftCol][(int) entityBottomRow];
			determineTileCollisions(entity, tileNum1, tileNum2);
			break;
		case "downRight":
			entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[(int) entityLeftCol][(int) entityBottomRow];
			tileNum2 = gp.tileM.mapTileNum[(int) entityRightCol][(int) entityBottomRow];
			determineTileCollisions(entity, tileNum1, tileNum2);
			entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[(int) entityRightCol][(int) entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[(int) entityRightCol][(int) entityBottomRow];
			determineTileCollisions(entity, tileNum1, tileNum2);
			break;
		case "left":
			entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[(int) entityLeftCol][(int) entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[(int) entityLeftCol][(int) entityBottomRow];
			determineTileCollisions(entity, tileNum1, tileNum2);
			break;
		case "right":
			entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
			tileNum1 = gp.tileM.mapTileNum[(int) entityRightCol][(int) entityTopRow];
			tileNum2 = gp.tileM.mapTileNum[(int) entityRightCol][(int) entityBottomRow];
			determineTileCollisions(entity, tileNum1, tileNum2);
			break;
		}
		return entity.direction;

	}

	private void determineTileCollisions(Entity entity, int tileNum1, int tileNum2) {
		if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
			entity.collisionOn = true;
		}
		if (entity.getName().equals("Fire Projectile")) {
			if (gp.tileM.tile[tileNum1].projectilesGoThrough || gp.tileM.tile[tileNum2].projectilesGoThrough) {
				entity.collisionOn = false;
			}
		}
	}

	public int checkObject(Entity entity, boolean player) {
		int index = 999;

		for (int i = 0; i < gp.obj.size(); i++) {
			if (gp.obj.get(i) != null) {
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;

				gp.obj.get(i).solidArea.x = gp.obj.get(i).worldX + gp.obj.get(i).solidArea.x;
				gp.obj.get(i).solidArea.y = gp.obj.get(i).worldY + gp.obj.get(i).solidArea.y;

				switch (entity.direction) {
				case "up":
					entity.solidArea.y -= entity.speed;
					break;
				case "down":
					entity.solidArea.y += entity.speed;
					break;
				case "left":
					entity.solidArea.x -= entity.speed;
					break;
				case "right":
					entity.solidArea.x += entity.speed;
					break;

				}
				if (entity.solidArea.intersects(gp.obj.get(i).solidArea)) {
					if (gp.obj.get(i).collision) {
						entity.collisionOn = true;
					}
					if (player) {
						index = i;
					}
				}
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				gp.obj.get(i).solidArea.x = gp.obj.get(i).solidAreaDefaultX;
				gp.obj.get(i).solidArea.y = gp.obj.get(i).solidAreaDefaultY;
			}

		}

		return index;
	}

	public int checkEntity(Entity entity, Entity[] target) {
		int index = 999;

		for (int i = 0; i < target.length; i++) {
			if (target[i] != null) {
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;

				target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
				target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

				if (entity.solidArea.intersects(target[i].solidArea)) {
					if (target[i] != entity) {
						entity.collisionOn = true;
						index = i;
					}
				}
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				target[i].solidArea.x = target[i].solidAreaDefaultX;
				target[i].solidArea.y = target[i].solidAreaDefaultY;

			}

		}

		return index;
	}

	public int checkEntity(Entity entity, ArrayList<Entity> e) {
		int index = 999;
		for (int i = 0; i < e.size(); i++) {
			Entity target = e.get(i);
			if (target != null) {
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;

				target.solidArea.x = target.worldX + target.solidArea.x;
				target.solidArea.y = target.worldY + target.solidArea.y;

				switch (entity.direction) {
				case "up":
					entity.solidArea.y -= entity.speed;
					break;
				case "down":
					entity.solidArea.y += entity.speed;
					break;
				case "left":
					entity.solidArea.x -= entity.speed;
					break;
				case "right":
					entity.solidArea.x += entity.speed;
					break;
				}

				if (entity.solidArea.intersects(target.solidArea)) {
					if (target != entity) {
						entity.collisionOn = true;
						index = i;
					}
				}
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				target.solidArea.x = target.solidAreaDefaultX;
				target.solidArea.y = target.solidAreaDefaultY;

			}

		}

		return index;
	}

	public int checkAreaEntity(Rectangle area, int worldX, int worldY, Entity[] target) {
		int index = 999;
		int defaultAreaX = area.x;
		int defaultAreaY = area.y;
		int defaultAreaWidth = area.width;
		int defaultAreaHeight = area.height;

		for (int i = 0; i < target.length; i++) {
			if (target[i] != null) {
				area.x = worldX + area.x;
				area.y = worldY + area.y;

				target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
				target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

				if (area.intersects(target[i].solidArea)) {
					if (target[i].solidArea != area) {
						index = i;
					}
				}
				area.x = defaultAreaX;
				area.y = defaultAreaY;
				area.width = defaultAreaWidth;
				area.height = defaultAreaHeight;
				target[i].solidArea.x = target[i].solidAreaDefaultX;
				target[i].solidArea.y = target[i].solidAreaDefaultY;

			}
		}
		return index;
	}

	public int checkAreaEntity(Rectangle area, int worldX, int worldY, ArrayList<Entity> e) {
		int index = 999;
		int defaultAreaX = area.x;
		int defaultAreaY = area.y;
		int defaultAreaWidth = area.width;
		int defaultAreaHeight = area.height;

		for (int i = 0; i < e.size(); i++) {
			Entity target = e.get(i);
			if (e != null) {
				area.x = worldX + area.x;
				area.y = worldY + area.y;

				target.solidArea.x = target.worldX + target.solidArea.x;
				target.solidArea.y = target.worldY + target.solidArea.y;

				if (area.intersects(target.solidArea)) {
					if (target.solidArea != area) {
						index = i;
					}
				}
				area.x = defaultAreaX;
				area.y = defaultAreaY;
				area.width = defaultAreaWidth;
				area.height = defaultAreaHeight;
				target.solidArea.x = target.solidAreaDefaultX;
				target.solidArea.y = target.solidAreaDefaultY;

			}
		}
		return index;
	}

	public void checkPlayer(Entity entity) {
		entity.solidArea.x = entity.worldX + entity.solidArea.x;
		entity.solidArea.y = entity.worldY + entity.solidArea.y;

		gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
		gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

		switch (entity.direction) {
		case "up":
			entity.solidArea.y -= entity.speed;
			break;
		case "down":
			entity.solidArea.y += entity.speed;
			break;
		case "left":
			entity.solidArea.x -= entity.speed;
			break;
		case "right":
			entity.solidArea.x += entity.speed;
			break;
		}

		if (entity.solidArea.intersects(gp.player.solidArea)) {
			// entity.collisionOn = true;
			entity.collisionOnPlayer = true;
		}
		entity.solidArea.x = entity.solidAreaDefaultX;
		entity.solidArea.y = entity.solidAreaDefaultY;
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;

	}

	public int checkAgroRange(Entity enemy, Entity player) {
		int range = -1;
		if (enemy.name == "Zombie" && player.name == "Player") {

			int x2 = player.worldX + player.solidArea.x;
			int y2 = player.worldY + player.solidArea.y;

			int x1 = enemy.worldX + enemy.solidArea.x;
			int y1 = enemy.worldY + enemy.solidArea.y;

			int xDiff = Math.abs(x1 - x2);
			int yDiff = Math.abs(y1 - y2);
			range = (int) Math.hypot(xDiff, yDiff);

		}
		return range;

	}
}
