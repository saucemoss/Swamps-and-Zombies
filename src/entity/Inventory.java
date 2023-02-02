package entity;

public class Inventory {

	public int turrets;
	public int palisades;
	public int keys = 0;
	public int barrels = 0;
	public int materials = 0;
	public int ammoCount = 0;

	public int getTurrets() {
		return turrets;
	}

	public void setTurrets(int turrets) {
		this.turrets = turrets;
	}

	public int getPalisades() {
		return palisades;
	}

	public void setPalisades(int palisades) {
		this.palisades = palisades;
	}
	
	public void resetInventory() {
		turrets = 0;
		palisades = 0;
		keys = 0;
		barrels = 0;
		materials = 0;
		ammoCount = 0;

	}

}
