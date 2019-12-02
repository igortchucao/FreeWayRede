package FreeWay.Game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyListener;

public class MenuIniciar  {

	private static final long serialVersionUID = 1L;

	public boolean active = false;
	public int selector = 1;
	private int WIDTH = 1400;
	private int HEIGTH = 760;
	private int FONT = 50;

	public void render(Graphics g) {

		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGTH);

		for (int i = 0; i < 14; i++) {
			g.setColor(Color.white);
			g.drawLine(i * 100, 0, i * 100, HEIGTH);
		}

		for (int i = 0; i < 8; i++) {
			g.setColor(Color.white);
			g.drawLine(0, i * 100, WIDTH, i * 100);
		}

		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, FONT + 100));
		g.drawString("FreeWAY", 350 + 18, 200);

		if (selector == 1) {
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, FONT + 30));
			g.drawString("New Game", 550 - 53, 500);
		} else {
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, FONT));
			g.drawString("New Game", 550 + 18, 500);
		}

		if (selector == 2) {
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, FONT + 30));
			g.drawString("Multiplayer", 550 - 50, 600);
		} else {
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, FONT));
			g.drawString("Multiplayer", 550 + 15, 600);
		}
		if (selector == 3) {
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, FONT + 30));
			g.drawString("Exit", 650 - 30, 700);
		} else {
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, FONT));
			g.drawString("Exit", 650 + 3, 700);
		}
	}

}
