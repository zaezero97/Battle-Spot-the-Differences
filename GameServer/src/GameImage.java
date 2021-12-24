import java.awt.Image;

import javax.swing.ImageIcon;

public class GameImage {
	ImageIcon[][] image= new ImageIcon[3][2];
	ImageIcon[][] resizeimage =new ImageIcon[3][2];
	GameImage(String theme) {
		Image img;
		for(int i=0;i<image.length;i++)
		{
			for(int j=0;j<image[i].length;j++)
			{
				resizeimage[i][j]=new ImageIcon(theme+""+(i+1)+"-"+(j+1)+".jpg");
				if(((double)resizeimage[i][j].getIconWidth()/resizeimage[i][j].getIconHeight())>=1.45)
				{
					img=resizeimage[i][j].getImage().getScaledInstance(1000, 300, Image.SCALE_SMOOTH);
					
				}
				else
				{
					img=resizeimage[i][j].getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
					
				}
				image[i][j]=new ImageIcon(img);
			}
		}
	}
}
