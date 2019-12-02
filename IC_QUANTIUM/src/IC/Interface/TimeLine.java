package IC.Interface;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import IC.Portas.Portas;

public class TimeLine {
	public int POS_X = 50, POS_Y = 100, DIS = 98;
	public int TM_POS_X = 850, TM_POS_Y = 500;

	public List<Line> lines = new ArrayList<Line>();

	public List<Slot> medidorGeral = new ArrayList<Slot>();

	public static int FIRST = 0;
	public static int FIRSTs = 0;

	public int TM_POS_XBot = 100, TM_POS_YBot = 40;
	public int POS_XBotA = POS_X, POS_XBotB = POS_X + TM_POS_X - TM_POS_XBot, POS_YBotA = POS_Y + TM_POS_Y,
			POS_YBotB = POS_Y + TM_POS_Y;

	public TimeLine() {
		for (int i = 0; i < 20; i++) {
			lines.add(new Line(POS_X + 25, POS_Y + 75 + i * 50));
		}
		
		for (int i = 0; i < 10; i++) {
			medidorGeral.add(new Slot(POS_X + 50 + i * 75, POS_Y));
		}
	}

	public void render(Graphics g) {
		// RETANGULO PRINCIPAL
		g.setColor(Color.white);
		g.fillRect(POS_X, POS_Y, TM_POS_X, TM_POS_Y);

		for (int i = FIRST; i < FIRST + 8; i++) {
			if (FIRST + 8 > lines.size())
				i = lines.size();
			if (FIRST < 0)
				i = 0;
			Line l = lines.get(i);
			l.render(g);
		}

		g.setColor(Color.black);
		g.fillRect(POS_XBotA, POS_YBotA, TM_POS_XBot, TM_POS_YBot);
		g.setColor(Color.black);
		g.fillRect(POS_XBotB, POS_YBotB, TM_POS_XBot, TM_POS_YBot);
	}

	public boolean colocadoNaCaixa(int x, int y, Portas porta) {
		for (int i = 0; i < lines.size(); i++) {
			for (int j = 0; j < lines.get(i).slots.size(); j++) {
				if (x > lines.get(i).slots.get(j).POS_X && x < lines.get(i).slots.get(j).POS_X + 50
						&& y > lines.get(i).slots.get(j).POS_Y && y < lines.get(i).slots.get(j).POS_Y + 50) {
					lines.get(i).slots.get(j).porta = porta;
					lines.get(i).slots.get(j).porta.POS_X = lines.get(i).slots.get(j).POS_X;
					lines.get(i).slots.get(j).porta.POS_Y = lines.get(i).slots.get(j).POS_Y;
					lines.get(i).slots.get(j).porta.HEIGHT = lines.get(i).slots.get(j).TM;
					lines.get(i).slots.get(j).porta.WIDTH = lines.get(i).slots.get(j).TM;
					return true;
				}
			}
		}
		return false;
	}

	public int button(int x, int y) {
		if ((x > POS_XBotA && x < POS_XBotA + TM_POS_XBot) && y > POS_YBotA && y < POS_YBotA + TM_POS_YBot) {
			return 1;
		} else if ((x > POS_XBotB && x < POS_XBotB + TM_POS_XBot) && y > POS_YBotB && y < POS_YBotB + TM_POS_YBot) {
			return 2;
		} else {
			return 0;
		}
	}
}
