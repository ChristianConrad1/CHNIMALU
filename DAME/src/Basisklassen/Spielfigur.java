package Basisklassen;
import java.io.Serializable;

public class Spielfigur implements Serializable{
	private Spielfeld position;
	private FarbEnum farbe;
	private String anzeigeID;
	private boolean dame = false;
	private boolean kannSpringen=false;
	private boolean inListe = false;
	private boolean[] sprungCases;
	private boolean[] ziehCases;

	/**
	 * Konstruktor der Klasse Spielfigur. Setzt die Position und die Spielfigur
	 * des instanzierten Objekts
	 * 
	 * @param FarbEnum
	 *            c
	 * @param Spielfeld
	 *            position
	 */
	public Spielfigur(FarbEnum c, Spielfeld position) {
		
		setSprungCases(new boolean[4]);
		setZiehCases(new boolean[4]);
		
		setFarbe(c);
		setPosition(position);
		if(c==FarbEnum.schwarz) {
			this.anzeigeID="[X]";
		}
		if(c==FarbEnum.weiss){
			this.anzeigeID="[O]";
		}
	}
	
	public void setKannSpringen(boolean springen){
		this.kannSpringen=springen;
	}
	
	public boolean getKannSpringen(){
		return this.kannSpringen;
	}
	
	public String getAnzeigeID(){
		return this.anzeigeID;
	}

	/**
	 * Setzt eine übergebene Farbe
	 * 
	 * @param FarbEnum
	 *            c
	 */
	
	
	public void setFarbe(FarbEnum c) {
		this.farbe = c;
	}

	/**
	 * Setzt eine übergebene Position auf dem Spielfeld
	 * 
	 * @param Spielfeld
	 *            position
	 */
	public void setPosition(Spielfeld position) {
		if(position == null){
			throw new NullPointerException("Position ist null!");
		}
		this.position = position;
	}
	

	/**
	 * Gibt die Farbe der Spielfigur zurück
	 * 
	 * @return FarbEnum farbe
	 */
	public FarbEnum getFarbe() {
		return this.farbe;
	}

	/**
	 * Gibt die Position auf dem Spielfeld zurück
	 * 
	 * @return Spielfeld Position
	 */
	public Spielfeld getPosition() {
		return this.position;
	}

	public boolean isDame() {
		return dame;
	}

	public void setDame() {
		this.dame = true;
		if(this.farbe==FarbEnum.weiss){
		this.anzeigeID="[*O*]";	
		
		}
		else if(this.farbe==FarbEnum.schwarz){
			this.anzeigeID="[*X*]";
		}
		position.setAusgabeID(this.anzeigeID);
		
		
	
	}

	public boolean[] getSprungCases() {
		return sprungCases;
	}

	public void setSprungCases(boolean[] sprungCases) {
		this.sprungCases = sprungCases;
	}

	public boolean[] getZiehCases() {
		return ziehCases;
	}

	public void setZiehCases(boolean[] ziehCases) {
		this.ziehCases = ziehCases;
	}
	public boolean getInListe(){
		return inListe;
	}
	public void setInListe(boolean inListe){
		this.inListe = inListe;
	}

}
