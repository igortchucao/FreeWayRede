package IC.Interface;

import java.awt.Color;
import java.awt.Graphics;

public class Medidor {
	public int POS_X = 0, POS_Y = 0, TM = 20;

	public Medidor(int x, int y) {
		this.POS_X = x;
		this.POS_Y = y - 20;
	}

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(POS_X, POS_Y, TM, TM);

		g.setColor(Color.white);
		g.fillRect(POS_X + 1, POS_Y + 1, TM - 1, TM - 2);
	}
}
