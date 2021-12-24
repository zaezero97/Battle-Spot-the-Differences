package 틀린그림찾기;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;

public class Lobby extends JFrame {
	private static final long serialVersionUID = 1L;
	private String UserName;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	public Lobby(String username, String ip_addr, String port_no)
	{
		setVisible(true);

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			GMsg obcm = new GMsg("100", username);
			SendObject(obcm);
			ListenNetwork net = new ListenNetwork();
			net.start();
		}
		catch(NumberFormatException | IOException e){
			e.printStackTrace();
			System.out.println("connect error");
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	class ListenNetwork extends Thread{
		public void run() {
			while(true) {
				try {
					Object obcm = null;
					String msg = null;
					GMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof GMsg) {
						cm = (GMsg) obcm;
						
					} else
						continue;
				
				}catch(IOException e){
					try {
						ois.close();
						oos.close();
						socket.close();
						break;
					} catch (Exception ee) {
						break;
					}
				}
			}
			
		}
	}
	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			
			oos.writeObject(ob);
			
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
			System.out.println("error");
		}
	}
}
