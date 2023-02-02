package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_Gate extends Entity {
	public OBJ_Gate(GamePanel gp){
		super(gp);
		collision = true;
		type = "object";
		name = "Gate";
		try {
			image = ImageIO.read(getClass().getResource("/objects/gate2.png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDialogue();
	}
	public void setDialogue() {
		dialogues[0] = "I need a key...";
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
