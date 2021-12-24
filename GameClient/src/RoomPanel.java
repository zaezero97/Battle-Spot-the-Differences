import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class RoomPanel extends JPanel{
	String Roomname;
	String Roomtheme;
	int userN;
	boolean status;
	JLabel RoomnameLabel;
	JLabel RoomuserNLabel;
	JLabel RoomstatusLabel;
	JButton Enterbtn;
	JLabel RoomThemeLabel;
	void ChangeRoom(){
		if(status==true)
		{
			RoomstatusLabel.setText("게임 중");
			Enterbtn.setEnabled(false);
		}
		else if(status==false)
		{
			RoomstatusLabel.setText("대기 중");
			Enterbtn.setEnabled(true);
		}
		RoomuserNLabel.setText(userN+" / 2");
	}
	RoomPanel(String name,String theme,int n,boolean flag){
	
		this.Roomname=name;
		this.Roomtheme=theme;
		this.userN=n;
		this.status=flag;
		setLayout(null);
		this.setBorder(new LineBorder(Color.BLACK));
		RoomnameLabel = new JLabel(this.Roomname);
		RoomnameLabel.setBounds(12, 10, 189, 33);
		add(RoomnameLabel);
		
		RoomuserNLabel = new JLabel(userN+" / 2");
		RoomuserNLabel.setBounds(165, 53, 108, 20);
		add(RoomuserNLabel);
		
		RoomstatusLabel = new JLabel();
		RoomstatusLabel.setBounds(33, 53, 108, 20);
		add(RoomstatusLabel);
		
		Enterbtn = new JButton("입장하기");
		Enterbtn.setBounds(154, 83, 108, 33);
		add(Enterbtn);
		
		if(status==true)
		{
			RoomstatusLabel.setText("게임 중");
			Enterbtn.setEnabled(false);
		}
		else if(status==false)
		{
			RoomstatusLabel.setText("대기 중");
		}
		RoomThemeLabel = new JLabel(this.Roomtheme);
		RoomThemeLabel.setBounds(212, 10, 83, 33);
		add(RoomThemeLabel);
		
	}
}
