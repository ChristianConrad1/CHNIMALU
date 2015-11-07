package JUNIT;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Basisklassen.FarbEnum;
import Basisklassen.Spielfeld;
import Basisklassen.Spielfigur;


public class JUNITSpielfeldTest {
	
	protected Spielfeld p;
	protected Spielfigur f;
	
	@Before
	public void testSpielfeld() {
		System.out.println("Spielfeld-Objekt wird erstellt...");
		p = new Spielfeld("a1");
		f = new Spielfigur(FarbEnum.weiss, p);
		
	}
	@Test
	public void testGetID() {
		System.out.println("Gebe ID aus:");
		System.out.println(p.getID());
	}
	@Test
	public void testSetFigur() {
		System.out.println("Teste SetFigur");
		p.setFigur(f);
		System.out.println(p.getHatFigur());
		System.out.println(f.getFarbe());
		System.out.println(f.getPosition());
	}
	public void testRemoveFigur() {
		System.out.println("Teste RemoveFigur");
		p.removeFigur();
		System.out.println(p.getHatFigur());;
	}

}
