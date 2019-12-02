package FreeWay.Entities;

import java.io.Serializable;
import java.util.Random;

public class VehiclesData implements Serializable {

	private static final long serialVersionUID = 1L;
	public int MAO = 0;
	public int SPEED = 0;
	public int COR = 0;
	public int POS_X = 0;
	public boolean FAIXA = false;

	private Random rand = new Random();

	public VehiclesData() {
		COR = rand.nextInt(5);
		MAO = rand.nextInt(2);
		SPEED = 1 + rand.nextInt(3);
	}

	public void randomize() {
		if (FAIXA == false) {
			COR = rand.nextInt(5);
			MAO = rand.nextInt(2);
			SPEED = 3 + rand.nextInt(3);
			POS_X = 0;
			FAIXA = true;
		}
	}
	
	public void tick() {
		POS_X += SPEED;
		if(POS_X > 1400)
			FAIXA = false;
	}
}
