package FreeWay.Game;

import java.awt.Graphics;
import java.util.Random;

import FreeWay.Entities.Player;
import FreeWay.Entities.PlayerData;
import FreeWay.Entities.VehiclesData;
import FreeWay.Fases.Fase;
import FreeWay.Fases.Fase1;
import FreeWay.Fases.Fase2;
import FreeWay.Fases.Fase3;

public class SinglePlayer {
	public Fase faseAtual;
	public static Player player;
	private Thread threadVehicles;

	public Random rand;

	public SinglePlayer() {
		player = new Player(0, new PlayerData(750));
		rand = new Random();
		threadVehicles = new Thread(random);
		threadVehicles.start();
	}
	
	private Runnable random = new Runnable() {
		public void run() {
			while(true) {
				for (int i = 0; i < Game.vehiclesDatas.size(); i++) {
					VehiclesData v = Game.vehiclesDatas.get(i);
					v.randomize();
				}
			}
		}
	};

	public void render(Graphics g) {
		player.render(g, 0);
	}

	public void tick() {
		if (player.dados.SCORE == 0)
			faseAtual = new Fase();
		else if (player.dados.SCORE == 5 && !(faseAtual instanceof Fase1))
			faseAtual = new Fase1();
		else if (player.dados.SCORE == 10 && !(faseAtual instanceof Fase2))
			faseAtual = new Fase2();
		else if (player.dados.SCORE == 15 && !(faseAtual instanceof Fase3))
			faseAtual = new Fase3();

		faseAtual.velocidade = faseAtual.velocidadeInicial + player.dados.SCORE * 0.1;

		if (player.dados.POS_Y < 50) {
			player.dados.POS_Y = 659;
			player.dados.SCORE += 5;
		}

		if (player.isColliding()) {
			player.dados.POS_Y = 659;
			player.LIFE -= 1;
			faseAtual = new Fase();
		}

		for (int i = 0; i < 10; i++) {
			VehiclesData v = Game.vehiclesDatas.get(i);
			v.tick(2);
			Game.vehicles.get(i).dados = v;
		}

		player.tick();
	}
}