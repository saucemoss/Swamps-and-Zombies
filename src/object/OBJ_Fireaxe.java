package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_Fireaxe extends Entity

{
	public static int knockback = 20;
	public static int cooldown = 30;
	public static int damage = 6;
	public OBJ_Fireaxe(GamePanel gp){
		super(gp);
		setDialogue();
		type = "object";
		name = "Fireaxe";
		try {
			image = ImageIO.read(getClass().getResource("/objects/fireaxe.png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setDialogue() {
		dialogues[0] = "Fireaxe? This will come in handy...";

	}

	@Override
	public String getName() {
		return name;
	}



}
