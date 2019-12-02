package FreeWay.Fases;

import FreeWay.Game.Game;

public class Fase3 extends Fase{
	public Fase3() {
		velocidade = (float) 2.5;
		for (int i = 0; i < 10; i++) {
			Game.vehicles.get(i).dados.typeVehicules = 8;
		}
	}
}
