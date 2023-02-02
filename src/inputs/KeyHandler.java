package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;

public class KeyHandler implements KeyListener {
	public GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed, shootPressed, ePressed, spacePressed;

	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		// PLAY STATE
		if (gp.gameState == gp.playState) {

			switch (code) {
			case KeyEvent.VK_W:
				upPressed = true;
				break;

			case KeyEvent.VK_A:
				leftPressed = true;
				break;
			case KeyEvent.VK_S:
				downPressed = true;
				break;
			case KeyEvent.VK_D:
				rightPressed = true;
				break;
			case KeyEvent.VK_SPACE:
				spacePressed = true;
				break;
			case KeyEvent.VK_E:
				ePressed = true;
				break;
			case KeyEvent.VK_P:
				gp.gameState = gp.pauseState;
				break;
			case KeyEvent.VK_C:
				gp.gameState = gp.craftState;
				break;
			case KeyEvent.VK_ESCAPE:
				gp.gameState = gp.menuState;
				break;
			}
		}

		// CRAFT STATE
		else if (gp.gameState == gp.craftState) {
			switch (code) {
			case KeyEvent.VK_C:
				gp.gameState = gp.playState;
				break;
			}
		}

		// PAUSE STATE
		else if (gp.gameState == gp.pauseState) {
			switch (code) {
			case KeyEvent.VK_P:
				gp.gameState = gp.playState;

				break;

			}
		}
		// DIALOGUE STATE
		else if (gp.gameState == gp.dialogueState) {
			switch (code) {
			case KeyEvent.VK_SPACE:
				spacePressed = true;
				break;
			}

		}
		// START STATE
		else if (gp.gameState == gp.startGameState) {
			switch (code) {
			case KeyEvent.VK_SPACE:
				gp.gameState = gp.playState;
				break;
			}

		}
		// END STATE
		else if (gp.gameState == gp.endGameState) {
			switch (code) {
			case KeyEvent.VK_SPACE:
				gp.reset();
				break;
			}

		}

		// GAMEOVER STATE
		else if (gp.gameState == gp.gameOverState) {
			switch (code) {
			case KeyEvent.VK_SPACE:
				gp.reset();
				break;
			}

		}

		// MENU STATE
		else if (gp.gameState == gp.menuState) {

			switch (code) {
			case KeyEvent.VK_1:
				gp.levelManager.setupLevel(0);
				gp.gameState = gp.startGameState;
				break;

			case KeyEvent.VK_2:
				gp.levelManager.setupLevel(1);
				gp.gameState = gp.startGameState;

				break;
			case KeyEvent.VK_3:
				gp.levelManager.setupLevel(3);
				gp.gameState = gp.startGameState;
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;

			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W) {
			upPressed = false;
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = false;
		}
		if (code == KeyEvent.VK_S) {
			downPressed = false;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = false;
		}
		if (code == KeyEvent.VK_SPACE) {
			spacePressed = false;
		}
		if (code == KeyEvent.VK_E) {
			ePressed = false;
		}
	}

}
