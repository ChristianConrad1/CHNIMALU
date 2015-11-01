import java.io.Serializable;


public abstract class KI implements Serializable, iBediener{
	Spieler spieler;
	private Spielfeld[][] brettArray;
	private Spielbrett brett;
	
	public KI(Spieler s, Spielfeld[][] brettArray){
		setSpieler(s);
		this.brettArray = brett.getNotation();
	}
	
	public void setSpieler(Spieler s){
		
	if(s==null){
		 throw new NullPointerException ("Spieler ist null!");
	}
		this.spieler=s;
	}
	public void setSpielbrett(Spielfeld[][] brettArray){
		if(brettArray == null){
			throw new NullPointerException("Spielbrett ist null!");
		}
		this.brettArray = brettArray;
	}
	
	public void wasMacheIch() {
		if(spieler.getMussSpringen() == true){
			schlagen();
		}
		//kann ich ne Dame bekommen? Wenn ja, Dame machen
		//sonst zufälligen Zug machen.
		//3 Methoden (schlagen, dame, zufälliger Zug)
	}
	
	//Ziel Koordinaten implementieren
	public void schlagen() {
		for (int i = 0; i < this.brettArray.length; i++) {
			for (int j = 0; j < this.brettArray[i].length; j++) {
				if (this.brettArray[i][j].getFigur().getKannSpringen() == true) {
					int x = this.brettArray[i][j].getPosX();
					int y = this.brettArray[i][j].getPosY();
					String s = Rewandler(x, y);

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
								String s2 = Rewandler(x2, y2);
								bewegeSpielfigur(s, s2);
							}

						}
						caseNumber++;

					}

					
				}

			}
		}
	}
	
	
	public String Rewandler(int x, int y){
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
