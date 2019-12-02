package FreeWay.Entities;

import java.io.Serializable;
import java.util.Random;

public class VehiclesData implements Serializable {

	private static final long serialVersionUID = 1L;
	public int MAO = 0;
	public double SPEED = 0;
	public int COR = 0;
	public double POS_X = 100;
	public int typeVehicules = 5;
	public boolean FAIXA = false;

	private Random rand = new Random();

	public VehiclesData() {
		COR = rand.nextInt(5);
		MAO = rand.nextInt(2);
		SPEED = 3 + rand.nextDouble();
	}

	public void randomize() {
		if (FAIXA == false) {
			COR = rand.nextInt(typeVehicules);
			MAO = rand.nextInt(2);
			SPEED = 3 + rand.nextDouble();
			POS_X = 0;
			FAIXA = true;
			if (COR < 5)
				SPEED = 3 + rand.nextDouble();
			else if (COR == 5)
				// MOTO
				SPEED = 10;
			else if (COR == 6)
				// POLICIA
				SPEED = 15;
			else if (COR == 7)
				SPEED = 10;
		}
	}

	public void tick(double multVelocidade) {
		POS_X += SPEED * multVelocidade;
		if (POS_X > 1400)
			FAIXA = false;
	}
}
