package FreeWay.Game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import FreeWay.Entities.Player;
import FreeWay.Entities.PlayerData;
import FreeWay.Entities.Vehicles;
import FreeWay.Entities.VehiclesData;
import FreeWay.Graficos.Spritesheet;
import FreeWay.World.World;

public class Game extends Canvas implements KeyListener, Runnable {

	private static final long serialVersionUID = 1L;
	// DADOS DA JANELA DO JOGO
	public static JFrame frame;
	private Thread thread;
	public static final int WIDTH = 350;
	public static final int HEIGHT = 190;
	private final int SCALE = 4;
	public static final int TAM_IMG = 16;
	private static boolean isRunning = true;

	// DADOS DO MUNDO
	private BufferedImage image;
	private World world = new World();
	public static Spritesheet spritesheet = new Spritesheet("/sprite.png");

	// DADOS PARA O TICK
	private int frames = 0;
	private int maxFrames = 10;

	// VEICULOS E DADOS DOS JOGADORES
	public static List<Vehicles> vehicles;
	public static List<VehiclesData> vehiclesDados;
	public int PLAYER = 0;
	public int NUM_PLAYER = 0;
	public int MAX_VEHICLES = 10;
	public List<Player> players;
	public List<PlayerData> playersDados;
	private MenuIniciar menu;

	// LIGAÇÕES COM O SERVIDOR
	private static int PORTA_PADRAO = 80;
	private Socket cliente;
	private static DataInputStream ouvir;
	private static DataOutputStream falar;
	private ObjectOutputStream outObject;
	private ObjectInputStream inObject;

	public static Random rand = new Random();

	public Game() {
		// ATIVA O TECLADO PARA ESSA CLASSE
		this.addKeyListener(this);

		// CRIA E ASSOCIA OS DADOS A TELA
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		// INSTANCIA OS VETORES
		players = new ArrayList<Player>();
		playersDados = new ArrayList<PlayerData>();

		// INSTACIA A LISTA DE vehicles
		this.vehicles = new ArrayList<Vehicles>();

		// CRIA CARROS GENÉRICOS PARA ALTERAR QUANDO O VETOR FAIXAS[] ESTÁ "false"
		for (int i = 0; i < MAX_VEHICLES; i++) {
			vehicles.add(new Vehicles(new VehiclesData()));
		}

		// SPRITES
		spritesheet = new Spritesheet("/sprite.png");

		// imagem do fundo do jogo. Sem ela o jogo fica no vacoo
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		// INSTANCIA O MENU
		menu = new MenuIniciar();
	}

	// DADOS DA TELA JFRAME
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
		Game game = new Game();
		try {
			game.cliente = new Socket("localhost", PORTA_PADRAO);
		} catch (IOException e) {
			e.printStackTrace();
		}
		game.initFrame();
		game.start();
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
		/* Renderização do Fundo do jogo */
		Graphics g = image.getGraphics();
		g.setColor(Color.gray);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		// world.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		if (menu.active) {
			menu.render(g);
		} else {
			world.render(g, players.get(PLAYER).SCORE);

			for (int i = 0; i < vehicles.size(); i++) {
				// uma var generica de Vehicles para selecionar todas das listas
				Vehicles e = vehicles.get(i);
				e.render(g, i);
			}
			for (int i = 0; i < players.size(); i++) {
				Player p = players.get(i);
				p.render(g, i * (1400 / NUM_PLAYER));
			}
		}
		bs.show();
	}

	private void tick() {
		frames++;
		if (frames > maxFrames) {
			frames = 0;
		}

		players.get(PLAYER).tick(this);
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
		try {
			ouvir = new DataInputStream(cliente.getInputStream());
			PLAYER = ouvir.readInt();

			System.out.println("sou o jogador " + PLAYER);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {

				try {

					inObject = new ObjectInputStream(cliente.getInputStream());
					vehiclesDados = (List<VehiclesData>) inObject.readObject();

					inObject = new ObjectInputStream(cliente.getInputStream());
					playersDados = (List<PlayerData>) inObject.readObject();

					falar = new DataOutputStream(cliente.getOutputStream());
					falar.writeBoolean(true);

				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}

				// ATT OS DADOS COM OS DADOS DO SERVIDOR
				for (int i = 0; i < vehiclesDados.size(); i++) {
					vehicles.set(i, new Vehicles(vehiclesDados.get(i)));
					//System.out.println(vehiclesDados.get(i).POS_X);
				}

				if (NUM_PLAYER < playersDados.size()) {
					for (int i = NUM_PLAYER; i < playersDados.size(); i++) {
						players.add(new Player(i, new PlayerData()));
						// playersDados.add(new PlayerData());
					}
					NUM_PLAYER = playersDados.size();
				}

				for (int i = 0; i < playersDados.size(); i++) {
					if (i != PLAYER)
						players.set(i, new Player(i, playersDados.get(i)));
				}

				/*
				 * try { outObject = new ObjectOutputStream(cliente.getOutputStream());
				 * outObject.writeObject(playersDados); } catch (IOException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 */
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
	public void keyPressed(KeyEvent e) {
		if (menu.active) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (menu.selector > 1)
					menu.selector -= 1;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (menu.selector < 3)
					menu.selector += 1;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				switch (menu.selector) {
				case 1:

					break;

				case 2:
					menu.active = false;
					break;

				case 3:
					System.exit(0);
					break;
				}
			}
		} else {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				players.get(PLAYER).UP = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				players.get(PLAYER).DOWN = true;
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			players.get(PLAYER).UP = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			players.get(PLAYER).DOWN = false;
		}
	}

}
