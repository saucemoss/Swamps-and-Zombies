package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.GamePanel;
import object.OBJ_FlameTurret;
import object.OBJ_Flamethrower;

public class Projectile extends Entity {
	int ttl;
	public String name;
	String lastDirection;
	public boolean alive = true;
	public boolean ignite = false;
	boolean stop = false;
	private int directionX;
	private int directionY;
	double spread;
	public String originator;
	Random rand = new Random();

	public Projectile(GamePanel gp) {
		super(gp);
		solidArea = new Rectangle();
		solidArea.x = 17;
		solidArea.y = 19;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 16;
		solidArea.height = 20;
		name = "Projectile";

	}



	public void set(int worldX, int worldY, int directionX, int directionY, String offsetDir, String direction, int ttl,
			String originator) {
		this.directionX = directionX;
		this.directionY = directionY;
		this.worldX = worldX;
		this.worldY = worldY;
		this.ttl = ttl;
		this.ignite = willIgnite();
		this.direction = direction;
		this.originator = originator;
		offsetByDirection(offsetDir);

	}


	private void offsetByDirection(String offsetDir) {

		switch (offsetDir) {
		case "up":
			worldY -= 32;
			break;
		case "down":
			worldY += 32;
			break;
		case "left":
			worldX -= 32;
			break;
		case "right":
			worldX += 32;
			break;
		}

	}

	private double spread() {
		return rand.nextFloat()-0.5f;
	}

	private int flameFallTime() {
		return (rand.nextInt(20));
	}

	private boolean willIgnite() {
		int x = (rand.nextInt(11)) - 5;
		if (x > 1) {
			return true;
		} else
			return false;
	}

	@Override
	public void update() {
		if (name == "Fire Projectile")
			flameThrower();
		collidedWZombie();
		gp.cChecker.checkTileCollision(this);
		gp.cChecker.checkObject(this, false);

	}

	private void collidedWZombie() {
		int zombieHittedId = gp.cChecker.checkEntity(this, gp.zombies);
		int zombieCounter = 0;

		if (collisionOn && zombieHittedId != 999) {
			if (ignite && gp.zombies.get(zombieHittedId).takingDamage) {

			} else {
				if (originator.equals("Player")) {
					gp.zombies.get(zombieHittedId).receiveDamage(OBJ_Flamethrower.damage);
				}
				if (originator.equals("Flame Turret")) {
					gp.zombies.get(zombieHittedId).receiveDamage(OBJ_FlameTurret.damage);
				}
			}
			if (!ignite)
				alive = false;
		}

	}

	private void flameThrower() {
		spread = spread();

		spriteCounter++;
		if (spriteCounter > 2) {
			if (spriteNum == 1) {
				spriteNum = 2;
			} else if (spriteNum == 2) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}
		ttl--;

		if (ttl == flameFallTime() && ignite) {
			ttl = 25;
			stop = true;
		}
		if (ttl < 0) {
			alive = false;
		}
		int distanceX = 0;
		int distanceY = 0;
		if (!stop && !collisionOn) {

			if (originator.equals("Player")) {
				distanceX = directionX - gp.player.screenX;
				distanceY = directionY - gp.player.screenY;
			}
			if (originator.equals("Flame Turret")) {
				distanceX = directionX - worldX;
				distanceY = directionY - worldY;
			}

			double angle = Math.atan2(distanceY, distanceX);
			worldX += speed * Math.cos(angle + spread);
			worldY += speed * Math.sin(angle + spread);
		}
	}

	@Override
	public void draw(Graphics2D g2) {

		BufferedImage image = null;

		if (spriteNum == 1) {
			image = up1;
		}
		if (spriteNum == 2) {
			image = up2;
		}

		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;

		if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
				&& worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
				&& worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
				&& worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
			g2.drawImage(projectileLight, screenX - 50, screenY - 50, 150, 150, null);
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
//			 g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width,
//			 solidArea.height);
		}
	}


}
