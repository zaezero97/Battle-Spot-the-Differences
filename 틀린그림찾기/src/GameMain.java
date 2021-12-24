package 틀린그림찾기;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class GameMain extends JFrame{
	private JPanel contentPane;
	private JTextField UserName;
	private JTextField IpAddress;
	private JTextField PortNumber;
	private ImageIcon logoimg,btnimg;
	private Graphics g;
	
	
	


	public GameMain() {
		contentPane = new JPanel();
		
		
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(350, 200, 400, 500);

		JLabel lblNewLabel = new JLabel("User Name");
		lblNewLabel.setBounds(161, 148, 82, 33);
		contentPane.add(lblNewLabel);
		
		UserName = new JTextField();
		UserName.setHorizontalAlignment(SwingConstants.CENTER);
		UserName.setBounds(142, 191, 116, 33);
		contentPane.add(UserName);
		UserName.setColumns(10);
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setBounds(161, 234, 82, 33);
		contentPane.add(lblIpAddress);
		
		IpAddress = new JTextField();
		IpAddress.setHorizontalAlignment(SwingConstants.CENTER);
		IpAddress.setText("127.0.0.1");
		IpAddress.setColumns(10);
		IpAddress.setBounds(142, 276, 116, 33);
		contentPane.add(IpAddress);
		
		JLabel lblPortNumber = new JLabel("Port Number");
		lblPortNumber.setBounds(161, 314, 82, 33);
		contentPane.add(lblPortNumber);
		
		PortNumber = new JTextField();
		PortNumber.setText("30000");
		PortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		PortNumber.setColumns(10);
		PortNumber.setBounds(142, 357, 116, 33);
		contentPane.add(PortNumber);
		
		btnimg=new ImageIcon("게임시작버튼.png");
		Image rebtnimg =btnimg.getImage();
		btnimg=new ImageIcon(rebtnimg.getScaledInstance(317, 38, Image.SCALE_SMOOTH));
		
		JButton btnConnect = new JButton();
		
		btnConnect.setContentAreaFilled(false);
		btnConnect.setFocusPainted(false);
		btnConnect.setIcon(btnimg);
		btnConnect.setOpaque(false);
		btnConnect.setBounds(33, 415, 317, 38);
		contentPane.add(btnConnect);
		
		btnConnect.addActionListener(new StartListener());
		
		logoimg=new ImageIcon("틀린그림찾기로고.png");
		JLabel Logolabel = new JLabel(logoimg);
		Logolabel.setBounds(22, 20, 352, 69);
		contentPane.add(Logolabel);
		
	}
	class StartListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String username = UserName.getText().trim();
			String ip_addr = IpAddress.getText().trim();
			String port_no = PortNumber.getText().trim();
			setVisible(false);
			Lobby lobby = new Lobby(username, ip_addr, port_no);
			
		}
		
	}
	
	 public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					GameMain frame= new GameMain();
					frame.setVisible(true);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
