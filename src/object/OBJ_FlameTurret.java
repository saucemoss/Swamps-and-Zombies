package object;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_FlameTurret extends Entity {
	public static int damage = 2;
	public static int turretCooldown = 4;
	public static int range = 200;
	int CooldownCounter = 0;
	private boolean targetLocked;
	BufferedImage imageTop, imageBottom;
	double angle;

	public OBJ_FlameTurret(GamePanel gp) {
		super(gp);
		type = "object";
		name = "Flame Turret";
		dialogueIndex = 0;

		try {
			imageTop = ImageIO.read(getClass().getResource("/objects/turret3Top.png"));
			imageTop = uTool.scaleImage(imageTop, gp.tileSize, gp.tileSize);
			imageBottom = ImageIO.read(getClass().getResource("/objects/turret3Bottom.png"));
			imageBottom = uTool.scaleImage(imageBottom, gp.tileSize, gp.tileSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// setDialogue();
	}

	public void setDialogue() {

	}

	@Override
	public void update() {
		CooldownCounter++;
		Entity target = null;
		if (CooldownCounter >= turretCooldown) {

			for (Entity e : gp.zombies) {
				int distance = uTool.distanceBetween(worldX, worldY, e.worldX, e.worldY);
				if (distance < range && !targetLocked && e.alive) {
					target = e;
					break;
				}
			}
			if (target != null) {
				angle = Math.atan2(worldX-target.worldX+24,worldY-target.worldY+24);
				angle = angle * 180 / Math.PI;
				if (angle < 0) {
					angle += 360;
				}
				String offsetDir;
				if (angle >= 45 && angle <= 134) {
					offsetDir = "left";
				} else if (angle >= 135 && angle <= 224) {
					offsetDir = "down";
				} else if (angle >= 225 && angle <= 314) {
					offsetDir = "right";
				} else {
					offsetDir = "up";
				}
				projectile = new OBJ_FireProjectile(gp);
				projectile.set(worldX, worldY, target.worldX, target.worldY, offsetDir, offsetDir, 35, "Flame Turret");
				gp.projectileList.add(projectile);
				CooldownCounter = 0;
			}
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
				&& worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
				&& worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
				&& worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
			g2.drawImage(imageBottom, screenX, screenY, null);
			AffineTransform at = AffineTransform.getTranslateInstance(screenX, screenY);
			at.rotate(Math.toRadians(-angle), imageTop.getWidth()/2, imageTop.getHeight()/2);
			g2.drawImage(imageTop, at, null);


		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
