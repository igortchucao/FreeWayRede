package FreeWay.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
	private int PORTA_PADRAO = 80;
	private int JOGADORES = 0;
	private List<Socket> sala;

	public Main() {
		sala = new ArrayList<Socket>();
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}

	public void start() {
		try {
			ServerSocket socketServidor = new ServerSocket(PORTA_PADRAO);

			System.out.println("Servidor iniciado ...");
			System.out.println("------------------------------");
			System.out.println("Ouvindo na porta: " + PORTA_PADRAO);

			while (true) {
				Socket conexao = socketServidor.accept();
				System.out.println("SERVIDOR : " + conexao.getRemoteSocketAddress());
				sala.add(conexao);
				JOGADORES += 1;
				if (JOGADORES == 1) {
					List<Socket> aux = new ArrayList<Socket>();
					aux.addAll(sala);
					new Thread(new Sala(aux, JOGADORES)).start();
					sala.removeAll(sala);
					JOGADORES = 0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
