package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import entity.Entity;
import inputs.KeyHandler;
import inputs.MyMouseListener;
import object.OBJ_FlameTurret;
import object.OBJ_Flamethrower;
import object.OBJ_Palisade;
import utilz.UtilityTool;

public class UI {
	GamePanel gp;
	Graphics2D g2;
	private BufferedImage turretButtonImg, palisadeButtonImg, selectedObjImg, keyButtonImg;
	private Rectangle turretButton = new Rectangle();
	private Rectangle palisadeButton = new Rectangle();
	private Rectangle buildMenu = new Rectangle();
	private Rectangle keyButton = new Rectangle();
	public ArrayList<Rectangle> availableArea = new ArrayList<>();
	public boolean messageOn, played;
	public String message;
	int messageCounter = 0;
	int tileUnderMouse;
	public boolean gameFinished = false;
	public Font pixelFont;
	public String currentDialogue, selectedObj = "Nothing", startDialogue, endDialogue;
	double oneScale, hpBarValue, ammoBarValue;
	public UtilityTool uTool = new UtilityTool();
	protected MyMouseListener mouse;
	double playTime;
	DecimalFormat dFormat = new DecimalFormat("#0.00");
	KeyHandler keyH;

	public UI(GamePanel gp, KeyHandler keyH, MyMouseListener mouse) {
		this.gp = gp;
		this.keyH = keyH;
		this.mouse = mouse;
		gp.animManager.loadUI();

		InputStream is = getClass().getResourceAsStream("/fonts/Early GameBoy.ttf");
		try {
			pixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void draw(Graphics2D g2) {
//debug buildings
		gp.player.inv.setPalisades(2);
		gp.player.inv.setTurrets(2);
		this.g2 = g2;

		g2.setFont(pixelFont);
		g2.setColor(Color.white);

		if (gp.gameState == gp.playState) {
			played = false;
			drawPlayerHud();
			drawHealthBar();
			drawAmmoBar();
			drawWeaponIcons();
			drawBarrelCount();
			// do play stuff later
		} else if (gp.gameState == gp.pauseState) {
			drawPauseScreen();
		} else if (gp.gameState == gp.dialogueState) {
			drawDialogueScreen();
		} else if (gp.gameState == gp.gameOverState) {
			drawGameOverScreen();
		} else if (gp.gameState == gp.endGameState) {
			drawEndGame();
		} else if (gp.gameState == gp.startGameState) {
			drawStartGame();
		} else if (gp.gameState == gp.craftState) {
			if (!played) {
				gp.playSE(8);
				played = true;
			}
			drawPlayerHud();
			drawHealthBar();
			drawAmmoBar();
			drawWeaponIcons();
			drawBarrelCount();
		} else if (gp.gameState == gp.menuState) {
			drawMenuState();
		}
	}

	private void drawMenuState() {
		String options = "1 - map 1\n2 - map 2\n3 - map 3\n\nESC - Quit";
		int x = gp.tileSize / 2;
		int y = gp.tileSize / 2;
		int width = gp.tileSize * 7;
		int height = gp.tileSize * 8;
		drawSubWindow(x, y, width, height);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
		x += gp.tileSize/ 2;
		y += gp.tileSize;

		for (String line : options.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
		g2.drawString("FPS:" + gp.FPSout, gp.tileSize * 29 - 20, gp.tileSize);
		g2.drawString("Zombies:" + gp.zombies.size(), gp.tileSize * 29 - 20, gp.tileSize * 2 - gp.tileSize / 2);

		
	}

	private void drawStartGame() {

		startDialogue = "You are on a contaminated swamp. "
				+ "\nEach day new barrels with radioactive waste can be found."
				+ "\nCollect them at night when zombies are less aggressive. "
				+ "\nWhen all barrels found, dump them into the storage. "
				+ "\nBeware, during the day zombies will be attracted \nto ever growing pile of wase you've accumulated."
				+ "\nMore waste - more zombies. Look for supplie drops \nto help you denfend."
				+ "\nRemember to place defences before storing barrels each night. \nOnce they breach your defences all is lost."
				+ "\n\nWSAD to move \nE to change weapon \nC or RMB to place defences\nLMB to attack";

		int x = gp.tileSize * 2;
		int y = gp.tileSize / 2;
		int width = gp.screenWidth - (gp.tileSize * 4);
		int height = gp.tileSize * 17;
		drawSubWindow(x, y, width, height);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 22F));
		x += gp.tileSize;
		y += gp.tileSize;

		for (String line : startDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
		x = gp.screenWidth - (gp.tileSize * 9);
		y = gp.tileSize * 17;
		g2.drawString("Press space to continue", x, y + (gp.tileSize / 4));

	}

	private void drawEndGame() {
		endDialogue = "\n\n        You survived!\n        THE END";
		int x = gp.tileSize * 2;
		int y = gp.tileSize / 2;
		int width = gp.screenWidth - (gp.tileSize * 4);
		int height = gp.tileSize * 5;
		drawSubWindow(x, y, width, height);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
		x += gp.tileSize;
		y += gp.tileSize;

		for (String line : endDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
		x = gp.tileSize * 8;
		y = gp.tileSize * 5;
		g2.drawString("Press space to start new game.", x, y + (gp.tileSize / 4));

	}

	private void drawBarrelCount() {
		g2.drawImage(AnimationManager.radioactiveBarrel, gp.tileSize + gp.tileSize / 2, gp.tileSize * 4 + 4,
				gp.tileSize, gp.tileSize, null);

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 19F));
		g2.drawString(gp.player.inv.barrels + " of " + gp.wm.barrelsThisWave, gp.tileSize * 2 + 26,
				gp.tileSize * 4 + 36);

	}

	private void drawPlayerHud() {
		int x = gp.tileSize;
		int y = gp.tileSize;
		int width = gp.tileSize * 4;
		int height = gp.tileSize * 5;
		g2.setColor(Color.white);
		g2.fillRoundRect(gp.tileSize + 24, gp.tileSize * 3, gp.tileSize, gp.tileSize, 25, 25);
		g2.fillRoundRect(gp.tileSize * 2 + 24, gp.tileSize * 3, gp.tileSize, gp.tileSize, 25, 25);
		g2.fillRoundRect(gp.tileSize * 3 + 24, gp.tileSize * 3, gp.tileSize, gp.tileSize, 25, 25);
		g2.fillRoundRect(gp.tileSize + 24, gp.tileSize * 4 + 4, gp.tileSize * 3, gp.tileSize, 25, 25);

		// <--BEHIND WINDOW
		// hud
		drawSubWindow(x + 10, y, width - 20, height - 30);
		// night/day txt
		drawSubWindow(gp.tileSize * 5 - 10, gp.tileSize, gp.tileSize * 4, gp.tileSize);
		String hudLine = "";
		if (gp.wm.night) {
			hudLine = "Night: " + (gp.wm.currentWave + 1) + " of " + gp.wm.waves.size();
		} else {
			hudLine = "Day: " + (gp.wm.currentWave + 1) + " of " + gp.wm.waves.size();
		}
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
		g2.drawString(hudLine, gp.tileSize * 5 + 8, gp.tileSize + 30);

		g2.drawString("FPS:" + gp.FPSout, gp.tileSize * 29 - 20, gp.tileSize);
		g2.drawString("Zombies:" + gp.zombies.size(), gp.tileSize * 29 - 20, gp.tileSize * 2 - gp.tileSize / 2);

		// build menu
		buildMenu(y, width, height);
	}

	private void buildMenu(int y, int width, int height) {
		turretButtonImg = AnimationManager.flameTurretIcon;
		palisadeButtonImg = AnimationManager.lvl1Palisade;
		keyButtonImg = AnimationManager.key;

		turretButton.x = gp.tileSize * 5;
		turretButton.y = y * 2 + 10;
		turretButton.height = gp.tileSize;
		turretButton.width = gp.tileSize;
		g2.setColor(Color.white);
		g2.fillRoundRect(turretButton.x, turretButton.y, turretButton.width, turretButton.height, 25, 25);

		palisadeButton.x = gp.tileSize * 5;
		palisadeButton.y = y * 3 + 10;
		palisadeButton.height = gp.tileSize;
		palisadeButton.width = gp.tileSize;
		g2.setColor(Color.white);
		g2.fillRoundRect(palisadeButton.x, palisadeButton.y, palisadeButton.width, palisadeButton.height, 25, 25);

		keyButton.x = gp.tileSize * 5;
		keyButton.y = y * 4 + 10;
		keyButton.height = gp.tileSize;
		keyButton.width = gp.tileSize;
		g2.setColor(Color.white);
		g2.fillRoundRect(keyButton.x, keyButton.y, keyButton.width, keyButton.height, 25, 25);
		// <--behind window
		drawSubWindow(gp.tileSize * 5 - 10, y + gp.tileSize, gp.tileSize + gp.tileSize / 2 - 5,
				height - 30 - gp.tileSize);
		buildMenu.x = gp.tileSize * 5 - 10;
		buildMenu.y = gp.tileSize * 2;
		buildMenu.width = width;
		buildMenu.height = height - 30 - gp.tileSize;
		g2.setStroke(new BasicStroke(2));

		if (gp.player.inv.keys > 0) {
			g2.drawImage(keyButtonImg, keyButton.x, keyButton.y, keyButton.width, keyButton.height, null);
			g2.drawString("" + gp.player.inv.keys, keyButton.x + 30, keyButton.y + gp.tileSize - 8);
		}
		if (gp.player.inv.getPalisades() > 0) {
			g2.drawImage(palisadeButtonImg, palisadeButton.x, palisadeButton.y, palisadeButton.width,
					palisadeButton.height, null);
			g2.drawString("" + gp.player.inv.getPalisades(), palisadeButton.x + 30, palisadeButton.y + gp.tileSize - 8);
		}
		if (gp.player.inv.getTurrets() > 0) {
			g2.drawImage(turretButtonImg, turretButton.x, turretButton.y, turretButton.width, turretButton.height,
					null);
			g2.drawString("" + gp.player.inv.getTurrets(), turretButton.x + 30, turretButton.y + gp.tileSize - 8);
		}

		if (buildMenu.contains(mouse.mouseLeftPressedX, mouse.mouseLeftPressedY) && mouse.mouseLeftPressed) {
			gp.gameState = gp.craftState;
		}
		if (gp.gameState == gp.craftState) {
			gp.player.snapToGrid();
			Color redAlpha = new Color(255, 0, 0, 150);
			Color greenAlpha = new Color(0, 255, 0, 150);
			Color greyAlpha = new Color(40, 40, 40, 150);
			Color whiteRange = new Color(255, 255, 255, 70);

			int mX = (mouse.mouseMovedX + gp.tileSize / 2) / gp.tileSize * gp.tileSize;
			int mY = (mouse.mouseMovedY + gp.tileSize / 2) / gp.tileSize * gp.tileSize;
			int wX = mX + gp.player.worldX / gp.tileSize * gp.tileSize - gp.player.screenX;
			int wY = mY + gp.player.worldY / gp.tileSize * gp.tileSize - gp.player.screenY;
			int sX = wX - gp.player.worldX + gp.player.screenX;
			int sY = wY - gp.player.worldY + gp.player.screenY;
			// get tile underneath mouse cursor
			//tileUnderMouse = gp.tileM.mapTileNum[wX / gp.tileSize][wY / gp.tileSize];

			// draw grid close to player
			int col = 0;
			int row = 0;
			int buildRange = 170;

			while (col < gp.tileM.dimension && row < gp.tileM.dimension) {
				int tileNum = gp.tileM.mapTileNum[col][row];
				int worldX = col * gp.tileSize;
				int worldY = row * gp.tileSize;
				int screenX = worldX - gp.player.worldX + gp.player.screenX;
				int screenY = worldY - gp.player.worldY + gp.player.screenY;
				int distance = uTool.distanceBetween(worldX, worldY, gp.player.worldX, gp.player.worldY);
				if (distance < buildRange) {
					g2.setColor(Color.black);
					g2.drawRect(screenX, screenY, gp.tileSize, gp.tileSize);
					entity.Entity objOnGound = gp.aSetter.getObjFromCoords(col, row);
					if (objOnGound != null) {
						g2.setColor(greyAlpha);
						if (objOnGound.name.equals("Flame Turret") && selectedObj.equals("Flame Turret")) {
							g2.setColor(whiteRange);
							int objX = objOnGound.worldX - gp.player.worldX + gp.player.screenX;
							int objY = objOnGound.worldY - gp.player.worldY + gp.player.screenY;
							g2.drawOval(objX - OBJ_FlameTurret.range + gp.tileSize / 2,
									objY - OBJ_FlameTurret.range + gp.tileSize / 2, OBJ_FlameTurret.range * 2,
									OBJ_FlameTurret.range * 2);
						}
					} else if (tileNum == 31 && selectedObj.equals("Flame Turret") && objOnGound == null) {
						g2.setColor(greenAlpha);
						addToAvailableArea(col, row);
					} else if (gp.tileM.tile[tileNum].tileName.equals("Dirt") && selectedObj.equals("Palisade")
							&& objOnGound == null) {
						g2.setColor(greenAlpha);
						addToAvailableArea(col, row);
					} else if (gp.tileM.tile[tileNum].collision) {
						g2.setColor(greyAlpha);
					} else {
						g2.setColor(redAlpha);
					}
					g2.setStroke(new BasicStroke(2));
					g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);

					if (sX - gp.tileSize / 2 == screenX && sY - gp.tileSize / 2 == screenY) {
						g2.setColor(Color.black);
						g2.drawRect(sX - gp.tileSize / 2 - 2, sY - gp.tileSize / 2 - 2, gp.tileSize, gp.tileSize);
						if (selectedObj.equals("Flame Turret")) {
							g2.setColor(whiteRange);
							g2.fillOval(sX - gp.tileSize / 2 - OBJ_FlameTurret.range + gp.tileSize / 2,
									sY - gp.tileSize / 2 - 2 - OBJ_FlameTurret.range + gp.tileSize / 2,
									OBJ_FlameTurret.range * 2, OBJ_FlameTurret.range * 2);

						}
					}
				}
				col++;

				if (col == gp.tileM.dimension) {
					col = 0;
					row++;
				}
			}

			// set button to clickable
			if (gp.player.inv.turrets > 0) {
				if (turretButton.contains(mouse.mouseMovedX, mouse.mouseMovedY)) {
					g2.setColor(Color.white);
					g2.setStroke(new BasicStroke(2));
					g2.drawRoundRect(turretButton.x, turretButton.y, turretButton.width, turretButton.height, 25, 25);

					if (mouse.mouseLeftPressed) {
						availableArea.clear();
						selectedObj = "Flame Turret";
						selectedObjImg = turretButtonImg;
					}
				}
			}
			if (gp.player.inv.palisades > 0) {
				if (palisadeButton.contains(mouse.mouseMovedX, mouse.mouseMovedY)) {
					g2.setColor(Color.white);
					g2.setStroke(new BasicStroke(2));
					g2.drawRoundRect(palisadeButton.x, palisadeButton.y, palisadeButton.width, palisadeButton.height,
							25, 25);
					if (mouse.mouseLeftPressed) {
						availableArea.clear();
						selectedObj = "Palisade";
						selectedObjImg = palisadeButtonImg;
					}
				}
			}

			if (mouse.mouseRightPressed) {
				selectedObj = "Nothing";
				selectedObjImg = null;
				availableArea.clear();
			}

			g2.setStroke(new BasicStroke(2));
			g2.drawImage(selectedObjImg, mouse.mouseMovedX, mouse.mouseMovedY, gp.tileSize, gp.tileSize, null);

			if (selectedObj.equals("Palisade") && gp.player.inv.palisades > 0) {
				for (Rectangle r : availableArea) {
					if (r.contains(wX, wY) && mouse.mouseClicked) {
						gp.aSetter.setupObject(new OBJ_Palisade(gp), r.x / gp.tileSize, r.y / gp.tileSize, false);
						gp.player.inv.palisades--;
						gp.playSE(7);
						mouse.mouseClicked = false;
					}
				}
			} else if (selectedObj.equals("Palisade") && gp.player.inv.palisades <= 0) {
				selectedObj = "Nothing";
			}

			if (selectedObj.equals("Flame Turret") && gp.player.inv.turrets > 0) {
				for (Rectangle r : availableArea) {
					if (r.contains(wX, wY) && mouse.mouseClicked) {
						gp.aSetter.setupObject(new OBJ_FlameTurret(gp), r.x / gp.tileSize, r.y / gp.tileSize, false);
						gp.player.inv.turrets--;
						gp.playSE(7);
						mouse.mouseClicked = false;
					}
				}
			} else if (selectedObj.equals("Flame Turret") && gp.player.inv.turrets <= 0) {
				selectedObj = "Nothing";
			}
			mouse.mouseClicked = false;
			availableArea.clear();
		}
	}

	private void addToAvailableArea(int col, int row) {
		Rectangle r = new Rectangle(col * gp.tileSize, row * gp.tileSize, gp.tileSize, gp.tileSize);
		availableArea.add(r);
	}

	private void drawAmmoBar() {

		oneScale = (double) gp.tileSize * 3 / OBJ_Flamethrower.ammoCapacity;
		ammoBarValue = oneScale * gp.player.inv.ammoCount;

		g2.setColor(new Color(50, 72, 89));
		g2.fillRect(gp.tileSize + 24, gp.tileSize + gp.tileSize, (int) ammoBarValue, gp.tileSize / 2);

	}

	private void drawHealthBar() {
		oneScale = (double) gp.tileSize * 3 / gp.player.maxLife;
		hpBarValue = oneScale * gp.player.currentLife;

		g2.setColor(new Color(193, 61, 55));
		g2.fillRect(gp.tileSize + 24, gp.tileSize + 15, (int) hpBarValue, gp.tileSize / 2);

	}

	private void drawWeaponIcons() {

		if (gp.player.hasFireAxe)
			g2.drawImage(AnimationManager.fireaxe, gp.tileSize + 24, gp.tileSize * 3, gp.tileSize, gp.tileSize, null);

		if (gp.player.hasFlameThrower)
			g2.drawImage(AnimationManager.flamethrower, gp.tileSize * 2 + 24, gp.tileSize * 3, gp.tileSize, gp.tileSize,
					null);
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(2));
		if (gp.player.hasFireAxeEquipped)
			g2.drawRoundRect(gp.tileSize + 24, gp.tileSize * 3, gp.tileSize, gp.tileSize, 25, 25);

		if (gp.player.hasFlameThrowerEquipped)
			g2.drawRoundRect(gp.tileSize * 2 + 24, gp.tileSize * 3, gp.tileSize, gp.tileSize, 25, 25);
		g2.setStroke(new BasicStroke(1));
	}

	private void drawGameOverScreen() {
		g2.setColor(Color.black);
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 42F));
		String text = "GAME OVER";
		String text2 = "press space to restart";
		int x = getXforCenteredText(text);
		int y = gp.screenHeight / 2;
		g2.drawString(text, x, y);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 22F));
		x = getXforCenteredText(text2);
		y = gp.screenHeight / 2 + gp.tileSize * 3;
		g2.drawString(text2, x, y);

	}

	private void drawDialogueScreen() {

		int x = gp.tileSize * 2;
		int y = gp.tileSize / 2;
		int width = gp.screenWidth - (gp.tileSize * 4);
		int height = gp.tileSize * 3;
		drawSubWindow(x, y, width, height);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
		x += gp.tileSize;
		y += gp.tileSize;

		for (String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F));
		x = gp.screenWidth - (gp.tileSize * 9);
		y = gp.tileSize * 3;
		g2.drawString("Press space to continue", x, y + (gp.tileSize / 4));

	}

	private void drawSubWindow(int x, int y, int width, int height) {
		Color c = new Color(0, 0, 0, 220);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

	}

	private void drawPauseScreen() {
		g2.setColor(Color.black);
		g2.fillRect(gp.screenWidth / 2 - (4 * gp.tileSize), gp.screenHeight / 2 - (2 * gp.tileSize), gp.tileSize * 8,
				gp.tileSize * 4);
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 42F));
		String text = "PAUSED";
		int x = getXforCenteredText(text);
		int y = gp.screenHeight / 2;
		g2.drawString(text, x, y);

	}

	public int getXforCenteredText(String text) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return gp.screenWidth / 2 - length / 2;
	}
}
