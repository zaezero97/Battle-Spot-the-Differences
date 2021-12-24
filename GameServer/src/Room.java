import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

public class Room implements Serializable {
	public String roomname;
	public String theme;
	public Vector<GameServer.UserService> Users = new Vector<GameServer.UserService>();
	public int stage;
	public boolean status;
	public ArrayList<String> stagewinner=new ArrayList<String>();
	public String winner,loser;
	public Vector<String> Point = new Vector<String>();
	Room(String name,String theme)
	{
		this.stage=0;
		this.roomname=name;
		this.theme=theme;
		this.status=false;
	}
	
	
}
