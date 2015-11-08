package SpeichernLaden;
import java.util.Scanner;

import Basisklassen.Spiel;
import Basisklassen.Spielbrett;
import Basisklassen.Spielfeld;
import Interfaces.iBediener;
import Interfaces.iDatenzugriff;

public class SpielTestLaden {
	
	static Spielbrett brett;
	//static Spieler spieler1;
	//static Spieler spieler2;
	static Spiel s1;
	static iBediener b;
	static Scanner sc;
	static Scanner sf;
	static iDatenzugriff d;
	static DatenzugriffSerialisiert d1;
	static DatenzugriffCSV d2;

	public static void main (String Args[]){
		

		
		
	    sf=new Scanner(System.in);	   
	    System.out.println("Mit welchem Format soll gelesen werden? CSV (1) SER (2):");
	    int abfrage = sf.nextInt();
	    if(abfrage == 1){
	    	d2 = new DatenzugriffCSV();
	    	d = d2;
	    	s1 = d.load("savegame/save.csv");
	    }
	    else if(abfrage == 2){
	    	d1 = new DatenzugriffSerialisiert();
	    	d = d1;
	    	s1 = d.load("savegame/save.ser");
	    }
	    
	    
	    
	    b = s1;
	    brett = s1.getBrett();
	   
	    
	   System.out.println("Spieler mit der Farbe " + s1.getSpielerAktiv().getFarbe() + " ist Aktiv");
	    testAusgabe();
	    testScanner();	
	    
	    //testAusgabe(); //Wird nicht benötigt, da in testScanner() schon testAusgabe aufgerufen wird.
	}

	
	
	
	public static void testAusgabe() {
		Spielfeld[][] ar=brett.getNotation();
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
	
	
	public static void testScanner(){
		sc = new Scanner(System.in);
		System.out.print("Gib das Feld an, von dem du das Steinchen bewegen möchtest: ");
	    String eingabe = sc.nextLine();

	    System.out.print("Gib dein Zielfeld an: ");
	    String ausgabe = sc.nextLine();	
	    b.bewegeSpielfigur(eingabe,ausgabe);
		testAusgabe();
		testScanner();		
	}	

	
	}