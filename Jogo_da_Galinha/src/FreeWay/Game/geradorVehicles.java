package FreeWay.Game;

import FreeWay.Servidor.Sala;

public class geradorVehicles implements Runnable {

	public Sala sala;

	public geradorVehicles(Sala sala) {
		this.sala = sala;
	}

	@Override
	public void run() {
		while (true) {
			if (sala.retorno) {
				for (int i = 0; i < 10; i++) {
					sala.vehicles.get(i).randomize();
					sala.vehicles.get(i).tick();
				}
				sala.retorno = false;
			}
			System.out.println();
		}
	}

}
