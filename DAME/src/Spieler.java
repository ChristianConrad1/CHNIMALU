import java.io.Serializable;

public class Spieler implements Serializable{
	private String name;
	private FarbEnum farbe;
	private KI ki;
	private boolean istAktiv=false; 
	private boolean mussSpringen=false;

	/**
	 * KONSTRUKTOR
	 * 
	 * @param name
	 *            Spielername
	 * @param c
	 *            Farbe aus Farbenum
	 */

	public Spieler(String name, FarbEnum c) {
		this.setName(name);
		this.setFarbe(c);
		//ki = new KI(this); Wird wieder reinkommentiert sobald implementiert!
	}
	public void setMussSpringen(boolean springen){
		this.mussSpringen=springen;
	}
	
	public boolean getMussSpringen(){
		return this.mussSpringen;
	}
	
	public boolean getAktiv(){
		return this.istAktiv;
	}
	
	public void setAktiv(boolean aktiv){
		this.istAktiv=aktiv;
	}

	/**
	 * 
	 * @param name
	 *            Spielername
	 */
	public void setName(String name) {
		if (name == null) {
			throw new NullPointerException("Name ist null!");
		}
		this.name = name;
	}

	/**
	 * 
	 * @return name Spielername
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param c
	 *            Farbe aus Farbenum
	 */
	public void setFarbe(FarbEnum c) {
		this.farbe = c;
	}

	/**
	 * 
	 * @return farbe
	 */
	public FarbEnum getFarbe() {
		return this.farbe;
	}
}
