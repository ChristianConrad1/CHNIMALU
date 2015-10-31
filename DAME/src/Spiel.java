import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;

public class Spiel implements iBediener, Serializable {
	private PrintWriter pw;
	private Spieler winner;
	private Spieler spielerA;
	private Spieler spielerB;
	private Spieler spielerAktiv; //der spieler der gerade am zug ist, nicht zu verwechseln mit der aktiven Spielfigur
	private Spielbrett brett;
	private Spielfigur aktiveSpielfigur = null; // Setze immer null nach Zug
	private Spielfeld[][] brettArray;
	private boolean spielende = false;
	private final int reihen = 4;
	private int sprungKonflikt=0; //Wenn 2 Steinchen springen kï¿½nnen, wird hier true gesetzt! Wird erhï¿½ht wenn ein steinchen springen kann, ab sprungKonflikt=2 haben wir einen Konflikt!
	Scanner sc; //benï¿½tigen wir fï¿½r den Fall dass der Gegner ein gegnerisches Steinchen entfernen darf

	public Spiel(Spieler spieler1, Spieler spieler2, Spielbrett brett) {
		this.brett = brett;
		setSpieler(spieler1, spieler2);
		this.brettArray = brett.getNotation();
		setAlleFiguren();
		sc=new Scanner(System.in);
		
		
	}

	public void setAlleFiguren() {
		if (brett == null) {
			throw new NullPointerException("Spielbrett ist null!");
		}
		// schwarze Steine ([x])
		// ausgeben------------------------------------------------------
		int f = 0;
		for (int n = 0; n < this.reihen; n++) {
			for (; f < brettArray.length; f += 2) {
				brettArray[f][n].setFigur(new Spielfigur(FarbEnum.schwarz, brettArray[f][n]));
			}
			if (f % 2 == 0)
				f = 1;
			else
				f = 0;
		}
		// -----------------------------------------------------------------------------------
		// weisse Steine ([o])
		// ausgeben------------------------------------------------------
		int x = 1;
		for (int n = brettArray.length - 1; n > (brettArray.length - 1) - this.reihen; n--) { // (n-3)
																								// da
																								// rÃ¼ckwÃ¤rts
																								// gezÃ¤hlt
																								// wird,
																								// muss
																								// bei
																								// Anpassung
																								// der
																								// Reihen
																								// auch
																								// angepasst
																								// werden
			for (; x < brettArray.length; x += 2) {
				brettArray[x][n].setFigur(new Spielfigur(FarbEnum.weiss, brettArray[x][n]));
			}
			if (x % 2 == 0)
				x = 1;
			else
				x = 0;
		}
		// -----------------------------------------------------------------------------------

	}

	/**
	 * lege Spieler fest!
	 * 
	 * @param spieler1
	 * @param spieler2
	 */
	public void setSpieler(Spieler spieler1, Spieler spieler2) {
		if (spieler1 != null && spieler2 != null) {
			this.spielerA = spieler1;
			this.spielerAktiv = spielerA;
			this.spielerA.setAktiv(true);
			this.spielerB = spieler2;
		} else {
			throw new NullPointerException("Einer oder mehrere Spieler sind null!");
		}
	}

	/**
	 * bewegt eine Spielfigur von gewï¿½hltem Feld auf ein anderes gï¿½ltiges Feld
	 * 
	 * @param String1
	 *            eingabe Feld von Spielfigur
	 * @param String2
	 *            eingabe Zielfeld
	 */
	
	//**************************************BEWEGE SPIELFIGUR METHODE********************************************
	public void bewegeSpielfigur(String s1, String s2) {
		brett.Umwandler(s1);
		int a = brett.getKoordX();
		int b = brett.getKoordY();
		brett.Umwandler(s2);
		int c = brett.getKoordX();
		int d = brett.getKoordY();

		//spielerMussSpringen();

		// Überprüfe ob unsere übergebenen Koordinaten in unserem Array-Feld
		// enthalten sind
		if (a < 0 | a > this.brettArray.length - 1) {
			throw new RuntimeException(
					"Ihre X Koordinate ist nicht in unserem Spielfeld!");
		}
		if (b < 0 | b > this.brettArray.length - 1) {
			throw new RuntimeException(
					"Ihre Y Koordinate ist nicht in unserem Spielfeld!");
		}

		Spieler spielerAktiv = null;
		Spieler spielerGegner = null;
		FarbEnum farbeAktiv = null;
		FarbEnum farbeGegner = null;
		System.out.println("Hallo");
		if (spielerA.getAktiv() == true) {
			spielerAktiv = spielerA;
			spielerGegner = spielerB;
			farbeAktiv = FarbEnum.schwarz;
			farbeGegner = FarbEnum.weiss;
		}
		if (spielerB.getAktiv() == true) {
			spielerAktiv = spielerB;
			spielerGegner = spielerA;
			farbeAktiv = FarbEnum.weiss;
			farbeGegner = FarbEnum.schwarz;
		}

		// ------------------------------Wähle
		// Spielfigur--------------------------
		try {
			if (spielerAktiv.getAktiv() == true && spielerAktiv != null) {

				if (brettArray[a][b].getHatFigur() == true
						&& brettArray[a][b].getFigur().getFarbe() == farbeAktiv) {
					this.aktiveSpielfigur = brettArray[a][b].getFigur();
					this.aktiveSpielfigur.setPosition(brettArray[a][b]);
				} else {
					throw new RuntimeException(
							"Keine schwarze Spielfigur ausgewï¿½hlt!");
				}
			}

		} catch (Exception e) {
			System.err.println(e);

		}

		// ------------------------------------------------------ZIELFELD (Hier
		// soll Stein als nächstes
		// hin!)----------------------------------------------------------------------

		if (c < 0 | c > this.brettArray.length - 1) {
			throw new RuntimeException(
					"Ihre X Koordinate ist nicht in unserem Spielfeld!");
		}

		if (d < 0 | d > this.brettArray.length - 1) {
			throw new RuntimeException(
					"Ihre Y Koordinate ist nicht in unserem Spielfeld!");
		}
		try {

			if (this.aktiveSpielfigur != null) {

				if (spielerAktiv.getAktiv() == true) {
					if (this.aktiveSpielfigur.isDame() == true) {
						bewegeDame(c, d);
					}

					aktiveSpielfigur = this.brettArray[a][b].getFigur();

					int links = aktiveSpielfigur.getPosition().getPosX() - 1;
					int rechts = aktiveSpielfigur.getPosition().getPosX() + 1;
					int oben = aktiveSpielfigur.getPosition().getPosY() + 1;
					int unten = aktiveSpielfigur.getPosition().getPosY() - 1;

					int koordX = 0;
					int koordY = 0;
					int koordX1 = 0;
					int koordY1 = 0;

					// CASE: LINKS OBEN:
					if (c == links && d == oben) {
						koordX = links;
						koordY = oben;
						koordX1 = links - 1;
						koordY1 = oben + 1;
					}
					// CASE: RECHTS OBEN:
					if (c == rechts && d == oben) {
						koordX = rechts;
						koordY = oben;
						koordX1 = rechts + 1;
						koordY1 = oben + 1;
					}
					// CASE: LINKS UNTEN:
					if (c == links && d == unten) {
						koordX = links;
						koordY = unten;
						koordX1 = links - 1;
						koordY1 = unten - 1;
					}
					// CASE: RECHTS UTNEN:
					if (c == rechts && d == unten) {
						koordX = rechts;
						koordY = unten;
						koordX1 = rechts + 1;
						koordY1 = unten - 1;
					}

					if (spielerAktiv.getMussSpringen() == true
							&& aktiveSpielfigur.getKannSpringen() == true) {

						if (aktiveSpielfigur.getKannSpringen() == true) {
							// ----------------------------------SPRINGEN IN
							// EINE VON VIER
							// RICHTUNGEN---------------------------------------

							// Spezialfall: Gewählte Spielfigur hat Sprung,
							// nutzt diesen aber nicht! Lösche sie!!
							if (aktiveSpielfigur.getKannSpringen() == true
									&& spielerAktiv.getMussSpringen()
									&& this.brettArray[c][d].getFigur() == null) {
								this.brettArray[a][b].getFigur().getPosition()
								.removeFigur();
								System.out
								.println("UPS! Sie haben einen Stein mit Schlagmöglichkeit gewählt, sind dieser jedoch nicht nachgegangen! Der Stein wurde zur Bestrafung vom Spielfeld entfernt!");

							}
							// -------------------------------------------------------SPRINGEN----------------------------------------------------

							// ---------------------------------AUFÜHRUNG DER
							// SPRUNG CASES:------------------------------------
							if (c == koordX && d == koordY) {
								while (aktiveSpielfigur.getKannSpringen() == true) {
									// Hat er das Feld links oben gewaehlt?
									if (this.brettArray[koordX][koordY]
											.getHatFigur() == true
											&& this.brettArray[koordX][koordY]
													.getFigur().getFarbe() == FarbEnum.weiss
													&& this.brettArray[koordX1][koordY1]
															.getHatFigur() == false) { // Ist
										// das
										// Feld
										// bespringbar?
										// ï¿½berspringe
										// ich
										// damit
										// ein
										// Gegnerstein?

										this.aktiveSpielfigur.getPosition()
										.removeFigur();
										this.brettArray[koordX][koordY]
												.getFigur().getPosition()
												.removeFigur();
										this.brettArray[koordX1][koordY1]
												.setFigur(aktiveSpielfigur);
										aktiveSpielfigur
										.setPosition(this.brettArray[koordX1][koordY1]);
										System.out
										.println("Zug vollendet, muss allerdings nochmal springen wenn nochmal kann!");

										spielerB.setMussSpringen(false);
										aktiveSpielfigur.setKannSpringen(false);

										spielerMussSpringen(); // ï¿½berprï¿½ft
										// nochmals alle
										// Steine auf
										// mï¿½gliche
										// Sprï¿½nge

										aktiveSpielfigur = brettArray[links - 1][oben + 1]
												.getFigur(); // weise neue
										// Steinposition
										// der aktuellen
										// Spielfigur
										// zu, damit im
										// falle noch
										// einer
										// Schlagmï¿½glichkeit
										// weiterspringt!
										links = aktiveSpielfigur.getPosition()
												.getPosX() - 1;
										oben = aktiveSpielfigur.getPosition()
												.getPosY() + 1;

										spielerB.setMussSpringen(false);// Jetzt
										// gilt
										// ja
										// nur:
										// Nur
										// der
										// eine
										// Stein
										// interessiert
										// uns
										// ï¿½berhaupt!
										if (aktiveSpielfigur.getKannSpringen() == true) {
											System.out
											.println("Der selbe Stein konnte weitere Steine ï¿½berspringen!");
										}

										System.out
										.println("Sprung vollendet nach Links-Oben");

									}
								}
								aktiveSpielfigur = null;
								spielerWechsel();
							}

						}
					}

					// *******************************************NORMALES ZIEHEN!****************************************

					// --------------------------------------ALLGEMEINESZIEHEN----------------------------

					if (c == koordX && d == koordY
							&& this.brettArray[c][d].getHatFigur() == false) {

						if (aktiveSpielfigur.getKannSpringen() == false) {

							aktiveSpielfigur.getPosition().removeFigur();
							aktiveSpielfigur.setPosition(this.brettArray[c][d]);
							this.brettArray[c][d].setFigur(aktiveSpielfigur);
							// Ist die Spielfigur jetzt eine Dame?-------------------
							if (aktiveSpielfigur.getPosition().getPosY() == 11) {
								aktiveSpielfigur.setDame();
								System.out.println("setDame aufgerufen");
							}

							System.out.println("Zug vollendet!");

						} else if (aktiveSpielfigur.getKannSpringen() == true) {
							aktiveSpielfigur.getPosition().removeFigur();
						}

						if (spielerAktiv.getMussSpringen() == true) {
							if (this.sprungKonflikt > 1) { // im Moment kï¿½nnen
								// mehrere Figuren
								// springen! (max
								// 2?)

								spielerAktiv.setAktiv(false);
								spielerGegner.setAktiv(true);// Hier nur
								// Spielerwechsel
								// fï¿½r
								// Eingabe.
								// Zugwechsel
								// ist deutlich
								// komplexer als
								// ein einfacher
								// temporï¿½rer
								// Spielerwechsel!
								// (scan vom
								// Spielfeld
								// etc)
								System.out
								.println("Sie hatten mehrere Steine mit Sprungmoeglichkeiten! Spieler B, Sie duerfen nun eines dieser Steinchen von Spieler A wï¿½hlen,so dass dieses vom Feld gelï¿½scht wird!");

								String eingabe = sc.nextLine();
								brett.Umwandler(eingabe);
								int x = brett.getKoordX();
								int y = brett.getKoordY();
								if (this.brettArray[x][y].getFigur() != null) {
									if (this.brettArray[x][y].getFigur()
											.getFarbe() == FarbEnum.schwarz
											&& this.brettArray[x][y].getFigur()
											.getKannSpringen()) {
										this.brettArray[x][y].removeFigur();

									}
								} else {
									throw new RuntimeException(
											"Wï¿½hlen sie bitte nur eine der Steine die in beim vorhergehenden Zug eine Sprungmï¿½glichkeit hatten, die von ihrem Gegner nicht wahrgenommen wurde!");
								}

								spielerAktiv.setAktiv(true); // Die Eingabe
								// wurde
								// beendet. Der
								// einfache
								// Spielerwechsel
								// ist nicht das
								// gleiche, wie
								// ein
								// Zugwechsel!
								spielerGegner.setAktiv(false);

							} else {
								// Gibt es nur eine Spielfigur die springen
								// kann, suchen wir diese einfach in unserem
								// Array
								for (int i = 0; i < this.brettArray.length; i++) {
									for (int j = 0; j < this.brettArray[i].length; j++) {
										if (this.brettArray[i][j].getFigur() != null
												&& this.brettArray[i][j]
														.getFigur().getFarbe() == FarbEnum.schwarz
														&& this.brettArray[i][j]
																.getFigur()
																.getKannSpringen()) {

											Spielfigur testSpieler = this.brettArray[i][j]
													.getFigur();

											testSpieler.getPosition()
											.removeFigur();

											testSpieler = null;
											System.out
											.println("Jetzt wird bestraft!! Sie hatten eine Sprungmï¿½glichkeit! Diese wurde nicht wahrgenommen, der Stein wurde entfernt!"); // danach
											// muss
											// springen
											// deaktivieren
										}
									}
								}
							}
						}

						aktiveSpielfigur = null;
						spielerAktiv.setMussSpringen(false);
						spielerWechsel();

					}
				

					
					else if(spielerB.getMussSpringen()==false){
						throw new RuntimeException("Wï¿½hlen sie bitte ein Feld, auf das sie ziehen kï¿½nnen!");
					}
					
			
					
				}
					
				else if(spielerB.getMussSpringen()==true){
					throw new RuntimeException("Wï¿½hlen sie bitte eine Figur die springen kann, bzw ein Feld das bespringbar ist!!");
				
				}
				
			}
			
	//}
					
	}catch (Exception e) {
		System.err.println(e);
			
	}
		
		isSpielende();		//Prueft ob beide Spieler noch Spielfiguren haben
	
	}


		
	
//++++++++++++++++++++++++++++++++ BEWEGE DAME ++++++++++++++++++++++++++++++
	private void bewegeDame(int c, int d) {
		int richtung = d - aktiveSpielfigur.getPosition().getPosY();
		int abstandx = Math.abs(c - aktiveSpielfigur.getPosition().getPosX());
		int abstandy = Math.abs(d - aktiveSpielfigur.getPosition().getPosY());
		if (abstandx == abstandy) { // Figur darf nur diagonal laufen

			if (this.brettArray[c][d].getHatFigur() == false) { // nur Bewegen

				aktiveSpielfigur.getPosition().removeFigur();
				this.brettArray[c][d].setFigur(aktiveSpielfigur);
				this.brettArray[c][d].getFigur().setPosition(this.brettArray[c][d]);
				aktiveSpielfigur = null;
				System.out.println("Zug vollendet");
				spielerWechsel();
		

			}

			// -----------------------------------Zielfeld hat gegnerische
			// Spielfigur -------------------------------------------
			else if (this.brettArray[c][d].getHatFigur() == true
					&& this.brettArray[c][d].getFigur().getFarbe() != aktiveSpielfigur.getFarbe()) {

				// Sprung nach oben
				if (richtung > 0) {
					if (c < (aktiveSpielfigur.getPosition().getPosX())) { // Sprung
																			// nach
																			// links
						if (this.brettArray[c - 1][d + 1].getHatFigur() == true) {
							throw new RuntimeException("Hier ist bereits eine Figur! Feld nicht bespringbar.");
						}
						System.out.println("sprung nach links");
						aktiveSpielfigur.getPosition().removeFigur();
						this.brettArray[c - 1][d + 1].setFigur(aktiveSpielfigur);
						this.brettArray[c - 1][d + 1].getFigur().setPosition(this.brettArray[c][d]);
						this.brettArray[c][d].removeFigur();
						aktiveSpielfigur = null;
						System.out.println("Zug vollendet");
						spielerWechsel();
					
					}

					else {
						if (this.brettArray[c + 1][d + 1].getHatFigur() == true) {
							throw new RuntimeException("Hier ist bereits eine Figur! Feld nicht bespringbar.");
						} else if (c > (aktiveSpielfigur.getPosition().getPosX())) {
							System.out.println("sprung nach rechts!");
							aktiveSpielfigur.getPosition().removeFigur();
							this.brettArray[c + 1][d + 1].setFigur(aktiveSpielfigur);
							this.brettArray[c + 1][d + 1].getFigur().setPosition(this.brettArray[c][d]);
							this.brettArray[c][d].removeFigur();
							aktiveSpielfigur = null;
							System.out.println("Zug vollendet");
							spielerWechsel();
						
						}
					}
				}
				// Sprung nach unten
				if (richtung < 0) {
					if (c < (aktiveSpielfigur.getPosition().getPosX())) {
						if (this.brettArray[c - 1][d - 1].getHatFigur() == true) {
							throw new RuntimeException("Hier ist bereits eine Figur! Feld nicht bespringbar.");
						}
						System.out.println("sprung nach links");
						aktiveSpielfigur.getPosition().removeFigur();
						this.brettArray[c - 1][d - 1].setFigur(aktiveSpielfigur);
						this.brettArray[c - 1][d - 1].getFigur().setPosition(this.brettArray[c][d]);
						this.brettArray[c][d].removeFigur();
						aktiveSpielfigur = null;
						System.out.println("Zug vollendet");
						spielerWechsel();
					
						
					}

					else if (this.brettArray[c + 1][d - 1].getHatFigur() == true) {
						throw new RuntimeException("Hier ist bereits eine Figur! Feld nicht bespringbar.");
					} else if (c > (aktiveSpielfigur.getPosition().getPosX())) {
						System.out.println("sprung nach rechts!");
						aktiveSpielfigur.getPosition().removeFigur();
						this.brettArray[c + 1][d - 1].setFigur(aktiveSpielfigur);
						this.brettArray[c + 1][d - 1].getFigur().setPosition(this.brettArray[c][d]);
						this.brettArray[c][d].removeFigur();
						aktiveSpielfigur = null;
						System.out.println("Zug vollendet");
						spielerWechsel();
					
					

					}

				}
			}
		} else {
			throw new RuntimeException("Nicht diagonal");
		}

		//PrÃ¼fen, ob beide Spieler noch Spielfiguren haben---------------
		isSpielende();
		//-------------------------------------------------------
	}

	//###############################		SPIELER WECHSEL  	#########################################
	
	public void spielerWechsel() {
		

	
		
		if (spielerA.getAktiv() == true) {
			spielerA.setAktiv(false);
			spielerB.setAktiv(true);
			spielerAktiv = spielerB;
			spielerMussSpringen();
	
		} else {
			spielerA.setAktiv(true);
			spielerB.setAktiv(false);
			spielerAktiv = spielerA;
			spielerMussSpringen();
			
		}
	}

	/**
	 * Speichert die aktuelle Belegung des Spielbretts in CSV-Notation.
	 * Speicherort: savegame/savegame.csv
	 */
	@Override
	public void belegungCSV() {

		try {
			pw = new PrintWriter(new FileWriter("savegame/belegung.csv"));
		} catch (FileNotFoundException e) {
			System.err.println("DATEI ZUM SPEICHERN NICHT GEFUNDEN!");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Spielfeld[][] belegung = brett.getNotation();
		for (int i = this.brettArray.length - 1; i >= 0; i--) {
			for (int n = 0; n < belegung[i].length; n++) {
				pw.print(belegung[n][i].getAusgabeID() + "\t");
			}
			pw.println();
		}
		
		pw.close();
	}
	
	
	//#############################################################		SPIELER MUSS SPRINGEN ()  	#######################################################################
	
	private void spielerMussSpringen(){ 
		
		//Bevor Spieler gewechselt wird und alles neu ï¿½berprï¿½ft wird, setze bei allen Steinen kannSchlagen auf false. Auch spielerA,B mussSpringen() wird auf false zurï¿½ckgesetzt. 
			
		this.sprungKonflikt=0;
		
		for(int i=0; i<this.brettArray.length; i++){
			for(int j=0; j<this.brettArray[i].length; j++){
				if(this.brettArray[i][j].getFigur()!=null){
					spielerA.setMussSpringen(false);
					spielerB.setMussSpringen(false);
					this.brettArray[i][j].getFigur().setKannSpringen(false); //Kennt nun keine Spielfigur mehr, die Springen kann
				}
			}
		}
		
		//----------------------------------KANN SPIELER A MIT EINER FIGUR SPRINGEN??--------------------------
		if(spielerA.getAktiv()==true){
			
		for(int i=0; i<this.brettArray.length; i++){
			for(int j=0; j<this.brettArray[i].length; j++){
				if(this.brettArray[i][j].getFigur()!=null && this.brettArray[i][j].getFigur().getFarbe()==FarbEnum.schwarz){
					Spielfigur testSpieler=this.brettArray[i][j].getFigur();
					
		

					int links=testSpieler.getPosition().getPosX()-1;
					int rechts=testSpieler.getPosition().getPosX()+1;
					int oben=testSpieler.getPosition().getPosY()+1;
					int unten=testSpieler.getPosition().getPosY()-1;
					
					int n = 0;
					
					//Fall OBEN LINKS ist eine Figur die ich ï¿½berspringen kann!
					
						while (this.brettArray.length-(links-n) > 1 && this.brettArray.length-(links-n) <this.brettArray.length && this.brettArray.length-(oben+n) < brettArray.length && this.brettArray.length-(oben+n) > 1&&testSpieler.isDame() == true&&brettArray[links-n][oben+n].getFigur() == null) {
							
								n++;
							}
							
							
					
							if (this.brettArray.length-(links-n) > 1 && this.brettArray.length-(links-n) <this.brettArray.length && this.brettArray.length-(oben+n) < brettArray.length && this.brettArray.length-(oben+n) > 1 && brettArray[links-n][oben+n].getFigur() != null
									&& brettArray[links-n-1][oben+n+1].getFigur() == null) {
								if (brettArray[links-n][oben+n].getFigur().getFarbe() == FarbEnum.weiss) {


								brettArray[testSpieler.getPosition().getPosX()][testSpieler.getPosition().getPosY()]
										.getFigur().setKannSpringen(true);

								this.spielerA.setMussSpringen(true);
								System.out.println(
										"Sprungmoeglichkeit!");
								
							
							}
						
				
							}
		
		
						//Fall OBEN RECHTS ist eine Figur die ich ï¿½berspringen kann!
						
							while (this.brettArray.length-(rechts+n) > 1 && this.brettArray.length-(oben+n) > 1 && this.brettArray.length-(oben+n) < this.brettArray.length && this.brettArray.length-(rechts+n) < this.brettArray.length  &&testSpieler.isDame() == true&&brettArray[rechts+n][oben+n].getFigur() == null) {
								
								n++;
							}
							
							
					
							if (this.brettArray.length-(rechts+n) > 1 && this.brettArray.length-(oben+n) > 1 && this.brettArray.length-(oben+n) < this.brettArray.length && this.brettArray.length-(rechts+n) < this.brettArray.length  && brettArray[rechts+n][oben+n].getFigur() != null
									&& brettArray[rechts+n+1][oben+n+1].getFigur() == null) {
								if (brettArray[rechts+n][oben+n].getFigur().getFarbe() == FarbEnum.weiss) {


								brettArray[testSpieler.getPosition().getPosX()][testSpieler.getPosition().getPosY()]
										.getFigur().setKannSpringen(true);

								this.spielerA.setMussSpringen(true);
								System.out.println(
										"Sprungmoeglichkeit!");
							
							}
						
				
							}
						//Fall UNTEN RECHTS ist eine Figur die ich ï¿½berspringen kann!
							
							while (this.brettArray.length-(rechts+n) > 1 &&this.brettArray.length-(unten-n) > 1 && this.brettArray.length-(unten-n) < this.brettArray.length && this.brettArray.length-(rechts+n) < this.brettArray.length &&testSpieler.isDame() == true&&brettArray[rechts+n][unten-n].getFigur() == null) {
								
								n++;
							}
							
							
					
							if (this.brettArray.length-(rechts+n) > 1 && this.brettArray.length-(unten-n) < this.brettArray.length  && brettArray[rechts+n][unten-n].getFigur() != null
									&& brettArray[rechts+n+1][unten-n-1].getFigur() == null) {
								if (brettArray[rechts+n][unten-n].getFigur().getFarbe() == FarbEnum.weiss) {


								brettArray[testSpieler.getPosition().getPosX()][testSpieler.getPosition().getPosY()]
										.getFigur().setKannSpringen(true);

								this.spielerA.setMussSpringen(true);
								System.out.println(
										"Sprungmoeglichkeit!");
								
							}
						
				
							}
						//Fall UNTEN LINKS ist eine Figur die ich ï¿½berspringen kann!
							
							while (this.brettArray.length-(links-n) <this.brettArray.length && this.brettArray.length-(unten-n) < this.brettArray.length && this.brettArray.length-(links-n) > 1 && this.brettArray.length-(unten-n)>1 &&testSpieler.isDame() == true&&brettArray[links-n][unten-n].getFigur() == null) {
								
								n++;
							}
							
							
					
							if (this.brettArray.length-(links-n) <this.brettArray.length && this.brettArray.length-(unten-n) < this.brettArray.length && this.brettArray.length-(links-n) > 1 && this.brettArray.length-(unten-n)>1 && brettArray[links-n][unten-n].getFigur() != null
									&& brettArray[links-n-1][unten-n-1].getFigur() == null) {
								if (brettArray[links-n][unten-n].getFigur().getFarbe() == FarbEnum.weiss) {


								brettArray[testSpieler.getPosition().getPosX()][testSpieler.getPosition().getPosY()]
										.getFigur().setKannSpringen(true);

								this.spielerA.setMussSpringen(true);
								System.out.println(
										"Sprungmoeglichkeit!");
								
							}
						
				
							}
							if(testSpieler.getKannSpringen()==true){ //Erhï¿½he einmal sprungKonflikt sobald dieser Stein eine Springmï¿½glichkeit kennt. So ist garantiert dass bei 2 steinen 2 Springmï¿½gl. mï¿½glich sind!
							this.sprungKonflikt++;
							}
					}
				
			

			}
		}
		if(spielerA.getMussSpringen()==false) {
			System.out.println("Alle ihre Figuren sind im moment spielbar!");
		}
		
		}
		
		
		//----------------------------------KANN SPIELER B MIT EINER FIGUR SPRINGEN??--------------------------
		
		if(spielerB.getAktiv()==true){
			
			for(int i=0; i<this.brettArray.length; i++){
				for(int j=0; j<this.brettArray[i].length; j++){
					if(this.brettArray[i][j].getFigur()!=null && this.brettArray[i][j].getFigur().getFarbe()==FarbEnum.weiss){
						Spielfigur testSpieler=this.brettArray[i][j].getFigur();
						
			

						int links=testSpieler.getPosition().getPosX()-1;
						int rechts=testSpieler.getPosition().getPosX()+1;
						int oben=testSpieler.getPosition().getPosY()+1;
						int unten=testSpieler.getPosition().getPosY()-1;
						
						int n = 0;
						
						//Fall OBEN LINKS ist eine Figur die ich ï¿½berspringen kann!
						
							while (this.brettArray.length-(links-n) > 1 && this.brettArray.length-(links-n) <this.brettArray.length && this.brettArray.length-(oben+n) < brettArray.length && this.brettArray.length-(oben+n) > 1&&testSpieler.isDame() == true&&brettArray[links-n][oben+n].getFigur() == null) {
								
									n++;
								}
								
								
						
								if (this.brettArray.length-(links-n) > 1 && this.brettArray.length-(links-n) <this.brettArray.length && this.brettArray.length-(oben+n) < brettArray.length && this.brettArray.length-(oben+n) > 1 && brettArray[links-n][oben+n].getFigur() != null
										&& brettArray[links-n-1][oben+n+1].getFigur() == null) {
									if (brettArray[links-n][oben+n].getFigur().getFarbe() == FarbEnum.schwarz) {


									brettArray[testSpieler.getPosition().getPosX()][testSpieler.getPosition().getPosY()]
											.getFigur().setKannSpringen(true);

									this.spielerB.setMussSpringen(true);
									System.out.println(
											"Sprungmoeglichkeit!");
								
								}
							
					
								}
			
			
							//Fall OBEN RECHTS ist eine Figur die ich ï¿½berspringen kann!
							
								while (this.brettArray.length-(rechts+n) > 1 && this.brettArray.length-(oben+n) > 1 && this.brettArray.length-(oben+n) < this.brettArray.length && this.brettArray.length-(rechts+n) < this.brettArray.length  &&testSpieler.isDame() == true&&brettArray[rechts+n][oben+n].getFigur() == null) {
									
									n++;
								}
								
								
						
								if (this.brettArray.length-(rechts+n) > 1 && this.brettArray.length-(oben+n) > 1 && this.brettArray.length-(oben+n) < this.brettArray.length && this.brettArray.length-(rechts+n) < this.brettArray.length  && brettArray[rechts+n][oben+n].getFigur() != null
										&& brettArray[rechts+n+1][oben+n+1].getFigur() == null) {
									if (brettArray[rechts+n][oben+n].getFigur().getFarbe() == FarbEnum.schwarz) {


									brettArray[testSpieler.getPosition().getPosX()][testSpieler.getPosition().getPosY()]
											.getFigur().setKannSpringen(true);

									this.spielerB.setMussSpringen(true);
									System.out.println(
											"Sprungmoeglichkeit!");
									
								}
							
					
								}
							//Fall UNTEN RECHTS ist eine Figur die ich ï¿½berspringen kann!
								
								while (this.brettArray.length-(rechts+n) > 1 &&this.brettArray.length-(unten-n) > 1 && this.brettArray.length-(unten-n) < this.brettArray.length && this.brettArray.length-(rechts+n) < this.brettArray.length &&testSpieler.isDame() == true&&brettArray[rechts+n][unten-n].getFigur() == null) {
									
									n++;
								}
								
								
						
								if (this.brettArray.length-(rechts+n) > 1 && this.brettArray.length-(unten-n) < this.brettArray.length  && brettArray[rechts+n][unten-n].getFigur() != null
										&& brettArray[rechts+n+1][unten-n-1].getFigur() == null) {
									if (brettArray[rechts+n][unten-n].getFigur().getFarbe() == FarbEnum.schwarz) {


									brettArray[testSpieler.getPosition().getPosX()][testSpieler.getPosition().getPosY()]
											.getFigur().setKannSpringen(true);

									this.spielerB.setMussSpringen(true);
									System.out.println(
											"Sprungmoeglichkeit!");
									
								}
							
					
								}
							//Fall UNTEN LINKS ist eine Figur die ich ï¿½berspringen kann!
								
								while (this.brettArray.length-(links-n) <this.brettArray.length && this.brettArray.length-(unten-n) < this.brettArray.length && this.brettArray.length-(links-n) > 1 && this.brettArray.length-(unten-n)>1 &&testSpieler.isDame() == true&&brettArray[links-n][unten-n].getFigur() == null) {
									
									n++;
								}
								
								
						
								if (this.brettArray.length-(links-n) <this.brettArray.length && this.brettArray.length-(unten-n) < this.brettArray.length && this.brettArray.length-(links-n) > 1 && this.brettArray.length-(unten-n)>1 && brettArray[links-n][unten-n].getFigur() != null
										&& brettArray[links-n-1][unten-n-1].getFigur() == null) {
									if (brettArray[links-n][unten-n].getFigur().getFarbe() == FarbEnum.schwarz) {


									brettArray[testSpieler.getPosition().getPosX()][testSpieler.getPosition().getPosY()]
											.getFigur().setKannSpringen(true);

									this.spielerB.setMussSpringen(true);
									System.out.println(
											"Sprungmoeglichkeit!");
							
								}
							
					
								}
								if(testSpieler.getKannSpringen()==true){ //Erhï¿½he einmal sprungKonflikt sobald dieser Stein eine Springmï¿½glichkeit kennt. So ist garantiert dass bei 2 steinen 2 Springmï¿½gl. mï¿½glich sind!
									this.sprungKonflikt++;
									}
						}
					
					

				}
			}

			if (spielerB.getMussSpringen() == false) {
				System.out.println("Alle ihre Figuren sind im moment spielbar!");
			}
		}

	}
	
	//############################################################################# ENDE SPIELER MUSS SPRINGEN ##################################################
	
	public void setBrettArray(Spielfeld[][] brettArray) {
		this.brettArray = brettArray;
	}


	public Spieler getSpielerA() {
		return spielerA;
	}

	public Spieler getSpielerB() {
		return spielerB;
	}

	public Spieler getSpielerAktiv() {
		return spielerAktiv;
	}

	public Spielbrett getBrett() {
		return brett;
	}

	public void isSpielende() {
		int weiss = 0;
		int schwarz = 0;
		for (int i = 0; i < this.brettArray.length; i++) {
			for (int n = 0; n < this.brettArray[i].length; n++) {
				if (this.brettArray[i][n].getFigur() != null) {
					if (this.brettArray[i][n].getFigur().getFarbe() == FarbEnum.schwarz) {
						schwarz++;
					} else if (this.brettArray[i][n].getFigur().getFarbe() == FarbEnum.weiss) {
						weiss++;
					}
				}
			}
		}
		if (schwarz == 0) {
			this.winner = spielerB;
			this.spielende = true;
		} else if (weiss == 0) {
			this.winner = spielerA;
			this.spielende = true;
		}

	}
}
