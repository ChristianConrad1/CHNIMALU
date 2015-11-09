package Basisklassen;
import java.awt.Color;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Spielfeld extends JLabel implements Serializable{
	
	private Spielfigur figur;
	private String ID;
	private String ausgabeID;
	private boolean hatFigur;
	private int positionX;
	private int positionY;

	
	
	/**
	 * Konstruktor der Klasse Spielfeld. Setzt die ID des instanzierten Objekts und
	 * Setzt die Spielfigur auf null. 
	 * 
	 * @param String  ID
	 *           
	 */
	public Spielfeld(String ID){
		super(ID);
		super.setOpaque(true);
		setID(ID);
		this.figur = null;
		this.hatFigur = false;		
	}
	
	public void setPosX(int positionX){
		this.positionX=positionX;
	}
	
	public void setPosY(int positionY){
		this.positionY=positionY;
	}
	

	public int getPosX(){
		return this.positionX;
	}
	
	public int getPosY(){
		return this.positionY;
	}
	
	
	/**
	 * Setzt eine �bergebene ID
	 * 
	 * @param String
	 *            ID
	 */
	public void setID(String ID){
		if(ID==null){
			throw new NullPointerException ("Keine g�ltige ID!");
		}
		this.ID=ID;
	}
	
	public void setAusgabeID(String ID){
		if(ID==null){
			throw new NullPointerException ("Keine g�ltige ID!");
		}
		this.ausgabeID=ID;
	}
	
	/**
	 * Gibt die ID des Spielfelds zur�ck
	 * 
	 * @return String ID
	 */
	public String getID(){
		return this.ID;
	}
	
	public String getAusgabeID(){
		return this.ausgabeID;
	}
	
	/**
	 * Setzt eine �bergebene Spielfigur
	 * 
	 * @param Spielfigur
	 *            figur
	 */
	public void setFigur(Spielfigur figur){
		if(figur==null){
			throw new NullPointerException ("Figur ist null!");
		}
		this.figur=figur;
		this.hatFigur = true;
		setAusgabeID(this.figur.getAnzeigeID());
		
		//Beim setzen der Figur andere Farbe des Felds zuweisen
		if(figur.getFarbe() == FarbEnum.schwarz){
			this.setIcon(new ImageIcon("testBLACKStone.png"));
			this.setBackground(Color.YELLOW);
			if(figur.isDame()){
				this.setBackground(Color.CYAN);
			}
		}
		else if(figur.getFarbe() == FarbEnum.weiss){
			this.setIcon(new ImageIcon("testWhiteStone.png"));
			this.setBackground(Color.RED);
			if(figur.isDame()){
				this.setBackground(Color.PINK);
			}
		}
		//-----------------------------------------------------
		
	}
	
	public Spielfigur getFigur(){
		return this.figur;
	}
	
	/**
	 * Setzt die Spielfigur eines Spielfelds auf null
	 * 
	 */
	public void removeFigur(){
		this.figur=null;
		this.hatFigur = false;
		setAusgabeID("[ ]");
		//Beim entfernen einer Figur das Feld auf Standardfarbe setzen
		this.setIcon(new ImageIcon("testBLACKStone.png"));
		this.setBackground(Color.BLACK);
		//-----------------------------------------------------------
	}
	
	/**
	 * Gibt zur�ck ob das Spielfeld eine Figur hat.
	 * 
	 * @return boolean hatFigur
	 */
	public boolean getHatFigur(){
		return this.hatFigur;
	}
	public void setXY(String s){
		try{
			if(s.charAt(0) < 97 | s.charAt(0) >109){
				throw new RuntimeException("Falsche Eingabe");
			}
		int c = s.charAt(0);
		c=c-97;
		
		String s2=s.substring(1);
		Integer i = 0;
		i = Integer.parseInt(s2);
		i -= 1;
		if(i <0 | i>11){
			throw new RuntimeException("Falsche Eingabe");
		}
		this.positionX=c;
		this.positionY=i;
		}
		catch(Exception e){
			System.err.println(e);
		}
	}
}