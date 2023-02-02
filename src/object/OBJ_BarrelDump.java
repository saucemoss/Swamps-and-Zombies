package object;

import entity.Entity;
import main.AnimationManager;
import main.GamePanel;

public class OBJ_BarrelDump extends Entity {
	int doorOpenCounter = 0, staggerCounter = 0;
	boolean open;

	public OBJ_BarrelDump(GamePanel gp) {
		super(gp);
		type = "object";
		name = "Barrel Dump";
		collision = false;
		image = AnimationManager.dumpDoorArray[0];
		open = false;
		maxLife = 50;
		currentLife = maxLife;
		alive = true;

	}

	@Override
	public void setDialogue(int i) {

		switch (i) {
		case 0:
			dialogues[0] = "I should dump radioactive barrels here.";
			break;
		case 1:
			dialogues[0] = "My geiger counter tells me \nthere's more to collect.";
			break;
		case 2:
			dialogues[0] = "Cool! I think that all for this night!";
			break;
		}
	}


	@Override
	public void update() {
		if(takingDamage) {
			stagger();
		}
		if (gp.player.uTool.distanceBetween(worldX, worldY, gp.player.worldX, gp.player.worldY) > 100  && open) {
			closing = true;
		}
		if (gp.player.uTool.distanceBetween(worldX, worldY, gp.player.worldX, gp.player.worldY) < 100  && !open) {
			opening = true;
		}
		if (opening) {
			doorOpenCounter++;
			if(doorOpenCounter==2) {
				gp.playSE(8);
			}
			if (doorOpenCounter <= 52) {
				image = AnimationManager.dumpDoorArray[doorOpenCounter / 4];
			} else {
				image = AnimationManager.dumpDoorArray[14];
				open = true;
				opening = false;

			}
		}
		if (closing) {
			doorOpenCounter++;
			if(doorOpenCounter==54) {
				gp.playSE(8);
			}
			if (doorOpenCounter >= 52 && doorOpenCounter < 104) {
				image = AnimationManager.dumpDoorArray[doorOpenCounter / 4];
			} else {
				image = AnimationManager.dumpDoorArray[25];
				doorOpenCounter = 0;
				closing = false;
				open = false;

			}
		}
	}
	@Override
	public void receiveDamage(int dmg) {
		if (currentLife > 0 && !takingDamage) {
			takingDamage = true;
			currentLife -= dmg;
			System.out.println(currentLife);
		} else if(currentLife <= 0) {
			gp.gameState = gp.gameOverState;
		}
	}

	@Override
	public void stagger() {
		staggerCounter++;
		image = AnimationManager.dumpDoorArray[25];
		if (staggerCounter >= 40) {
			takingDamage = false;
			staggerCounter = 0;
		}
	}
	@Override
	public String getName() {
		return name;
	}
}
