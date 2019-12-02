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
	}

	@Override
	public synchronized void run() {
		// SORTEIA UMA SEMENTE PARA QUE CADA JOGO TENHA UM FLUXO DE CARROS DIFERENTE
		if (jogadoresConectados.size() >= 1) {
			new Thread() {
				public synchronized void run() {
					try {
						// FALA QUE SE CONECTOU
						falar0 = new DataOutputStream(jogadoresConectados.get(0).getOutputStream());
						falar0.writeBoolean(true);

						// FALA QUAL PLAYER É
						falar0 = new DataOutputStream(jogadoresConectados.get(0).getOutputStream());
						falar0.writeInt(0);

						// RECEBE QUAL FASE É
						ouvir = new DataInputStream(jogadoresConectados.get(0).getInputStream());
						MAP = ouvir.readInt();

						// LOOP DO GAME
						while (isRunning) {
							// ENVIA OS DADOS DOS JOGADORES
							outObject0 = new ObjectOutputStream(jogadoresConectados.get(0).getOutputStream());
							outObject0.writeObject(players);

							// ENVIA OS DADOS DOS VEHICLES
							outObject0 = new ObjectOutputStream(jogadoresConectados.get(0).getOutputStream());
							outObject0.writeObject(vehicles);

							// ATUALIZA OS DADOS DO JOGADOR ATUAL
							inObject0 = new ObjectInputStream(jogadoresConectados.get(0).getInputStream());
							players.set(0, (PlayerData) inObject0.readObject());

						}
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}

			}.start();

			if (jogadoresConectados.size() >= 2) {
				new Thread() {
					public synchronized void run() {
						try {
							falar1 = new DataOutputStream(jogadoresConectados.get(1).getOutputStream());
							falar1.writeBoolean(true);

							falar1 = new DataOutputStream(jogadoresConectados.get(1).getOutputStream());
							falar1.writeInt(1);

							while (true) {
								if (MAP != 10) {
									falar1 = new DataOutputStream(jogadoresConectados.get(1).getOutputStream());
									falar1.writeInt(MAP);
									break;
								}
							}
							while (isRunning) {
								outObject1 = new ObjectOutputStream(jogadoresConectados.get(1).getOutputStream());
								outObject1.writeObject(players);

								outObject1 = new ObjectOutputStream(jogadoresConectados.get(1).getOutputStream());
								outObject1.writeObject(vehicles);

								inObject1 = new ObjectInputStream(jogadoresConectados.get(1).getInputStream());
								players.set(1, (PlayerData) inObject1.readObject());

							}
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}

				}.start();

				if (jogadoresConectados.size() >= 3) {
					new Thread() {
						public synchronized void run() {
							try {
								falar2 = new DataOutputStream(jogadoresConectados.get(2).getOutputStream());
								falar2.writeBoolean(true);

								falar2 = new DataOutputStream(jogadoresConectados.get(2).getOutputStream());
								falar2.writeInt(2);
								while (true) {
									if (MAP != 10) {
										falar2 = new DataOutputStream(jogadoresConectados.get(2).getOutputStream());
										falar2.writeInt(MAP);
										break;
									}
								}
								while (isRunning) {
									outObject2 = new ObjectOutputStream(jogadoresConectados.get(2).getOutputStream());
									outObject2.writeObject(players);

									outObject2 = new ObjectOutputStream(jogadoresConectados.get(2).getOutputStream());
									outObject2.writeObject(vehicles);

									inObject2 = new ObjectInputStream(jogadoresConectados.get(2).getInputStream());
									players.set(2, (PlayerData) inObject2.readObject());

								}
							} catch (IOException | ClassNotFoundException e) {
								e.printStackTrace();
							}
						}

					}.start();

					if (jogadoresConectados.size() >= 4) {
						new Thread() {
							public synchronized void run() {
								try {
									falar3 = new DataOutputStream(jogadoresConectados.get(3).getOutputStream());
									falar3.writeBoolean(true);

									falar3 = new DataOutputStream(jogadoresConectados.get(3).getOutputStream());
									falar3.writeInt(3);
									while (true) {
										if (MAP != 10) {
											falar3 = new DataOutputStream(jogadoresConectados.get(3).getOutputStream());
											falar3.writeInt(MAP);
											break;
										}
									}
									while (isRunning) {
										outObject3 = new ObjectOutputStream(
												jogadoresConectados.get(3).getOutputStream());
										outObject3.writeObject(players);

										outObject3 = new ObjectOutputStream(
												jogadoresConectados.get(3).getOutputStream());
										outObject3.writeObject(vehicles);

										inObject3 = new ObjectInputStream(jogadoresConectados.get(3).getInputStream());
										players.set(3, (PlayerData) inObject3.readObject());

									}
								} catch (IOException | ClassNotFoundException e) {
									e.printStackTrace();
								}
							}

						}.start();
					}
				}
			}
		}
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
	}

}
