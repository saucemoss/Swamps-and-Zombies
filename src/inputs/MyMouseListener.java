package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.GamePanel;

public class MyMouseListener implements MouseListener, MouseMotionListener {

	public boolean mouseLeftPressed, mouseLeftReleased, mouseMoved, mouseRightPressed, mouseClicked, craftStateSwitched;
	public int mouseLeftPressedX, mouseLeftPressedY, mouseMovedX, mouseMovedY, mouseEnteredX, mouseEnteredY,
			mouseExitedX, mouseExitedY;
	public GamePanel gp;

	public MyMouseListener(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseLeftPressedX = (int) (e.getX() * gp.fullScreenOffsetFactor);
		mouseLeftPressedY = (int) (e.getY() * gp.fullScreenOffsetFactor);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseLeftPressedX = (int) (e.getX() * gp.fullScreenOffsetFactor);
		mouseLeftPressedY = (int) (e.getY() * gp.fullScreenOffsetFactor);
		mouseMovedX = (int) (e.getX() * gp.fullScreenOffsetFactor);
		mouseMovedY = (int) (e.getY() * gp.fullScreenOffsetFactor);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouseClicked = true;
		if (e.getButton() == 3) {
			if (gp.gameState == gp.playState) {
				gp.gameState = gp.craftState;
			}else if(gp.gameState == gp.craftState) {
				gp.gameState = gp.playState;
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			mouseLeftPressed = true;
			mouseLeftPressedX = (int) (e.getX() * gp.fullScreenOffsetFactor);
			mouseLeftPressedY = (int) (e.getY() * gp.fullScreenOffsetFactor);
		}
		if (e.getButton() == 3) {
			mouseRightPressed = true;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1) {
			mouseLeftPressed = false;
		}
		if (e.getButton() == 3) {

			mouseRightPressed = false;
		}
		mouseClicked = false;

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseExitedX = (int) (e.getX() * gp.fullScreenOffsetFactor);
		mouseExitedY = (int) (e.getY() * gp.fullScreenOffsetFactor);

	}

}
