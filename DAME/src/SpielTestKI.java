
	import java.util.Scanner;

	public class SpielTestKI {
		
		static Spielbrett brett;
		static Spieler spieler1;
		static Spieler spieler2;
		static Spiel s1;
		static iBediener b;
		static Scanner sc;

		public static void main (String Args[]){
			brett=new Spielbrett();
			spieler1=new Spieler("bernd",FarbEnum.schwarz);
			spieler2=new Spieler("berndy",FarbEnum.weiss, true, brett);
			
			
			s1=new Spiel(spieler1, spieler2, brett);
		    sc=new Scanner(System.in);	   
		    b= s1;
		    testAusgabe();
		    testScanner();
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
			String eingabe;
			String ausgabe;
			if(s1.getSpielerAktiv().isKI() == true){
		    	eingabe = s1.getSpielerAktiv().getKi().wasMacheIch()[0];
		    	ausgabe = s1.getSpielerAktiv().getKi().wasMacheIch()[1];
		    	System.out.println("Startfeld: " + eingabe);
		    	System.out.println("Zielfeld: " + ausgabe);
		    }
			else{
			System.out.print("Gib das Feld an, von dem du das Steinchen bewegen möchtest: ");
		    eingabe = sc.nextLine();
		    if(eingabe.equals("belegung")){ //Wenn save eingegeben wird wird gespeichert und die Methode verlassen.
		    	b.belegungCSV();
		    	return;
		    }
		    System.out.print("Gib dein Zielfeld an: ");
		    ausgabe = sc.nextLine();	
			}
		    b.bewegeSpielfigur(eingabe,ausgabe);
		
			testAusgabe();
			testScanner();

			
		}

		}




