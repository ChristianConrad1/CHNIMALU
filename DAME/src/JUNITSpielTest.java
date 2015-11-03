import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class JUNITSpielTest {
/*------------------------------
 * 	GIBT NOCH EINE EXCEPTION BEIM SPRINGEN, DEREN URSPRUNG ICH NOCH NICHT GEFUNDEN HABE
 * 	aktiveSpielfigur.getKannSpringen() liefert im Debug false. Wo wird überprüft, ob die Spielfigur springen kann?
 * 	ICH HABE IM GEFÜHL, DASS IRGENDWAS MIT DEM ARRAY NICHT STIMMT, ABER ICH KOMME MOMENTAN NICHT DRAUF 
 --------------------------------*/
	Spielbrett brett;
	Spieler spielerA;
	Spieler spielerB;
	Spiel s1;
	 iBediener b;
	 Scanner sc;
	 Scanner si;
	 Spielfeld ar[][];
	 Spielfigur fig1;
	 Spielfigur fig2;
	 Spielfigur fig3;
	
	@Before
	public void test() {
	
		brett=new Spielbrett();
		spielerA=new Spieler("Bernd", FarbEnum.schwarz);
		spielerB=new Spieler("Berndy", FarbEnum.weiss);
		s1=new Spiel(spielerA, spielerB, brett);
		  sc=new Scanner(System.in);	   
		  si=new Scanner(System.in);	  
		    b = s1;
		    ar = s1.getBrett().getNotation();

		    
		    
	}
	
	@Test
	public void testFall(){
		
		
	    System.out.println("Schlagen/Pusten (1)");
	    System.out.println("Rückwärts Schlagen (2)");
	    System.out.println("Ziehen (3)");
	    System.out.println("Pusten mit zwei Schlagmoeglichkeiten (4)");
	    System.out.println("2x hintereinander schlagen (5)");
	    System.out.println("Dame werden (6)");
	    System.out.println("Dame ziehen (7)");
	    System.out.println("Dame schlagen (8)");

	    int fall = si.nextInt();
		  
	    switch(fall){
	    case 1: //Schlagen /Pusten
	    	//Schwarze wählen und setzen---
			fig1=ar[0][0].getFigur();
			ar[5][5].setFigur(fig1);
			fig1.setPosition(ar[5][5]);
			ar[0][0].removeFigur();
			//----------------------------
	    	//Weisse wählen und setzen---
			fig2=ar[11][11].getFigur();
			ar[4][6].setFigur(fig2);
			fig2.setPosition(ar[4][6]);
			ar[11][11].removeFigur();
			//----------------------------

	    	break;
	    case 2://Rückwärts schlagen
	    	//Schwarze wählen und setzen---
			fig1=ar[0][0].getFigur();
			ar[0][0].removeFigur();
			ar[6][6].setFigur(fig1);
			fig1.setPosition(ar[5][5]);
			//----------------------------
	    	//Weisse wählen und setzen---
			fig2=ar[11][11].getFigur();
			ar[11][11].removeFigur();
			ar[5][5].setFigur(fig2);
			fig2.setPosition(ar[4][6]);
			//----------------------------

	    	break;
	    case 3: //ziehen
	    	//Hier muss nichts stehen, da nur die Standard-Belegung ausgegeben wird und das wird sowieso immer am ende dieser methode gemacht
	    	break;
	    case 4: //Pusten 2 Schlagmöglichkeiten
	    	//Schwarzes Spielfeld entfernen
	    	ar[3][3].removeFigur();
	    	ar[5][3].removeFigur();
	    	//Schwarze wählen und setzen---
			fig1=ar[0][0].getFigur();
			ar[0][0].removeFigur();
			
			ar[6][4].setFigur(fig1);
			fig1.setPosition(ar[6][4]);
			//----------------------------
	    	//Weisse wählen und setzen---
			fig2=ar[11][11].getFigur();
			ar[11][11].removeFigur();
			ar[4][6].setFigur(fig2);
			fig2.setPosition(ar[4][6]);
			//----------------------------
	    	//Weisse wählen und setzen---
			fig3=ar[11][9].getFigur();
			ar[11][9].removeFigur();
			ar[4][4].setFigur(fig3);
			fig3.setPosition(ar[4][4]);
			//----------------------------

	    	break;
	    case 5: //doppelt schlagen
	    	//Schwarze wählen und setzen---
			fig1=ar[0][0].getFigur();
			ar[0][0].removeFigur();
			ar[4][4].setFigur(fig1);
			fig1.setPosition(ar[4][4]);
			//----------------------------
	    	//Weisse wählen und setzen---
			fig2=ar[11][11].getFigur();
			ar[11][11].removeFigur();
			ar[5][5].setFigur(fig2);
			fig2.setPosition(ar[5][5]);
			//----------------------------
	    	//Weisse wählen und setzen---
			fig3=ar[8][8].getFigur();
			ar[8][8].removeFigur();
			ar[7][7].setFigur(fig3);
			fig3.setPosition(ar[7][7]);
			//----------------------------

	    	break;
	    case 6://dame werden
	    	//Weiße Figur entfernen
	    	ar[11][11].removeFigur();
	    	ar[10][10].removeFigur();
	    	
	    	//Schwarze wählen und setzen---
			fig1=ar[0][0].getFigur();
			ar[0][0].removeFigur();
			ar[10][10].setFigur(fig1);
			fig1.setPosition(ar[10][10]);
			//----------------------------

	    	break;
	    case 7://dame ziehen
	    	//weißes feld entfernen
	    	ar[10][10].removeFigur();
	    	//Weiße Figuren entfernen
	    	ar[11][11].removeFigur();
	    	ar[9][9].removeFigur();
	    	ar[8][8].removeFigur();
	    	
	    	//Schwarze wählen und setzen---
			fig1=ar[0][0].getFigur();
			ar[0][0].removeFigur();
			fig1.setDame();
			ar[11][11].setFigur(fig1);
			fig1.setPosition(ar[10][10]);

	    	break;
	    case 8://dame schlagen
	    	//Weiße Figuren entfernen
	    	ar[11][11].removeFigur();
	    	ar[9][9].removeFigur();
	    	ar[8][8].removeFigur();
	    	
	    	//Schwarze wählen und setzen---
			fig1=ar[0][0].getFigur();

			ar[0][0].removeFigur();
			fig1.setDame();
			ar[11][11].setFigur(fig1);
			fig1.setPosition(ar[10][10]);

	    	break; 

	    }
		s1.setBrettArray(ar);
		testAusgabe();
		testScanner();
	    
	}

	
	public  void testAusgabe() {

		char rand='A';
		int zahlrand=12;
		
		for(int r=1; r<=ar.length; r++){
			System.out.print(" "+rand + "\t ");
			rand++;
		}
		System.out.println("");
		
		for(int i=11; i>=0; i--){
			
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
	


	public  void testScanner(){

		System.out.print("Gib das Feld an, von dem du das Steinchen bewegen möchtest: ");
	    String eingabe = sc.nextLine(); 
	    System.out.print("Gib dein Zielfeld an: ");
	    String ausgabe = sc.nextLine();	
	    
	    b.bewegeSpielfigur(eingabe,ausgabe);
	    
	//    System.out.println(this.brett.getBrettArray().equals(s1.getBrett().getBrettArray())); //debug
	
		testAusgabe();
		testScanner();

		
	}
	
		
	}