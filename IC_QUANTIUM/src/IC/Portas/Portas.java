package IC.Portas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Portas {
	public Color COR = Color.white;
	public String TEXT = "#";
	public int POS_X, POS_Y;
	public int INICIALX, INICIALY;
	public int MaskPOS_X, MaskPOS_Y;
	public int FONT;
	public int WIDTH = 60;
	public int HEIGHT = 60;
	public boolean MOUS;
	
	public Portas() {
		
	}
	
	public Portas(int x, int y) {
		POS_X = x;
		POS_Y = y;
		INICIALX = x;
		INICIALY = y;
	}
	public void render(Graphics g) {
		g.setColor(COR);
		g.fillRect(POS_X, POS_Y, WIDTH, HEIGHT);
		
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, FONT));
		g.drawString("" + TEXT, POS_X + MaskPOS_X, POS_Y + MaskPOS_Y);
	}
	
	public void tick() {
		
	}
}