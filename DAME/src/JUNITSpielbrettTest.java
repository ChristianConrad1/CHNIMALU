

import org.junit.Before;
import org.junit.Test;


public class JUNITSpielbrettTest {

private Spielbrett brett;
	@Before
	public void testSpielbrett() {
		System.out.println("Erstelle Spielbrett() Objekt");
		brett=new Spielbrett();
	}
	

	@Test
	public void testGetNotation() {
		Spielfeld[][] ar=brett.getNotation();
		char rand='A';
		int zahlrand=11;
		
		for(int r=0; r<ar.length; r++){
			System.out.print(" "+rand + "\t ");
			rand++;
		}
		System.out.println("");
		
		for(int i=0; i<ar.length; i++){
			
			for(int j=0; j<ar[i].length+1; j++){
				if(j<ar[i].length){
				System.out.print(ar[j][i].getAusgabeID() + "\t"); //i und j vertauscht
				}
				else{
					System.out.println(zahlrand);
					zahlrand--;
				}
			}
			System.out.println();
		}
	}

}
