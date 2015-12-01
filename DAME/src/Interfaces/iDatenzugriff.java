package Interfaces;
import Basisklassen.Spiel;

public interface iDatenzugriff {
	public void save(Object o, String pfad);
	public Object load(String pfad);
}
