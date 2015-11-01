
public class KI_Dame extends KI {
	private Spielfeld[][] brettArray;



	public KI_Dame(Spieler s, Spielbrett brett) {
		super(s);
		if (brett != null) {
			this.brettArray = brett.getNotation();
		} else
			throw new NullPointerException("übergebenes Spielbrett ist Null");
	}

	@Override
	public String[] wasMacheIch() { // Muss koordinaten für die bewege Spielfigur
								// zurückgeben... entweder direkt in
								// Schachnotation (String Array) oder als zahlen
								// bewegeSpielfigur(spieler.getKI().wasMacheIch()[0],
								// spieler.getKI().wasMacheIch()[1]); <- so wird
								// die methode gameloop aufgerufen, falls der
								// spieler eine KI ist.
			if(schlagen() != null)
				return schlagen();
			else
				return ziehen();
	}

	public String[] schlagen() { // muss Koordinaten von Spielfeld zurückgeben, damit
		// wasMacheIch() diese weitergeben kann

		String[] rueckgabe = null; 					
		for (int i = 0; i < this.brettArray.length; i++) {
			for (int j = 0; j < this.brettArray[i].length; j++) {
				if (this.brettArray[i][j].getFigur().getKannSpringen() == true) {
					int x = this.brettArray[i][j].getPosX();
					int y = this.brettArray[i][j].getPosY();
					int links = this.brettArray[i][j].getPosX() - 1;
					int rechts = this.brettArray[i][j].getPosX() + 1;
					int oben = this.brettArray[i][j].getPosY() + 1;
					int unten = this.brettArray[i][j].getPosY() - 1;

					// ----------------------------ALLE 4 FAELLE DER
					// DIAGONALEN UEBERPRUEFUNG-------------------------
			

					int coordX = 0;
					int coordY = 0;
					int a = 0;
					int b = 0;
					int caseNumber = 1;

					while (caseNumber < 5) {

						switch (caseNumber) {
						case 1: // OBEN LINKS
							coordX = links;
							coordY = oben;
							a = -1;
							b = 1;
							break;

						case 2: // OBEN RECHTS
							coordX = rechts;
							coordY = oben;
							a = 1;
							b = 1;

							break;

						case 3: // UNTEN LINKS
							coordX = links;
							coordY = unten;
							a = -1;
							b = -1;
							break;

						case 4: // UNTEN RECHTS
							coordX = rechts;
							coordY = unten;
							a = 1;
							b = -1;
							break;
						}

						while (this.brettArray.length - (coordX) > 1
								&& this.brettArray.length - (coordX) < this.brettArray.length
								&& this.brettArray.length - (coordY) < brettArray.length
								&& this.brettArray.length - (coordY) > 1
								&& this.brettArray[i][j].getFigur().isDame() == true
								&& brettArray[coordX][coordY].getFigur() == null) {

							coordX += a;
							coordY += b;

						}

						if (this.brettArray.length - (coordX) > 1
								&& this.brettArray.length - (coordX) < this.brettArray.length
								&& this.brettArray.length - (coordY) < brettArray.length
								&& this.brettArray.length - (coordY) > 1
								&& brettArray[coordX][coordY].getFigur() != null
								&& brettArray[coordX + a][coordY + b].getFigur() == null) {
							if (brettArray[coordX][coordY].getFigur().getFarbe() != this.brettArray[i][j].getFigur()
									.getFarbe()) {

								int x2 = coordX;
								int y2 = coordY;
								
								rueckgabe = new String[2];
								
								rueckgabe[0] = rewandler(x, y);
								rueckgabe[1] = rewandler(x2, y2);
							
								
								
								// String s2 = rewandler(x2, y2);
								// bewegeSpielfigur(s, s2); //Denkfehler, KI hat
								// keine Beziehung zu Spiell und zusätzlich ist s kein String sondern ein Spieler 
							}

						}
						caseNumber++;

					}

				}
			}
		}
	
	
	return rueckgabe;
	}

	public String[] ziehen() { // muss Koordinaten von Spielfeld zurückgeben, damit wasMacheIch()
							// diese weitergeben kann

	//Kann irgendeine Spielfigur dame werden?
		//Wenn ja dann geb die Koordinaten für diesen Zug zurück
	//Ansonsten
		//Führe zufälligen Zug aus. evtl. auch führe zufälligen Zug mit Dame aus, falls es eine gibt?
		
	return null; //steht hier nur, weil diese Methode noch nicht implementiert ist und eclipse sonst meckert 	
	}
	
	public String rewandler(int x, int y){
		Integer a;
		Integer b;
		
		a = x + 97;
		b = y + 1;
		
		String c = a.toString();
		String d = b.toString();
		
		String s = c + d;
		return s;		
	}

}
