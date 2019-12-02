package FreeWay.Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import FreeWay.Game.Game;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	private int PLAYER = 0;

	public int SCORE = 0;
	public PlayerData dados = new PlayerData();
	public boolean UP = false;
	public boolean DOWN = false;
	public int speed = 3;
	public int frames = 0;
	public int maxFrames = 8;
	public int index = 0;
	public int maxIndex = 2;

	private BufferedImage[] playerImage = new BufferedImage[maxIndex + 2];

	public Player(int player, PlayerData dados) {
		this.dados = dados;
		this.PLAYER = player;
		for (int i = 0; i < 2; i++) {
			playerImage[i] = Game.spritesheet.getSprite(0 + (64 * i), 128, 64, 64);
			playerImage[i + 2] = Game.spritesheet.getSprite(0 + (64 * i), 64, 64, 64);
		}
	}

	public void render(Graphics g, int x) {
		if (UP || DOWN)
			dados.MOV = true;
		else
			dados.MOV = false;
		if (dados.MOV) {
			if (x == PLAYER) {
				g.drawImage(playerImage[index], dados.POS_X + x, dados.POS_Y, 55, 55, null);
			} else {
				g.drawImage(playerImage[index + 2], dados.POS_X + x, dados.POS_Y, 55, 55, null);
			}
		} else {
			if (x == PLAYER) {
				g.drawImage(playerImage[0], dados.POS_X + x, dados.POS_Y, 55, 55, null);
			} else {
				g.drawImage(playerImage[2], dados.POS_X + x, dados.POS_Y, 55, 55, null);
			}
		}
	}

	public void tick(Game game) {
		if (dados.POS_Y < 50) {
			dados.POS_Y = 659;
			SCORE += 1;
		}

		/*for (int i = 0; i < game.vehicles.size(); i++) {
			if ((dados.POS_X < game.vehicles.get(i).dados.POS_X || dados.POS_X > game.vehicles.get(i).dados.POS_X + 20)
					&& (dados.POS_Y < i * 58 || dados.POS_Y > i * 58 + 20)) {
				dados.POS_Y = 659;
				SCORE = 0;
			}
		}*/

		if (UP)
			dados.POS_Y -= speed;
		if (DOWN)
			dados.POS_Y += speed;

		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 0;
			}
		}
	}
}