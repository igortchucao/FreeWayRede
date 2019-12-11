package FreeWay.Fases;

import FreeWay.Game.Game;

public class Fase2 extends Fase{
	public Fase2() {
		Game.world.MAP = 2;
		velocidade = (float) 2.0;
		for (int i = 0; i < 10; i++) {
			Game.vehicles.get(i).dados.typeVehicules = 7;
		}
	}
}
