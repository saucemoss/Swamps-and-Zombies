package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import utilz.UtilityTool;

public class AnimationManager {
	public static BufferedImage up1Player, up2Player, down1Player, down2Player, left1Player, left2Player, right1Player,
			right2Player, idle1Player, idle2Player, up1Shooting, up2Shooting, down1Shooting, down2Shooting,
			left1Shooting, left2Shooting, right1Shooting, right2Shooting, idle1Shooting, idle2Shooting,
			down1WalkShooting, down2WalkShooting, right1WalkShooting, right2WalkShooting, damaged,
			zombieDeathSpritesheetAnim, downAttack1, downAttack2, downAttack3, downAttack4, downAttack5, downAttack6,
			up1Zombie, up2Zombie, down1Zombie, down2Zombie, left1Zombie, left2Zombie, right1Zombie, right2Zombie,
			idle1Zombie, idle2Zombie, damagedZombie, left1PlayerAxe, left2PlayerAxe, right1PlayerAxe, right2PlayerAxe,
			down1PlayerAxe, down2PlayerAxe, idle1PlayerAxe, idle2PlayerAxe, up1ShootingFlame, up2ShootingFlame,
			down1Flame, down2Flame, down1ShootingFlame, down2ShootingFlame, down1WalkShootingFlame,
			down2WalkShootingFlame, left1Flame, left2Flame, left1ShootingFlame, left2ShootingFlame, right1Flame,
			right2Flame, right1ShootingFlame, right2ShootingFlame, right1WalkShootingFlame, right2WalkShootingFlame,
			idle1Flame, idle2Flame, up1FlameProjectile, up2FlameProjectile, flameProjectileLight, lvl1Palisade,
			lvl2Palisade, lvl3Palisade, tileSpriteSheet, flameTurretIcon, flamethrower, fireaxe, radioactiveBarrel, dumpDoor, key;
	public static BufferedImage[] ZDeathAnimArray = new BufferedImage[13];
	public static BufferedImage[] downAttackArray = new BufferedImage[20];
	public static BufferedImage[] rightAttackArray = new BufferedImage[20];
	public static BufferedImage[] leftAttackArray = new BufferedImage[20];
	public static BufferedImage[] upAttackArray = new BufferedImage[20];
	public static BufferedImage[] swampTileAnimArray = new BufferedImage[10];
	public static BufferedImage[] swampCactusGreenMovingAnimArray = new BufferedImage[10];
	public static BufferedImage[] dumpDoorArray = new BufferedImage[26];
	public static ArrayList<BufferedImage> tileSpritesArray = new ArrayList<>();

	GamePanel gp;
	public UtilityTool uTool = new UtilityTool();

	public AnimationManager(GamePanel gp) {
		this.gp = gp;

	}

	public final void initMiscAnims() {
		loadObjectImages();
		loadDumpDoor();
		loadZombieAnim();
		loadZombieDeathAnim();
	}

	public final void loadUI() {
		flameTurretIcon = setup("flame turret icon", "ui");
	}

	public final void loadObjectImages() {
		key = setup("key", "objects");
		lvl1Palisade = setup("palisadeLvl1", "objects");
		lvl2Palisade = setup("palisadeLvl2", "objects"); 
		lvl3Palisade = setup("palisadeLvl3", "objects");
		flamethrower = setup("flamethrower", "objects");
		fireaxe = setup("fireaxe", "objects");
		radioactiveBarrel = setup("radioactiveBarrel", "objects");
	}

	public final void loadZombieAnim() {

		up1Zombie = setup("up1", "Zombie");
		up2Zombie = setup("up2", "Zombie");
		down1Zombie = setup("down1", "Zombie");
		down2Zombie = setup("down2", "Zombie");
		left1Zombie = setup("left1", "Zombie");
		left2Zombie = setup("left2", "Zombie");
		right1Zombie = setup("right1", "Zombie");
		right2Zombie = setup("right2", "Zombie");
		idle1Zombie = setup("down1", "Zombie");
		idle2Zombie = setup("down2", "Zombie");
		damagedZombie = setup("zombieTakingDamage", "Zombie");

	}

	public final void loadDumpDoor() {
		try {
			BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/objects/dumpDoor.png"));

			for (int i = 0; i < dumpDoorArray.length; i++) {
				dumpDoorArray[i] = img.getSubimage(0, i * 16, 16, 16);
				dumpDoorArray[i] = uTool.scaleImage(dumpDoorArray[i], gp.tileSize, gp.tileSize);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final void loadSwampCactusMovingGreen() {
		try {
			BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/tiles/swampCactusMovingGreen.png"));

			for (int i = 0; i < swampCactusGreenMovingAnimArray.length; i++) {
				swampCactusGreenMovingAnimArray[i] = img.getSubimage(0, i * 16, 16, 16);
				swampCactusGreenMovingAnimArray[i] = uTool.scaleImage(swampCactusGreenMovingAnimArray[i], gp.tileSize, gp.tileSize);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final void loadSwampBubblingTileAnim() {
		try {
			BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/tiles/swampTileAnimSheet.png"));
			for (int i = 0; i < swampTileAnimArray.length; i++) {
				swampTileAnimArray[i] = img.getSubimage(0, i * 16, 16, 16);
				swampTileAnimArray[i] = uTool.scaleImage(swampTileAnimArray[i], gp.tileSize, gp.tileSize);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public final void loadTilesFromSpriteSheet() {
		try {
			tileSpriteSheet = ImageIO.read(getClass().getResourceAsStream("/tiles/swampTileSheet.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				BufferedImage img = tileSpriteSheet.getSubimage(j * 16, i * 16, 16, 16);
				img = uTool.scaleImage(img, gp.tileSize, gp.tileSize);
				tileSpritesArray.add(img);


			}
		}

	}

	public final void loadZombieDeathAnim() {
		try {
			zombieDeathSpritesheetAnim = ImageIO
					.read(getClass().getResourceAsStream("/Zombie/zombieDeathAnimSpriteSheet.png"));
			for (int i = 0; i < ZDeathAnimArray.length; i++) {
				ZDeathAnimArray[i] = zombieDeathSpritesheetAnim.getSubimage(0, i * 16, 16, 16);
				ZDeathAnimArray[i] = uTool.scaleImage(ZDeathAnimArray[i], gp.tileSize, gp.tileSize);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public final void loadPlayerFlameThrowerMoveSet() {

		up1FlameProjectile = setup("fire1", "objects");
		up2FlameProjectile = setup("fire2", "objects");
		flameProjectileLight = setup("light5", "night");

		up1ShootingFlame = setup("10", "Player");
		up2ShootingFlame = setup("11", "Player");

		down1Flame = setup("12", "Player");
		down2Flame = setup("13", "Player");

		down1ShootingFlame = setup("14", "Player");
		down2ShootingFlame = setup("15", "Player");
		down1WalkShootingFlame = setup("16", "Player");
		down2WalkShootingFlame = setup("17", "Player");

		left1Flame = setup("20", "Player");
		left2Flame = setup("21", "Player");
		left1ShootingFlame = setup("18", "Player");
		left2ShootingFlame = setup("19", "Player");

		right1Flame = setup("24", "Player");
		right2Flame = setup("25", "Player");
		right1ShootingFlame = setup("22", "Player");
		right2ShootingFlame = setup("23", "Player");
		right1WalkShootingFlame = setup("24", "Player");
		right2WalkShootingFlame = setup("25", "Player");

		idle1Flame = setup("22", "Player");
		idle2Flame = setup("23", "Player");
	}

	public final void loadPlayerDefaultMoveSet() {
		up1Player = setup("00", "Player");
		up2Player = setup("01", "Player");
		down1Player = setup("02", "Player");
		down2Player = setup("03", "Player");
		left1Player = setup("04", "Player");
		left2Player = setup("05", "Player");
		right1Player = setup("06", "Player");
		right2Player = setup("07", "Player");
		idle1Player = setup("08", "Player");
		idle2Player = setup("09", "Player");
	}

	public final void loadPlayerFireAxeMoveSet() {
		left1PlayerAxe = setup("32", "Player");
		left2PlayerAxe = setup("33", "Player");
		right1PlayerAxe = setup("30", "Player");
		right2PlayerAxe = setup("31", "Player");
		down1PlayerAxe = setup("28", "Player");
		down2PlayerAxe = setup("29", "Player");
		idle1PlayerAxe = setup("26", "Player");
		idle2PlayerAxe = setup("27", "Player");

		// down attack sprites
		downAttackArray[0] = setup("34", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[1] = setup("34", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[2] = setup("34", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[3] = setup("34", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[4] = setup("34", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[5] = setup("34", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[6] = setup("35", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[7] = setup("35", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[8] = setup("36", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[9] = setup("36", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[10] = setup("37", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[11] = setup("37", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[12] = setup("38", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[13] = setup("38", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[14] = setup("39", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[15] = setup("39", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[16] = setup("39", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[17] = setup("39", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[18] = setup("39", "Player", gp.tileSize * 2, gp.tileSize * 2);
		downAttackArray[19] = setup("39", "Player", gp.tileSize * 2, gp.tileSize * 2);
		// right attack sprites
		rightAttackArray[0] = setup("40", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[1] = setup("40", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[2] = setup("40", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[3] = setup("40", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[4] = setup("41", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[5] = setup("41", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[6] = setup("41", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[7] = setup("42", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[8] = setup("42", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[9] = setup("42", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[10] = setup("43", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[11] = setup("43", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[12] = setup("43", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[13] = setup("44", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[14] = setup("44", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[15] = setup("44", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[16] = setup("45", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[17] = setup("45", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[18] = setup("45", "Player", gp.tileSize * 2, gp.tileSize * 2);
		rightAttackArray[19] = setup("45", "Player", gp.tileSize * 2, gp.tileSize * 2);
		// left attack sprites
		leftAttackArray[0] = setup("46", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[1] = setup("46", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[2] = setup("46", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[3] = setup("46", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[4] = setup("47", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[5] = setup("47", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[6] = setup("47", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[7] = setup("48", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[8] = setup("48", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[9] = setup("48", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[10] = setup("49", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[11] = setup("49", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[12] = setup("49", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[13] = setup("50", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[14] = setup("50", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[15] = setup("50", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[16] = setup("51", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[17] = setup("51", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[18] = setup("51", "Player", gp.tileSize * 2, gp.tileSize * 2);
		leftAttackArray[19] = setup("51", "Player", gp.tileSize * 2, gp.tileSize * 2);
		// up attack sprties
		upAttackArray[0] = setup("52", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[1] = setup("52", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[2] = setup("52", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[3] = setup("52", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[4] = setup("53", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[5] = setup("53", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[6] = setup("53", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[7] = setup("54", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[8] = setup("54", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[9] = setup("54", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[10] = setup("55", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[11] = setup("55", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[12] = setup("55", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[13] = setup("56", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[14] = setup("56", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[15] = setup("56", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[16] = setup("57", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[17] = setup("57", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[18] = setup("57", "Player", gp.tileSize * 2, gp.tileSize * 2);
		upAttackArray[19] = setup("57", "Player", gp.tileSize * 2, gp.tileSize * 2);

	}

	public BufferedImage setup(String imageName, String name, int width, int height) {
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

	public BufferedImage setup(String imageName, String name) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/" + name + "/" + imageName + ".png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
}
