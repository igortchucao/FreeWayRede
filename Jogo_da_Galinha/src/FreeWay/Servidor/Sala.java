package FreeWay.Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import FreeWay.Entities.PlayerData;
import FreeWay.Entities.VehiclesData;
import FreeWay.Game.geradorVehicles;

public class Sala implements Runnable {
	private FreeWay servidor;
	private int PORTA_PADRAO;
	public List<VehiclesData> vehicles;
	public List<PlayerData> players;
	public boolean retorno;
	
	// SABER SE AS FAIXAS ESTAO OCUPADAS
	public static boolean faixas[] = new boolean[10];

	public Sala(FreeWay servidor) {
		vehicles = new ArrayList<VehiclesData>();
		players = new ArrayList<PlayerData>();
		
		for (int i = 0; i < faixas.length; i++)
			faixas[i] = false;

		for (int i = 0; i < 10; i++)
			vehicles.add(new VehiclesData());

		this.servidor = servidor;
		this.PORTA_PADRAO = servidor.PORTA_PADRAO;
	}

	@Override
	public void run() {
		try {
			ServerSocket socketServidor = new ServerSocket(PORTA_PADRAO);

			System.out.println("Servidor iniciado ...");
			System.out.println("------------------------------");
			System.out.println("Ouvindo na porta: " + PORTA_PADRAO);
			new Thread(new geradorVehicles(this)).start();
			while (true) {
				Socket conexao = socketServidor.accept();
				servidor.clientes.add(conexao);
				System.out.println("SERVIDOR : " + conexao.getRemoteSocketAddress());
				players.add(new PlayerData());
				new Thread(new Server(this, conexao, players.size() - 1)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
