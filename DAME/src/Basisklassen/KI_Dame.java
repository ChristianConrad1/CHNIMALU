package Basisklassen;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import org.omg.Messaging.SyncScopeHelper;

public class KI_Dame extends KI implements Serializable{
	private Spielfeld[][] brettArray;

	public KI_Dame(Spieler s) {
		super(s);
	}

	@Override
	public String[] wasMacheIch(Spielbrett brett) { // Muss koordinaten für die bewege
									// Spielfigur
		// zurückgeben... entweder direkt in
		// Schachnotation (String Array) oder als zahlen
		// bewegeSpielfigur(spieler.getKI().wasMacheIch()[0],
		// spieler.getKI().wasMacheIch()[1]); <- so wird
		// die methode gameloop aufgerufen, falls der
		// spieler eine KI ist.
		if(brett != null){
			brettArray = brett.getNotation();
		}
		if (schlagen() != null){
			this.spieler.setWirdSchlagen(true);
			return schlagen();}
		else if  (dameWerden() != null){
			System.out.println("Ich bin intelligent!");
			return dameWerden();}
		else if  (ziehen() != null){
			return ziehen();}		
		else{
			throw new RuntimeException("Alle Spielfiguren geblockt!");
		}
		
	}

	public String[] schlagen() { // muss Koordinaten von Spielfeld zurückgeben,
									// damit
		// wasMacheIch() diese weitergeben kann
	
		String[] rueckgabe = null;
		for (int i = 0; i < this.brettArray.length; i++) {
			
			for (int j = 0; j < this.brettArray[i].length; j++) {
				if(this.brettArray[i][j].getFigur()!=null){

				if (this.brettArray[i][j].getFigur().getKannSpringen() == true) {
					System.out.println("Gibt schlagen zuerueck");
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
				
								return rueckgabe;
								
//								if((brettArray[x][y].getHatFigur() && brettArray[x][y].getFigur().getInListe()) 
//										|| (brettArray[x2][y2].getHatFigur() && brettArray[x2][y2].getFigur().getInListe())){
//									rueckgabe = null;
//								}
								
							}

						}
						caseNumber++;

					}

				}
				}
			}
		}
		return null;
		
	}

	public String[] dameWerden() {
		String[] rueckgabe = null;
		for (int i = 0; i < this.brettArray.length; i++) {
			for (int j = 0; j < this.brettArray[i].length; j++) {
				if (this.brettArray[i][j].getFigur() != null && this.brettArray[i][j].getFigur().getFarbe() == this.spieler.getFarbe()){
						Spielfigur testSpieler = this.brettArray[i][j].getFigur();
						int links = this.brettArray[i][j].getPosX() - 1;
						int rechts = this.brettArray[i][j].getPosX() + 1;
						int oben = this.brettArray[i][j].getPosY() + 1;
						int unten = this.brettArray[i][j].getPosY() - 1;
						
						if(links >=0 && oben == 11 && this.brettArray[i][j].getFigur().getFarbe()==FarbEnum.schwarz &&
							this.brettArray[links][oben].getHatFigur() == false){
							rueckgabe = new String[2];

							rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(), testSpieler.getPosition().getPosY());
							rueckgabe[1] = rewandler(links, oben);
							if(!testSpieler.getInListe()){
								return rueckgabe;
							}
							
						}
						if(rechts <=brettArray.length  && oben == 11 && this.brettArray[i][j].getFigur().getFarbe()==FarbEnum.schwarz &&
								this.brettArray[rechts][oben].getHatFigur() == false){
								rueckgabe = new String[2];

								rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(), testSpieler.getPosition().getPosY());
								rueckgabe[1] = rewandler(rechts, oben);
								if(!testSpieler.getInListe()){
									return rueckgabe;
								}
								
					}
						if(rechts <=brettArray.length-1 && unten == 0 && this.brettArray[i][j].getFigur().getFarbe()==FarbEnum.weiss &&
								this.brettArray[rechts][unten].getHatFigur() == false){
								rueckgabe = new String[2];

								rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(), testSpieler.getPosition().getPosY());
								rueckgabe[1] = rewandler(rechts, unten);
								if(!testSpieler.getInListe()){
									return rueckgabe;
								}
					}
						if(links >=0 && unten == 0 && this.brettArray[i][j].getFigur().getFarbe()==FarbEnum.weiss &&
								this.brettArray[links][unten].getHatFigur() == false){
								rueckgabe = new String[2];

								rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(), testSpieler.getPosition().getPosY());
								rueckgabe[1] = rewandler(links, unten);
								if(!testSpieler.getInListe()){
									return rueckgabe;
								}
					}
				}
			}
		}
		return rueckgabe;
	}
	public String[] ziehen() { // muss Koordinaten von Spielfeld zurückgeben,
								// damit wasMacheIch()
								// diese weitergeben kann
		String[] rueckgabe = new String[2];
		ArrayList<Spielfigur> nurSpieler=new ArrayList<>();
	//	Spielfigur[] nurSpieler=new Spielfigur[24];
		if (this.spieler.getAktiv() == true) {
			int c=0; 
			for (int i = 0; i < this.brettArray.length; i++) {
				for (int j = 0; j < this.brettArray[i].length; j++) {
					if (this.brettArray[i][j].getFigur() != null && this.brettArray[i][j].getFigur().getFarbe() == this.spieler.getFarbe()) {
						
						Spielfigur testSpieler = this.brettArray[i][j].getFigur();
						nurSpieler.add(testSpieler);
						c++;
					}
				}
			}
		}

		
						while(this.spieler.getAktiv()==true && nurSpieler!=null){
						
						Collections.shuffle(nurSpieler);
						if(nurSpieler.isEmpty()==true){
							return null;
						}

						
			Spielfigur testSpieler = nurSpieler.get(0);
			if (testSpieler.getInListe()) {
				boolean settedNew = false;
				for (int v = 1; v < nurSpieler.size(); v++) {
					if (!nurSpieler.get(v).getInListe()) {
						testSpieler = nurSpieler.get(v);
						settedNew = true;
					}
				}
				if(!settedNew){
					return null;
				}
			}
			
						
						//durchlaufe komplettes array
					
						int links = testSpieler.getPosition().getPosX() - 1;
						int rechts = testSpieler.getPosition().getPosX() + 1;
						int oben = testSpieler.getPosition().getPosY() + 1;
						int unten = testSpieler.getPosition().getPosY() - 1;

						// ----------------------------ALLE 4 FAELLE DER
						// DIAGONALEN UEBERPRUEFUNG-------------------------
						
						int coordX = 0;
						int coordY = 0;
						int a = 0;
						int b = 0;
						int caseNumber = 1;
						boolean[] ziehCases=new boolean[4];
						testSpieler.setZiehCases(ziehCases);

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
									&& this.brettArray.length - (coordY) > 1 && testSpieler.isDame() == true
									&& brettArray[coordX][coordY].getFigur() == null) {
								if(this.brettArray.length - (coordX)==0 | this.brettArray.length - (coordY)==0 | this.brettArray.length - (coordX) == this.brettArray.length | this.brettArray.length - (coordY) == this.brettArray.length ){
								rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(), testSpieler.getPosition().getPosY());
								rueckgabe[1] = rewandler(coordX, coordY);
								
								nurSpieler.remove(0);
								return rueckgabe;
								}	
								coordX += a;
								coordY += b;
								

							}

							if (this.brettArray.length - (coordX) > 0 && this.brettArray.length - (coordY) > 0
									&& this.brettArray.length - (coordX) < this.brettArray.length
									&& this.brettArray.length - (coordY) < brettArray.length
									
									&& brettArray[coordX][coordY].getHatFigur() == false) {
								
								if (this.spieler.getFarbe()==FarbEnum.schwarz&& (caseNumber == 1)) {
									
									ziehCases[0]=true;	//diese Spielfigur kann nach oben links ziehen
									testSpieler.setZiehCases(ziehCases);
								} 
								if (this.spieler.getFarbe()==FarbEnum.schwarz&& (caseNumber == 2)) {
									
									ziehCases[1]=true;	//diese Spielfigur kann nach oben rechts ziehen
									testSpieler.setZiehCases(ziehCases);
								}	
								if (this.spieler.getFarbe()==FarbEnum.weiss&& (caseNumber == 3)) {
									
									ziehCases[2]=true;	//diese Spielfigur kann nach unten links ziehen
									testSpieler.setZiehCases(ziehCases);
								}	
								if (this.spieler.getFarbe()==FarbEnum.weiss&& (caseNumber == 4)) {
									
									ziehCases[3]=true;	//diese Spielfigur kann nach unten rechts ziehen
									testSpieler.setZiehCases(ziehCases);
								}	
								
							}
					
							
							caseNumber++;
						}
						
						
												
						if(ziehCases[0]==true && ziehCases[1]==true){
							int randomSprungcase=1;
							randomSprungcase = 1 + (int)(Math.random()*2);
							if(randomSprungcase==1){	//Random fall 1: Zieh nach Links oben!
								rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(),testSpieler.getPosition().getPosY());
								rueckgabe[1] = rewandler(testSpieler.getPosition().getPosX()-1,testSpieler.getPosition().getPosY()+1);
								nurSpieler.remove(0);
								return rueckgabe;
								
							}
								
							else{	//Random fall 2: Zieh nach Rechts oben!
								rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(),testSpieler.getPosition().getPosY());
								rueckgabe[1] = rewandler(testSpieler.getPosition().getPosX()+1,testSpieler.getPosition().getPosY()+1);
								nurSpieler.remove(0);
								return rueckgabe;
							}
							
						}
						else if(ziehCases[0]==true&&ziehCases[1]==false){ //Falls nur oben links moeglich, nehme diesen zug!
							rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(),testSpieler.getPosition().getPosY());
							rueckgabe[1] = rewandler(testSpieler.getPosition().getPosX()-1,testSpieler.getPosition().getPosY()+1);
							nurSpieler.remove(0);
							return rueckgabe;
						}
						else if(ziehCases[1]==true&&ziehCases[0]==false){ //Falls nur oben rechts moeglich, nehme diesen zug!
							rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(),testSpieler.getPosition().getPosY());
							rueckgabe[1] = rewandler(testSpieler.getPosition().getPosX()+1,testSpieler.getPosition().getPosY()+1);
							nurSpieler.remove(0);
							return rueckgabe;
						}
						
						if(ziehCases[2]==true && ziehCases[3]==true){
							int randomSprungcase=1;
							randomSprungcase = 1 + (int)(Math.random()*2);
							if(randomSprungcase==1){	//Random fall 1: Zieh nach Links unten!
								rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(),testSpieler.getPosition().getPosY());
								rueckgabe[1] = rewandler(testSpieler.getPosition().getPosX()-1,testSpieler.getPosition().getPosY()-1);
								nurSpieler.remove(0);
								return rueckgabe;
							}
								
							else{	//Random fall 2: Zieh nach Rechts unten!
								rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(),testSpieler.getPosition().getPosY());
								rueckgabe[1] = rewandler(testSpieler.getPosition().getPosX()+1,testSpieler.getPosition().getPosY()-1);
								nurSpieler.remove(0);
								return rueckgabe;
							}
							
							
						}
						else if(ziehCases[2]==true&&ziehCases[3]==false){ //Falls nur unten links moeglich, nehme diesen zug!
							rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(),testSpieler.getPosition().getPosY());
							rueckgabe[1] = rewandler(testSpieler.getPosition().getPosX()-1,testSpieler.getPosition().getPosY()-1);
							nurSpieler.remove(0);
							return rueckgabe;
						}
						else if(ziehCases[3]==true&&ziehCases[2]==false){ //Falls nur unten rechts moeglich, nehme diesen zug!
							rueckgabe[0] = rewandler(testSpieler.getPosition().getPosX(),testSpieler.getPosition().getPosY());
							rueckgabe[1] = rewandler(testSpieler.getPosition().getPosX()+1,testSpieler.getPosition().getPosY()-1);
							nurSpieler.remove(0);
							return rueckgabe;
						}
						
						}
						//Im Moment endlosschleife die nur abgebrochen wird wenn ziehen kann
		return null;
		}
		
	public String rewandler(int x, int y) {
		Character a;
		Integer b;

		a = (char) ((char)x + 97);
		b = y + 1;

		String c = a.toString();
		String d = b.toString();

		String s = c + d;
		return s;
	}



}
