

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class GameServer extends JFrame{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private int RoomNum=0;
	private JTextField txtPortNumber;
	//private String[][][] AnswerPoint=new String[3][3][7];
	private HashMap<String,Coordinate> AnswerPoint= new HashMap<String,Coordinate>();
	private HashMap<String,GameImage> gameimages = new HashMap<String,GameImage>();
	private Vector <Room>RoomList = new Vector<Room>();
	private ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private static final int BUF_LEN = 128; 
	private Vector<UserScore> UserScores=new Vector<UserScore>();
	
	public void init_images() {
		gameimages.put("영화",new GameImage("영화"));
		gameimages.put("그림", new GameImage("그림"));
	}
	public void init_coor() {
		try {
			Scanner scanner = new Scanner(new File("./정답좌표.txt"));
			int n=0;
			Coordinate c =new Coordinate();
			while(scanner.hasNext())
			{
				
				String str=scanner.nextLine();
				c.Point[n]=str.split("/");
				n++;
			}
			AnswerPoint.put("영화",c);
			scanner = new Scanner(new File("./그림정답좌표.txt"));
			n=0;
			c =new Coordinate();
			while(scanner.hasNext())
			{
				String str=scanner.nextLine();
				c.Point[n]=str.split("/");
				n++;
			}
			AnswerPoint.put("그림",c);
			
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
	}
	public void init_score() throws FileNotFoundException {
		Scanner sc=new Scanner(new File("./Score.txt"));
		int n=0;
		while(sc.hasNext())
		{
			
			String data=sc.nextLine();
			UserScore user=new UserScore(data.split(",")[0]);
			user.win=Integer.parseInt(data.split(",")[1]);
			user.lose=Integer.parseInt(data.split(",")[2]);
			UserScores.add(user);
			n++;
		}
		
		
	}
	public GameServer() throws FileNotFoundException {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		
		
		
		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			
			}
		});
		btnServerStart.setBounds(12, 356, 150, 35);
		JButton savebtn=new JButton("save");
		savebtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 try {
					 FileWriter output = new FileWriter("./Score.txt");
					for(int i=0;i<UserScores.size();i++)
					{
						String data=UserScores.get(i).UserName+","+UserScores.get(i).win+","+UserScores.get(i).lose;
						output.write(data+"\n");
						AppendText("save :"+data);
					}
					output.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		savebtn.setBounds(180, 356, 100, 35);
		contentPane.add(savebtn);
		contentPane.add(btnServerStart);
		
		init_images();
		init_coor();
		init_score();
		
			
		
	}
	
	
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
					AppendText("새로운 참가자 from " + client_socket);
					// User 당 하나씩 Thread 생성
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // 새로운 참가자 배열에 추가
					new_user.start(); // 만든 객체의 스레드 실행
					AppendText("현재 참가자 수 " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
				
			}
			
		}
	}
	public void AppendText(String str) {
		// textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(GMsg msg) {
		// textArea.append("사용자로부터 들어온 object : " + str+"\n");
		textArea.append("code = " + msg.getCode() + "\n");
		textArea.append("Name = " + msg.getUserName() + "\n");
		
		textArea.setCaretPosition(textArea.getText().length());
	}
	public void AppendRoom() {
		UserService user;
		for(Room r:RoomList)
		{
			textArea.append("****************************\n");
			textArea.append("roomname: "+r.roomname+", Theme: "+r.theme+"\n");
			textArea.append("users: ");
			for(int i=0;i<r.Users.size();i++)
			{
				user=(UserService)r.Users.get(i);
				textArea.append(user.UserName+",");
			}
			textArea.append("\n");
			textArea.append("****************************\n");
		
			
		}
	}
class UserService extends Thread{
		

		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		private StringBuffer listString=new StringBuffer();
		private StringBuffer userlistString = new StringBuffer();
		private Socket client_socket;
		private Vector user_vc;
		private Room curRoom;
		private String RoomName = "";
		private String RoomTheme= "";
		public String UserName = "";
		private int AnsN;
		private int WrongN;
		
		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// 매개변수로 넘어온 자료 저장
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			try {
				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());

			} catch (Exception e) {
				AppendText("userService error");
			}
			
		}
		
		// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public synchronized void WriteAllObject(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				
				UserService user = (UserService) user_vc.elementAt(i);
				if(user.curRoom==null)
					user.WriteOneObject(ob);
			}
		}
		public synchronized void WriteRoom(Object ob) {
			for(int i=0;i<curRoom.Users.size();i++)
			{
				UserService user=(UserService)curRoom.Users.elementAt(i);
				user.WriteOneObject(ob);
			}
		}
		public synchronized void WriteRoomOther(Object ob) {
			for(int i=0;i<curRoom.Users.size();i++)
			{
				
				UserService user=(UserService)curRoom.Users.elementAt(i);
				if(user!=this)
					user.WriteOneObject(ob);
			}
		}
		public synchronized void WriteOneObject(Object ob) {
			try {
			    oos.writeObject(ob);
			} 
			catch (IOException e) {
				AppendText("oos.writeObject(ob) error");		
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();
			}
		}
		public synchronized void WriteOthers(Object ob) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				if (user != this &&user.curRoom==null)
					user.WriteOneObject(ob);
			}
		}
		public synchronized void StringUserList()
		{
			userlistString.setLength(0);
			for (int i = 0; i < user_vc.size(); i++) {
				UserService user = (UserService) user_vc.elementAt(i);
				userlistString.append(user.UserName+"/");
			}
			
		}
		
		public synchronized  void Login() {
			AppendText("새로운 참가자 " + UserName + " 입장.");
			
			GMsg sendlist=new GMsg("10","server");
			StringRoomList(RoomList);
			StringUserList();
			sendlist.roomlist=listString.toString();
			sendlist.userlist=userlistString.toString();
			AppendText("roomlist :"+sendlist.roomlist);
			AppendText("userlist :"+sendlist.userlist);
			sendlist.roomnum=RoomList.size();
			WriteOneObject(sendlist);
			sendlist.code="50";
			WriteOthers(sendlist);
		}
		public synchronized void Logout() {
			String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
			UserVec.removeElement(this);// Logout한 현재 객체를 벡터에서 지운다
			if(curRoom!=null)
				QuitRoom();
			StringUserList();
			GMsg sendlist=new GMsg("50","server");
			sendlist.userlist=userlistString.toString();
			WriteAllObject(sendlist);
			AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
		}
		
		public synchronized void QuitRoom() {
			
			Iterator<UserService> useriter;
			
			useriter=curRoom.Users.iterator();
			while(useriter.hasNext())
			{
				UserService user=useriter.next();
				if(user.UserName.equals(this.UserName))
				{
					useriter.remove();
				}
						
			}	
			if(curRoom.Users.size()==1)
			{
				GMsg ob= new GMsg("320","server");
				WriteRoomOther(ob);
				ob.code="65";
				ob.roomname=curRoom.roomname;
				WriteAllObject(ob);
			}
			else if(curRoom.Users.size()==0)
			{
				RoomList.remove(curRoom);
				GMsg ob= new GMsg("30","server");
				StringRoomList(RoomList);
				ob.roomlist=listString.toString();
				ob.roomnum=RoomList.size();
				WriteAllObject(ob);
			}
			
			this.curRoom=null;
			
		}
		public synchronized void CreateRoom(GMsg ob) {
			for(Room r:RoomList) {
				if(r.roomname.equals(ob.roomname))
				{
					ob.code="150";
					return ;
				}
			}
			Room z=new Room(ob.roomname,ob.theme);
			RoomList.add(z);
			
		}
		public synchronized void StringRoomList(Vector<Room> r)
		{
			listString.setLength(0);
			for(Room room : r)
			{
				listString.append(room.roomname+","+room.theme+","+room.Users.size()+","+room.status+"/");
			}
			
		}
		
		public synchronized String Enterroom(String name)
		{
			
			for(Room r:RoomList)
			{
				if(r.roomname.equals(name))
				{
					
					if(r.Users.size()==2)
					{
						return "210";
					}
					else
					{
						curRoom= r;
						RoomName=r.roomname;
						RoomTheme=r.theme;
						r.Users.add(this);
						
						return "200";
					}
				}
			}
			return "error";
		}
		public synchronized void  Checkpoint(GMsg ob) {
			int x,y;
			String[][] APoint;
			APoint=AnswerPoint.get(curRoom.theme).Point;
			for(int i=0;i<APoint[curRoom.stage].length;i++)
			{
				x=Integer.parseInt(APoint[curRoom.stage][i].split(",")[0]);
				y=Integer.parseInt(APoint[curRoom.stage][i].split(",")[1]);
				if(ob.x>x-15 && ob.x<x+15 &&ob.y>y-15&&ob.y<y+15)
				{
					for(int z=0;z<curRoom.Point.size();z++)
					{
						if(x==Integer.parseInt(curRoom.Point.get(z).split(",")[0])&&y==Integer.parseInt(curRoom.Point.get(z).split(",")[1]))
						{	
							WrongN++;
							ob.code="550";
							return;
						}
					}
					curRoom.Point.add(APoint[curRoom.stage][i]);
					ob.x=x;
					ob.y=y;
					AnsN++;
					WrongN=0;
					ob.AnsN=AnsN;
					WriteOneObject(ob);
					ob.code="510";
					WriteRoomOther(ob);
					if(AnsN==4&&curRoom.stage!=2)
					{
						GMsg ob1 = new GMsg("450","server");
						ob1.img[0]=new ImageIcon("nextstage.png");
						
						curRoom.stagewinner.add(curRoom.stage,UserName);
						curRoom.Point.clear();
						curRoom.stage++;
						WriteRoom(ob1);
						
					}
					else if(AnsN==4 &&curRoom.stage==2) {
						curRoom.stagewinner.add(curRoom.stage,UserName);
						int n=0;
						GMsg ob1= new GMsg("45","server");
						curRoom.status=false;
						ob1.roomname=curRoom.roomname;
						WriteAllObject(ob1);
						ob1.code="600";
						for(int q=0;q<curRoom.stagewinner.size();q++)
						{
							
							if(curRoom.stagewinner.get(q).equals(UserName))
								n++;
							
						}
						if(n>=2)
						{
							curRoom.winner=this.UserName;
							for(int z=0;z<curRoom.Users.size();z++)
							{
								
								UserService user=(UserService)curRoom.Users.elementAt(z);
								if(user!=this)
									curRoom.loser=user.UserName;
							}
							ob1.img[0]=new ImageIcon("win.png");
							WriteOneObject(ob1);
							ob1.img[0]=new ImageIcon("lose.png");
							WriteRoomOther(ob1);
						}
						else if(n<2)
						{
							curRoom.loser=this.UserName;
							for(int z=0;z<curRoom.Users.size();z++)
							{
								
								UserService user=(UserService)curRoom.Users.elementAt(z);
								if(user!=this)
									curRoom.winner=user.UserName;
							}
							ob1.img[0]=new ImageIcon("lose.png");
							WriteOneObject(ob1);
						    ob1.img[0]=new ImageIcon("win.png");
						    WriteRoomOther(ob1);
						}
						SaveScore(curRoom.winner,curRoom.loser);
						init_Roomdata();
						
					}
					return;
				}
			}
			WrongN++;
			ob.code="550";
			WriteOneObject(ob);
			return;
		}
		public void init_Roomdata() {
			curRoom.Point.clear();
			curRoom.stage=0;
			curRoom.stagewinner.clear();
			curRoom.winner=null;
			curRoom.loser=null;
		}
		public void SaveScore(String winner,String loser) {
			
			int a=0,b=0;
			for(int i=0;i<UserScores.size();i++)
			{
				
				if(UserScores.get(i).UserName==winner)
				{
					UserScores.get(i).win++;
					a++;
				}
				if(UserScores.get(i).UserName==loser)
				{
					UserScores.get(i).lose++;
					b++;
				}
			}
			
			if(a==0)
			{
				UserScore winuser=new UserScore(winner);
				winuser.win++;
				UserScores.add(winuser);
			}
			if(b==0)
			{
				UserScore loseuser=new UserScore(loser);
				loseuser.lose++;
				UserScores.add(loseuser);
			}
			
		}
		public void Get_Score(GMsg ob)
		{
			for(int i=0;i<UserScores.size();i++)
			{
				if(UserScores.get(i).UserName.equals(ob.UserName))
				{
					ob.win=UserScores.get(i).win;
					ob.lose=UserScores.get(i).lose;
					WriteOneObject(ob);
					return;
				}
			}
			ob.win=0;
			ob.lose=0;
			WriteOneObject(ob);
		}
		public synchronized void Hint() {
			Random random =new Random();
			String[][] APoint;
			APoint=AnswerPoint.get(curRoom.theme).Point;
			
			while(true) {
				int i;
				int n=random.nextInt(APoint[curRoom.stage].length-1);
				for(i=0;i<curRoom.Point.size();i++)
				{
					if(curRoom.Point.get(i).equals(APoint[curRoom.stage][n]))
						break;
				}
				if(i==curRoom.Point.size())
				{
					GMsg ob=new GMsg("1000","server");
					ob.x=Integer.parseInt(APoint[curRoom.stage][n].split(",")[0]);
					ob.y=Integer.parseInt(APoint[curRoom.stage][n].split(",")[1]);
					WriteOneObject(ob);
					break;
				}
				
			}
			
		}
		public synchronized void CheckWrongN() {
			if(this.WrongN>=5) {
				GMsg ob = new GMsg("700","server");
				WriteOneObject(ob);
				WrongN=0;
			}
		}
		public synchronized void WriteName(GMsg ob) {
			for(int i=0;i<user_vc.size();i++) {
				UserService user=(GameServer.UserService) user_vc.get(i);
				if(user.UserName.equals(ob.UserName))
				{
					
					ob.UserName=this.UserName;
					user.WriteOneObject(ob);
					break;
				}
			}
		}
		public void run() {
			while(true) {
				try {
					Object obcm = null;
					String msg = null;
					GMsg cm = null;
					
					if (socket == null)
						break;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (obcm == null)
						break;
					if (obcm instanceof GMsg) {
						cm = (GMsg) obcm;
						AppendObject(cm);
					} else
						continue;
					if(cm.getCode().equals("1"))
					{
						int n=0;
						for(int i=0;i<user_vc.size();i++)
						{
							UserService user=(GameServer.UserService) user_vc.get(i);
							if(user.UserName.equals(cm.UserName))
							{
								cm.code="-1";
								WriteOneObject(cm);
								n++;
								break;
							}
						}
						if(n==0)
						{	
							cm.code="5";
							WriteOneObject(cm);
						}
							
					}
					else if(cm.getCode().equals("10"))
					{
						this.UserName=cm.getUserName();
						Login();
					}
					else if(cm.getCode().equals("15"))
					{
						
						StringRoomList(RoomList);
						StringUserList();
						cm.roomlist=listString.toString();
						cm.userlist=userlistString.toString();
						AppendText("roomlist :"+cm.roomlist);
						AppendText("userlist :"+cm.userlist);
						cm.roomnum=RoomList.size();
						WriteOneObject(cm);
					}
					else if(cm.getCode().equals("100"))
					{
						CreateRoom(cm);
						cm.UserName="server";
						if(cm.code=="150")
						{
							
							WriteOneObject(cm);
							continue;
						}
						else
						{
							
							cm.code=Enterroom(cm.roomname);
							if(cm.code=="error")
								AppendText("room enter error");
							cm.img[0]=new ImageIcon("틀린그림찾기로고.png");
							AppendRoom();
							WriteOneObject(cm);
							cm.code="30";
							cm.roomnum=RoomList.size();
							StringRoomList(RoomList);
							cm.roomlist=listString.toString();
							WriteOthers(cm);
						}
					}
					else if(cm.getCode().equals("200"))
					{
						cm.code=Enterroom(cm.roomname);
						if(cm.code=="error")
							AppendText("room enter error");
						cm.UserName="server";
						cm.img[0]=new ImageIcon("틀린그림찾기로고.png");
						AppendRoom();
						WriteOneObject(cm);
					}
					else if(cm.getCode().equals("300"))
					{
						
						for(int i=0;i<curRoom.Users.size();i++)
						{
							UserService userob=(UserService)curRoom.Users.elementAt(i);
							cm.roomuserlist[i]=userob.UserName;
						}
						cm.UserName="server";
						WriteRoom(cm);
						
						if(curRoom!=null && curRoom.Users.size()==2)
						{
							GMsg ob= new GMsg("310","server");
							WriteRoom(ob);
							ob.code="60";
							ob.roomname=curRoom.roomname;
							WriteAllObject(ob);
						}
					}
					
					else if(cm.getCode().equals("350"))
					{
						
						QuitRoom();
						AppendRoom();
						WriteOneObject(cm);
						
					}
					else if(cm.getCode().equals("400"))
					{
						cm.UserName="server";
						WriteRoom(cm);
						curRoom.status=true;
						cm.roomname=curRoom.roomname;
						cm.code="40";
						WriteAllObject(cm);
					}
					else if(cm.getCode().equals("410"))
					{
						AnsN=0;
						WrongN=0;
						cm.UserName="server";
						cm.stage=curRoom.stage;
						cm.img[0]=gameimages.get(curRoom.theme).image[curRoom.stage][0];
						cm.img[1]=gameimages.get(curRoom.theme).image[curRoom.stage][1];
						WriteOneObject(cm);
					}
					else if(cm.getCode().equals("500"))
					{
						Checkpoint(cm);
						CheckWrongN();
					}
					else if(cm.getCode().equals("800"))
					{
						WriteRoomOther(cm);
					}
					else if(cm.getCode().equals("1000"))
					{
						Hint();
					}
					else if(cm.getCode().equals("1500"))
					{
						Get_Score(cm);
					}
					else if(cm.getCode().equals("1700")) 
					{
						for(int i=0;i<user_vc.size();i++) 
						{
							UserService user=(GameServer.UserService) user_vc.get(i);
							if(user.UserName.equals(cm.UserName))
							{
								if(user.curRoom!=null)
								{
									cm.code="1750";
								}
								WriteOneObject(cm);
								break;
							}
						}
					}
					else if(cm.getCode().equals("1800"))
					{
						CreateRoom(cm);
						if(cm.code=="150")
						{
							
							WriteOneObject(cm);
							continue;
						}
						else
						{
							
							cm.code=Enterroom(cm.roomname);
							if(cm.code=="error")
								AppendText("room enter error");
							cm.img[0]=new ImageIcon("틀린그림찾기로고.png");
							AppendRoom();
							WriteOneObject(cm);
							cm.code="1800";
							WriteName(cm);
							cm.code="30";
							cm.roomnum=RoomList.size();
							StringRoomList(RoomList);
							cm.roomlist=listString.toString();
							WriteOthers(cm);
							
						}
						
					}
				}catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						client_socket.close();
						Logout(); // 에러가난 현재 객체를 벡터에서 지운다
						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝
			} // while
		} // run
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameServer frame = new GameServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}


