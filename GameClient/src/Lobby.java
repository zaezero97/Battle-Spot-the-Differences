import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class Lobby extends JFrame {
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollpane;
	private JTextPane userlistpane;
	private JPanel contentpane;
	private String UserName;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JLabel toplabel;
	private JPanel midPanel;
	private JPanel rightPanel;
	private JButton roomcreateBtn; 
	private JPanel UserListPanel;
	private boolean flag;
	private String[] themes = {"��ȭ","�׸�"};
	createDialog createdialog;
	public class createDialog extends JDialog{
		private JLabel roomname = new JLabel("�� �̸�");
		private JTextField roomnametxt=new JTextField(10);
		private JLabel theme = new JLabel("���� �׸�");
		private JComboBox<String> themebox = new JComboBox<String>(themes);
		private JButton okbtn = new JButton("�����");
		private JPanel panel= new JPanel();
		public String onetooneuser;
		public createDialog(JFrame frame,String title,int n){
			super(frame,title);
			setLayout(null);
			panel.setLayout(null);
			panel.setBounds(10,10, 370,450);
			panel.setBorder(new TitledBorder("�� �����"));
			roomname.setBounds(25, 50, 60, 25);
			roomnametxt.setBounds(95, 50, 150, 30);
			theme.setBounds(25, 100, 60, 25);
			themebox.setBounds(95, 100, 150,30);
			okbtn.setBounds(200, 350, 100, 50);
			panel.add(roomname);
			panel.add(roomnametxt);
			panel.add(theme);
			panel.add(themebox);
			panel.add(okbtn);
			add(panel);
			okbtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					GMsg roomcm;
					if(n==1) 
					{
						roomcm=new GMsg("100",UserName);
					}
					else {
						System.out.println(onetooneuser);
						roomcm=new GMsg("1800",onetooneuser);
					}
					roomcm.roomname=roomnametxt.getText();
					roomcm.theme=themebox.getSelectedItem().toString();
					SendObject(roomcm);
					setVisible(false);
			}
					
				
			
			});
		}
		
		
	}

	public class EnterListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			GMsg EnterMsg= new GMsg("200",UserName);
			
			JButton a=(JButton)e.getSource();
			RoomPanel b=(RoomPanel) a.getParent();
			
			EnterMsg.roomname=b.Roomname;
			SendObject(EnterMsg);
		}
		
	}
	public class createListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			createdialog.setVisible(true);
			createdialog.setBounds(350, 200, 400, 500);
			
		}
		
	}
	
	public Lobby() { //���ӳ����� �ٽ� �κ�������� ������ �ʿ���� ������ �����ڸ� ���� ����
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//ȭ�� ����
		contentpane=new JPanel();
		contentpane.setBackground(new Color(189, 183, 107));
		midPanel=new JPanel();
		midPanel.setBackground(new Color(189, 183, 107));
		toplabel=new JLabel("Ʋ�� �׸� ã�� �κ�");
		toplabel.setFont(new Font("MD��Ʈü",Font.BOLD,30));
		toplabel.setHorizontalAlignment(JLabel.CENTER);
		toplabel.setBorder(new EtchedBorder());
		rightPanel = new JPanel();
		rightPanel.setLocation(719, 70);
		createdialog = new createDialog(this,"�� �����ϱ�",1);
		contentpane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentpane.setLayout(null);
		midPanel.setLayout(null);
		
		midPanel.setBorder(new TitledBorder(null, "�� ���", 0, 0, new Font("���ʷչ���",Font.BOLD,30)));
		contentpane.add(toplabel);
		contentpane.add(midPanel);
		contentpane.add(rightPanel);
		
		roomcreateBtn=new JButton("�� �����");
		//userlistpane= new JTextPane();
		//userlistpane.setBackground(new Color(248, 248, 255));
		//userlistpane.setFont(new Font("����ü", Font.PLAIN,14));
		UserListPanel=new JPanel();
		UserListPanel.setLayout(null);
		scrollpane= new JScrollPane(UserListPanel);
	
		
		toplabel.setBounds(94, 10, 880, 50);
		midPanel.setBounds(12,60,700,600);
		
		rightPanel.setLayout(null);
		rightPanel.setSize(255, 583);
		rightPanel.setBackground(new Color(189, 183, 107));
		rightPanel.add(scrollpane);
		rightPanel.add(roomcreateBtn);
		scrollpane.setBounds(12, 10, 231, 278);
		roomcreateBtn.setBounds(78, 298, 100, 50);
		
		JButton btnNewButton = new JButton("������");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
			}
		});
		btnNewButton.setBounds(78, 371, 100, 50);
		rightPanel.add(btnNewButton);
		
		
		
		roomcreateBtn.addActionListener(new createListener());
		
		setContentPane(contentpane);
		setBounds(350, 100, 1000, 700);
	}
	
		
	
	public Lobby(String username, ObjectInputStream ois, ObjectOutputStream oos) {
		// TODO Auto-generated constructor stub
		
			
			this();
			flag=true;
			UserName=username;
			this.ois=ois;
			this.oos=oos;
			
			
			ListenNetwork net = new ListenNetwork();
			net.start();
		
	}
	public Lobby(String username, ObjectInputStream ois, ObjectOutputStream oos,int a) {
		// TODO Auto-generated constructor stub
		
			
			this();
			flag=true;
			UserName=username;
			this.ois=ois;
			this.oos=oos;
			
			
			
			ListenNetwork net = new ListenNetwork();
			net.start();
			GMsg ob=  new GMsg("15",UserName);
			SendObject(ob);
			
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public synchronized void roomview(GMsg ob) {
		midPanel.removeAll();
		int j=0;
		String r,rname=null,rtheme = null;
		int ruserN;
		boolean rstatus;
		if(ob.roomnum!=0)
		{
			for(int i=0;i<ob.roomnum;i++)
			{
				r=ob.roomlist.split("/")[i];
				rname=r.split(",")[0];
				rtheme=r.split(",")[1];
				ruserN=Integer.parseInt(r.split(",")[2]);
				rstatus=Boolean.parseBoolean(r.split(",")[3]);
				RoomPanel roompanel = new RoomPanel(rname,rtheme,ruserN,rstatus);
				roompanel.Enterbtn.addActionListener(new EnterListener());
				if(i%2==0&&i!=0)
				{
					j++;
				}
				
				midPanel.add(roompanel);
				roompanel.setBounds(15+(280*(i%2)), 45+(130*j), 270, 120);
				
			}
		}
		
		repaint();
		revalidate();
	}
	public synchronized void UserlistView(GMsg g) {
		String ulist[]=g.userlist.split("/");
		UserListPanel.removeAll();
		
		for(int i=0;i<ulist.length;i++)
		{
			
			JLabel userlabel=new JLabel(ulist[i]);
			userlabel.setFont(new Font("MD��Ʈü",Font.BOLD,15));
			userlabel.setBounds(0, 35*i, 230, 30);
			userlabel.setHorizontalAlignment(JLabel.LEFT);
			userlabel.addMouseListener(new MouseAdapter(){

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					if(e.getClickCount()==2)
					{
						GMsg ob=new GMsg("1500",userlabel.getText());
						SendObject(ob);
					}
					if(e.getButton()==3&&!userlabel.getText().equals(UserName))
					{
						
						int result=JOptionPane.showConfirmDialog(null, userlabel.getText()+"�Կ��� 1��1 ��û�� �Ͻðڽ��ϱ�? ","1��1 ��û", JOptionPane.YES_NO_OPTION);
						if(result==JOptionPane.YES_OPTION)
						{
							GMsg ob= new GMsg("1700",userlabel.getText());
							SendObject(ob);
						}
					}
				}
				
			});
			UserListPanel.add(userlabel);
		}
		repaint();
		revalidate();
	}
	
	
	public void erroraddroom() {
		
		JOptionPane.showMessageDialog(null, "�Ȱ��� �̸��� ���� �����Ͽ� ���� ������ �� �����ϴ�", "�� ���� �Ұ�", JOptionPane.ERROR_MESSAGE);

		
	}
	public void showDialog(String name) {
		createdialog=new createDialog(this,"�� �����ϱ�",2);
		createdialog.onetooneuser=name;
		createdialog.setVisible(true);
		createdialog.setBounds(350, 200, 400, 500);
	}
	public void EnterRoom(ImageIcon img) {
		flag=false;
		setVisible(false);
		new GameView(UserName,ois,oos,img);
	}
	public void roomchangestatus(GMsg ob) {
		System.out.println(ob.code);
		Component[] a=midPanel.getComponents();
		for(int i=0;i<a.length;i++)
		{
			RoomPanel r=(RoomPanel)a[i];
			System.out.println(r.Roomname);
			if(r.Roomname.equals(ob.roomname))
			{
				if(ob.code.equals("40"))
				{
					r.status=true;
					r.Enterbtn.setEnabled(false);
				}
				else if(ob.code.equals("45"))
				{
					r.Enterbtn.setEnabled(true);
					r.status=false;
				}
				else if(ob.code.equals("60"))
				{
					r.userN++;
				}
				else if(ob.code.equals("65"))
				{
					r.userN--;
				}
			}
			r.ChangeRoom();
		}
		
		repaint();
		revalidate();
	}
	class ListenNetwork extends Thread{
		public void run() {
			while(flag) {
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
						System.out.println(cm.code);
					} else
						continue;
					switch(cm.getCode())
					{
						case "10": //ó�� �α���
							roomview(cm);
							UserlistView(cm);
							break;
						case "15": // ����ٰ� �ٽ� �κ� ����
							roomview(cm);
							UserlistView(cm);
							break;
						case "30": //�� ����Ʈ�� ��ȭ�� �����
							roomview(cm);
							break;
						case "60":
							roomchangestatus(cm);
							break;
						case "65":
							roomchangestatus(cm);
							break;
						case "40":
							roomchangestatus(cm);
							break;
						case "45":
							roomchangestatus(cm);
							break;
						case "50": //��������Ʈ�� ��ȭ�� �����
							UserlistView(cm);
							break;
						case "150":
							erroraddroom();
							break;
						case "200":	
							EnterRoom(cm.img[0]);
							break;
						case "210":
							JOptionPane.showMessageDialog(null, "�ο��� �� ���� �濡 ���� �� �����ϴ�.!", "�� ���� �Ұ�", JOptionPane.ERROR_MESSAGE);
							break;
						case "1500":
							JOptionPane.showMessageDialog(null, cm.UserName+"���� ������ : "+"��:"+cm.win+"   ��:"+cm.lose, "����", JOptionPane.PLAIN_MESSAGE);
							break;
						case "1700":
							showDialog(cm.UserName);
							break;
						case "1750":
							JOptionPane.showMessageDialog(null, "���� ���� �濡 �� �ֽ��ϴ�", "1��1 �Ұ�", JOptionPane.ERROR_MESSAGE);
							break;
						case "1800":
							int result=JOptionPane.showConfirmDialog(null, cm.UserName+"�Կ��� 1��1 ��û�� �Խ��ϴ�. ����? ","1��1 ��û", JOptionPane.YES_NO_OPTION);
							if(result==JOptionPane.YES_OPTION)
							{
								cm.code="200";
								SendObject(cm);
							}
							break;
					}
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

	public synchronized void SendObject(Object ob) { // ������ �޼����� ������ �޼ҵ�
		try {
			
			oos.writeObject(ob);
			
		} catch (IOException e) {
			
			System.out.println("error");
		}
	}
}
