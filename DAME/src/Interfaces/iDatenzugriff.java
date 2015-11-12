package Interfaces;
import Basisklassen.Spiel;

public interface iDatenzugriff {
	public void save(Object o);
	public Object load(String pfad);
}
