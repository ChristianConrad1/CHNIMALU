package Basisklassen;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Spielbrett extends JPanel implements Serializable{
	private Spielfeld[][] brett;

	private int KoordX;
	private int KoordY;
/**
 * Konstruktor erzeugt Spielbrett aus Spielfeldern
 */
	public Spielbrett() {

		brett=new Spielfeld[12][12];
		char n = 'a';
		int m=1;
		int p=11;
		for (int i = 0; i < brett.length; i++) {
			for (int j = 0; j < brett[i].length; j++) {
				this.brett[i][j] = new Spielfeld(n+ ""+m);
				

				
				brett[i][j].setPosX(i);
				brett[i][j].setPosY(j);
				
				
				m++;
			}
		
			n++;
			m=1;
	}
		for (int i = 0; i < brett.length; i++) {
			for (int j=0; j<brett[i].length; j++) {
				this.brett[i][j].setAusgabeID("[ ]"); 
			}
	}
		this.initBrett();
	}
/**
 * gibt Array aus Spielfeldern mit Schachnotation-ID zurück
 * @return Spielfeld[][] brett
 */
	public Spielfeld[][] getNotation() {
		return this.brett;
	}
	/**
	 * Wandelt x und y Koordinate in String um
	 * @param x trivial
	 * @param y trivial
	 * @return s.o. :D
	 */
	
	public void Umwandler(String s){
		try{
			if(s.charAt(0) < 97 | s.charAt(0) >109){
				throw new RuntimeException("Falsche Eingabe");
			}
		int c = s.charAt(0);
		c=c-97;
		
		String s2=s.substring(1);
		Integer i = 0;
		i = i.parseInt(s2);
		i -= 1;
		if(i <0 | i>11){
			throw new RuntimeException("Falsche Eingabe");
		}
		this.KoordX=c;
		this.KoordY=i;
		}
		catch(Exception e){
			System.err.println(e);
		}
	}
	

	public void setBrett(Spielfeld[][] brett) {
		this.brett = brett;
	}
	
	public int getKoordX(){
		return this.KoordX;
	}
	public int getKoordY(){
		return this.KoordY;
	}

	public void initBrett() {
		int counter = 0;
		boolean farbe = false;
		super.setLayout(new GridBagLayout());
		super.setSize(new Dimension(768,768)); //Größe des von JPanel erbenden Spielbretts
		GridBagConstraints c = new GridBagConstraints();
		for (int i = 11; i >= 0; i--) {
			c.gridy = counter;
			for (int n = 0; n < brett[i].length; n++) {
				c.gridx = n;
				this.brett[n][i].setPreferredSize(new Dimension(64,64));
				super.add(this.brett[n][i], c);
				if (farbe){
					this.brett[n][i].setIcon(new ImageIcon("res/img/Schwarz_FELD.png"));
					this.brett[n][i].setBackground(Color.BLACK);
				}
					else{
						this.brett[n][i].setIcon(new ImageIcon("res/img/weiss_FELD.png"));
						this.brett[n][i].setBackground(Color.WHITE);
					}
				farbe=!farbe;
				
			}
			farbe=!farbe;
			counter++;
		}

	}

}
