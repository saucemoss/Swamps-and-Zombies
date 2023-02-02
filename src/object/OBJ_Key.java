package object;

import entity.Entity;
import main.AnimationManager;
import main.GamePanel;

public class OBJ_Key extends Entity  {

	public OBJ_Key(GamePanel gp) {
		super(gp);
		type = "object";
		name = "Key";
		collision = true;
		image = AnimationManager.key;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
