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
				//O SERVIDOR FICA AGUARDANDO UMA CONECÇÃO DO CLIENTE 
				Socket conexao = socketServidor.accept();
				System.out.println("SERVIDOR : " + conexao.getRemoteSocketAddress());
				//ENTAO ELE ADICIONA A CONECÇÃO DO CLIENTE AO VETOR "sala"
				sala.add(conexao);
				JOGADORES += 1;
				//SE A QUANTIDADE DOS JOGADORES IGUALA AO VALOR AQUI ESTABELECIDO, ELE COMECA O JOGO 
				if (JOGADORES == 2) {
					//COPIA A CONECÇÃO PARA UM VETOR AUXILIAR
					List<Socket> aux = new ArrayList<Socket>();
					aux.addAll(sala);
					//CRIA UMA NOVA THREAD PARA GERENCIAR A SALA CRIADA COM OS JOGADORES 
					new Thread(new Sala(aux, JOGADORES)).start();
					//LIMPA O VETOR "sala" PARA PROXIMAS THREADS 
					sala.removeAll(sala);
					JOGADORES = 0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
