package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import inputs.KeyHandler;
import inputs.MyMouseListener;
import main.AnimationManager;
import main.GamePanel;
import object.OBJ_FireProjectile;
import object.OBJ_Fireaxe;
import object.OBJ_Flamethrower;
import object.OBJ_FuelTank;
import object.OBJ_Medkit;
import utilz.UtilityTool;

public class Player extends Entity {

	public KeyHandler keyH;
	protected MyMouseListener mouse;
	public int screenX, screenX2;
	public int screenY, screenY2;
	public Inventory inv = new Inventory();
	private int CooldownCounter = 0, staggerCounter = 0, attackAnimCounter = 0;
	public int maxLife;
	private Rectangle attackArea;
	public int attackAreaDefaultX, attackAreaDefaultY, attackAreaDefaultWidth, attackAreaDefaultHeight, prevTilePosY,
			prevTilePosX;

	public boolean hasFlameThrower, hasFlameThrowerEquipped, hasFireAxe, hasFireAxeEquipped, takingDamage, attacking,
			moving, countingCooldown, invisible;
	public String offsetDir;

	public Player(GamePanel gp, KeyHandler keyH, MyMouseListener mouse) {
		super(gp);
		this.gp = gp;
		this.keyH = keyH;
		this.mouse = mouse;
		setDefaultValues();
		gp.animManager.loadPlayerFireAxeMoveSet();
		gp.animManager.loadPlayerDefaultMoveSet();
		gp.animManager.loadPlayerFlameThrowerMoveSet();
		getPlayerImage();
		
		screenX = gp.screenWidth / 2 - gp.tileSize / 2;
		screenY = gp.screenHeight / 2 - gp.tileSize / 2;

		solidArea = new Rectangle();
		solidArea.x = 13;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 20;
		solidArea.height = 25;
		tilePosX = (worldX + solidArea.x) / gp.tileSize;
		tilePosY = (worldY + solidArea.y) / gp.tileSize;
		attackArea = new Rectangle(0, 0, 0, 0);
		attackAreaDefaultX = attackArea.x;
		attackAreaDefaultY = attackArea.y;
		attackAreaDefaultWidth = attackArea.width;
		attackAreaDefaultHeight = attackArea.height;

	}

	public void setDefaultValues() {

		prevTilePosY = 0;
		prevTilePosX = 0;
		speed = 4;
		direction = "down";
		animDirection = "idle";
		maxLife = 10;
		currentLife = maxLife;
		name = "Player";
		hasFireAxe = false;
		hasFireAxeEquipped = false;
		hasFlameThrower = false;
		hasFlameThrowerEquipped = false;
	}

	public void pickUpObject(int i) {
		if (i != 999) {
			String objectName = gp.obj.get(i).name;
			switch (objectName) {
			case "Medkit":
				if (currentLife < maxLife) {
					if (OBJ_Medkit.restore + currentLife > maxLife)
						currentLife = maxLife;
					else
						currentLife += OBJ_Medkit.restore;
					gp.obj.remove(i);
					gp.playSE(3);
				}
				break;
			case "Fuel":
				if (inv.ammoCount < OBJ_Flamethrower.ammoCapacity) {
					inv.ammoCount += OBJ_FuelTank.fuelAmount;
					if (inv.ammoCount > OBJ_Flamethrower.ammoCapacity)
						inv.ammoCount = 500;
					gp.obj.remove(i);
					gp.playSE(3);
				}
				break;
			case "Key":
				inv.keys++;
				gp.obj.remove(i);
				gp.playSE(3);
				break;
			case "Radioactive Barrel":
				inv.barrels++;
				gp.obj.remove(i);
				gp.playSE(3);
				break;
			case "Gate":
				if (inv.keys > 0) {
					gp.obj.remove(i);
					gp.playSE(7);
					inv.keys--;
				} else {
					gp.obj.get(i).speak();
				}
				break;
			case "Chest":
				gp.obj.get(i).speak();
				break;
			case "Barrel Dump":

				if (inv.barrels == 0) {
					gp.obj.get(i).setDialogue(0);
					gp.obj.get(i).speak();
				} else if (inv.barrels < gp.wm.barrelsThisWave) {
					gp.obj.get(i).setDialogue(1);
					gp.obj.get(i).speak();
				} else {
					gp.obj.get(i).setDialogue(2);
					gp.obj.get(i).speak();
					gp.wm.barrelsDeposited = inv.barrels;
					inv.barrels = 0;
				}

				break;
			case "Military Crate":
				gp.obj.get(i).getCrateMaterials();
				gp.obj.get(i).speak();
				gp.obj.remove(i);
				gp.playSE(3);
				break;
			case "Flamethrower":
				gp.obj.get(i).speak();
				gp.obj.remove(i);
				gp.playSE(3);
				if (inv.ammoCount < OBJ_Flamethrower.ammoCapacity)
					inv.ammoCount += 100;
				if (inv.ammoCount > OBJ_Flamethrower.ammoCapacity)
					inv.ammoCount = 500;
				hasFireAxeEquipped = false;
				hasFlameThrower = true;
				hasFlameThrowerEquipped = true;
				setPlayerWithFlameThrowerAnimationFrames();
				break;
			case "Fireaxe":
				gp.obj.get(i).speak();
				gp.obj.remove(i);
				gp.playSE(3);
				hasFireAxe = true;
				hasFireAxeEquipped = true;
				hasFlameThrowerEquipped = false;
				setPlayerWithFireAxeAnimationFrames();
				break;
			}

		}

	}

	@Override
	public void stagger() {
		staggerCounter++;
		image = up1;
		if (staggerCounter == 20) {
			takingDamage = false;
			staggerCounter = 0;
		}
	}

	@Override
	public void update() {

		updateGlobalPositionMovingVariables();

		applyTileSpeedVariation();

		staggerDamagedPlayer();

		dialogueGameState();

		gameOverGameState();

		playGameState();

	}

	private void applyTileSpeedVariation() {
		int tileNum = gp.tileM.mapTileNum[tilePosX][tilePosY];
		speed = gp.tileM.getTile(tileNum).playerSpeed;
	}

	private void updateGlobalPositionMovingVariables() {

		tilePosX = (worldX + 7 + solidArea.x) / gp.tileSize;
		tilePosY = (worldY + 16 + solidArea.y) / gp.tileSize;

		if (prevTilePosX != tilePosX || prevTilePosY != tilePosY) {
			moving = true;
			prevTilePosX = tilePosX;
			prevTilePosY = tilePosY;


			//System.out.println(tilePosX + ", " + tilePosY);
		} else {
			moving = false;
		}
	}

	private void playGameState() {
		if (gp.gameState == gp.playState) {
			if (hasFireAxeEquipped || hasFlameThrowerEquipped) {
				CooldownCounter++;
			} else {
				CooldownCounter = 0;
			}
			if (!attacking)
				playerInputHandler();
			swtichSprites();
			switchAttackSprites();
		}
	}

	private void switchAttackSprites() {
		// downaAttack animation
		if (!attacking) {
			attackAnimCounter = 0;
		}
		attackAnimCounter++;
		if (attackAnimCounter <= 19) {
			attackSpriteNum = attackAnimCounter;
			// attack area
			resetAttackArea();
			switch (animDirection) {
			case "downAttack":
				attackArea.x -= gp.tileSize / 2;
				attackArea.y += gp.tileSize / 2;
				attackArea.width = gp.tileSize * 2;
				attackArea.height = gp.tileSize;
				slashZombieOnHitboxDetection();
				break;
			case "upAttack":
				attackArea.x -= gp.tileSize / 2;
				attackArea.y -= gp.tileSize / 2;
				attackArea.width = gp.tileSize * 2;
				attackArea.height = gp.tileSize;
				slashZombieOnHitboxDetection();
				break;
			case "leftAttack":
				attackArea.x -= gp.tileSize / 2;
				attackArea.y -= gp.tileSize / 2;
				attackArea.width = gp.tileSize;
				attackArea.height = gp.tileSize * 2;
				slashZombieOnHitboxDetection();
				break;
			case "rightAttack":
				attackArea.x += gp.tileSize / 2;
				attackArea.y -= gp.tileSize / 2;
				attackArea.width = gp.tileSize;
				attackArea.height = gp.tileSize * 2;
				slashZombieOnHitboxDetection();
				break;
			}
		}

		if (attackAnimCounter > 19) {

			attackAnimCounter = 0;
			attacking = false;
		}

	}

	private void slashZombieOnHitboxDetection() {
		int zombieIndex;
		zombieIndex = gp.cChecker.checkAreaEntity(attackArea, worldX, worldY, gp.zombies);
		if (zombieIndex != 999 && !gp.zombies.get(zombieIndex).takingDamage) {
			gp.zombies.get(zombieIndex).receiveDamage(OBJ_Fireaxe.damage);
			gp.zombies.get(zombieIndex).knockBack(animDirection);
		}
	}

	private void resetAttackArea() {
		attackArea.x = attackAreaDefaultX;
		attackArea.y = attackAreaDefaultY;
		attackArea.height = attackAreaDefaultHeight;
		attackArea.width = attackAreaDefaultWidth;
	}

	private void playerInputHandler() {
		if (keyH.ePressed) {

			if (hasFireAxeEquipped && hasFlameThrower) {
				setPlayerWithFlameThrowerAnimationFrames();
				hasFireAxeEquipped = false;
			} else if (hasFlameThrowerEquipped && hasFireAxe) {
				setPlayerWithFireAxeAnimationFrames();
				hasFlameThrowerEquipped = false;
			}
			keyH.ePressed = false;

		}

		direction = "idle";
		animDirection = "idle";

		if ((keyH.shootPressed || mouse.mouseLeftPressed) && hasFlameThrowerEquipped) {
			setAnimToAngle();
			fireFlameThrower();
		} else if ((keyH.shootPressed || mouse.mouseLeftPressed) && hasFireAxeEquipped && !attacking) {
			setAnimToAngle();
			fireAxeAttack();
		}

		if ((keyH.shootPressed || mouse.mouseLeftPressed) && hasFlameThrowerEquipped
				&& (!keyH.upPressed && !keyH.downPressed && !keyH.leftPressed && !keyH.rightPressed)) {
			animDirection += "Shooting";

		} else if ((keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) && !attacking) {

//				 walk animation
//				 up
			if (keyH.upPressed && (!keyH.leftPressed && !keyH.rightPressed)) {
				direction = "up";
				animDirection = "up";

			}
			// upLeft
			if (keyH.leftPressed && keyH.upPressed) {
				direction = "upLeft";
				animDirection = "left";

			}
			// upRight
			if (keyH.upPressed && keyH.rightPressed) {
				direction = "upRight";
				animDirection = "right";

			}
			// right
			if (keyH.rightPressed && (!keyH.upPressed && !keyH.downPressed)) {
				direction = "right";
				animDirection = "right";

			}
			// left
			if (keyH.leftPressed && (!keyH.upPressed && !keyH.downPressed)) {
				direction = "left";
				animDirection = "left";

			}
			// down
			if (keyH.downPressed && (!keyH.rightPressed && !keyH.leftPressed)) {
				direction = "down";
				animDirection = "down";

			}
			// downRight
			if (keyH.downPressed && keyH.rightPressed) {
				direction = "downRight";
				animDirection = "right";

			}
			// downLeft
			if (keyH.downPressed && keyH.leftPressed) {
				direction = "downLeft";
				animDirection = "left";

			}
			if ((keyH.shootPressed || mouse.mouseLeftPressed) && hasFlameThrowerEquipped) {
				setAnimToAngle();
				animDirection += "WalkShooting";
				fireFlameThrower();
			}

			playerCollisions();

			playerMoveOnWorld();
		}
	}

	private void fireAxeAttack() {

		if (CooldownCounter >= OBJ_Fireaxe.cooldown && !attacking) {
			animDirection += "Attack";
			attacking = true;
			gp.playSE(2);
			CooldownCounter = 0;

		} else {
			animDirection = "idle";
		}

	}

	private void swtichSprites() {
		spriteCounter++;
		if (spriteCounter > 8)

		{
			if (spriteNum == 1) {
				spriteNum = 2;
			} else if (spriteNum == 2) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}
	}

	private void playerMoveOnWorld() {
		if (!collisionOn) {

			switch (direction) {
			case "upLeft":
				worldY -= Math.round(speed * 0.66);
				worldX -= Math.round(speed * 0.66);
				break;
			case "upRight":
				worldY -= Math.round(speed * 0.66);
				worldX += Math.round(speed * 0.66);
				break;
			case "up":
				worldY -= speed;
				break;
			case "down":
				worldY += speed;
				break;
			case "downLeft":
				worldY += Math.round(speed * 0.66);
				worldX -= Math.round(speed * 0.66);
				break;
			case "downRight":
				worldY += Math.round(speed * 0.66);
				worldX += Math.round(speed * 0.66);
				break;
			case "left":
				worldX -= speed;
				break;
			case "right":
				worldX += speed;
				break;
			}
		}
	}

	private void playerCollisions() {
		collisionOn = false;
		// tile collision
		gp.cChecker.checkTileCollision(this);
		// object collision
		int objInt = gp.cChecker.checkObject(this, true);
		pickUpObject(objInt);

		// Zombie collision
		gp.cChecker.checkEntity(this, gp.zombies);
	}

	private void gameOverGameState() {
		if (gp.gameState == gp.gameOverState) {
			if (keyH.spacePressed) {
				// restart
			}
		}
	}

	private void dialogueGameState() {
		if (gp.gameState == gp.dialogueState) {
			if (keyH.spacePressed) {
				gp.dialogueManager.speak();
				gp.player.keyH.spacePressed = false;
			}
		}
	}

	private void staggerDamagedPlayer() {
		if (takingDamage) {
			stagger();
		}
	}

	@Override
	public void receiveDamage(int dmg) {

		if (currentLife > 0) {
			takingDamage = true;
			currentLife -= dmg;
		} else if (currentLife <= 0) {
			dyingAnimPlay = true;
			gp.gameState = gp.gameOverState;
		}
	}

	private void setAnimToAngle() {

		int distanceX = mouse.mouseLeftPressedX - (screenX + gp.tileSize / 2);
		int distanceY = mouse.mouseLeftPressedY - (screenY + gp.tileSize / 2);

		double angle = Math.atan2(distanceX, distanceY);

		angle = angle * 180 / Math.PI;
		if (angle < 0) {
			angle += 360;
		}

		if (angle >= 45 && angle <= 134) {
			animDirection = "right";
			offsetDir = "right";
		} else if (angle >= 135 && angle <= 224) {
			animDirection = "up";
			offsetDir = "up";
		} else if (angle >= 225 && angle <= 314) {
			animDirection = "left";
			offsetDir = "left";
		} else {
			animDirection = "down";
			offsetDir = "down";
		}
	}

	private void fireFlameThrower() {

		if (CooldownCounter >= OBJ_Flamethrower.cooldown && inv.ammoCount > 0) {
			inv.ammoCount--;
			projectile = new OBJ_FireProjectile(gp);
			projectile.set(worldX, worldY, mouse.mouseLeftPressedX, mouse.mouseLeftPressedY, offsetDir, offsetDir, 35,
					"Player");
			gp.projectileList.add(projectile);
			CooldownCounter = 0;
		}

	}

	public void getPlayerImage() {

		up1 = AnimationManager.up1Player;
		up2 = AnimationManager.up2Player;
		down1 = AnimationManager.down1Player;
		down2 = AnimationManager.down2Player;
		left1 = AnimationManager.left1Player;
		left2 = AnimationManager.left2Player;
		right1 = AnimationManager.right1Player;
		right2 = AnimationManager.right2Player;
		idle1 = AnimationManager.idle1Player;
		idle2 = AnimationManager.idle2Player;

	}

	private void setPlayerWithFlameThrowerAnimationFrames() {

		hasFlameThrowerEquipped = true;

		up1Shooting = AnimationManager.up1ShootingFlame;
		up2Shooting = AnimationManager.up2ShootingFlame;

		down1 = AnimationManager.down1Flame;
		down2 = AnimationManager.down2Flame;

		down1Shooting = AnimationManager.down1ShootingFlame;
		down2Shooting = AnimationManager.down2ShootingFlame;
		down1WalkShooting = AnimationManager.down1WalkShootingFlame;
		down2WalkShooting = AnimationManager.down2WalkShootingFlame;

		left1 = AnimationManager.left1Flame;
		left2 = AnimationManager.left2Flame;
		left1Shooting = AnimationManager.left1ShootingFlame;
		left2Shooting = AnimationManager.left2ShootingFlame;

		right1 = AnimationManager.right1Flame;
		right2 = AnimationManager.right2Flame;
		right1Shooting = AnimationManager.right1ShootingFlame;
		right2Shooting = AnimationManager.right2ShootingFlame;
		right1WalkShooting = AnimationManager.right1WalkShootingFlame;
		right2WalkShooting = AnimationManager.right2WalkShootingFlame;

		idle1 = AnimationManager.idle1Flame;
		idle2 = AnimationManager.idle2Flame;

	}

	public void setPlayerWithFireAxeAnimationFrames() {
		hasFireAxeEquipped = true;
		down1 = AnimationManager.down1PlayerAxe;
		down2 = AnimationManager.down2PlayerAxe;
		left1 = AnimationManager.left1PlayerAxe;
		left2 = AnimationManager.left2PlayerAxe;
		right1 = AnimationManager.right1PlayerAxe;
		right2 = AnimationManager.right2PlayerAxe;
		idle1 = AnimationManager.idle1PlayerAxe;
		idle2 = AnimationManager.idle2PlayerAxe;

	}

	public BufferedImage setup(String imageName, int width, int height) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/" + name + "/" + imageName + ".png"));
			image = uTool.scaleImage(image, width, height);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}

	@Override
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;
		switch (animDirection) {
		case "up":
			if (spriteNum == 1) {
				image = up1;
			}
			if (spriteNum == 2) {
				image = up2;
			}
			break;
		case "upWalkShooting":
			if (spriteNum == 1) {
				image = up1;
			}
			if (spriteNum == 2) {
				image = up2;
			}
			break;
		case "upShooting":
			if (spriteNum == 1) {
				image = up1Shooting;
			}
			if (spriteNum == 2) {
				image = up2Shooting;
			}
			break;
		case "down":
			if (spriteNum == 1) {
				image = down1;
			}
			if (spriteNum == 2) {
				image = down2;
			}
			break;
		case "downShooting":
			if (spriteNum == 1) {
				image = down1Shooting;
			}
			if (spriteNum == 2) {
				image = down2Shooting;
			}
			break;
		case "downLeftShooting":
			if (spriteNum == 1) {
				image = left1Shooting;
			}
			if (spriteNum == 2) {
				image = left2Shooting;
			}
			break;
		case "downRightShooting":
			if (spriteNum == 1) {
				image = right1;
			}
			if (spriteNum == 2) {
				image = right1;
			}
			break;
		case "downWalkShooting":
			if (spriteNum == 1) {
				image = down1WalkShooting;
			}
			if (spriteNum == 2) {
				image = down2WalkShooting;
			}
			break;
		case "left":
			if (spriteNum == 1) {
				image = left1;
			}
			if (spriteNum == 2) {
				image = left2;
			}
			break;
		case "leftShooting":
			if (spriteNum == 1) {
				image = left1Shooting;
			}
			if (spriteNum == 2) {
				image = left2Shooting;
			}
			break;
		case "upLeftShooting":
			if (spriteNum == 1) {
				image = left1Shooting;
			}
			if (spriteNum == 2) {
				image = left2Shooting;
			}
			break;
		case "leftWalkShooting":
			if (spriteNum == 1) {
				image = left1;
			}
			if (spriteNum == 2) {
				image = left2;
			}
			break;
		case "upLeftWalkShooting":
			if (spriteNum == 1) {
				image = left1;
			}
			if (spriteNum == 2) {
				image = left2;
			}
			break;
		case "right":
			if (spriteNum == 1) {
				image = right1;
			}
			if (spriteNum == 2) {
				image = right2;
			}
			break;
		case "rightWalkShooting":
			if (spriteNum == 1) {
				image = right1;
			}
			if (spriteNum == 2) {
				image = right2;
			}
			break;
		case "upRightWalkShooting":
			if (spriteNum == 1) {
				image = right1;
			}
			if (spriteNum == 2) {
				image = right2;
			}
			break;
		case "downRightWalkShooting":
			if (spriteNum == 1) {
				image = right1;
			}
			if (spriteNum == 2) {
				image = right2;
			}
			break;
		case "rightShooting":
			if (spriteNum == 1) {
				image = idle1;
			}
			if (spriteNum == 2) {
				image = idle2;
			}
			break;
		case "upRightShooting":
			if (spriteNum == 1) {
				image = right1WalkShooting;
			}
			if (spriteNum == 2) {
				image = right2WalkShooting;
			}
			break;
		case "idle":
			if (spriteNum == 1) {
				image = idle1;
			}
			if (spriteNum == 2) {
				image = idle2;
			}
			break;
		case "idleWalkShooting":
			if (spriteNum == 1) {
				image = down1WalkShooting;
			}
			if (spriteNum == 2) {
				image = down2WalkShooting;
			}
			break;
		case "idleShooting":
			if (spriteNum == 1) {
				image = down1WalkShooting;
			}
			if (spriteNum == 2) {
				image = down1WalkShooting;
			}
			break;
		case "downAttack":
			tempScreenX = screenX - gp.tileSize / 2;
			tempScreenY = screenY - gp.tileSize / 2;
			image = AnimationManager.downAttackArray[attackSpriteNum];
			break;
		case "leftAttack":
			tempScreenX = screenX - gp.tileSize / 2;
			tempScreenY = screenY - gp.tileSize / 2;
			image = AnimationManager.leftAttackArray[attackSpriteNum];
			break;
		case "rightAttack":
			tempScreenX = screenX - gp.tileSize / 2;
			tempScreenY = screenY - gp.tileSize / 2;
			image = AnimationManager.rightAttackArray[attackSpriteNum];
			break;
		case "upAttack":
			tempScreenX = screenX - gp.tileSize / 2;
			tempScreenY = screenY - gp.tileSize / 2;
			image = AnimationManager.upAttackArray[attackSpriteNum];
			break;
		}
		if(!invisible)
		g2.drawImage(image, tempScreenX, tempScreenY, null);

		if (currentLife < maxLife) {
			double oneScale = (double) gp.tileSize / maxLife;
			double hpBarValue = oneScale * currentLife;
			g2.setColor(Color.black);
			g2.fillRect(screenX - 2, screenY - 12, gp.tileSize + 4, 8);
			g2.setColor(Color.green);
			g2.fillRect(screenX, screenY - 10, (int) hpBarValue, 4);

		}
// solid area for collision
//		 g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width,
//		 solidArea.height);
// attack area
//		g2.setColor(Color.red);
//		g2.drawRect(attackArea.x + screenX, attackArea.y + screenY, attackArea.width, attackArea.height);

	}

	public void reset() {
		setDefaultValues();
		getPlayerImage();
		inv.resetInventory();
		
	}

}
