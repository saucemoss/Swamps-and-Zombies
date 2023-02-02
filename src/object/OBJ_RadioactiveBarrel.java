
package object;

import entity.Entity;
import main.AnimationManager;
import main.GamePanel;

public class OBJ_RadioactiveBarrel extends Entity {
	public OBJ_RadioactiveBarrel(GamePanel gp) {
		super(gp);
		type = "object";
		name = "Radioactive Barrel";
		dialogueIndex = 0;

		image = AnimationManager.radioactiveBarrel;

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
