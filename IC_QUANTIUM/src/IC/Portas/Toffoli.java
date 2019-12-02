package IC.Portas;

import java.awt.Color;

public class Toffoli extends Portas {

	public Toffoli() {
		super();
		COR = Color.green;
		TEXT = "Tof";
		MaskPOS_X = 7;
		MaskPOS_Y = 35;
		FONT = 26;
		POS_X = 1100;
		POS_Y = 100;

		INICIALX = 1100;
		INICIALY = 100;
	}

	public Toffoli(int x, int y) {
		super();
		COR = Color.green;
		TEXT = "Tof";
		MaskPOS_X = 7;
		MaskPOS_Y = 35;
		FONT = 26;
		POS_X = x;
		POS_Y = y;
	}
}
