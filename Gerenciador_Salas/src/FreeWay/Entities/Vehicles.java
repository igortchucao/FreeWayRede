package FreeWay.Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Random;

import FreeWay.Graficos.Spritesheet;

public class Vehicles implements Serializable {

	private static final long serialVersionUID = 1L;
	private BufferedImage vehicleImage;
	public int MAO = 0;
	public int SPEED = 0;
	public int COR = 0;
	public int POS_X = 0;
	public boolean FAIXA;

	public static Spritesheet spritesheet = new Spritesheet("/sprite.png");

	public Vehicles(Random rand) {
		COR = rand.nextInt(5);
		MAO = rand.nextInt(2);
		SPEED = 1 + rand.nextInt(3);
		FAIXA = false;
		vehicleImage = spritesheet.getSprite(0 + (64 * this.COR), 0, 64, 64);
	}

	public void randomize(Random rand) {
		if (FAIXA == false) {
			COR = rand.nextInt(5);
			MAO = rand.nextInt(2);
			SPEED = 3 + rand.nextInt(6);
			POS_X = 0;
			FAIXA = true;
			vehicleImage = spritesheet.getSprite(0 + (64 * this.COR), 0, 64, 64);
		}
	}

	public void tick(Random rand) {
		POS_X += SPEED;
		if (POS_X > 1400)
			FAIXA = false;
	}

	public void render(Graphics g, int faixa) {
		if (MAO == 1) {
			g.drawImage(vehicleImage, POS_X, 80 + faixa * 59, -64, 64, null);
		} else {
			g.drawImage(vehicleImage, 1400 - POS_X, 80 + faixa * 59, 64, 64, null);
		}
	}
}
