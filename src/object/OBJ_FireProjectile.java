package object;

import entity.Projectile;
import main.AnimationManager;
import main.GamePanel;


public class OBJ_FireProjectile extends Projectile{

	public OBJ_FireProjectile(GamePanel gp) {

		super(gp);
		this.gp = gp;

		speed = 10;
		name = "Fire Projectile";
		getImage();
		playSound();
	}
	private void playSound() {
		gp.playSE(10);

	}
	private void getImage() {
			up1 = AnimationManager.up1FlameProjectile;
			up2 = AnimationManager.up2FlameProjectile;
			projectileLight = AnimationManager.flameProjectileLight;

	}

	@Override
	public String getName() {
		return name;
	}

}
