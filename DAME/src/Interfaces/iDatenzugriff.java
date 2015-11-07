package Interfaces;
import Basisklassen.Spiel;

public interface iDatenzugriff {
	public void save(Spiel s);
	public Spiel load();
}
