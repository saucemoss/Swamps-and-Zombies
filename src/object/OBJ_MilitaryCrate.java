package object;

import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_MilitaryCrate extends Entity {
	String stuff = "";
	Random rand;


	public OBJ_MilitaryCrate(GamePanel gp) {
		super(gp);
		rand = new Random();
		solidArea.width = gp.tileSize*3;
		type = "object";
		name = "Military Crate";
		try {
			image = ImageIO.read(getClass().getResource("/objects/crate.png"));
			image = uTool.scaleImage(image, gp.tileSize * 3, gp.tileSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		collision = true;
	}

	public void setDialogue() {
		dialogues[0] = "Found " + stuff;
	}

	@Override
	public void getCrateMaterials() {
		int randObj = rand.nextInt(200);
		System.out.println(randObj);

		if (randObj < 40) {
			gp.player.inv.palisades++;
			stuff += "1x palisade!";
		} else if (randObj >= 40 && randObj < 80) {
			gp.player.inv.turrets++;
			stuff += "1x fire turret!";
		} else if (randObj >= 80 && randObj < 120) {
			gp.player.inv.palisades++;
			gp.player.inv.palisades++;
			stuff += "2x palisade!";
		} else if (randObj >= 120 && randObj < 140) {
			gp.player.inv.turrets++;
			gp.player.inv.turrets++;
			stuff += "2x fire turret!";
		} else if (randObj >= 140 && randObj < 160) {
			gp.player.inv.palisades++;
			gp.player.inv.turrets++;
			stuff += "1x palisade and 1x fire turret!";
		} else if (randObj >= 160 && randObj < 170) {
			OBJ_Fireaxe.damage += 2;
			OBJ_Fireaxe.knockback += 5;
			if (OBJ_Fireaxe.cooldown > 4) {
				OBJ_Fireaxe.cooldown -= 4;
			}
			stuff += "fire Axe upgrade!";
		} else if (randObj >= 170 && randObj < 180) {
			OBJ_Flamethrower.damage += 1;
			if (OBJ_Flamethrower.cooldown > 1) {
				OBJ_Flamethrower.cooldown -= 1;
			}
			OBJ_Flamethrower.ammoCapacity += 100;
			stuff += "flamethrower upgrade!";
		} else if (randObj >= 180 && randObj < 190) {
			OBJ_FlameTurret.damage += 1;
			OBJ_FlameTurret.range += 20;
			stuff += "fire turrets upgrade!";
		} else {
			gp.player.currentLife = gp.player.maxLife;
			gp.player.inv.ammoCount = OBJ_Flamethrower.ammoCapacity;
			stuff += "full napalm and medkits!";
		}
		setDialogue();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
