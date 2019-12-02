package FreeWay.Fases;

import FreeWay.Game.Game;

public class Fase1 extends Fase {
	public Fase1() {
		Game.world.MAP = 1;
		velocidade = (float) 1.5;
		for (int i = 0; i < 10; i++) {
			Game.vehicles.get(i).dados.typeVehicules = 6;
		}
	}
}
