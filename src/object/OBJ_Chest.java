package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity {
	public OBJ_Chest(GamePanel gp) {
		super(gp);
		type = "object";
		name = "Chest";
		dialogueIndex = 0;
		try {
			image = ImageIO.read(getClass().getResource("/objects/chest.png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setDialogue();
	}

	public void setDialogue() {
		dialogues[0] = "I should prepeare before the day light... \nI need to find somewhere safe.";
		dialogues[1] = "Hmmm... At least here I can \ndefend more easily from the wave.";
		dialogues[2] = "The Infected. \nFar more aggresive in the light.";
		dialogues[3] = "More will come at Sunrise.";

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
