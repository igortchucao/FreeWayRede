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
	public List<VehiclesData> vehiclesDatas;
	public int PLAYER = 0;
	public int NUM_PLAYER = 0;
	public int MAX_VEHICLES = 10;
	public static List<Player> players;
	public List<PlayerData> playersDados;
	private MenuIniciar menu;

	// LIGAÇÕES COM O SERVIDOR
	private static int PORTA_PADRAO = 80;
	private Socket cliente;
	private static DataInputStream ouvir;
	private static DataOutputStream falar;
	private ObjectOutputStream outObject;
	private ObjectInputStream inObject;
	public boolean isConnected = false;
	public boolean allConnected = false;
	public boolean loop = false;

	public static Random rand;

	public Game() {

		// ATIVA O TECLADO PARA ESSA CLASSE
		this.addKeyListener(this);

		// CRIA E ASSOCIA OS DADOS A TELA
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		// INSTANCIA OS VETORES
		players = new ArrayList<Player>();
		playersDados = new ArrayList<PlayerData>();

		// INSTACIA A LISTA DE vehicles
		vehicles = new ArrayList<Vehicles>();
		vehiclesDatas = new ArrayList<VehiclesData>();

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
		// RENDERIZAÇÃO DO FUNDO DO JOGO
		Graphics g = image.getGraphics();
		g.setColor(Color.gray);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		if (!menu.menuSelect.equals("play")) {
			menu.render(g);
		} else {
			world.render(g);

			for (int i = 0; i < vehicles.size(); i++) {
				Vehicles e = vehicles.get(i);
				e.render(g, i);
			}
			for (int i = 0; i < players.size(); i++) {
				Player p = players.get(i);
				p.render(g, PLAYER);
			}
		}
		bs.show();
	}

	private void tick() {
		frames++;
		if (frames > maxFrames) {
			frames = 0;
		}
		if (menu.menuSelect.equals("menu")) {

		} else {
			if (players.size() > 0) {
				players.get(PLAYER).tick();
			}
		}
	}

	// FUNÇÃO QUE RODA NA THREAD
	public void run() {
		long lastTime = System.nanoTime();

		// CONSTANTE TICK POR SEG
		double amountOfTicks = 60.0;

		// CALCULO PARA O MOMENTO CERTO DE FAZER O UPDATE DO JOGO
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		double timer = System.currentTimeMillis();
		requestFocus();

		// LOOP DO JOGO
		while (isRunning) {
			if (menu.menuSelect.equals("wait")) {
				if (!isConnected) {
					try {
						cliente = new Socket("172.18.3.6", PORTA_PADRAO);
					} catch (IOException e) {
						e.printStackTrace();
					}
					isConnected = true;
				}
				if (!allConnected) {
					try {
						ouvir = new DataInputStream(cliente.getInputStream());
						allConnected = ouvir.readBoolean();
						menu.menuSelect = "play";
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if (allConnected && !loop) {
				// RECEBE QUAL JOGADOR ELE É E A SEMENTE PARA O RAND
				try {

					ouvir = new DataInputStream(cliente.getInputStream());
					PLAYER = ouvir.readInt();

				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// CRIA CARROS GENÉRICOS PARA ALTERAR QUANDO O VETOR FAIXAS[] ESTÁ "false"
				for (int i = 0; i < MAX_VEHICLES; i++) {
					vehicles.add(new Vehicles());
					vehiclesDatas.add(new VehiclesData());
				}
				loop = true;
			}

			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				if (allConnected) {
					// RECEBE AS POSIÇÕES DOS PLAYERS
					try {
						inObject = new ObjectInputStream(cliente.getInputStream());
						playersDados = (List<PlayerData>) inObject.readObject();

						inObject = new ObjectInputStream(cliente.getInputStream());
						vehiclesDatas = (List<VehiclesData>) inObject.readObject();

					} catch (IOException | ClassNotFoundException e1) {
						e1.printStackTrace();
					}
					// CONFERE A QUANTIDADE DE JOGADORES CONECTADOS
					if (NUM_PLAYER < playersDados.size()) {
						for (int i = NUM_PLAYER; i < playersDados.size(); i++) {
							// ADICIONA JOGADORES INICIAIS A LISTA DE PLAYERS
							players.add(new Player(i, null));
						}
					}

					// ATUALIZA O NUM_PLAYERS
					NUM_PLAYER = playersDados.size();

					// ATUALIZA OS DADOS DOS VEICULOS, COM OS DADOS DO SERVIDOR
					for (int i = 0; i < vehiclesDatas.size(); i++)
						vehicles.get(i).dados = vehiclesDatas.get(i);

					// ATUALIZA OS DADOS DOS JOGADORES, COM OS DADOS DO SERVIDOR
					for (int i = 0; i < playersDados.size(); i++)
						players.get(i).dados = playersDados.get(i);
				}
				tick();
				render();
				frames++;
				delta--;
				if (allConnected) {
					// ATUALIZA NO SERVIDOR A POSIÇÃO DO PLAYER ATUAL
					try {
						outObject = new ObjectOutputStream(cliente.getOutputStream());
						outObject.writeObject(players.get(PLAYER).dados);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
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

					break;

				case 2:
					menu.menuSelect = "wait";
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
		}
	}

}
