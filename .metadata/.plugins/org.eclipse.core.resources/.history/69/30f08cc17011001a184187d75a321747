package IC.Main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;

import IC.Interface.Slot;
import IC.Interface.TimeLine;
import IC.Portas.Cnot;
import IC.Portas.Hadamar;
import IC.Portas.Not;
import IC.Portas.Phase;
import IC.Portas.Portas;
import IC.Portas.Toffoli;

public class Main extends Canvas implements Serializable, Runnable, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -553037493727457036L;

	// DADOS DA JANELA DO JOGO
	public static JFrame frame;
	public static JLabel mousePosition;
	private Thread thread;
	public static final int WIDTH = 350;
	public static final int HEIGHT = 190;
	public static final int SCALE = 4;
	public static final int TAM_IMG = 16;
	private BufferedImage image;
	public boolean isRunning = true;
	private int frames = 0;
	private int maxFrames = 0;
	private List<Portas> portas = new ArrayList<Portas>();
	private menuDePortas menu = new menuDePortas();
	private TimeLine timeLine = new TimeLine();

	private Random rand = new Random(55);
	private Random rand2 = new Random(55);

	public Main() {
		// CRIA E ASSOCIA OS DADOS A TELA
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		mousePosition = new JLabel();
		addMouseListener(this); // listens for own mouse and
		addMouseMotionListener(this); // mouse-motion events
		portas.add(new Not());
		portas.add(new Toffoli());
		portas.add(new Hadamar());
		portas.add(new Phase());
		portas.add(new Cnot());
	}

	public void initFrame() {
		frame = new JFrame();
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		Main ic = new Main();
		ic.initFrame();
		try {
			ic.start();
		} catch (NumberFormatException e) {
			System.out.println("Porta não é um número.");
		}
	}

	// inicia a thread
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	// para a thread
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		// RENDER DO FUNDO DO GAME
		Graphics g = image.getGraphics();
		g.setColor(Color.gray);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		menu.render(g);
		timeLine.render(g);
		for (int i = 0; i < portas.size(); i++) {
			Portas p = portas.get(i);
			p.render(g);
		}

		bs.show();

	}

	private void tick() {
		frames++;
		if (frames > maxFrames) {
			frames = 0;
		}
	}

	// função que roda na thread
	public void run() {
		long lastTime = System.nanoTime();

		// constante ticks por segundo
		double amountOfTicks = 60.0;

		// calculo para o momento certo para fazer o update do jogo (constante)
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;

			}

			if (System.currentTimeMillis() - timer >= 1000) {
				frames = 0;
				timer += 1000;
			}
		}

		stop();

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		for (int i = 0; i < portas.size(); i++) {
			Portas p = portas.get(i);
			if (p.MOUS) {
				p.POS_X = e.getX();
				p.POS_Y = e.getY();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (int i = 0; i < portas.size(); i++) {
			Portas p = portas.get(i);
			if ((e.getX() > p.POS_X && e.getX() < p.POS_X + 60) && e.getY() > p.POS_Y && e.getY() < p.POS_Y + 60)
				p.MOUS = true;
			else
				p.MOUS = false;
		}

		for (int i = 0; i < timeLine.lines.size(); i++) {
			for (int j = 0; j < timeLine.lines.get(i).slots.size(); j++) {
				Slot s = timeLine.lines.get(i).slots.get(j);
				if ((e.getX() > s.POS_X + s.TM && e.getX() < s.POS_X + 75) && e.getY() > s.POS_Y + 10
						&& e.getY() < s.POS_Y + s.TM - 10)
					s.SHOW = true;
				else
					s.SHOW = false;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (timeLine.button(e.getX(), e.getY()) == 1) {
			TimeLine.FIRSTs++;
		}else if(timeLine.button(e.getX(), e.getY()) == 2) {
			TimeLine.FIRSTs--;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (int i = 0; i < portas.size(); i++) {
			Portas p = portas.get(i);
			if (p.MOUS) {
				/*
				 * if (p instanceof Not) portas.add(new Not()); else if (p instanceof Hadamar)
				 * portas.add(new Hadamar()); else if (p instanceof Cnot) portas.add(new
				 * Cnot()); else if (p instanceof Phase) portas.add(new Phase()); else if (p
				 * instanceof Toffoli) portas.add(new Toffoli());
				 */
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (int i = 0; i < portas.size(); i++) {
			Portas p = portas.get(i);
			if (p instanceof Not)
				if (timeLine.colocadoNaCaixa(p.POS_X, p.POS_Y, new Not())) {

				}
			if (p instanceof Hadamar)
				if (timeLine.colocadoNaCaixa(p.POS_X, p.POS_Y, new Hadamar())) {

				}
			if (p instanceof Cnot)
				if (timeLine.colocadoNaCaixa(p.POS_X, p.POS_Y, new Cnot())) {

				}
			if (p instanceof Phase)
				if (timeLine.colocadoNaCaixa(p.POS_X, p.POS_Y, new Phase())) {

				}
			if (p instanceof Toffoli)
				if (timeLine.colocadoNaCaixa(p.POS_X, p.POS_Y, new Toffoli())) {

				}

		}
		for (int i = 0; i < portas.size(); i++) {
			Portas p = portas.get(i);
			if (p.MOUS) {
				p.POS_X = p.INICIALX;
				p.POS_Y = p.INICIALY;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
