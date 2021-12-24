package 틀린그림찾기;

import java.util.Vector;

public class Room {
	private int roomid;
	private int gametheme;
	private Vector Users = new Vector();
	
	Room(int roomid,int gametheme,String user)
	{
		this.roomid=roomid;
		this.gametheme=gametheme;
		Users.add(user);
	}
}
