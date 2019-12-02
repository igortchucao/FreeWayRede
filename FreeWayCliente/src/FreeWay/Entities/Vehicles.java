package FreeWay.Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Random;

import FreeWay.Graficos.Spritesheet;

public class Vehicles implements Serializable {

	private static final long serialVersionUID = 1L;
	private BufferedImage vehicleImage;
	public VehiclesData dados;

	public static Spritesheet spritesheet = new Spritesheet("/sprite.png");

	public Vehicles() {
		dados = new VehiclesData();
		vehicleImage = spritesheet.getSprite(0 + (64 * dados.COR), 0, 64, 64);
	}

	public void tick(Random rand) {
		dados.POS_X += dados.SPEED;
		if (dados.POS_X > 1400)
			dados.FAIXA = false;
		vehicleImage = spritesheet.getSprite(0 + (64 * dados.COR), 0, 64, 64);
	}

	public void render(Graphics g, int faixa) {
		if (dados.MAO == 1) {
			g.drawImage(vehicleImage, dados.POS_X, 80 + faixa * 59, -64, 64, null);
		} else {
			g.drawImage(vehicleImage, 1400 - dados.POS_X, 80 + faixa * 59, 64, 64, null);
		}
	}
}
