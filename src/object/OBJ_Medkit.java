package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_Medkit extends Entity {
	public static int restore = 5;

	public OBJ_Medkit(GamePanel gp){
		super(gp);
		type = "object";
		name = "Medkit";
		collision = true;
		try {
			image = ImageIO.read(getClass().getResource("/objects/medkit.png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
