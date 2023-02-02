package object;

import entity.Entity;
import main.AnimationManager;
import main.GamePanel;

public class OBJ_Flamethrower extends Entity{
	public static int damage = 4;
	public static int cooldown = 4;
	public static int ammoCapacity = 500;

	public OBJ_Flamethrower(GamePanel gp) {
		super(gp);
		setDialogue();
		type = "object";
		name = "Flamethrower";
		image = AnimationManager.flamethrower;
	}

	public void setDialogue() {
		dialogues[0] = "Nice! I could use this...";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
