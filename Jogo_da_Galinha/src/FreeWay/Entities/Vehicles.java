package FreeWay.Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Random;

import FreeWay.Game.Game;
import FreeWay.Graficos.Spritesheet;

public class Vehicles implements Serializable{

	private static final long serialVersionUID = 1L;
	private BufferedImage vehicleImage;
	public VehiclesData dados;

	public static Spritesheet spritesheet = new Spritesheet("/sprite.png");
	
	public Vehicles(VehiclesData dados) {
		this.dados = dados;
		vehicleImage = spritesheet.getSprite(0 + (64 * this.dados.COR), 0, 64, 64);
	}
	
	public void render(Graphics g, int faixa) {
		if(dados.MAO == 1) {
			g.drawImage(vehicleImage, dados.POS_X, 80 + faixa * 59, -64, 64, null);
		}else {
			g.drawImage(vehicleImage, 1400 - dados.POS_X, 80 + faixa * 59, 64, 64, null);
		}
	}
}
