package FreeWay.Entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Random;

import FreeWay.Audio.Sound;
import FreeWay.Game.Game;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	private int PLAYER = 0;

	public PlayerData dados = new PlayerData(0);
	public boolean UP = false;
	public boolean DOWN = false;
	public int speed = 3;
	public int frames = 0;
	public int maxFrames = 8;
	public int index = 0;
	public int maxIndex = 2;
	public int LIFE = 3;
	private Random rand = new Random();

	private BufferedImage[] playerImage = new BufferedImage[maxIndex + 2];

	public Player(int player, PlayerData dados) {
		this.dados = dados;
		this.PLAYER = player;
		for (int i = 0; i < 2; i++) {
			playerImage[i] = Game.spritesheet.getSprite(0 + (64 * i), 128, 64, 64);
			playerImage[i + 2] = Game.spritesheet.getSprite(0 + (64 * i), 4 * 64, 64, 64);
		}
	}

	public void render(Graphics g, int player) {
		for (int i = 0; i < 2; i++) {
			playerImage[i] = Game.spritesheet.getSprite(0 + (64 * i), 128, 64, 64);
			playerImage[i + 2] = Game.spritesheet.getSprite(0 + (64 * i), 4 * 64, 64, 64);
		}
		if (UP || DOWN)
			dados.MOV = true;
		else
			dados.MOV = false;
		if (dados.MOV) {
			if (player == PLAYER) {
				g.drawImage(playerImage[index], dados.POS_X, dados.POS_Y, 55, 55, null);

				g.setColor(Color.red);
				g.setFont(new Font("arial", Font.BOLD, 35));
				g.drawString("" + PLAYER, 20, 22);

				g.setColor(Color.red);
				g.setFont(new Font("arial", Font.BOLD, 35));
				g.drawString("" + dados.SCORE, dados.POS_X, 68);

			} else {
				g.drawImage(playerImage[index + 2], dados.POS_X, dados.POS_Y, 55, 55, null);

				g.setColor(Color.red);
				g.setFont(new Font("arial", Font.BOLD, 35));
				g.drawString("" + dados.SCORE, dados.POS_X, 68);
			}
		} else {
			if (player == PLAYER) {
				g.drawImage(playerImage[0], dados.POS_X, dados.POS_Y, 55, 55, null);

				g.setColor(Color.red);
				g.setFont(new Font("arial", Font.BOLD, 35));
				g.drawString("" + PLAYER, 20, 22);

				g.setColor(Color.red);
				g.setFont(new Font("arial", Font.BOLD, 35));
				g.drawString("" + dados.SCORE, dados.POS_X, 68);
			} else {
				g.drawImage(playerImage[2], dados.POS_X, dados.POS_Y, 55, 55, null);

				g.setColor(Color.red);
				g.setFont(new Font("arial", Font.BOLD, 35));
				g.drawString("" + dados.SCORE, dados.POS_X, 68);
			}
		}
		for (int i = 0; i < LIFE; i++) {
			g.drawImage(playerImage[0], 1300 + i * 22, 720, 30, 30, null);
		}
	}

	public void tick() {
		if (dados.POS_Y < 50) {
			dados.POS_Y = 659;
			dados.SCORE += 1;
		}

		if (UP)
			dados.POS_Y -= speed;
		if (DOWN && dados.POS_Y < 659)
			dados.POS_Y += speed;

		if (isColliding()) {
			dados.POS_Y = 659;
			LIFE -= 1;
		}

		if (isNear()) {
			int aux = rand.nextInt(3);
			switch (aux) {
			case 0:
				Sound y = new Sound("C:\\Users\\igors\\Desktop\\TPREDS\\GIT\\FreeWayRede\\Gerenciador_Salas2.2\\res\\buzina1.wav"); // Chamando a classe aonde est� o audio.
				break;
			case 1:
				Sound y2 = new Sound("C:\\Users\\igors\\Desktop\\TPREDS\\GIT\\FreeWayRede\\Gerenciador_Salas2.2\\res\\buzina2.wav"); // Chamando a classe aonde est� o audio.
				break;
			case 2:
				Sound y3 = new Sound("C:\\Users\\igors\\Desktop\\TPREDS\\GIT\\FreeWayRede\\Gerenciador_Salas2.2\\res\\buzina3.wav"); // Chamando a classe aonde est� o audio.
				break;
			}
		}

		if (LIFE == 0) {
			dados.SCORE = 0;
			LIFE = 3;
		}

		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 0;
			}
		}
	}

	public boolean isColliding() {
		for (int i = 0; i < Game.vehicles.size(); i++) {
			if (Game.vehicles.get(i).dados.MAO == 1) {
				if (Game.vehicles.get(i).dados.getPOS_X() - 50 < dados.POS_X + 20
						&& Game.vehicles.get(i).dados.getPOS_X() - 50 + 43 > dados.POS_X + 20
						&& (80 + i * 59) + 10 < dados.POS_Y + 20 && (80 + i * 59) + 10 + 30 > dados.POS_Y + 20) {
					return true;
				}
			} else {
				if (1400 - (Game.vehicles.get(i).dados.getPOS_X() - 5) < dados.POS_X + 20
						&& 1400 - (Game.vehicles.get(i).dados.getPOS_X() - 5) + 45 > dados.POS_X + 20
						&& (80 + i * 59) + 10 < dados.POS_Y + 20 && (80 + i * 59) + 10 + 30 > dados.POS_Y + 20) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isNear() {
		for (int i = 0; i < Game.vehicles.size(); i++) {
			if (Game.vehicles.get(i).dados.MAO == 1) {
				if (Game.vehicles.get(i).dados.getPOS_X() - 80 < dados.POS_X + 20
						&& Game.vehicles.get(i).dados.getPOS_X() - 80 + 303 > dados.POS_X + 20
						&& (80 + i * 59) - 10 < dados.POS_Y + 20 && (80 + i * 59) - 20 + 60 > dados.POS_Y + 20) {
					return true;
				}
			} else {
				if (1400 - (Game.vehicles.get(i).dados.getPOS_X() + 25) < dados.POS_X + 20
						&& 1400 - (Game.vehicles.get(i).dados.getPOS_X() + 25) + 303 > dados.POS_X + 20
						&& (80 + i * 59) - 10 < dados.POS_Y + 20 && (80 + i * 59) - 20 + 60 > dados.POS_Y + 20) {
					return true;
				}
			}
		}
		return false;
	}
}