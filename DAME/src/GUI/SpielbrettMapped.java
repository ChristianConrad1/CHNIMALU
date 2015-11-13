package GUI;

import javax.swing.JPanel;

import Basisklassen.Spielfeld;

public class SpielbrettMapped extends JPanel {
	
	SpielfeldMapped[][] brettArray;
	
	
	public SpielbrettMapped(){
		
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

		
	}
	
	public SpielfeldMapped[][] getNotation(){
		return this.brettArray;
	}
	
	

}
