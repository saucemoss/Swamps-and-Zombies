package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_FuelTank extends Entity {
	public static int fuelAmount = 150;
	public OBJ_FuelTank(GamePanel gp){
		super(gp);
		type = "object";
		name = "Fuel";

		collision = true;
		try {
			image = ImageIO.read(getClass().getResource("/objects/fuel.png"));
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
