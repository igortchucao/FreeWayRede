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
	private Thread thread1;
	private Thread thread2;
	private Thread thread3;
	private Thread thread0;
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
	private ObjectInputStream inObject1;
	private ObjectInputStream inObject2;
	private ObjectInputStream inObject3;

	public List<PlayerData> players;
	public List<VehiclesData> vehicles;

	public int MAP = 10;

	private static boolean isRunning = true;
	public static boolean retorno = true;

	private int frames = 0;
	private int maxFrames = 2;

	public Sala(List<Socket> jogadores, int numJogadores) {
		this.jogadoresConectados = jogadores;

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

		//thread1 = new Thread(player1thread);
		//thread2 = new Thread(player2thread);
		//thread3 = new Thread(player3thread);
		thread0 = new Thread(player0thread);
		vehiclesthread1 = new Thread(vehiclesthread); 
		//thread1.start();
		//thread2.start();
		//thread3.start();
		thread0.start();
		//vehiclesthread1.start();
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
	
	private Runnable player0thread = new Runnable() {
		public void run() {
			player0();
		}
	};
	
	private Runnable vehiclesthread = new Runnable() {
		public void run() {
			//renderTick();
		}
	};

	private Runnable player1thread = new Runnable() {
		public void run() {
			player1();
		}
	};

	private Runnable player2thread = new Runnable() {
		public void run() {
			player2();
		}
	};

	private Runnable player3thread = new Runnable() {
		public void run() {
			player3();
		}
	};

	public void player0() {
		try {
			System.out.println("passou 0");
			// FALA QUE SE CONECTOU
			falar0 = new DataOutputStream(jogadoresConectados.get(0).getOutputStream());
			falar0.writeBoolean(true);
			System.out.println("passou 1");
			// FALA QUAL PLAYER É
			falar0 = new DataOutputStream(jogadoresConectados.get(0).getOutputStream());
			falar0.writeInt(0);
			System.out.println("passou 2");
			// RECEBE QUAL FASE É
			ouvir = new DataInputStream(jogadoresConectados.get(0).getInputStream());
			MAP = ouvir.readInt();
			System.out.println("passou 3");
			// LOOP DO GAME
			while (isRunning) {
				System.out.println(vehicles.get(1).POS_X);
				// ENVIA OS DADOS DOS JOGADORES
				outObject0 = new ObjectOutputStream(jogadoresConectados.get(0).getOutputStream());
				outObject0.writeObject(players);

				// ENVIA OS DADOS DOS VEHICLES
				outObject0 = new ObjectOutputStream(jogadoresConectados.get(0).getOutputStream());
				outObject0.writeObject(vehicles);

				// ATUALIZA OS DADOS DO JOGADOR ATUAL
				/*inObject0 = new ObjectInputStream(jogadoresConectados.get(0).getInputStream());
				players.set(0, (PlayerData) inObject0.readObject());
*/
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void player1() {
		try {
			// FALA QUE SE CONECTOU
			falar1 = new DataOutputStream(jogadoresConectados.get(1).getOutputStream());
			falar1.writeBoolean(true);

			// FALA QUAL PLAYER É
			falar1 = new DataOutputStream(jogadoresConectados.get(1).getOutputStream());
			falar1.writeInt(1);

			// RECEBE QUAL FASE É
			ouvir = new DataInputStream(jogadoresConectados.get(1).getInputStream());
			MAP = ouvir.readInt();

			// LOOP DO GAME
			while (isRunning) {
				// ENVIA OS DADOS DOS JOGADORES
				outObject1 = new ObjectOutputStream(jogadoresConectados.get(1).getOutputStream());
				outObject1.writeObject(players);

				// ENVIA OS DADOS DOS VEHICLES
				outObject1 = new ObjectOutputStream(jogadoresConectados.get(1).getOutputStream());
				outObject1.writeObject(vehicles);

				// ATUALIZA OS DADOS DO JOGADOR ATUAL
				inObject1 = new ObjectInputStream(jogadoresConectados.get(1).getInputStream());
				players.set(1, (PlayerData) inObject1.readObject());

			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void player2() {
		try {
			// FALA QUE SE CONECTOU
			falar2 = new DataOutputStream(jogadoresConectados.get(2).getOutputStream());
			falar2.writeBoolean(true);

			// FALA QUAL PLAYER É
			falar2 = new DataOutputStream(jogadoresConectados.get(2).getOutputStream());
			falar2.writeInt(2);

			// RECEBE QUAL FASE É
			ouvir = new DataInputStream(jogadoresConectados.get(2).getInputStream());
			MAP = ouvir.readInt();

			// LOOP DO GAME
			while (isRunning) {
				// ENVIA OS DADOS DOS JOGADORES
				outObject2 = new ObjectOutputStream(jogadoresConectados.get(2).getOutputStream());
				outObject2.writeObject(players);

				// ENVIA OS DADOS DOS VEHICLES
				outObject2 = new ObjectOutputStream(jogadoresConectados.get(2).getOutputStream());
				outObject2.writeObject(vehicles);

				// ATUALIZA OS DADOS DO JOGADOR ATUAL
				inObject2 = new ObjectInputStream(jogadoresConectados.get(2).getInputStream());
				players.set(2, (PlayerData) inObject2.readObject());

			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void player3() {
		try {
			// FALA QUE SE CONECTOU
			falar3 = new DataOutputStream(jogadoresConectados.get(3).getOutputStream());
			falar3.writeBoolean(true);

			// FALA QUAL PLAYER É
			falar3 = new DataOutputStream(jogadoresConectados.get(3).getOutputStream());
			falar3.writeInt(3);

			// RECEBE QUAL FASE É
			ouvir = new DataInputStream(jogadoresConectados.get(3).getInputStream());
			MAP = ouvir.readInt();

			// LOOP DO GAME
			while (isRunning) {
				// ENVIA OS DADOS DOS JOGADORES
				outObject3 = new ObjectOutputStream(jogadoresConectados.get(3).getOutputStream());
				outObject3.writeObject(players);

				// ENVIA OS DADOS DOS VEHICLES
				outObject3 = new ObjectOutputStream(jogadoresConectados.get(3).getOutputStream());
				outObject3.writeObject(vehicles);

				// ATUALIZA OS DADOS DO JOGADOR ATUAL
				inObject3 = new ObjectInputStream(jogadoresConectados.get(3).getInputStream());
				players.set(3, (PlayerData) inObject3.readObject());

			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		System.out.println("deu certo");
		// CONSTANTE TICK POR SEG
		double amountOfTicks = 60.0;

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
