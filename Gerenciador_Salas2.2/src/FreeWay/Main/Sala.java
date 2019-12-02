package FreeWay.Main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import FreeWay.Entities.PlayerData;
import FreeWay.Entities.VehiclesData;

public class Sala implements Runnable {

	public static List<Socket> jogadoresConectados;
	private Thread thread0;
	private Thread thread1;
	private Thread thread2;
	private Thread thread3;
	private Thread thread00;
	private Thread thread11;
	private Thread thread22;
	private Thread thread33;
	private Thread vehiclesthread1;
	private static DataOutputStream falar0;
	private static DataOutputStream falar1;
	private static DataOutputStream falar2;
	private static DataOutputStream falar3;
	private static DataInputStream ouvir;

	private ObjectOutputStream outObject0;
	private ObjectOutputStream outObject1;
	private ObjectOutputStream outObject2;
	private ObjectOutputStream outObject3;

	private ObjectInputStream inObject0;

	public List<PlayerData> players;
	public List<VehiclesData> vehicles;

	public int MAP = 10;
	public int numJogadores = 0;

	private static boolean isRunning = true;
	public static boolean retorno = true;

	private int frames = 0;
	private int maxFrames = 2;

	public Sala(List<Socket> jogadores, int numJogadores) {
		this.jogadoresConectados = jogadores;
		this.numJogadores = numJogadores;
		System.out.println("SALA CRIADA COM OS JOGADORES: ");
		for (int i = 0; i < numJogadores; i++) {
			System.out.println(this.jogadoresConectados.get(i).getRemoteSocketAddress());
		}
		players = new ArrayList<PlayerData>();
		for (int i = 0; i < numJogadores; i++) {
			players.add(new PlayerData(500 + i * 100));
		}

		vehicles = new ArrayList<VehiclesData>();
		for (int i = 0; i < 10; i++) {
			vehicles.add(new VehiclesData());
		}
		start();
	}

	public synchronized void start() {
		if (numJogadores >= 1) {
			thread0 = new Thread(vehicles0thread);
			thread0.start();
		}
		if (numJogadores >= 2) {
			thread1 = new Thread(vehicles1thread);
			thread1.start();
		}
		if (numJogadores >= 3) {
			thread2 = new Thread(vehicles2thread);
			thread2.start();
		}
		if (numJogadores >= 4) {
			thread3 = new Thread(vehicles3thread);
			thread3.start();
		}
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread0.join();
			vehiclesthread1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private Runnable vehicles0thread = new Runnable() {
		public void run() {
			startGame(falar0, ouvir, 0);
			while (isRunning) {
				vehiclesOut(outObject0, 0);
				playerOut(outObject0, 0);
			}
		}
	};

	private Runnable vehicles1thread = new Runnable() {
		public void run() {
			startGame(falar1, null, 1);
			while (isRunning) {
				vehiclesOut(outObject1, 1);
				playerOut(outObject1, 1);
			}
		}
	};

	private Runnable vehicles2thread = new Runnable() {
		public void run() {
			startGame(falar2, null, 2);
			while (isRunning) {
				vehiclesOut(outObject2, 2);
				playerOut(outObject2, 2);
			}
		}
	};

	private Runnable vehicles3thread = new Runnable() {
		public void run() {
			startGame(falar3, null, 3);
			while (isRunning) {
				vehiclesOut(outObject3, 3);
				playerOut(outObject3, 3);
			}
		}
	};

	public void playerOut(ObjectOutputStream outObject, int playerCliente) {
		try {
			// ENVIA OS DADOS PLAYERS
			outObject = new ObjectOutputStream(jogadoresConectados.get(playerCliente).getOutputStream());
			outObject.writeObject(players);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void vehiclesOut(ObjectOutputStream outObject, int playerCliente) {
		try {
			// ENVIA OS DADOS DOS VEHICLES
			outObject = new ObjectOutputStream(jogadoresConectados.get(playerCliente).getOutputStream());
			outObject.writeObject(vehicles);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startGame(DataOutputStream outPut, DataInputStream inPut, int playerCliente) {
		try {
			// ENVIA QUAL JOGADOR ELE �
			outPut = new DataOutputStream(jogadoresConectados.get(playerCliente).getOutputStream());
			outPut.writeInt(playerCliente);

			if (playerCliente == 0) {
				// L� QUAL MAPA
				inPut = new DataInputStream(jogadoresConectados.get(playerCliente).getInputStream());
				MAP = inPut.readInt();
			} else {
				// ENVIA QUAL MAPA
				outPut = new DataOutputStream(jogadoresConectados.get(playerCliente).getOutputStream());
				outPut.writeInt(MAP);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		// CONSTANTE TICK POR SEG
		double amountOfTicks = 20.0;

		// CALCULO PARA O MOMENTO CERTO DE FAZER O UPDATE DO JOGO
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		double timer = System.currentTimeMillis();

		// LOOP DO JOGO
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {

				frames++;
				if (frames > maxFrames) {
					frames = 0;
					for (int i = 0; i < 10; i++) {
						VehiclesData v = vehicles.get(i);
						v.randomize();
						v.tick(1);
					}
				}
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