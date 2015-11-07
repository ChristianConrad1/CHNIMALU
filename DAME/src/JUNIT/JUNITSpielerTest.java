package JUNIT;

	import static org.junit.Assert.*;

	import org.junit.Before;
	import org.junit.Test;

import Basisklassen.FarbEnum;
import Basisklassen.Spieler;

	public class JUNITSpielerTest {
		
		private Spieler s;
		@Before
		public void test() {
			System.out.println("erzeuge Spieler");
			s=new Spieler("Spieler1",FarbEnum.schwarz);
		}

		@Test
		public void testSpieler() {
			System.out.println(s.getFarbe());
			System.out.println(s.getName());
			
		}
		
	}
