package entity;

import main.GamePanel;

public class DialogueManager {
	GamePanel gp;
	public String dialogues[] = new String[20];
	int dialogueIndex = 0;

	public DialogueManager(GamePanel gp) {
		this.gp = gp;
	}

	public void speak() {
		gp.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;
		if (dialogues[dialogueIndex-1] == null) {
			gp.gameState = gp.playState;
			dialogueIndex = 0;
		}

	}

}
