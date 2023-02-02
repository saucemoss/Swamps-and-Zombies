package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.List;
import java.util.Random;

import main.AnimationManager;
import main.GamePanel;
import object.OBJ_Fireaxe;
import pathfinding.ExampleFactory;
import pathfinding.Map;
import pathfinding.Node;

public class Zombie extends Entity {
	int randdir, chaseSpeed, superSpeed, pathPoll, huntSpeed, distToPlayer, closeProximity = 60, pathTilePosX,
			pathTilePosY, pathTimer;
	int wPosX;
	int wPosY;
	Random random = new Random();
	public String target;
	Entity bDump = getBDump();
	Rectangle pathRect = new Rectangle();
	Rectangle nextCornerRect = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
	Map<Node> myMap4x;
	static final int mapMultiplier = 1;

	public List<Node> zPath;
	boolean stop, isHunting, needsPath, isAgro, onPath, atTheDump;

	public Zombie(GamePanel gp) {
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

	}

	private Entity getBDump() {
		return gp.aSetter.getbDump();

	}

	public Zombie(GamePanel gp, List<Node> zPath) {
		super(gp);
		this.zPath = zPath;
		name = "Zombie";
		direction = "idle";
		pathTimer = 0;
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
		pathTimer++;
		if (onPath && pathTimer > 20) {

			searchPath(gp.player.tilePosX,gp.player.tilePosY);
			pathTimer = 0;
		} else {
			setSpeed(1);
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
	}

	@Override
	public void snapToGrid() {
		worldX = (worldX + solidArea.x) / gp.tileSize * gp.tileSize;
		worldY = (worldY + solidArea.y) / gp.tileSize * gp.tileSize;
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
		

		slowDownOnPalisades();

		increaseAgroRangeWhenAttacked();
		
		if(getPlayerDistance()<150) {
			onPath = true;
		}
		
		setAction();

		walk();
		
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

	private void increaseAgroRangeWhenAttacked() {
		if (currentLife < maxLife && !isAgro) {
			isAgro = true;
			agroRange += 100;
		}
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

}
