package IC.Interface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Line {
	public int POS_X = 0, POS_Y = 0, LINE = 0;
	public List<Slot> slots;

	public Line(int x, int y) {
		this.POS_X = x;
		this.POS_Y = y;

		slots = new ArrayList<Slot>();
		for (int i = 0; i < 100; i++) {
			slots.add(new Slot(POS_X + 50 + i * 75, POS_Y));
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.drawString("q[" + LINE + "]", POS_X, POS_Y);

		g.setColor(Color.black);
		g.drawLine(POS_X + 25, POS_Y, POS_X + 800, POS_Y);

		for (int i = TimeLine.FIRSTs; i < TimeLine.FIRSTs + 10; i++) {
			if (TimeLine.FIRSTs + 10 > slots.size())
				i = slots.size();
			if (TimeLine.FIRSTs < 0)
				i = 0;
			Slot s = slots.get(i);
			s.render(g, TimeLine.FIRSTs);
		}
	}
}
