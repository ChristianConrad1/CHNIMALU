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
	private int sprungKonflikt=0; //Falls 2 Steinchen in Schlagmoeglichkeit kommen entsteht ein Konflikt! (zaehlt hoch, Konflikt ab 2!)
	private Scanner sc; 

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
		// weisse Steine ([o])
		int x = 1;
		for (int n = brettArray.length - 1; n > (brettArray.length - 1) - this.reihen; n--) { // (n-3) da rueckwaerts gezaehlt wird
																						
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
	 * bewegt eine Spielfigur von gew�hltem Feld auf ein anderes g�ltiges Feld
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

		spielerMussSpringen();

		// �berpr�fe ob unsere �bergebenen Koordinaten in unserem Array-Feld
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

		// ------------------------------W�hle Spielfigur--------------------------
		try {
			if (spielerAktiv.getAktiv() == true && spielerAktiv != null) {

				if (brettArray[a][b].getHatFigur() == true
						&& brettArray[a][b].getFigur().getFarbe() == farbeAktiv) {
					this.aktiveSpielfigur = brettArray[a][b].getFigur();
					this.aktiveSpielfigur.setPosition(brettArray[a][b]);
				} else {
					throw new RuntimeException(
							"Keine schwarze Spielfigur ausgew�hlt!");
				}
			}

		} catch (Exception e) {
			System.err.println(e);

		}

		// ------------------------------------------------------ZIELFELD (Hier soll Stein als n�chstes hin!)----------------------------------------------------------------------

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
					int Dx = 0; //Variable fuer X Verschiebung
					int Dy = 0; //Variable fuer Y Verschiebung

					// CASE: LINKS OBEN:
					if (c == links && d == oben) {
						koordX = links;
						koordY = oben;
						koordX1 = links - 1;
						koordY1 = oben + 1;
						Dx= -1; //Fuer die Verschiebung in oder entgegen X Richtung 
						Dy= 1;	//Fuer die Verschiebung in oder entgegen Y Richtung
					}
					// CASE: RECHTS OBEN:
					if (c == rechts && d == oben) {
						koordX = rechts;
						koordY = oben;
						koordX1 = rechts + 1;
						koordY1 = oben + 1;
						Dx= 1; //Fuer die Verschiebung in oder entgegen X Richtung 
						Dy= 1;	//Fuer die Verschiebung in oder entgegen Y Richtung
					}
					// CASE: LINKS UNTEN:
					if (c == links && d == unten) {
						koordX = links;
						koordY = unten;
						koordX1 = links - 1;
						koordY1 = unten - 1;
						Dx= -1; //Fuer die Verschiebung in oder entgegen X Richtung 
						Dy= -1;	//Fuer die Verschiebung in oder entgegen Y Richtung
					}
					// CASE: RECHTS UTNEN:
					if (c == rechts && d == unten) {
						koordX = rechts;
						koordY = unten;
						koordX1 = rechts + 1;
						koordY1 = unten - 1;
						Dx= +1; //Fuer die Verschiebung in oder entgegen X Richtung 
						Dy= -1;	//Fuer die Verschiebung in oder entgegen Y Richtung
					}
			
					if (spielerAktiv.getMussSpringen() == true) {
						
						if (aktiveSpielfigur.getKannSpringen() == true) {
							// ----------------------------------SPRINGEN IN
							// EINE VON VIER
							// RICHTUNGEN---------------------------------------

							// Spezialfall: Gew�hlte Spielfigur hat Sprung,
							// nutzt diesen aber nicht! L�sche sie!!
							if (aktiveSpielfigur.getKannSpringen() == true
									&& spielerAktiv.getMussSpringen()
									&& this.brettArray[c][d].getFigur() == null) {
								this.brettArray[a][b].getFigur().getPosition()
								.removeFigur();
								System.out
								.println("UPS! Sie haben einen Stein mit Schlagm�glichkeit gew�hlt, sind dieser jedoch nicht nachgegangen! Der Stein wurde zur Bestrafung vom Spielfeld entfernt!");

							}
							// -------------------------------------------------------SPRINGEN----------------------------------------------------

							// ---------------------------------AUF�HRUNG DER SPRUNG CASES:------------------------------------
							if (c == koordX && d == koordY) {
								while (aktiveSpielfigur.getKannSpringen() == true) {
									if (this.brettArray[koordX][koordY].getHatFigur() == true&& this.brettArray[koordX][koordY].getFigur().getFarbe() == farbeGegner
													&& this.brettArray[koordX1][koordY1].getHatFigur() == false) {
										
										
										this.aktiveSpielfigur.getPosition().removeFigur();
										this.brettArray[koordX][koordY].getFigur().getPosition().removeFigur();
										this.brettArray[koordX1][koordY1].setFigur(aktiveSpielfigur);
										aktiveSpielfigur.setPosition(this.brettArray[koordX1][koordY1]);
										
										System.out.println("Zug vollendet, muss allerdings nochmal springen wenn nochmal kann!");

										spielerAktiv.setMussSpringen(false);
										aktiveSpielfigur.setKannSpringen(false);

										spielerMussSpringen(); //ueberprueft nochmals alle Steine auf moegliche Spruenge
										
																			
										//HIER MUSS NOCH EIN BUG GEFIXT WERDEN BEIM DOPPELT SPRINGEN! noch nicht ueberschrieben
										aktiveSpielfigur = brettArray[koordX1][koordY1].getFigur(); //Weise neue Steinposition zu, damit im Falle erneuter Sprungmoeg
										System.out.println(aktiveSpielfigur);
										System.out.println(aktiveSpielfigur.getPosition().getPosX() +" "+ aktiveSpielfigur.getPosition().getPosY());
										koordX = aktiveSpielfigur.getPosition().getPosX() +Dx;
										koordY = aktiveSpielfigur.getPosition().getPosY() +Dy;
										koordX1 = koordX+Dx;
										koordY1 = koordY+Dy;
										
										System.out.println(koordX);
										System.out.println(koordY);

										
										if (aktiveSpielfigur.getKannSpringen() == true) {
											System.out.println("Der selbe Stein konnte weitere Steine �berspringen!");
										}

										System.out.println(aktiveSpielfigur.getKannSpringen());
										System.out.println("Sprung vollendet nach Links-Oben");

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
							if (this.sprungKonflikt > 1) { //Im Moment haben wir 2 Steine die Springen koennen

								spielerAktiv.setAktiv(false);
								spielerGegner.setAktiv(true);//Spielerwechsel fuer Eingabe, dann zueruck zum aktuellen Spieler!
								System.out
								.println("Sie hatten mehrere Steine mit Sprungmoeglichkeiten! Spieler B, Sie duerfen nun eines dieser Steinchen von Spieler A w�hlen,so dass dieses vom Feld gel�scht wird!");

								String eingabe = sc.nextLine();
								brett.Umwandler(eingabe);
								int x = brett.getKoordX();
								int y = brett.getKoordY();
								if (this.brettArray[x][y].getFigur() != null) {
									if (this.brettArray[x][y].getFigur().getFarbe() == farbeAktiv&& this.brettArray[x][y].getFigur().getKannSpringen()) {
										this.brettArray[x][y].removeFigur();

									}
								} else {
									throw new RuntimeException(
											"Waehlen sie bitte nur eine der Steine die in beim vorhergehenden Zug eine Sprungmoeglichkeit hatten, die von ihrem Gegner nicht wahrgenommen wurde!");
								}

								spielerAktiv.setAktiv(true); //Eingabe Beendet, zueruck zum aktuellen Spieler!
								spielerGegner.setAktiv(false);

							} else {
								// Gibt es nur eine Spielfigur die springen kann, suchen wir diese einfach in unserem Array
								for (int i = 0; i < this.brettArray.length; i++) {
									for (int j = 0; j < this.brettArray[i].length; j++) {
										if (this.brettArray[i][j].getFigur() != null
												&& this.brettArray[i][j].getFigur().getFarbe() == farbeAktiv
														&& this.brettArray[i][j].getFigur().getKannSpringen()) {

											Spielfigur testSpieler = this.brettArray[i][j].getFigur();

											testSpieler.getPosition().removeFigur();

											testSpieler = null;
											System.out.println("Jetzt wird bestraft!! Sie hatten eine Sprungmoeglichkeit! Diese wurde nicht wahrgenommen, der Stein wurde entfernt!"); // danach
											
										}
									}
								}
							}
						}

						aktiveSpielfigur = null;
						spielerAktiv.setMussSpringen(false);
						spielerWechsel();

					}
					
					else if(spielerAktiv.getMussSpringen()==false){
						throw new RuntimeException("Waehlen sie bitte ein Feld, auf das sie ziehen koennen!");
					}
					
				}
					
				else if(spielerAktiv.getMussSpringen()==true){
					throw new RuntimeException("Waehlen sie bitte eine Figur die springen kann, bzw ein Feld das bespringbar ist!!");
				
				}
				
			}
			
			
	}catch (Exception e) {
		System.err.println(e);
			
	}
		
		isSpielende();	//Prueft ob beide Spieler noch Spielfiguren haben
	
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

		//Pruefen ob beide Spieler noch Spielfiguren haben---------------
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
	//#########################		BEREITS NOCHMAL KURZ UND SCHOEN GESCHRIEBEN - NOCH NICHT IMPLEMENTIERT DA NOCH FEHLER AUSGEARBEITET WERDEN MUESSEN

	private void spielerMussSpringen() {

		// Bevor Spieler gewechselt wird und alles neu �berpr�ft wird, setze
		// bei allen Steinen kannSchlagen auf false. Auch spielerA,B
		// mussSpringen() wird auf false zur�ckgesetzt.

		this.sprungKonflikt = 0;

		Spieler spielerAktiv = null;
		FarbEnum farbeAktiv = null;
		FarbEnum farbeGegner = null;

		if (spielerA.getAktiv() == true) {
			spielerAktiv = spielerA;
			farbeAktiv = FarbEnum.schwarz;
			farbeGegner = FarbEnum.weiss;
		}
		if (spielerB.getAktiv() == true) {
			spielerAktiv = spielerB;
			farbeAktiv = FarbEnum.weiss;
			farbeGegner = FarbEnum.schwarz;
		}

		for (int i = 0; i < this.brettArray.length; i++) {
			for (int j = 0; j < this.brettArray[i].length; j++) {
				if (this.brettArray[i][j].getFigur() != null) {
					spielerA.setMussSpringen(false);
					spielerB.setMussSpringen(false);
					this.brettArray[i][j].getFigur().setKannSpringen(false); // Kennt nun keine Figur mehr die springen kann

				}
			}
		}

		// ----------------------------------KANN SPIELER A MIT EINER FIGUR
		// SPRINGEN??--------------------------
		if (spielerAktiv.getAktiv() == true) {

			for (int i = 0; i < this.brettArray.length; i++) {
				for (int j = 0; j < this.brettArray[i].length; j++) {
					if (this.brettArray[i][j].getFigur() != null
							&& this.brettArray[i][j].getFigur().getFarbe() == farbeAktiv) {
						Spielfigur testSpieler = this.brettArray[i][j]
								.getFigur();

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
									&& testSpieler.isDame() == true
									&& brettArray[coordX][coordY].getFigur() == null) {

								    coordX+=a;
									coordY+=b;
														
								}
								
							
						
						if (this.brettArray.length - (coordX) > 1
								&& this.brettArray.length - (coordX) < this.brettArray.length
								&& this.brettArray.length - (coordY) < brettArray.length
								&& this.brettArray.length - (coordY) > 1
								&& brettArray[coordX][coordY].getFigur() != null
								&& brettArray[coordX + a][coordY + b].getFigur() == null) {
							if (brettArray[coordX][coordY].getFigur().getFarbe() == farbeGegner) {

								testSpieler.setKannSpringen(true);
								this.spielerAktiv.setMussSpringen(true);
								System.out.println("Sprungmoeglichkeit!");

							}
							
						}
						caseNumber++;
						

						
						}
						if (testSpieler.getKannSpringen() == true) {//Erh�he pro Stein der Schlagm�glichkeit 'Sprungkonflikt' -> mehrere Male bedeutet somit mehrere Steine mit Sprungm�g.
							this.sprungKonflikt++;
						}
					}
				}
				}
			
			if (spielerAktiv.getMussSpringen() == false) {
				System.out
				.println("Alle ihre Figuren sind im moment spielbar!");
			}
		}
		}
	

	// #############################################################################
	// ENDE SPIELER MUSS SPRINGEN
	// ##################################################
	
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
