package FreeWay.Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import FreeWay.Game.Game;

public class Vehicles implements Serializable {

	private static final long serialVersionUID = 1L;
	public BufferedImage vehicleImage;
	public VehiclesData dados;

	//public static Game.spritesheet Game.spritesheet = new Game.spritesheet("/sprite.png");

	public Vehicles() {
		dados = new VehiclesData();
		vehicleImage = Game.spritesheet.getSprite(0 + (64 * dados.COR), 0, 64, 64);
	}

	public void tick() {
		dados.POS_X += dados.SPEED;
		if (dados.POS_X > 1400)
			dados.FAIXA = false;
	}

	public void render(Graphics g, int faixa) {
		if (dados.COR < 5)
			vehicleImage = Game.spritesheet.getSprite(0 + (64 * dados.COR), 0, 64, 64);
		else if (dados.COR == 5)
			// MOTO
			vehicleImage = Game.spritesheet.getSprite(4 * 64, 64, 64, 64);
		else if (dados.COR == 6)
			// POLICIA
			vehicleImage = Game.spritesheet.getSprite(3 * 64, 64, 64, 64);
		else if (dados.COR == 7) {
			// ONIBUS
			vehicleImage = Game.spritesheet.getSprite(0, 64, 128, 64);
			if (dados.MAO == 1) {
				g.drawImage(vehicleImage, (int) dados.POS_X, 80 + faixa * 59, -128, 64, null);
			} else {
				g.drawImage(vehicleImage, (int) (1400 - dados.POS_X), 80 + faixa * 59, 128, 64, null);
			}
		} 
		if(dados.COR != 7){
			if (dados.MAO == 1) {
				g.drawImage(vehicleImage,(int) dados.POS_X, 80 + faixa * 59, -64, 64, null);
			} else {
				g.drawImage(vehicleImage,(int) (1400 - dados.POS_X), 80 + faixa * 59, 64, 64, null);
			}
		}
	}
}
