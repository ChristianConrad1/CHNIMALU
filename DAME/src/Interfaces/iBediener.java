package Interfaces;

public interface iBediener {
	
	public void bewegeSpielfigur(String s1, String s2);
	public String belegungCSV();
	public void pusten(String feld);
	public void spielerHzfg(String name, boolean ki);
	public void speichern(String pfad);
	public void laden(String pfad);
	public void neuesSpiel();
	public void mail();
	public void init(Object view);
	
}
