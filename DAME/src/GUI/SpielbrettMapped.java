package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SpielbrettMapped extends JPanel{
	
	SpielfeldMapped[][] brettArray;
	private BufferedImage image;
	
	
	
	public SpielbrettMapped(){
		
		
		this.setOpaque(true);
		
		//Setze jedem Spielfeld(Mapped) seine ID nach Schachnotation a12 - l1
		
		brettArray=new SpielfeldMapped[12][12];
		char n = 'a';
		int m=1;
		int p=11;
		for (int i = 0; i < brettArray.length; i++) {
			for (int j = 0; j < brettArray[i].length; j++) {
				this.brettArray[i][j] = new SpielfeldMapped(n+ ""+m);
				m++;
			}
		
			n++;
			m=1;
	}
		 try {
	         image = ImageIO.read(new File(
	                 "res/img/spielbrettBackground.png"));
	     } catch (IOException e) {
	         e.printStackTrace();
	     }

		
	}
	
	public SpielfeldMapped[][] getNotation(){
		return this.brettArray;
	}

@Override
protected void paintComponent(Graphics g){
	
	super.paintComponent(g);

	g.drawImage(image,0,0,null);
}

}
