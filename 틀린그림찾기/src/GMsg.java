package 틀린그림찾기;

import java.io.Serializable;

public class GMsg implements Serializable{
	
	    public String code; // 10:로그인
	    public String UserName;
	    

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
