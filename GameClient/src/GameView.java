import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class GameView extends JFrame {
	private static final long serialVersionUID = 1L;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private boolean flag;
	private JPanel gameviewPanel;
	private PictureleftPanel pictureleft;
	private PicturerightPanel pictureright;
	private JPanel bottompanel;
	private JPanel contentpane;
	private JLabel user1label,user2label;
	private ArrayList <answerLabel> answer1 = new ArrayList<answerLabel>();
	private ArrayList <answerLabel> answer2 = new ArrayList<answerLabel>();
	private answerLabel answer1_1,answer1_2,answer1_3,answer1_4;
	private answerLabel answer2_1,answer2_2,answer2_3,answer2_4;
	private JButton gamestartbtn,quitroombtn;
	private String Username;
	private ImageIcon beforeimg;
	private JPanel beforepanel;
	private Timer timer;
	private JLabel timerLabel;
	private Graphics2D gc,gc2,gc3;
	private Graphics2D leftgc,rightgc;
	private Graphics2D Lg1,Lg2;
	private int stage;
	private Image leftimage,rightimage;
	private Image bottompanelimage;
	private JLabel HintLabel;
	JButton Hintbtn;
	private GameMouseListener M;
	private boolean Lockflag=false;
	private Image Lockleftimage,Lockrightimage;
	private boolean Itemflag;
	private JButton Itembtn;
	LockThread lockthread=null;
	ItemThread itemthread=null;
	private boolean Hintflag=false;
	HintThread hint;
	public void loadSound(String path) { 

		try {
			File audioFile = new File(path); // 오디오 파일의 경로명
			final AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile); // 오디오 파일로부터
			Clip sound;
			sound = AudioSystem.getClip(); // 비어있는 오디오 클립 만들기
			sound.addLineListener(new LineListener() {
				public void update(LineEvent e) {
					if (e.getType() == LineEvent.Type.STOP) { // clip.stop()이 호출되거나 재생이 끝났을 때
						try {
							audioStream.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			sound.open(audioStream); // 재생할 오디오 스트림 열기
			sound.start(); // 재생 시작
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	class PictureleftPanel extends JPanel{
		
		@Override
		public void paint(Graphics g) {
			// TODO Auto-generated method stub\
			super.paint(g);
			g.drawImage(leftimage, 0, 0, this.getWidth(), this.getHeight(), this);
			if(Itemflag==true)
				g.drawImage(new ImageIcon("먹물1.png").getImage(), 0, 50,500, 500, this);
			if(Lockflag==true)
				g.drawImage(new ImageIcon("gamelock.png").getImage(), 50, 50, 450,500, this);
			if(Hintflag==true)
				g.drawImage(new ImageIcon("circle.png").getImage(),hint.x-15,hint.y-15,40,40,this);
			
		}
		
		
	}
	class PicturerightPanel extends JPanel{
		
		public void paint(Graphics g) {
			// TODO Auto-generated method stub
			super.paint(g);
				g.drawImage(rightimage, 0, 0, this.getWidth(), this.getHeight(), this);
			if(Itemflag==true)
				g.drawImage(new ImageIcon("먹물1.png").getImage(), 0, 50,500, 500, this);
			if(Lockflag==true)
				g.drawImage(new ImageIcon("gamelock.png").getImage(), 50, 50, 450,500, this);
			if(Hintflag==true)
				g.drawImage(new ImageIcon("circle.png").getImage(),hint.x-15,hint.y-15,40,40,this);
		}
	}
	class answerLabel extends JLabel {
		boolean flag;
		answerLabel()
		{
			flag=false;
		}
		@Override
		public void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			
			if(flag==false)
				g.drawImage(new ImageIcon("오답이미지.png").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
			else
			{
				g.drawImage(new ImageIcon("정답이미지.png").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
			}
			
		}
		
		
	}
	class QuitListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			GMsg ob= new GMsg("350",Username);
			SendObject(ob);
			
		}
		
	}
	
	class gamestartListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
		
			GMsg ob=new GMsg("400",Username);
			SendObject(ob);
		}
		
	}
	class GameMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
				//System.out.println(e.getX()+","+e.getY());
				GMsg ob= new GMsg("500", Username);
				ob.x=e.getX();
				ob.y=e.getY();
				SendObject(ob);
		}

	}
	
	class HintListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			GMsg ob= new GMsg("1000",Username);
			SendObject(ob);
		}
		
	}
	
	public class Timer extends Thread{
		int n;
		Timer(int n){
			this.n=n;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(n>0)
			{
				try {
					timerLabel= new JLabel();
					timerLabel.setIcon(new ImageIcon(n+".png"));
					gameviewPanel.removeAll();
					gameviewPanel.add(timerLabel,BorderLayout.CENTER);
					timerLabel.setHorizontalAlignment(JLabel.CENTER);
					timerLabel.setOpaque(true);
					repaint();
					revalidate();
					Thread.sleep(1500);
					n--;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					return;
				}
			}
			GMsg ob= new GMsg("410", Username);
			SendObject(ob);
		}
		
		
	}
	public class HintThread extends Thread{
		int x,y;

		HintThread(int x, int y)
		{
			this.x=x;
			this.y=y;
			
		}
		public void end() {
			Hintflag=false;
			repaint();
			revalidate();
			this.interrupt();
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true)
			{
				Hintflag=true;
				repaint();
				revalidate();
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					return;
				}
				
				Hintflag=false;
				repaint();
				revalidate();
				break;
			}
		}
	}
	public class LockThread extends Thread{

		
		public  void end() {
			Lockflag=false;
			repaint();
			revalidate();
			this.interrupt();
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
				pictureleft.removeMouseListener(M);
				pictureright.removeMouseListener(M);
				Lockflag=true;
				repaint();
				revalidate();
				/*
				 * Lockleftimage= createImage(pictureleft.getWidth(),pictureleft.getHeight());
				 * Lockrightimage=createImage(pictureright.getWidth(),pictureright.getHeight());
				 * 
				 * Lg1=(Graphics2D) Lockleftimage.getGraphics(); Lg2=(Graphics2D)
				 * Lockrightimage.getGraphics();
				 * 
				 * Lg1.drawImage(leftimage, 0, 0, pictureleft.getWidth(),
				 * pictureleft.getHeight(), null); Lg2.drawImage(rightimage,0, 0,
				 * pictureright.getWidth(), pictureright.getHeight(), null); Lg1.drawImage(new
				 * ImageIcon("gamelock.png").getImage(), 50, 50, 450,500, null);
				 * Lg2.drawImage(new ImageIcon("gamelock.png").getImage(), 50, 50,450, 500,
				 * null); gc.drawImage(Lockleftimage, 0, 0, pictureleft.getWidth(),
				 * pictureleft.getHeight(), null); gc2.drawImage(Lockrightimage, 0, 0,
				 * pictureleft.getWidth(), pictureleft.getHeight(), null);
				 */
				
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				return;
			}
			
		
			Lockflag=false;
			repaint();
			revalidate();
			pictureleft.addMouseListener(M);
			pictureright.addMouseListener(M);
			}
		
		
	}
	
	public class ItemThread extends Thread{
		public  void end() {
			Itemflag=false;
			repaint();
			revalidate();
			this.interrupt();
		}
		@Override
		public void run() {
			Itemflag=true;
		
			repaint();
			revalidate();
			// TODO Auto-generated method stub
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				return;
			}
			
			Itemflag=false;
			repaint();
			revalidate();
		}
		
	}
	public GameView(String name,ObjectInputStream ois,ObjectOutputStream oos,ImageIcon img) {
		setBackground(new Color(189, 183, 107));
		flag = true;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.Username=name;
		this.ois=ois;
		this.oos=oos;
		this.beforeimg=img;
		this.setResizable(false);
		contentpane=new JPanel();
		bottompanel= new JPanel();
		beforepanel=new JPanel(){
			@Override
			public void paint(Graphics g) {
				// TODO Auto-generated method stub
				super.paint(g);
				g.drawImage(beforeimg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		contentpane.setLayout(null);
		bottompanel.setLayout(null);
		gameviewPanel=new JPanel();
		gameviewPanel.setLayout(new BorderLayout());
		gameviewPanel.add(beforepanel,BorderLayout.CENTER);
		gameviewPanel.setBounds(0, 0, 1086, 549);
		gameviewPanel.setBorder(new LineBorder(new Color(189, 183, 107), 2));
	
		contentpane.add(bottompanel);
		
		contentpane.add(gameviewPanel);
		
		contentpane.setBackground(new Color(189, 183, 107));
		
		
		
		user1label=new JLabel();
		user2label=new JLabel();
		user1label.setBorder(new TitledBorder(new LineBorder(Color.black,3)));
		user2label.setBorder(new TitledBorder(new LineBorder(Color.black,3)));
	
		
		bottompanel.setBounds(12,553, 1062, 100);
		user1label.setBounds(10, 10, 160, 80);
		user2label.setBounds(890, 10, 160, 80);
		
		
		bottompanel.add(user1label);
		bottompanel.add(user2label);
		gamestartbtn = new JButton("Game start");
		gamestartbtn.setBounds(471, 10, 105, 23);
		bottompanel.add(gamestartbtn);
		gamestartbtn.setEnabled(false);
		quitroombtn = new JButton("방 나가기");
		quitroombtn.setBounds(471, 43, 105, 23);
		quitroombtn.addActionListener(new QuitListener());
		bottompanel.add(quitroombtn);
		answer1_1 = new answerLabel();
		answer1_1.setBounds(170, 37, 50, 32);
		bottompanel.add(answer1_1);
		
		answer1_2 = new answerLabel();
		answer1_2.setBounds(220, 37, 50, 32);
		bottompanel.add(answer1_2);
		
		answer1_3 = new answerLabel();
		answer1_3.setBounds(270, 37, 50, 32);
		bottompanel.add(answer1_3);
		
		answer1_4 = new answerLabel();
		answer1_4.setBounds(320, 37, 50, 32);
		bottompanel.add(answer1_4);
		
		answer2_1 = new answerLabel();
		answer2_1.setBounds(830, 37, 50, 32);
		bottompanel.add(answer2_1);
		
		answer2_2 = new answerLabel();
		answer2_2.setBounds(780, 37, 50, 32);
		bottompanel.add(answer2_2);
		
		answer2_3 = new answerLabel();
		answer2_3.setBounds(730, 37, 50, 32);
		bottompanel.add(answer2_3);
		
		answer2_4 = new answerLabel();
		answer2_4.setBounds(680, 37, 50, 32);
		bottompanel.add(answer2_4);
		
		contentpane.add(bottompanel);
		answer1.add(answer1_1);
		answer1.add(answer1_2);
		answer1.add(answer1_3);
		answer1.add(answer1_4);
		
		answer2.add(answer2_1);
		answer2.add(answer2_2);
		answer2.add(answer2_3);
		answer2.add(answer2_4);
		
		pictureleft=new PictureleftPanel();
		pictureright=new PicturerightPanel();
		
		bottompanel.setBorder(new BevelBorder(BevelBorder.RAISED));
		bottompanel.setBackground(new Color(189, 183, 107));
		HintLabel= new JLabel();
		 Hintbtn = new JButton() {

			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				g.drawImage(new ImageIcon("힌트.png").getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
			}
			 
		 };
		Hintbtn.setBounds(380, 10, 79, 48);
		Hintbtn.setContentAreaFilled(false);
		Hintbtn.setBorderPainted(false);
		Hintbtn.setFocusPainted(false);
		Hintbtn.addActionListener(new HintListener());
		Hintbtn.setEnabled(false);
		bottompanel.add(Hintbtn);
		
		 Itembtn = new JButton("아이템");
		Itembtn.setBounds(380, 67, 79, 23);
		bottompanel.add(Itembtn);
		Itembtn.setEnabled(false);
		Itembtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Itembtn.setEnabled(false);
				GMsg ob=new GMsg("800",Username);
				SendObject(ob);
			}
			
		});
		M=new GameMouseListener();
		
		
		gamestartbtn.addActionListener(new gamestartListener());
		setVisible(true);
		setBounds(350, 100, 1100, 700);
		setContentPane(contentpane);
		GMsg obcm = new GMsg("300", Username);
		SendObject(obcm);
		ListenNetwork2 net= new ListenNetwork2();
		net.start();
		
	}
	class ListenNetwork2 extends Thread{
		public void run() {
			while(flag)
			{
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
					if(cm.getCode().equals("300"))
					{
						if(cm.roomuserlist[1]==null)
							user1label.setText(cm.roomuserlist[0]);
						else if(cm.roomuserlist[1]!=null)
						{
							for(int i=0;i<cm.roomuserlist.length;i++)
							{
								if(cm.roomuserlist[0].equals(Username))
								{
									user1label.setText(cm.roomuserlist[0]);
									user2label.setText(cm.roomuserlist[1]);
								}
								else
								{
									user1label.setText(cm.roomuserlist[1]);
									user2label.setText(cm.roomuserlist[0]);
								}
							
							}
							
						}
						user1label.setHorizontalAlignment(JLabel.CENTER);
						user2label.setHorizontalAlignment(JLabel.CENTER);
						user1label.setFont(new Font("맑은 고딕",Font.PLAIN,30));
						user2label.setFont(new Font("맑은 고딕",Font.PLAIN,30));
						
					}
					else if(cm.getCode().equals("310"))
					{
						gamestartbtn.setEnabled(true);
					}
					else if(cm.getCode().equals("320"))
					{
						gamestartbtn.setEnabled(false);
						user2label.setText(null);
					}
					else if(cm.getCode().equals("350"))
					{
						flag=false;
						Lobby lobby = new Lobby(Username,ois,oos,3);
						setVisible(false);
					}
					else if(cm.getCode().equals("400"))
					{
						
						gameviewPanel.removeAll();
						gamestartbtn.setEnabled(false);
						quitroombtn.setEnabled(false);
						repaint();
						revalidate();
						
						
						
						Timer timer= new Timer(3);
						timer.start();
						
					}
					else if(cm.getCode().equals("410"))
					{
						if(cm.stage==0)
						{
							Hintbtn.setEnabled(true);
							Itembtn.setEnabled(true);
						}
						gameviewPanel.removeAll();
						if(((double)cm.img[0].getIconWidth()/cm.img[0].getIconHeight())>=1.45)
							gameviewPanel.setLayout(new GridLayout(2,1,0,5));
						else 
							gameviewPanel.setLayout(new GridLayout(1,2,5,0));
						
						for(int i=0;i<answer1.size();i++)
						{
							answer1.get(i).flag=false;
							answer2.get(i).flag=false;
						}
						pictureleft.setBorder(new TitledBorder(new LineBorder(Color.orange,2)) );
						pictureright.setBorder(new TitledBorder(new LineBorder(Color.orange,2)));
						gameviewPanel.add(pictureleft);
						gameviewPanel.add(pictureright);
						repaint();
						revalidate();
						
						leftimage=createImage(pictureleft.getWidth(),pictureleft.getHeight());
						rightimage=createImage(pictureright.getWidth(),pictureright.getHeight());
						gc=(Graphics2D) pictureleft.getGraphics();
						gc2=(Graphics2D) pictureright.getGraphics();
						leftgc=(Graphics2D) leftimage.getGraphics();
						rightgc=(Graphics2D) rightimage.getGraphics();
						leftgc.drawImage(cm.img[0].getImage(), 0, 0,pictureleft.getWidth(),pictureleft.getHeight(),pictureleft);
						rightgc.drawImage(cm.img[1].getImage(),0,0,pictureright.getWidth(),pictureright.getHeight(),pictureright);
						//gc.drawImage(leftimage, 0, 0,pictureleft.getWidth(),pictureleft.getHeight(),pictureleft);
						//gc2.drawImage(rightimage,0,0,pictureright.getWidth(),pictureright.getHeight(),pictureright);
						pictureleft.addMouseListener(M);
						pictureright.addMouseListener(M);
						repaint();
						revalidate();
						
					}
					else if(cm.getCode().equals("500"))
					{
						
						loadSound("정답.wav");
						leftgc.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,0));
						leftgc.setColor(Color.blue);
						leftgc.drawOval(cm.x-20, cm.y-20, 50, 30);
						rightgc.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,0));
						rightgc.setColor(Color.blue);
						rightgc.drawOval(cm.x-20, cm.y-20, 50, 30);
					
						if(hint!=null&&cm.x==hint.x&&cm.y==hint.y)
						{
							hint.end();
						}
						for(int i=0;i<cm.AnsN;i++)
						{
							answer1.get(i).flag=true;
						}
						repaint();
						revalidate();
						
					}
					else if(cm.getCode().equals("550"))
					{
						loadSound("오답.wav");
					}
					else if(cm.getCode().equals("510"))
					{
				
						leftgc.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,0));
						leftgc.setColor(Color.red);
						leftgc.drawOval(cm.x-20, cm.y-20, 50, 30);
						rightgc.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,0));
						rightgc.setColor(Color.red);
						rightgc.drawOval(cm.x-20, cm.y-20, 50, 30);
						/*
						 * if(Lockflag==true) { Lg1.drawImage(leftimage, 0, 0, pictureleft.getWidth(),
						 * pictureleft.getHeight(), null); Lg2.drawImage(rightimage,0, 0,
						 * pictureright.getWidth(), pictureright.getHeight(), null); Lg1.drawImage(new
						 * ImageIcon("gamelock.png").getImage(), 50, 50, 450,500, null);
						 * Lg2.drawImage(new ImageIcon("gamelock.png").getImage(), 50, 50,450, 500,
						 * null); }
						 */
						for(int i=0;i<cm.AnsN;i++)
						{
							answer2.get(i).flag=true;
						}
						repaint();
						revalidate();
					}
				
					else if(cm.getCode().equals("450"))
					{
						if(lockthread!=null&&lockthread.isAlive())
							lockthread.end();
						if(itemthread!=null&&itemthread.isAlive())
							itemthread.end();
						if(hint!=null&&hint.isAlive())
							hint.end();
						pictureleft.removeMouseListener(M);
						pictureright.removeMouseListener(M);
						JLabel nextLabel=new JLabel(cm.img[0]);
						gameviewPanel.removeAll();
						gameviewPanel.setLayout(new BorderLayout());
						gameviewPanel.add(nextLabel,BorderLayout.CENTER);
						repaint();
						revalidate();
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Timer timer2=new Timer(3);
						timer2.start();
					}
					else if(cm.getCode().equals("600"))
					{
						if(lockthread!=null&&lockthread.isAlive())
							lockthread.end();
						if(itemthread!=null&&itemthread.isAlive())
							itemthread.end();
						if(hint!=null&&hint.isAlive())
							hint.end();
						JLabel endLabel=new JLabel(cm.img[0]);
						gameviewPanel.removeAll();
						gameviewPanel.setLayout(new BorderLayout());
						gameviewPanel.add(endLabel,BorderLayout.CENTER);
						gamestartbtn.setEnabled(true);
						quitroombtn.setEnabled(true);
						for(int i=0;i<answer1.size();i++)
						{
							answer1.get(i).flag=false;
							answer2.get(i).flag=false;
						}
						Hintbtn.setEnabled(false);
						Itembtn.setEnabled(false);
						repaint();
						revalidate();
						
					}
					else if(cm.getCode().equals("700"))
					{
						lockthread=new LockThread();
						lockthread.start();
					}
					else if(cm.getCode().equals("800"))
					{
						
						itemthread=new ItemThread();
						itemthread.start();
					}
					else if(cm.getCode().equals("1000"))
					{
						Hintbtn.setEnabled(false);
						
						hint=new HintThread(cm.x,cm.y);
						hint.start();
						
					}
				}catch(IOException e){
					try {
						ois.close();
						oos.close();
						flag=false;
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
			
			System.out.println("error");
		}
	}
}
