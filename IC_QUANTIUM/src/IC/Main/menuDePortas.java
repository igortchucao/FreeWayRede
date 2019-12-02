package IC.Main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import IC.Portas.Cnot;
import IC.Portas.Hadamar;
import IC.Portas.Not;
import IC.Portas.Phase;
import IC.Portas.Portas;
import IC.Portas.Toffoli;

public class menuDePortas {
	public List<Portas> portas = new ArrayList<Portas>();

	public menuDePortas() {
		portas.add(new Not());
		portas.add(new Toffoli());
		portas.add(new Hadamar());
		portas.add(new Cnot());
		portas.add(new Phase());
	}

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(950, 0, Main.WIDTH * Main.SCALE, Main.HEIGHT * Main.SCALE);

		for (int i = 0; i < portas.size(); i++) {
			Portas p = portas.get(i);
			p.render(g);
		}
	}
}
