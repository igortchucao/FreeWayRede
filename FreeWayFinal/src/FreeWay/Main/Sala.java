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
	
	private Thread outThread0;
	private Thread outThread1;
	private Thread outThread2;
	private Thread outThread3;
	
	private Thread inThread0;
	private Thread inThread1;
	private Thread inThread2;
	private Thread inThread3;

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
		for (int i = 0; i < numJogadores + 1; i++) {
			players.add(new PlayerData(500 + i * 100));
		}

		vehicles = new ArrayList<VehiclesData>();
		for (int i = 0; i < 10; i++) {
			vehicles.add(new VehiclesData());
		}
		start();
	}

	public void start() {
		if (numJogadores >= 1) {
			outThread0 = new Thread(outPutThread0);
			outThread0.start();
			inThread0 = new Thread(inPutThread0);
			inThread0.start();
		}
		if (numJogadores >= 2) {
			outThread1 = new Thread(outPutThread1);
			outThread1.start();
			inThread1 = new Thread(inPutThread1);
			inThread1.start();
		}
		if (numJogadores >= 3) {
			outThread2 = new Thread(outPutThread2);
			outThread2.start();
			inThread2 = new Thread(inPutThread2);
			inThread2.start();
		}
		if (numJogadores >= 4) {
			outThread3 = new Thread(outPutThread3);
			outThread3.start();
			inThread3 = new Thread(inPutThread3);
			inThread3.start();
		}
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			outThread0.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//FUN��O QUE COORDENA A THREAD DO JOGADOR 0
	private Runnable outPutThread0 = new Runnable() {
		public void run() {
			//PASSA OS DADOS INICIAIS 
			startGame(falar0, ouvir, 0);
			while (isRunning) {
				//TRANFERE OS CARROS DO SERVIDOR PARA O PLAYER 
				vehiclesOut(outObject0, 0);
				//TRANFERE A POSI��O DOS OUTROS JOGADORES 
				playerOut(outObject0, 0);
			}
		}
	};
	
	//FUN��O QUE RECEBE OS DADOS DO JOGADOR 0
	private Runnable inPutThread0 = new Runnable() {
		public void run() {
			while (isRunning) 
				//RECEBE A POSI��O DO JOGADOR 
				playerIn(inObject0, 0);
		}
	};
	
	private Runnable inPutThread1 = new Runnable() {
		public void run() {
			while (isRunning) 
				playerIn(inObject1, 1);
		}
	};
	
	private Runnable inPutThread2 = new Runnable() {
		public void run() {
			while (isRunning) 
				playerIn(inObject2, 2);
		}
	};
	
	private Runnable inPutThread3 = new Runnable() {
		public void run() {
			while (isRunning) 
				playerIn(inObject3, 3);
		}
	};

	private Runnable outPutThread1 = new Runnable() {
		public void run() {
			startGame(falar1, null, 1);
			while (isRunning) {
				vehiclesOut(outObject1, 1);
				playerOut(outObject1, 1);
			}
		}
	};

	private Runnable outPutThread2 = new Runnable() {
		public void run() {
			startGame(falar2, null, 2);
			while (isRunning) {
				vehiclesOut(outObject2, 2);
				playerOut(outObject2, 2);
			}
		}
	};

	private Runnable outPutThread3 = new Runnable() {
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
	
	public void playerIn(ObjectInputStream inObject, int playerCliente) {
		try {
			// RECEBE OS DADOS DO JOGADOR E ATUALIZA NO VETOR DE JOGADORES DO SEVIDOR 
			inObject = new ObjectInputStream(jogadoresConectados.get(playerCliente).getInputStream());
			players.set(playerCliente, (PlayerData) inObject.readObject());
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void startGame(DataOutputStream outPut, DataInputStream inPut, int playerCliente) {
		try {
			// ENVIA QUAL JOGADOR ELE �
			outPut = new DataOutputStream(jogadoresConectados.get(playerCliente).getOutputStream());
			outPut.writeInt(playerCliente);

			//SE ELE FOR O JOGADOR 0, ELE DEFINE QUAL MAPA USAR 
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
//FUN��O QUE ATUALIZA OS DADOS DOS VEICULOS NO SERVIDOR. ESTA FUN��O DESACELERA A THREAD PRINCIPAL DO SERVIDOR 
	//PARA ATUALIZAR A POSI��O DOS CARROS NA MESMA VELOCIDADE EM QUE O JOGO DO CLIENTE RODA. 
	//ENTAO UMA MANEIRA DE AJUSTAR AS VELOCIDADES DOS CARROS PARA O USUARIO � MUDAR A VELOCIDADE DESTA THREAD
	//E MUDAR A VELOCIDADE DOS CARROS. ESTA � UMA MANEIRA QUE CONSEGUI DE TIRAR OS LAGS DO JOGO. ACELEREI A
	// THREAD DO SERVIDOR E ENTAO OS LAGS SE TORNAVAM QUASE IMPERCEPTIVEIS.
	@Override
	public void run() {
		long lastTime = System.nanoTime();
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
				//ATUALIZA OS FRAMES 
				frames++;
				if (frames > maxFrames) {
					frames = 0;
					for (int i = 0; i < 10; i++) {
						//"VehiclesData" � UMA CLASSE QUE ARMAZENA A POSI��O DOS CARROS, COR, MAO E VELOCIDADE 
						VehiclesData v = vehicles.get(i);
						//DENTRO DESTA FUN��O, SE O CARRO PASSA DA POSI�AO "1400" (TAMANHO DA TELA),
							//ELE RANDOMIZA UMA NOVA COR, MAO E VELOCIDADE. (OS CARROS DIFERENTES S�O CORES)
						v.randomize();
						//ATUALIZA A VELOCIDADE DO CARRO. ESSE ARGUMENT
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
