import org.junit.Before;
import org.junit.Test;


public class JUNITSpielfigurTEST {

	private Spielfigur figur;
	private Spielfeld position;
	
	@Before
	public void testSpielfigur() {
		System.out.println("Spielfeld-Objekt wird erstellt...");
		position = new Spielfeld("a1");
		System.out.println("Spielfigur-Objekt wird erstellt...");
		figur = new Spielfigur(FarbEnum.schwarz, position);
	}
	
	@Test
	public void testSetterGetter(){
		figur.setFarbe(FarbEnum.weiss);
		position.setID("l12");
		figur.setPosition(position);
		System.out.println(figur.getFarbe());
		System.out.println(figur.getPosition().getID());
	}

}
