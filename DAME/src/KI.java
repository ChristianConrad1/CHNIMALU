import java.io.Serializable;


public abstract class KI implements Serializable{
	Spieler spieler;
	
	public KI(Spieler s){
		setSpieler(s);
	}
	
	public void setSpieler(Spieler s){
		
	if(s==null){
		 throw new NullPointerException ("Spieler ist null!");
	}
		this.spieler=s;
	}
}
