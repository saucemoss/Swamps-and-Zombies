package object;

import entity.Entity;
import main.AnimationManager;
import main.GamePanel;


public class OBJ_Palisade extends Entity{
	public int lvl = 1;
	public int damage = 0;
	public int hitPoints = 0;

	public OBJ_Palisade(GamePanel gp) {

		super(gp);
		this.gp = gp;
		type = "object";
		name = "Palisade";
		speedPenalty = 1;
		getImage();
	}
	private void getImage() {
		image = AnimationManager.lvl1Palisade;
	}
	public void upgrade(int lvl) {
		switch(lvl) {
		case 2:
			this.damage = 1;
			this.speedPenalty = 2;
			image = AnimationManager.lvl2Palisade;
			break;
		case 3:
			this.damage = 2;
			this.speedPenalty = 3;
			image = AnimationManager.lvl3Palisade;
			break;
		}

	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
