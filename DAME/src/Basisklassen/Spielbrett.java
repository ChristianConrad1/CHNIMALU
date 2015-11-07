package Basisklassen;
import java.io.Serializable;

public class Spielbrett implements Serializable{
	private Spielfeld[][] brett;

	private int KoordX;
	private int KoordY;
/**
 * Konstruktor erzeugt Spielbrett aus Spielfeldern
 */
	public Spielbrett() {
		brett=new Spielfeld[12][12];
		char n = 'a';
		int m=12;
		int p=11;
		for (int i = 0; i < brett.length; i++) {
			for (int j = 0; j < brett[i].length; j++) {
				this.brett[i][j] = new Spielfeld(n+ ""+m);
				brett[i][j].setPosX(i);
				brett[i][j].setPosY(j);
				m--;
			}
			n++;
			m=12;
	}
		for (int i = 0; i < brett.length; i++) {
			for (int j=0; j<brett[i].length; j++) {
				this.brett[i][j].setAusgabeID("[ ]"); 
			}
	}
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

}
