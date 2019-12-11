package FreeWay.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import FreeWay.Graficos.Spritesheet;

public class MenuIniciar {

	private static final long serialVersionUID = 1L;

	public String menuSelect = "menu";
	public int selector = 1;
	private int WIDTH = 1400;
	private int HEIGTH = 760;
	private int FONT = 50;

	private Spritesheet spritesheet = new Spritesheet("/Freeway_Atari_cover.png");

	public BufferedImage LOGO_ACT = spritesheet.spritesheet;

	public void render(Graphics g) {
		if (menuSelect.equals("menu")) {
			g.setColor(Color.black);
			g.fillRect(0, 0, WIDTH, HEIGTH);
			g.drawImage(LOGO_ACT, 0, 0, Game.WIDTH * 4, (Game.HEIGHT * 4), null);

			/*
			 * for (int i = 0; i < 14; i++) { g.setColor(Color.white); g.drawLine(i * 100,
			 * 0, i * 100, HEIGTH); }
			 * 
			 * for (int i = 0; i < 8; i++) { g.setColor(Color.white); g.drawLine(0, i * 100,
			 * WIDTH, i * 100); }
			 */

			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, FONT + 60));
			g.drawString("FreeWAY", 350 + 108, 170);

			if (selector == 1) {
				g.setColor(Color.white);
				g.setFont(new Font("arial", Font.BOLD, FONT + 10));
				g.drawString("New Game", 600 - 53, 650);
			} else {
				g.setColor(Color.white);
				g.setFont(new Font("arial", Font.BOLD, FONT - 20));
				g.drawString("New Game", 600 + 18, 650);
			}

			if (selector == 2) {
				g.setColor(Color.white);
				g.setFont(new Font("arial", Font.BOLD, FONT + 10));
				g.drawString("Multiplayer", 600 - 50, 700);
			} else {
				g.setColor(Color.white);
				g.setFont(new Font("arial", Font.BOLD, FONT - 20));
				g.drawString("Multiplayer", 600 + 15, 700);
			}
			if (selector == 3) {
				g.setColor(Color.white);
				g.setFont(new Font("arial", Font.BOLD, FONT + 10));
				g.drawString("Exit", 670 - 30, 750);
			} else {
				g.setColor(Color.white);
				g.setFont(new Font("arial", Font.BOLD, FONT - 20));
				g.drawString("Exit", 670 + 3, 750);
			}
		} else if (menuSelect.equals("wait")) {
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, FONT));
			g.drawString("AGUARDANDO JOGADORES", 650 + 3, 700);
		} else if (menuSelect.equals("single")) {

		}
	}

}
