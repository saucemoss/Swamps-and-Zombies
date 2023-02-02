package backup;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.List;
import java.util.Random;

import entity.Entity;
import main.AnimationManager;
import main.GamePanel;
import object.OBJ_Fireaxe;
import pathfinding.ExampleFactory;
import pathfinding.Map;
import pathfinding.Node;

public class ZombieBackup extends Entity {
	int randdir, chaseSpeed, superSpeed, pathPoll, huntSpeed, distToPlayer, closeProximity = 60, pathTilePosX,
			pathTilePosY;
	int wPosX;
	int wPosY;
	Random random = new Random();
	public String target;
	Entity bDump = getBDump();
	Rectangle pathRect = new Rectangle();
	Rectangle nextCornerRect = new Rectangle(0,0,gp.tileSize,gp.tileSize);
	Map<Node> myMap4x;
	static final int mapMultiplier = 1;

	public List<Node> zPath;
	boolean stop, isHunting, needsPath, isAgro, onPath, atTheDump;

	public ZombieBackup(GamePanel gp) {
		super(gp);
		name = "Zombie";
		direction = "idle";
		speed = 1;
		chaseSpeed = 2;
		huntSpeed = 3;
		superSpeed = 4;
		alive = true;
		maxLife = 20;
		currentLife = maxLife;
		agroRange = 250;
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;
		tilePosX = (worldX + solidArea.x) / gp.tileSize;
		tilePosY = (worldY + solidArea.y) / gp.tileSize;
		getZombieImage();
		getMap();

	}

	private Entity getBDump() {
		return gp.aSetter.getbDump();

	}

	public ZombieBackup(GamePanel gp, List<Node> zPath) {
		super(gp);
		this.zPath = zPath;
		name = "Zombie";
		direction = "idle";
		speed = 1;
		chaseSpeed = 2;
		huntSpeed = 3;
		alive = true;
		maxLife = 20;
		currentLife = maxLife;
		agroRange = 250;
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;
		tilePosX = (worldX + solidArea.x) / gp.tileSize;
		tilePosY = (worldY + solidArea.y) / gp.tileSize;
		getZombieImage();

	}

	public void setSpeed(double speed) {
		this.speed = speed - speedPenalty;
	}

	public void setAction() {
		setSpeed(1);
		if (zPath != null) {
			zPath.clear();
			needsPath = true;
		}
		actionLockCounter++;
		if (actionLockCounter >= 80) {

			int i = random.nextInt(100) + 1;
			if (i <= 25) {
				direction = "up";
			} else if (i > 25 && i <= 50) {
				direction = "left";
			} else if (i > 50 && i <= 75) {
				direction = "right";
			} else {
				direction = "down";
			}
			actionLockCounter = 0;
		}

	}

	@Override
	public void snapToGrid() {
		worldX = (worldX + solidArea.x) / gp.tileSize * gp.tileSize;
		worldY = (worldY + solidArea.y) / gp.tileSize * gp.tileSize;
	}

	public void chase() {
		collisionOnPlayer = false;
		gp.cChecker.checkPlayer(this);
		if (collisionOnPlayer && !gp.player.takingDamage) {
			gp.player.receiveDamage(1);
		}
		if (!collisionOn && !stop) {
			setSpeed(chaseSpeed);

			if (worldX < gp.player.worldX) {
				direction = "right";
				collisionOnPlayer = false;
				gp.cChecker.checkPlayer(this);
				if (!collisionOnPlayer) {
					worldX += speed;
				} else if (collisionOnPlayer && !gp.player.takingDamage) {
					gp.player.receiveDamage(1);
				}

			}
			if (worldX > gp.player.worldX) {
				direction = "left";
				collisionOnPlayer = false;
				gp.cChecker.checkPlayer(this);
				if (!collisionOnPlayer) {
					worldX -= speed;
				} else if (collisionOnPlayer && !gp.player.takingDamage) {
					gp.player.receiveDamage(1);
				}
			}
			if (worldY > gp.player.worldY) {
				direction = "up";
				collisionOnPlayer = false;
				gp.cChecker.checkPlayer(this);
				if (!collisionOnPlayer) {
					worldY -= speed;
				} else if (collisionOnPlayer && !gp.player.takingDamage) {
					gp.player.receiveDamage(1);
				}
			}
			if (worldY < gp.player.worldY) {
				direction = "down";
				collisionOnPlayer = false;
				gp.cChecker.checkPlayer(this);
				if (!collisionOnPlayer) {
					worldY += speed;
				} else if (collisionOnPlayer && !gp.player.takingDamage) {
					gp.player.receiveDamage(1);
				}

			}
		}
		wasChasing = true;
	}

	@Override
	public void stagger() {
		if (takingDamage) {
			if (currentLife < 4) {
				agroRange += 100;
			}
			staggerCounter++;
			image = damaged;
			if (staggerCounter == 10) {
				takingDamage = false;
				staggerCounter = 0;
			}
		}
	}

	public void setZombieDeathAnim() {

		solidArea.x = 0;
		solidArea.y = 0;
		solidArea.width = 0;
		solidArea.height = 0;
		deathAnimCounter++;
		if (deathAnimCounter == 2)
			gp.playSE(1);
		if (deathAnimCounter <= 48)
			image = AnimationManager.ZDeathAnimArray[deathAnimCounter / 4];
		else
			image = AnimationManager.ZDeathAnimArray[12];

		if (deathAnimCounter == 24) {
			stop = true;
		}
		if (deathAnimCounter >= 48) {
			deathAnimCounter = 0;
			die();
		}

	}

	@Override
	public void knockBack(String direction) {
		int knockBackFactor = OBJ_Fireaxe.knockback;
		gp.cChecker.checkTileCollision(this);
		if (!collisionOn) {
			int randomDir = (random.nextInt(11) - 5) * knockBackFactor / 4;
			switch (direction) {
			case "upAttack":
				this.direction = "up";
				gp.cChecker.checkTileCollision(this);
				if (!collisionOn) {
					worldY -= speed * knockBackFactor;
					worldX += randomDir;
				}
				break;
			case "downAttack":
				this.direction = "down";
				gp.cChecker.checkTileCollision(this);
				if (!collisionOn) {
					worldY += speed * knockBackFactor;
					worldX += randomDir;
				}
				break;
			case "leftAttack":
				this.direction = "left";
				gp.cChecker.checkTileCollision(this);
				if (!collisionOn) {
					worldX -= speed * knockBackFactor;
					worldY += randomDir;
				}
				break;
			case "rightAttack":
				this.direction = "right";
				gp.cChecker.checkTileCollision(this);
				if (!collisionOn) {
					worldX += speed * knockBackFactor;
					worldY += randomDir;
				}
				break;
			}
		}
		snapToGrid();
	}

	public void getZombieImage() {
		up1 = AnimationManager.up1Zombie;
		up2 = AnimationManager.up2Zombie;
		down1 = AnimationManager.down1Zombie;
		down2 = AnimationManager.down2Zombie;
		left1 = AnimationManager.left1Zombie;
		left2 = AnimationManager.left2Zombie;
		right1 = AnimationManager.right1Zombie;
		right2 = AnimationManager.right2Zombie;
		idle1 = AnimationManager.idle1Zombie;
		idle2 = AnimationManager.idle2Zombie;
		damaged = AnimationManager.damagedZombie;

	}

	@Override
	public void update() {
		resetCollision();

		wPosX = (worldX + solidArea.x);
		wPosY = (worldY + solidArea.y);
		tilePosX = wPosX / gp.tileSize;
		tilePosY = wPosY / gp.tileSize;
		pathTilePosX = tilePosX * mapMultiplier;
		pathTilePosY = tilePosY * mapMultiplier;
		int playerPathTilePosX = gp.player.tilePosX * mapMultiplier;
		int playerPathTilePosY = gp.player.tilePosY * mapMultiplier;

		slowDownOnPalisades();

		// zombie range
		distToPlayer = getPlayerDistance();
		if (distToPlayer > agroRange) { // player out of zombie agro range
			// Night - walk
			if (gp.wm.night) {
				resetCollision();
				gp.cChecker.checkTileCollision(this);
				setAction();
				walk();
			} else {// Day - go for dump
				setSpeed(chaseSpeed);
				if (!onPath && !atTheDump && wasChasing) {
					zPath = gp.tileM.myMap.findPath(tilePosX, tilePosY, bDump.worldX / gp.tileSize,
							bDump.worldY / gp.tileSize);
					pathPoll = 0;
					onPath = true;
				}

				getTargetDistances();
				if (distToPlayer < closeProximity) {

					onPath = false;
					atTheDump = false;
					chase();
				} else if (distToDump < closeProximity) {

					atTheDump = true;
					onPath = false;
					wasChasing = false;
					damageDump();
				} else if (!atTheDump) {

					wasChasing = false;
					goOnPath(); // move on path
				}

			}

		} else { // player in zombie agro range
			if (!onPath || gp.player.moving) {
				// zPath = gp.tileM.myMap.findPath(tilePosX, tilePosY, gp.player.tilePosX,
				// gp.player.tilePosY);
				zPath = myMap4x.findPath(pathTilePosX, pathTilePosY, playerPathTilePosX, playerPathTilePosY);
				onPath = true;
				pathPoll = 0;
			}

			getTargetDistances();
			if (distToPlayer < closeProximity) {
				onPath = false;
				// chase();
			} else {
				if (wasChasing) {
					snapToGrid();
					wasChasing = false;
				}
				goOnPath();
			}

		}

		increaseAgroRangeWhenAttacked();

		stagger();

		if (currentLife <= 0)
			dyingAnimPlay = true;
		if (dyingAnimPlay)

		{
			takingDamage = false;
			setZombieDeathAnim();
		}

		switchSprites();

	}

	private int getPlayerDistance() {
		if (!gp.player.invisible)
			return distToPlayer = uTool.distanceBetween(worldX, worldY, gp.player.worldX, gp.player.worldY);
		else
			return 9999;
	}

	private void goOnPath() {
		if (zPath != null && pathPoll < zPath.size()) {

			nextX = zPath.get(pathPoll).getxPosition() * gp.tileSize / mapMultiplier;
			nextY = zPath.get(pathPoll).getyPosition() * gp.tileSize / mapMultiplier;

			// Player collision
			gp.cChecker.checkObject(this, false);

			int downPosTile;
			int upPosTile;
			int rightPosTile;
			int leftPosTile;

			String nextTileDir = "";
			if (nextY > wPosY) {
				nextTileDir = "down";
			}
			if (nextY < wPosY) {
				nextTileDir = "up";
			}
			if (nextX > wPosX) {
				nextTileDir += "Right";
			}
			if (nextX < wPosX) {
				nextTileDir += "Left";
			}

			if (!stop) {
				if (wPosX == nextX && wPosY == nextY) {
					pathPoll++;
				} else {
					if (nextTileDir.equals("upRight")) {
						boolean upCollision = gp.tileM.tile[gp.tileM.mapTileNum[tilePosX][tilePosY - 1]].collision;
						boolean rightCollision = gp.tileM.tile[gp.tileM.mapTileNum[tilePosX + 1][tilePosY]].collision;
						if (upCollision) {
							nextCornerRect.x = tilePosX+1;
							nextCornerRect.y = tilePosY;
							
							direction = "right";
							worldX += speed;
						} else if (rightCollision) {
							nextCornerRect.x = tilePosX;
							nextCornerRect.y = tilePosY-1;
							direction = "up";
							worldY -= speed;
						} else {
							moveNormal();
						}
					} else if (nextTileDir.equals("downRight")) {
						boolean downCollision = gp.tileM.tile[gp.tileM.mapTileNum[tilePosX][tilePosY + 1]].collision;
						boolean rightCollision = gp.tileM.tile[gp.tileM.mapTileNum[tilePosX + 1][tilePosY]].collision;
						if (downCollision) {
							nextCornerRect.x = tilePosX+1;
							nextCornerRect.y = tilePosY;
							direction = "right";
							worldX += speed;
						} else if (rightCollision) {
							nextCornerRect.x = tilePosX;
							nextCornerRect.y = tilePosY+1;
							direction = "down";
							worldY += speed;
						} else {
							moveNormal();
						}
					} else if (nextTileDir.equals("upLeft")) {
						boolean upCollision = gp.tileM.tile[gp.tileM.mapTileNum[tilePosX + 1][tilePosY - 1]].collision;
						boolean leftCollision = gp.tileM.tile[gp.tileM.mapTileNum[tilePosX - 1][tilePosY
								+ 1]].collision;
						if (upCollision) {
							nextCornerRect.x = tilePosX-1;
							nextCornerRect.y = tilePosY+1;
							direction = "left";
							worldX -= speed;
						} else if (leftCollision) {
							nextCornerRect.x = tilePosX+1;
							nextCornerRect.y = tilePosY-1;
							direction = "up";
							worldY -= speed;
						} else {
							moveNormal();
						}
					} else if (nextTileDir.equals("downLeft")) {
						boolean downCollision = gp.tileM.tile[gp.tileM.mapTileNum[tilePosX-1][tilePosY
								+ 1]].collision;
						boolean leftCollision = gp.tileM.tile[gp.tileM.mapTileNum[tilePosX - 1][tilePosY]].collision;
						if (downCollision) {
							nextCornerRect.x = tilePosX;
							nextCornerRect.y = tilePosY-1;
							direction = "left";
							worldX -= speed;
						} else if (leftCollision) {
							nextCornerRect.x = tilePosX;
							nextCornerRect.y = tilePosY+1;
							direction = "down";
							worldY += speed;
						} else {
							moveNormal();
						}
					} else {
						moveNormal();
					}
				}
			}
		}

	}

	private void moveNormal() {
		if (wPosY > nextY) {
			direction = "up";
			worldY -= speed;
		}
		if (wPosY < nextY) {
			direction = "down";
			worldY += speed;
		}
		if (wPosX < nextX) {
			direction = "right";
			worldX += speed;
		}
		if (wPosX > nextX) {
			direction = "left";
			worldX -= speed;
		}
	}

	private void increaseAgroRangeWhenAttacked() {
		if (currentLife < maxLife && !isAgro) {
			isAgro = true;
			agroRange += 100;
		}
	}

	private void damageDump() {
		bDump.receiveDamage(1);
	}

	private void resetCollision() {
		collisionOn = false;
	}

	private void getTargetDistances() {

		distToPlayer = getPlayerDistance();
		distToDump = uTool.distanceBetween(worldX, worldY, bDump.worldX, bDump.worldY);
	}

	private void slowDownOnPalisades() {
		Entity e = gp.aSetter.getObjFromCoords(tilePosX, tilePosY);
		if (e != null && e.name.equals("Palisade")) {
			this.speedPenalty = e.speedPenalty;
			isSlowed = true;
		} else {
			this.speedPenalty = 0;
			if (isSlowed) {
				snapToGrid();
				isSlowed = false;
			}
		}
	}

	public void draw(Graphics2D g2) {
		super.draw(g2);
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.RED);
		int screenX = worldX + solidArea.x - gp.player.worldX + gp.player.screenX;
		int screenY = worldY + solidArea.y - gp.player.worldY + gp.player.screenY;

		if (zPath != null) {
			for (int i = 0; i < zPath.size(); i++) {
				g2.fillRect(
						zPath.get(i).getxPosition() * gp.tileSize / mapMultiplier - gp.player.worldX
								+ gp.player.screenX,
						zPath.get(i).getyPosition() * gp.tileSize / mapMultiplier - gp.player.worldY
								+ gp.player.screenY,
						gp.tileSize / mapMultiplier, gp.tileSize / mapMultiplier);
			}
		}
//		g2.setColor(Color.blue);
//		g2.fillRect(screenX, screenY, gp.tileSize / mapMultiplier, gp.tileSize / mapMultiplier);
//		g2.setColor(Color.red);
//		g2.drawRect(screenX, screenY, solidArea.width, solidArea.height);
		g2.setColor(Color.blue);
		g2.fillRect(nextCornerRect.x*gp.tileSize- gp.player.worldX + gp.player.screenX,
				nextCornerRect.y*gp.tileSize - gp.player.worldY + gp.player.screenY, nextCornerRect.width, nextCornerRect.height);

//		int dimension = mapMultiplier * gp.levelManager.mapDimension;
//		int col = 0;
//		int row = 0;
//		Color c = new Color(20, 20, 20, 200);
//		Color c2 = new Color(60, 20, 80, 150);
//		g2.setColor(c);
//		while (col < dimension && row < dimension) {
//
//			g2.setColor(c);
//			g2.drawRect(col * gp.tileSize / mapMultiplier - gp.player.worldX + gp.player.screenX,
//					row * gp.tileSize / mapMultiplier - gp.player.worldY + gp.player.screenY,
//					gp.tileSize / mapMultiplier, gp.tileSize / mapMultiplier);
//
//			col++;
//			if (col == dimension) {
//				col = 0;
//				row++;
//			}
//		}

	}

	private void getMap() {
		int dimension = mapMultiplier * gp.levelManager.mapDimension;
		myMap4x = new Map<>(dimension, dimension, new ExampleFactory());
		int col = 0;
		int row = 0;
		while (col < dimension && row < dimension) {
			int tileNum = gp.tileM.mapTileNum[col / mapMultiplier][row / mapMultiplier];
			if (gp.tileM.tile[tileNum].collision) {
				myMap4x.setWalkable(col, row, false);

			}
			col++;
			if (col == dimension) {
				col = 0;
				row++;
			}
		}
		// myMap4x.drawMap();
	}
}
