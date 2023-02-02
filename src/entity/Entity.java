package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import main.GamePanel;
import pathfinding.Node;
import utilz.UtilityTool;

public class Entity {
	protected GamePanel gp;

	public Entity(GamePanel gp) {
		this.gp = gp;
	}

	public int worldX, worldY, tilePosX, tilePosY, maxLife, agroRange, dialogueIndex, currentLife, chaseSpeed,
			huntSpeed, nextX, nextY, speedPenalty = 0;
	public double speed;
	public String name, type, direction, lastDirection, animDirection, collideDirection;
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, idle1, idle2, up1Shooting, up2Shooting,
			down1Shooting, down2Shooting, left1Shooting, left2Shooting, right1Shooting, right2Shooting, idle1Shooting,
			idle2Shooting, down1WalkShooting, down2WalkShooting, right1WalkShooting, right2WalkShooting, damaged,
			zombieDeathSpritesheetAnim, downAttack1, downAttack2, downAttack3, downAttack4, downAttack5, downAttack6,
			projectileLight;
	public Projectile projectile;
	public boolean alive, onPath, takingDamage, playerInRange, spawning, collisionOn, collision, collisionOnPlayer,
			collisionOnZombie, isHunting, wasWalking, isSlowed, wasChasing, closing, opening;
	public int spriteCounter = 0, staggerCounter = 0, deathAnimCounter = 0, spriteNum = 1, attackSpriteNum = 0,
			pathLockCounter = 0;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public int solidAreaDefaultX = solidArea.x;
	public int solidAreaDefaultY = solidArea.y;
	public int distToDump;
	public BufferedImage image = null;
	public int actionLockCounter = 0;
	protected boolean dyingAnimPlay = false;
	protected String dialogues[] = new String[20];
	public UtilityTool uTool = new UtilityTool();
	public List<Node> zPath;
	int[] prevPos = new int[2];

	public void update() { 
	}

	protected void switchSprites() {
		spriteCounter++;
		if (spriteCounter > 8) {
			if (spriteNum == 1) {
				spriteNum = 2;
			} else if (spriteNum == 2) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}
	}

	protected void walk() {
		if (!collisionOn) {
			switch (direction) {
			case "up":
				worldY -= speed;
				break;
			case "down":
				worldY += speed;
				break;
			case "left":
				worldX -= speed;
				break;
			case "right":
				worldX += speed;
				break;
			}
			wasWalking = true;
		}
	}

	public void receiveDamage(int dmg) {

		if (currentLife > 0) {
			takingDamage = true;
			currentLife -= dmg;
			gp.playSE(9);
		}
	}

	protected void die() {
		alive = false;
	}

	public void stagger() {
	}

	public void speak() {
		gp.gameState = gp.dialogueState;
		gp.dialogueManager.dialogues = dialogues;
		gp.dialogueManager.speak();
	}

	public void snapToGrid() {
		worldX = (worldX + solidArea.x + 16) / gp.tileSize * gp.tileSize;
		worldY = (worldY + solidArea.y + 16) / gp.tileSize * gp.tileSize;
	}

	public void speak(int i) {

	}

	public void setDialogue(int i) {

	}

	public void checkCollision() {
		
		collisionOn = false;
		gp.cChecker.checkTileCollision(this);
		collisionOnPlayer = false;
		gp.cChecker.checkPlayer(this);
		if (collisionOnPlayer && !gp.player.takingDamage) {
			gp.player.receiveDamage(1);
		}
	}
	public void searchPath(int col, int row) {
		int startCol = (worldX + solidArea.x)/gp.tileSize;
		int startRow = (worldY + solidArea.y)/gp.tileSize;

		gp.pathfinder.setNodes(startCol, startRow, col, row);

		if(gp.pathfinder.search() == true) {
			int nextX = gp.pathfinder.pathList.get(0).col * gp.tileSize;
			int nextY = gp.pathfinder.pathList.get(0).row * gp.tileSize;

			int leftX = worldX + solidArea.x;
			int rightX = worldX + solidArea.x + solidArea.width;
			int topY = worldY + solidArea.y;
			int bottomY = worldY + solidArea.y + solidArea.height;
			
			if(topY > nextY && leftX >= nextX && rightX < nextX + gp.tileSize) {
				direction = "up";
			}
			else if(topY < nextY && leftX >= nextX && rightX < nextX + gp.tileSize) {
				direction = "down";
			}
			else if (topY >= nextY && bottomY < nextY + gp.tileSize) {
				if(leftX > nextX) {
					direction = "left";
				}
				if(leftX < nextX) {
					direction = "right";
				}
			}
			else if(topY > nextY && leftX > nextX) {
				direction = "up";
				checkCollision();
				if(collisionOn) {
					direction = "left";
				}
			}else if(topY > nextY && leftX < nextX) {
				direction = "up";
				checkCollision();
				if(collisionOn) {
					direction = "right";
				}
			}
			else if( topY < nextY && leftX > nextX) {
				direction = "down";
				checkCollision();
				if(collisionOn) {
					direction = "left";
				}
			}
			else if( topY < nextY && leftX < nextX) {
				direction = "down";
				checkCollision();
				if(collisionOn) {
					direction = "right";
				}
			}
			
			int nextCol = gp.pathfinder.pathList.get(0).col;
			int nextRow = gp.pathfinder.pathList.get(0).row;
			if(nextCol == col && nextRow == row) {
				onPath = false;
			}
		}
		
		
	}

	public void getCrateMaterials() {
		// TODO Auto-generated method stub
	}

	public void knockBack(String animDirection2) {

	}

	public void draw(Graphics2D g2) {
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;

		if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
				&& worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
				&& worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
				&& worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

			if (!takingDamage && !dyingAnimPlay && type != "object") {

				switch (direction) {

				case "up":
					if (spriteNum == 1) {
						image = up1;
					}
					if (spriteNum == 2) {
						image = up2;
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

				case "left":
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

				case "idle":
					if (spriteNum == 1) {
						image = idle1;
					}
					if (spriteNum == 2) {
						image = idle2;
					}
					break;
				}
			}

			if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
					&& worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
					&& worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
					&& worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

				g2.drawImage(image, screenX, screenY, null);
				// g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width,
				// solidArea.height);
				// Zombie HPbar
				if (name == "Zombie" && alive && currentLife < maxLife && currentLife > 0) {
					double oneScale = (double) gp.tileSize / maxLife;
					double hpBarValue = oneScale * currentLife;
					g2.setColor(Color.black);
					g2.fillRect(screenX - 2, screenY - 12, gp.tileSize + 4, 8);
					g2.setColor(Color.red);
					g2.fillRect(screenX, screenY - 10, (int) hpBarValue, 4);

				}
				// base HPbar
				if (name == "Barrel Dump" && currentLife < maxLife) {
					double oneScale = (double) gp.tileSize / maxLife;
					double hpBarValue = oneScale * currentLife;
					g2.setColor(Color.black);
					g2.fillRect(screenX - 2, screenY - 12, gp.tileSize + 4, 8);
					g2.setColor(Color.green);
					g2.fillRect(screenX, screenY - 10, (int) hpBarValue, 4);

				}

			}
//			if (name == "Zombie") {
//				g2.setColor(Color.black);
//				g2.fillOval(worldX + gp.tileSize / 2 - gp.player.worldX + gp.player.screenX,
//						worldY + gp.tileSize / 2 - gp.player.worldY + gp.player.screenY, 10, 10);
//				g2.setColor(Color.red);
//				zPath = gp.zombies.get(0).getZPath();
//				for(int i = 0; i < zPath.size(); i++) {
//					g2.fillOval(zPath.get(i).getxPosition() *gp.tileSize - gp.player.worldX + gp.player.screenX,
//							zPath.get(i).getyPosition() *gp.tileSize   - gp.player.worldY + gp.player.screenY, 10, 10);
//				}
//				g2.fillOval(nextX - gp.player.worldX + gp.player.screenX,
//						nextY  - gp.player.worldY + gp.player.screenY, 10, 10);
//			}

		}

	}

	public Object getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
