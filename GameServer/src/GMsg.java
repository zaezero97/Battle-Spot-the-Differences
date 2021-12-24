
import java.io.Serializable;

import javax.swing.ImageIcon;

public class GMsg implements Serializable{
		private static final long serialVersionUID = 1L;
	    public String code; // 10:·Î±×ÀÎ
	    public String UserName;
	    public String roomname;
	    public String theme;
	    public String roomlist;
	    public String userlist;
	    public int roomnum;
	    public String[] roomuserlist=new String[2]; 
	    public ImageIcon[] img=new ImageIcon[2];
	    public int x,y;
	    public int AnsN;
	    public int stage;
	    public int win,lose;
	    public int getRoomnum() {
			return roomnum;
		}

	    public String getRoomname() {
			return roomname;
		}


	

		public String getRoomlist() {
			return roomlist;
		}

		public String getTheme() {
			return theme;
		}


		public GMsg(String code, String UserName) {
	        this.code = code;
	        this.UserName = UserName;
	        
	    }


		public String getCode() {
			return code;
		}

		public String getUserName() {
			return UserName;
		}

}
