package FreeWay.Game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

import javax.swing.JFrame;

import FreeWay.Audio.Sound;
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
	public static final int WIDTH = 350;
	public static final int HEIGHT = 190;
	public static final int SCALE = 4;
	public static final int TAM_IMG = 16;

	public static List<VehiclesData> vehiclesDatas;
	public static List<Vehicles> vehicles;

	public static List<Player> players;
	public List<PlayerData> playersDados;

	private Thread threadIn;
	private Thread threadOut;
	private Thread thread;
	public static boolean isConnected = false;
	public static boolean isRunning = false;
	private static boolean contagem = true;
	private static Socket cliente;
	private static ObjectInputStream inObject;
	private static ObjectOutputStream outObject;
	private static DataOutputStream falar;
	private static DataInputStream ouvir;
	private static int MAP = 0;

	// DADOS DO MUNDO
	private BufferedImage image;
	public static World world = new World();
	public static Spritesheet spritesheet;// = new Spritesheet("/sprite2.png");

	// DADOS PARA O TICK
	private int frames = 0;
	private int maxFrames = 5;

	private MenuIniciar menu;
	private SinglePlayer single;

	public int PLAYER = 0;
	public int contAux = 0;
	public int cont = 0;

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

	public Game() {
		// ATIVA O TECLADO PARA ESSA CLASSE
		this.addKeyListener(this);

		// CRIA E ASSOCIA OS DADOS A TELA
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		// SPRITES
		spritesheet = new Spritesheet("/sprite.png");

		// imagem do fundo do jogo. Sem ela o jogo fica no vacoo
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		// INSTANCIA OS VETORES
		players = new ArrayList<Player>();
		playersDados = new ArrayList<PlayerData>();

		vehiclesDatas = new ArrayList<VehiclesData>();
		vehicles = new ArrayList<Vehicles>();

		// INSTANCIA O SINGLE PLAYER
		single = new SinglePlayer();

		// INSTANCIA O MENU
		menu = new MenuIniciar();

		for (int i = 0; i < 10; i++) {
			vehiclesDatas.add(new VehiclesData());
			vehicles.add(new Vehicles());
		}

		for (int i = 0; i < 6; i++) {
			playersDados.add(new PlayerData(500));
			players.add(new Player(0, new PlayerData(500)));
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.initFrame();
		game.start();
	}

	public synchronized void start() {
		thread = new Thread(this);
		threadIn = new Thread(IN);
		threadOut = new Thread(OUT);
		thread.start();
		isRunning = true;
	}

	// PARA A THREAD
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
			threadIn.join();
			threadOut.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void connect() {
		// SE O JOGADOR NAO ESTIVER CONECTADO, CONECTE
		if (!isConnected) {
			try {
				cliente = new Socket("172.18.3.170", 80);

				// OUVE QUAL PLAYER ELE �
				ouvir = new DataInputStream(cliente.getInputStream());
				PLAYER = ouvir.readInt();
				System.out.println("SOU O PLAYER : " + PLAYER + "");

				if (PLAYER == 0) {
					// ENVIA QUAL MAPA �
					falar = new DataOutputStream(cliente.getOutputStream());
					falar.writeInt(1);

				} else {
					// ENVIA QUAL MAPA
					ouvir = new DataInputStream(cliente.getInputStream());
					MAP = ouvir.readInt();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			isConnected = true;
			threadIn.start();
			threadOut.start();
			menu.menuSelect = "play";
		}
	}

	private Runnable IN = new Runnable() {
		public void run() {
			while (isRunning) {
				inputServerVehicles();
				inputServerPlayers();
			}
		}
	};

	private Runnable OUT = new Runnable() {
		public void run() {
			while (isRunning) {
				outputServerPlayers();
			}
		}
	};

	public synchronized void inputServerVehicles() {
		// RECEBE AS POSI��ES DOS PLAYERS
		try {
			inObject = new ObjectInputStream(cliente.getInputStream());
			vehiclesDatas = (List<VehiclesData>) inObject.readObject();

			for (int i = 0; i < vehiclesDatas.size(); i++)
				vehicles.get(i).dados = vehiclesDatas.get(i);

		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public synchronized void inputServerPlayers() {
		// System.out.println("CLINTE: " + vehicles.get(1).dados.getPOS_X());
		// RECEBE AS POSI��ES DOS PLAYERS
		try {
			inObject = new ObjectInputStream(cliente.getInputStream());
			playersDados = (List<PlayerData>) inObject.readObject();

			for (int i = 0; i < playersDados.size(); i++)
				if (i != PLAYER)
					players.get(i).dados = playersDados.get(i);

		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public synchronized void outputServerPlayers() {
		// RECEBE AS POSI��ES DOS PLAYERS
		try {
			outObject = new ObjectOutputStream(cliente.getOutputStream());
			// System.out.println("CLIENTE : " );
			outObject.writeObject(players.get(PLAYER).dados);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void tick() {
		// ATUALIZA OS FRAMES DO JOGO
		frames++;
		if (frames > maxFrames) {
			frames = 0;
		}

		// SE ELE ESTIVER NO SINGLE
		if (menu.menuSelect.equals("single")) {
			single.tick();

		} else {
			if (players.size() > 0) {
				players.get(PLAYER).tick();
			}
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		// RENDERIZA��O DO FUNDO DO JOGO
		Graphics g = image.getGraphics();
		g.setColor(Color.gray);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		// SE ELE ESTIVER NO MENU, RENDERIZA O MENU
		if (menu.menuSelect.equals("menu") || menu.menuSelect.equals("wait")) {
			menu.render(g);
		} else if (menu.menuSelect.equals("play")) {
			// contagem = true;
			if (!contagem) {
				// RENDERIZA A RUA
				world.render(g);

				// RENDERIZA OS CARROS
				for (int i = 0; i < vehicles.size(); i++) {
					Vehicles e = vehicles.get(i);
					e.render(g, i);
				}

				// RENDERIZA OS PLAYERS
				for (int i = 0; i < 2; i++) {
					Player p = players.get(i);
					p.render(g, PLAYER);
				}
			} else {
				cont++;
				if (cont > 50) {
					cont = 0;
					contAux += 1;
				}
				if (contAux == 3)
					contagem = false;
				g.setColor(Color.white);
				g.setFont(new Font("arial", Font.BOLD, 300));
				g.drawString("" + contAux, 650 + 3, 500);
			}
		} else if (menu.menuSelect.equals("single")) {
			// RENDERIZA A RUA
			world.render(g);
			// RENDERIZA OS CARROS
			for (int i = 0; i < Game.vehicles.size(); i++) {
				Vehicles e = Game.vehicles.get(i);
				e.render(g, i);
			}
			single.render(g);
		}
		bs.show();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();

		// CONSTANTE TICK POR SEG
		double amountOfTicks = 100.0;

		// CALCULO PARA O MOMENTO CERTO DE FAZER O UPDATE DO JOGO
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		double timer = System.currentTimeMillis();
		requestFocus();

		// LOOP DO JOGO
		while (isRunning) {
			if (!isConnected && menu.menuSelect.equals("wait"))
				connect();

			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				frames++;
				delta--;
				render();
				tick();
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
		if (menu.menuSelect.equals("menu") || menu.menuSelect.equals("wait")) {
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
					menu.menuSelect = "single";
					break;

				case 2:
					menu.menuSelect = "wait";
					break;

				case 3:
					System.exit(0);
					break;
				}
			}
		} else if (menu.menuSelect.equals("play")) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				players.get(PLAYER).UP = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				players.get(PLAYER).DOWN = true;
			}
		} else if (menu.menuSelect.equals("single")) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				single.player.UP = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				single.player.DOWN = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_0)
				world.MAP = 0;
			if (e.getKeyCode() == KeyEvent.VK_1)
				world.MAP = 1;
			if (e.getKeyCode() == KeyEvent.VK_2)
				world.MAP = 2;
			if (e.getKeyCode() == KeyEvent.VK_3)
				world.MAP = 3;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (menu.menuSelect.equals("play")) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				players.get(PLAYER).UP = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				players.get(PLAYER).DOWN = false;
			}
		} else if (menu.menuSelect.equals("single")) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				single.player.UP = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				single.player.DOWN = false;
			}
		}
	}

}