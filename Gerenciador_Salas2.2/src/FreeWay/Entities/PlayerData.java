package FreeWay.Entities;

import java.io.Serializable;

public class PlayerData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public int POS_Y = 659;
	public int POS_X = 100;
	public boolean MOV = false;
	public int SCORE = 0;
	
	public PlayerData(int x) {
		POS_Y = 659;
		POS_X = x;
	}
}
