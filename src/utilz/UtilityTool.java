package utilz;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UtilityTool {

	public BufferedImage scaleImage(BufferedImage original, int width, int height) {
		BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
		Graphics2D g2 = scaledImage.createGraphics();
		g2.drawImage(original, 0,0, width, height, null);
		g2.dispose();
		return scaledImage;

	}

	public int distanceBetween(int x1, int y1, int x2, int y2) {
		int distance = 0;
		int xDiff = Math.abs(x1 - x2);
		int yDiff = Math.abs(y1 - y2);
		return distance = (int) Math.hypot(xDiff, yDiff);
	}



}
