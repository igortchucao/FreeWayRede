package FreeWay.Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import FreeWay.Entities.Player;

public class Server implements Runnable {

	private Socket conexao;
	private int PLAYER = 0;
	private static DataOutputStream falar;
	private static DataInputStream ouvir;
	private ObjectOutputStream outObject;
	private ObjectInputStream inObject;
	public static List<Player> Objetos;
	private static boolean isRunning = true;
	public Sala sala;

	public Server(Sala sala, Socket conexao, int player) {
		this.conexao = conexao;
		this.PLAYER = player;
		this.sala = sala;
	}

	@Override
	public void run() {
		try {
			falar = new DataOutputStream(conexao.getOutputStream());
			falar.writeInt(PLAYER);

			while (isRunning) {

				outObject = new ObjectOutputStream(conexao.getOutputStream());
				outObject.writeObject(sala.vehicles);

				outObject = new ObjectOutputStream(conexao.getOutputStream());
				outObject.writeObject(sala.players);

				ouvir = new DataInputStream(conexao.getInputStream());
				sala.retorno = ouvir.readBoolean();

				/*
				 * inObject = new ObjectInputStream(conexao.getInputStream());
				 * sala.players.set(PLAYER, (PlayerData) inObject.readObject());
				 */
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
