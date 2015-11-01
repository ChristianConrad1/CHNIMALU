import java.io.Serializable;

public class Spielfeld implements Serializable{
	
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
		setAusgabeID("[]");
	}
	
	/**
	 * Gibt zur�ck ob das Spielfeld eine Figur hat.
	 * 
	 * @return boolean hatFigur
	 */
	public boolean getHatFigur(){
		return this.hatFigur;
	}

}