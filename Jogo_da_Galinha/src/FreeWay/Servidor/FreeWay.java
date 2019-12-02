package FreeWay.Servidor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import FreeWay.Entities.PlayerData;
import FreeWay.Entities.Vehicles;
import FreeWay.Entities.VehiclesData;
import FreeWay.Game.Game;
import FreeWay.Game.geradorVehicles;

public class FreeWay extends Canvas implements Serializable, Runnable {

	private static final long serialVersionUID = -553037493727457036L;

	// DADOS DA JANELA DO JOGO
	public static JFrame frame;
	private Thread thread;
	public static final int WIDTH = 350;
	public static final int HEIGHT = 190;
	private final int SCALE = 4;
	public static final int TAM_IMG = 16;
	private BufferedImage image;
	public boolean isRunning = true;
	private int frames = 0;
	private int maxFrames = 0;

	public static int PORTA_PADRAO = 80;
	private static int PLAYERS = 0;
	public static int[] POS_PLAYER = new int[2];
	public List<Socket> clientes;

	public FreeWay() {
		// CRIA E ASSOCIA OS DADOS A TELA
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		clientes = new ArrayList<Socket>();
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
		FreeWay servidor = new FreeWay();
		servidor.initFrame();
		try {
			servidor.start();
		} catch (NumberFormatException e) {
			System.out.println("Porta não é um número.");
		}
	}

	// INICIA A THREAD 
	public synchronized void start() {
		thread = new Thread(this);
		new Thread(new Sala(this)).start();
		isRunning = true;
		thread.start();
	}

	// PARA A THREAD 
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
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		for (int i = 0; i < clientes.size(); i++) {
			// uma var generica de Vehicles para selecionar todas das listas
			Socket c = clientes.get(i);
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.drawString("Cliente : " + c.getRemoteSocketAddress(), 550 + 18, 500 + i * 50);
		}

		bs.show();

	}

	private void tick() {
		frames++;
		if (frames > maxFrames) {
			frames = 0;
			// for(int i = 0; i < rand.nextInt(); i ++)
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
}
