package FreeWay.Entities;

import java.io.Serializable;

public class PlayerData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public int POS_Y = 659;
	public int POS_X = 100;
	public boolean MOV = false;
	
	public PlayerData() {
		POS_Y = 659;
		POS_X = 400;
	}
}
