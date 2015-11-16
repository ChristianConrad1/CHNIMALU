package Basisklassen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import GUI.Sounds;
import Interfaces.iBediener;
import Interfaces.iDatenzugriff;
import Interfaces.iMessage;
import SpeichernLaden.DatenzugriffCSV;
import SpeichernLaden.DatenzugriffSerialisiert;

public class Spiel implements iBediener, Serializable {
	private iMessage msg;

	private boolean darfPusten;
	private boolean hatGeschlagen;
	private boolean warKi;
	private ArrayList<Spielfigur> figurenListe;                                                   
	
	private Spieler winner;
	private Spieler spielerA;
	private Spieler spielerB;       
	private Spieler spielerAktiv; // der spieler der gerade am zug ist, nicht zu
									// verwechseln mit der aktiven Spielfigur
	private Spielbrett brett;
	private Spielfigur aktiveSpielfigur = null; // Setze immer null nach Zug
	private Spielfeld[][] brettArray;
	private boolean spielende = false;
	private final int reihen = 4;
	private int sprungKonflikt = 0; // Falls 2 Steinchen in Schlagmoeglichkeit
									// kommen entsteht ein Konflikt! (zaehlt
									// hoch, Konflikt ab 2!)
	private FarbEnum farbeAktiv = null;
	private FarbEnum farbeGegner = null;
	private transient Scanner sc;

	private transient Sounds sound;

	public Spiel() {
		this.brett=new Spielbrett();

		this.brettArray = brett.getNotation();
		setAlleFiguren();
		sc = new Scanner(System.in);
		sound = new Sounds();
		
		figurenListe = new ArrayList<Spielfigur>();
	}

	public void setAlleFiguren() {
		boolean neuesSpiel = true;
		if (brett == null) {
			throw new NullPointerException("Spielbrett ist null!");
		}

		for (int i = 0; i < brettArray.length; i++) { // Komplettes Array
														// durchlaufen und
														// schauen, ob schon
														// spielfiguren stehen,
														// falls ja ist es kein
														// neues spiel
			for (int n = 0; n < brettArray[i].length; n++) {
				if (brettArray[i][n].getHatFigur()) {
					neuesSpiel = false;
					return;
				}
			}
		}
		if (neuesSpiel) { // Soll nicht gemacht werden
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
			for (int n = brettArray.length - 1; n > (brettArray.length - 1) - this.reihen; n--) { // (n-3)
																									// da
																									// rueckwaerts
																									// gezaehlt
																									// wird

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

	}

	/**
	 * bewegt eine Spielfigur von gew�hltem Feld auf ein anderes g�ltiges Feld
	 * 
	 * @param String1
	 *            eingabe Feld von Spielfigur
	 * @param String2
	 *            eingabe Zielfeld
	 */

	// **************************************BEWEGE SPIELFIGUR
	// METHODE********************************************
	public void bewegeSpielfigur(String s1, String s2) {
		brett.Umwandler(s1);
		int a = brett.getKoordX();
		int b = brett.getKoordY();
		brett.Umwandler(s2);
		int c = brett.getKoordX();
		int d = brett.getKoordY();

		if (!hatGeschlagen) {
			if (figurenListe.size() == 1) {
				System.out.println("Spielfigur mit ID: "+figurenListe.get(0).getPosition().getID() +" wird ENTFERNT!");
				figurenListe.get(0).getPosition().removeFigur();
				figurenListe.clear();
				warKi = false;
			} else if (!figurenListe.isEmpty()) {
				if(warKi){
					System.out.println("Spielfigur mit ID: "+figurenListe.get(0).getPosition().getID() +" wird ENTFERNT!");
					figurenListe.get(0).getPosition().removeFigur();
					figurenListe.clear();
					warKi = false;
				}
				else{
				darfPusten = true;
				msg.printPusten("Der " + farbeGegner.toString() + "e Spieler hatte mehrere Schlagmöglichkeiten!"
						+ "\nWählen Sie einen Stein des Gegners, der entfernt werden soll.");
				}
			for(int f = 0; f < figurenListe.size()-1; f++){
				figurenListe.get(f).setInListe(false);
			}
			}
		}
		//figurenListe.clear();
		//hatGeschlagen = false;
		spielerMussSpringen();

		// �berpr�fe ob unsere �bergebenen Koordinaten in unserem Array-Feld
		// enthalten sind
		if (a < 0 | a > this.brettArray.length - 1) {
			throw new RuntimeException("Ihre X Koordinate ist nicht in unserem Spielfeld!");
		}
		if (b < 0 | b > this.brettArray.length - 1) {
			throw new RuntimeException("Ihre Y Koordinate ist nicht in unserem Spielfeld!");
		}

		//Spieler spielerAktiv = null; //WAS SOLL DIESER SCHEISS?
//		Spieler spielerGegner = null;
//		FarbEnum farbeAktiv = null;
//		FarbEnum farbeGegner = null;
//		farbeAktiv = this.spielerAktiv.getFarbe();
//		if (spielerA.getAktiv() == true) {
//			spielerAktiv = spielerA;
//			spielerGegner = spielerB;
//			farbeAktiv = FarbEnum.schwarz;
//			farbeGegner = FarbEnum.weiss;
//		}
//		if (spielerB.getAktiv() == true) {
//			spielerAktiv = spielerB;
//			spielerGegner = spielerA;
//			farbeAktiv = FarbEnum.weiss;
//			farbeGegner = FarbEnum.schwarz;
//		}

		// ------------------------------W�hle
		// Spielfigur--------------------------
		try {
			if (spielerAktiv.getAktiv() == true && spielerAktiv != null) {
				if (brettArray[a][b].getFigur() != null && brettArray[a][b].getFigur().getFarbe() == farbeAktiv) {
					this.aktiveSpielfigur = brettArray[a][b].getFigur();
					this.aktiveSpielfigur.setPosition(brettArray[a][b]);
				} else {
					msg.printError("Keine " + this.spielerAktiv.getFarbe() + "e Spielfigur ausgew�hlt!");
					throw new RuntimeException("Keine " + this.spielerAktiv.getFarbe() + "e Spielfigur ausgew�hlt!");
					
				}
			}

		} catch (Exception e) {
			//msg.printError(e.toString());
			System.err.println(e);
			// System.err.println(e.getMessage());
			// e.printStackTrace();

		}

		// ------------------------------------------------------ZIELFELD (Hier
		// soll Stein als n�chstes
		// hin!)----------------------------------------------------------------------

		if (c < 0 | c > this.brettArray.length - 1) {
			throw new RuntimeException("Ihre X Koordinate ist nicht in unserem Spielfeld!");
		}

		if (d < 0 | d > this.brettArray.length - 1) {
			throw new RuntimeException("Ihre Y Koordinate ist nicht in unserem Spielfeld!");
		}
		try {

			if (this.aktiveSpielfigur != null) {

				if (spielerAktiv.getAktiv() == true) {
					if (this.aktiveSpielfigur.isDame() == true) {
						bewegeDame(c, d);
					}

					if (this.aktiveSpielfigur != null) { // ist diese Immernoch
															// != null? (nach
															// bewege Dame z.B.
															// n�tig)

						int links = aktiveSpielfigur.getPosition().getPosX() - 1;
						int rechts = aktiveSpielfigur.getPosition().getPosX() + 1;
						int oben = aktiveSpielfigur.getPosition().getPosY() + 1;
						int unten = aktiveSpielfigur.getPosition().getPosY() - 1;

						int koordX = 0;
						int koordY = 0;
						int koordX1 = 0;
						int koordY1 = 0;
						int Dx = 0; // Variable fuer X Verschiebung
						int Dy = 0; // Variable fuer Y Verschiebung

						// CASE: LINKS OBEN:
						if (c == links && d == oben) {
							koordX = links;
							koordY = oben;
							koordX1 = links - 1;
							koordY1 = oben + 1;
							Dx = -1; // Fuer die Verschiebung in oder entgegen X
										// Richtung
							Dy = 1; // Fuer die Verschiebung in oder entgegen Y
									// Richtung
						}
						// CASE: RECHTS OBEN:
						if (c == rechts && d == oben) {
							koordX = rechts;
							koordY = oben;
							koordX1 = rechts + 1;
							koordY1 = oben + 1;
							Dx = 1; // Fuer die Verschiebung in oder entgegen X
									// Richtung
							Dy = 1; // Fuer die Verschiebung in oder entgegen Y
									// Richtung
						}
						// CASE: LINKS UNTEN:
						if (c == links && d == unten) {
							koordX = links;
							koordY = unten;
							koordX1 = links - 1;
							koordY1 = unten - 1;
							Dx = -1; // Fuer die Verschiebung in oder entgegen X
										// Richtung
							Dy = -1; // Fuer die Verschiebung in oder entgegen Y
										// Richtung
						}
						// CASE: RECHTS UTNEN:
						if (c == rechts && d == unten) {
							koordX = rechts;
							koordY = unten;
							koordX1 = rechts + 1;
							koordY1 = unten - 1;
							Dx = +1; // Fuer die Verschiebung in oder entgegen X
										// Richtung
							Dy = -1; // Fuer die Verschiebung in oder entgegen Y
										// Richtung
						}

						if (spielerAktiv.getMussSpringen() == true) {

							if (aktiveSpielfigur.getKannSpringen() == true) {
								// ----------------------------------SPRINGEN IN
								// EINE VON VIER
								// RICHTUNGEN---------------------------------------

								// Spezialfall: Gew�hlte Spielfigur hat Sprung,
								// nutzt diesen aber nicht! L�sche sie!!

								// -------------------------------------------------------SPRINGEN----------------------------------------------------

								// ---------------------------------AUF�HRUNG
								// DER SPRUNG
								// CASES:------------------------------------
								///////////////////////////////////////////////////////////////////////////////////
								//Von Hier 
								//bis normales Ziehen fehlt noch eine abbruchbedinung, 
								//sonst bleibt er in der While, wenn man eine Spielfigur bewegt, die eigentlich schlagen könnte
								if (c == koordX && d == koordY) {
									if (this.brettArray[koordX][koordY].getFigur() != null
											&& this.brettArray[koordX][koordY].getFigur()
													.getFarbe() == farbeGegner) {
									while (aktiveSpielfigur.getKannSpringen() == true
											&& this.spielerAktiv.getMussSpringen() == true
											&& this.brettArray[koordX1][koordY1].getFigur() == null) {
//										if (this.brettArray[koordX][koordY].getFigur() != null
//												&& this.brettArray[koordX][koordY].getFigur()
//														.getFarbe() == farbeGegner) {

											this.aktiveSpielfigur.getPosition().removeFigur();
											this.brettArray[koordX][koordY].removeFigur();
											this.brettArray[koordX1][koordY1].setFigur(aktiveSpielfigur);
											aktiveSpielfigur.setPosition(this.brettArray[koordX1][koordY1]);
											sound.schlagSound();
											hatGeschlagen = true;
											msg.printOk(this.spielerAktiv.getName()  + " hat Sprung von " +s1 + " nach " +s2 +" ausgeführt.");
											
											System.out.println(
													"Zug vollendet, muss allerdings nochmal springen wenn nochmal kann!");
											spielerAktiv.setMussSpringen(false);
											aktiveSpielfigur.setKannSpringen(false);
											aktiveSpielfigur = brettArray[koordX1][koordY1].getFigur(); // Weise
																										// neue
																										// Steinposition
																										// zu,
																										// fuer
																										// erneutes
																										// Springen

											spielerMussSpringen();

											if (aktiveSpielfigur.getKannSpringen() == true) {
												System.out.println(
														"Der selbe Stein konnte weitere Steine ueberspringen!");

												boolean[] sprungCases = aktiveSpielfigur.getSprungCases();
												if (sprungCases[0] == true) { // spring
																				// als
																				// naechstes
																				// nach
																				// links
																				// oben
													koordX = aktiveSpielfigur.getPosition().getPosX() - 1;
													koordY = aktiveSpielfigur.getPosition().getPosY() + 1;
													koordX1 = koordX - 1;
													koordY1 = koordY + 1;
												} else if (sprungCases[1] == true) { // spring
																						// als
																						// naechstes
																						// nach
																						// rechts
																						// oben
													koordX = aktiveSpielfigur.getPosition().getPosX() + 1;
													koordY = aktiveSpielfigur.getPosition().getPosY() + 1;
													koordX1 = koordX + 1;
													koordY1 = koordY + 1;
												} else if (sprungCases[2] == true) { // spring
																						// als
																						// naechstes
																						// nach
																						// links
																						// unten
													koordX = aktiveSpielfigur.getPosition().getPosX() - 1;
													koordY = aktiveSpielfigur.getPosition().getPosY() - 1;
													koordX1 = koordX - 1;
													koordY1 = koordY - 1;
												} else if (sprungCases[3] == true) { // spring
																						// als
																						// naechstes
																						// nach
																						// rechts
																						// unten
													koordX = aktiveSpielfigur.getPosition().getPosX() + 1;
													koordY = aktiveSpielfigur.getPosition().getPosY() - 1;
													koordX1 = koordX + 1;
													koordY1 = koordY - 1;
												}
												
											}
											System.out.println("Sprung vollendet.");
										}

									}
									
								}
								}
						}


						// *******************************************NORMALES
						// ZIEHEN!****************************************

						// --------------------------------------ALLGEMEINESZIEHEN----------------------------

						if (c == koordX && d == koordY && this.brettArray[c][d].getFigur() == null) {
							if (aktiveSpielfigur != null 
									&& (aktiveSpielfigur.getFarbe() == FarbEnum.schwarz
											&& d > aktiveSpielfigur.getPosition().getPosY())
											| (aktiveSpielfigur.getFarbe() == FarbEnum.weiss
													&& d < aktiveSpielfigur.getPosition().getPosY())) {

								aktiveSpielfigur.getPosition().removeFigur();
								aktiveSpielfigur.setPosition(this.brettArray[c][d]);
								this.brettArray[c][d].setFigur(aktiveSpielfigur);
							
								hatGeschlagen = false;
								msg.printOk(spielerAktiv.getName() + " hat Zug von " +s1 + " nach " +s2 +" ausgeführt.");
								System.out.println("Zug vollendet!");
								sound.ziehSound();

							}

//							else if (aktiveSpielfigur != null && aktiveSpielfigur.getKannSpringen() == true) {
//								//aktiveSpielfigur.getPosition().removeFigur();
//							}
//							// else{
//							// throw new RuntimeException("Ung�ltiges
//							// Zielfeld!");
//							// }
//
//							if (spielerAktiv.getMussSpringen() == true) {
//								if (this.sprungKonflikt > 1) { // Im Moment
//																// haben wir 2
//																// Steine die
//																// Springen
//																// koennen
//
//									spielerAktiv.setAktiv(false);
//									spielerGegner.setAktiv(true);// Spielerwechsel
//																	// fuer
//																	// Eingabe,
//																	// dann
//																	// zueruck
//																	// zum
//																	// aktuellen
//																	// Spieler!
//
//									if (spielerGegner.isKI() == false) {
//										System.out.println(
//												"Sie hatten mehrere Steine mit Sprungmoeglichkeiten! Spieler B, Sie duerfen nun eines dieser Steinchen von Spieler A waehlen,so dass dieses vom Feld geloescht wird!");
//
//										String eingabe = sc.nextLine();
//										brett.Umwandler(eingabe);
//										int x = brett.getKoordX();
//										int y = brett.getKoordY();
//										if (this.brettArray[x][y].getFigur() != null) {
//											if (this.brettArray[x][y].getFigur().getFarbe() == farbeAktiv
//													&& this.brettArray[x][y].getFigur().getKannSpringen()) {
//												//this.brettArray[x][y].removeFigur();
//
//											}
//										} else {
//											throw new RuntimeException(
//													"Waehlen sie bitte nur eine der Steine die in beim vorhergehenden Zug eine Sprungmoeglichkeit hatten, die von ihrem Gegner nicht wahrgenommen wurde!");
//										}
//									}
//									System.out.println(
//											"Sprungkonflikt! Sie hatten mehrere Sprungmoeglichkeiten die Sie nicht genutzt haben! Ihr Gegner hat die Moeglichkeit genutzt und ihnen eines dieser Steinchen geloescht!");
//									if (spielerGegner.isKI() == true) {
//										// Finde ein Steinchen das haette
//										// Springen duerfen:
//										for (int i = 0; i < brettArray.length; i++) {
//											for (int j = 0; j < brettArray[i].length; j++) {
//												if (this.brettArray[i][j].getFigur() != null
//														&& this.brettArray[i][j].getFigur()
//																.getFarbe() == this.spielerAktiv.getFarbe()
//														&& this.brettArray[i][j].getFigur().getKannSpringen() == true) {
//													this.brettArray[i][j].getFigur().getPosition().removeFigur();
//												}
//											}
//										}
//
//									}
//
//									spielerAktiv.setAktiv(true); // Eingabe
//																	// Beendet,
//																	// zueruck
//																	// zum
//																	// aktuellen
//																	// Spieler!
//																	// Scheint
//																	// zwar
//																	// unn�tig,
//																	// weil
//																	// danach
//																	// Spielerwechsel
//																	// kommt,
//																	// ist es
//																	// aber
//																	// nicht, da
//									// Spielerwechsel methode noch weitere
//									// ueberpruefungen macht ;)
//									spielerGegner.setAktiv(false);
//
//								} else {
//									// Gibt es nur eine Spielfigur die springen
//									// kann, suchen wir diese einfach in unserem
//									// Array
//									for (int i = 0; i < this.brettArray.length; i++) {
//										for (int j = 0; j < this.brettArray[i].length; j++) {
//											if (this.brettArray[i][j].getFigur() != null
//													&& this.brettArray[i][j].getFigur().getFarbe() == farbeAktiv
//													&& this.brettArray[i][j].getFigur().getKannSpringen()) {
//
//												//figurenListe.add(this.brettArray[i][j].getFigur());
//											}
//										}
//									}
//								}
//							}
							if ((aktiveSpielfigur.getPosition().getPosY() == 11
									&& aktiveSpielfigur.getFarbe() == FarbEnum.schwarz)
									| (aktiveSpielfigur.getPosition().getPosY() == 0
											&& aktiveSpielfigur.getFarbe() == FarbEnum.weiss)) {
								aktiveSpielfigur.setDame();
								msg.printOk("Eine Figur ist zur Dame geworden!");
							}
							aktiveSpielfigur = null;
							spielerAktiv.setMussSpringen(false);
							spielerWechsel();

						}

						else {
							if (this.spielerAktiv.isKI() == false) {
								throw new RuntimeException("Waehlen sie bitte ein Feld, auf das sie ziehen koennen!");
							}
						}


						
						}	
						}
			}

			else if (spielerAktiv.getMussSpringen() == true) {
				throw new RuntimeException(
						"Waehlen sie bitte eine Figur die springen kann, bzw ein Feld das bespringbar ist!!");

			}

			// }

		} catch (Exception e) {
			//msg.printError(e.toString());

			// System.err.println(e.getMessage());
			// e.printStackTrace();

		}

	}
	
	// ++++++++++++++++++++++++++++++++ BEWEGE DAME
	// ++++++++++++++++++++++++++++++
	private void bewegeDame(int c, int d) {
		int richtung = d - aktiveSpielfigur.getPosition().getPosY();
		int abstandx = Math.abs(c - aktiveSpielfigur.getPosition().getPosX());
		int abstandy = Math.abs(d - aktiveSpielfigur.getPosition().getPosY());
		if (abstandx == abstandy) { // Figur darf nur diagonal laufen

			if (this.brettArray[c][d].getFigur() == null) { // nur Bewegen

				aktiveSpielfigur.getPosition().removeFigur();
				this.brettArray[c][d].setFigur(aktiveSpielfigur);
				aktiveSpielfigur.setPosition(this.brettArray[c][d]);
				aktiveSpielfigur = null;
				System.out.println("Zug vollendet");
				spielerWechsel();

			}

			// -----------------------------------Zielfeld hat gegnerische
			// Spielfigur -------------------------------------------
			else if (this.brettArray[c][d].getFigur() != null
					&& this.brettArray[c][d].getFigur().getFarbe() != aktiveSpielfigur.getFarbe()) {

				// Sprung nach oben
				if (richtung > 0) {
					if (c < (aktiveSpielfigur.getPosition().getPosX())) { // Sprung
																			// nach
																			// links
						if (this.brettArray[c - 1][d + 1].getFigur() != null) {
							throw new RuntimeException("Hier ist bereits eine Figur! Feld nicht bespringbar.");
						}
						System.out.println("sprung nach links");
						aktiveSpielfigur.getPosition().removeFigur();
						this.brettArray[c - 1][d + 1].setFigur(aktiveSpielfigur);
						aktiveSpielfigur.setPosition(this.brettArray[c - 1][d + 1]);
						this.brettArray[c][d].removeFigur();
						aktiveSpielfigur = null;
						System.out.println("Zug vollendet");
						spielerWechsel();

					}

					else {
						if (this.brettArray[c + 1][d + 1].getFigur() != null) {
							throw new RuntimeException("Hier ist bereits eine Figur! Feld nicht bespringbar.");
						} else if (c > (aktiveSpielfigur.getPosition().getPosX())) {
							System.out.println("sprung nach rechts!");
							aktiveSpielfigur.getPosition().removeFigur();
							this.brettArray[c + 1][d + 1].setFigur(aktiveSpielfigur);
							aktiveSpielfigur.setPosition(this.brettArray[c + 1][d + 1]);
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
						if (this.brettArray[c - 1][d - 1].getFigur() != null) {
							throw new RuntimeException("Hier ist bereits eine Figur! Feld nicht bespringbar.");
						}
						System.out.println("sprung nach links");
						aktiveSpielfigur.getPosition().removeFigur();
						this.brettArray[c - 1][d - 1].setFigur(aktiveSpielfigur);
						aktiveSpielfigur.setPosition(this.brettArray[c - 1][d - 1]);
						this.brettArray[c][d].removeFigur();
						aktiveSpielfigur = null;
						System.out.println("Zug vollendet");
						spielerWechsel();

					}

					else if (this.brettArray[c + 1][d - 1].getFigur() != null) {
						throw new RuntimeException("Hier ist bereits eine Figur! Feld nicht bespringbar.");
					} else if (c > (aktiveSpielfigur.getPosition().getPosX())) {
						System.out.println("sprung nach rechts!");
						aktiveSpielfigur.getPosition().removeFigur();
						this.brettArray[c + 1][d - 1].setFigur(aktiveSpielfigur);
						aktiveSpielfigur.setPosition(this.brettArray[c + 1][d - 1]);
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

		// Pruefen ob beide Spieler noch Spielfiguren haben---------------
		isSpielende();
		// -------------------------------------------------------
	}

	// ############################### SPIELER WECHSEL
	// #########################################

	public void spielerWechsel() {
		
		if (spielerA.getAktiv() == true) {
			spielerA.setAktiv(false);
			spielerB.setAktiv(true);
			spielerAktiv = spielerB;
			this.farbeAktiv = spielerAktiv.getFarbe();
			this.farbeGegner = spielerA.getFarbe();

		} else {
			spielerA.setAktiv(true);
			spielerB.setAktiv(false);
			spielerAktiv = spielerA;
			this.farbeAktiv = spielerAktiv.getFarbe();
			this.farbeGegner = spielerB.getFarbe();

		}
		System.out.println("Spieler mit der Farbe >>" +spielerAktiv.getFarbe().toString() + "<< und dem Namen >>" + spielerAktiv.getName() + "<< ist aktiv.");
		msg.printSpielerAktiv("Spieler mit der Farbe >>" +spielerAktiv.getFarbe().toString() + "<< und dem Namen >>" + spielerAktiv.getName() + "<< ist aktiv.");
		if (spielerAktiv.isKI()) {
			
			String[] zug = spielerAktiv.getKi().wasMacheIch(brett);
			System.out.println("KI die Zug durchführen wird, gehört dem Spieler: "+spielerAktiv.getKi().spieler.getName());
			if(spielerA.isKI() && spielerB.isKI()){ //Zugbestätigungen bei Ki nur, wenn Ki gegen Ki
			msg.printOkWindow("KI mit der Farbe -" +spielerAktiv.getFarbe().toString().toUpperCase()+"-\nmoechte die Spielfigur von "+zug[0].toUpperCase()+" nach "+zug[1].toUpperCase()+" bewegen.");
			}
			bewegeSpielfigur(zug[0], zug[1]);
			
		}
	}

	/**
	 * Speichert die aktuelle Belegung des Spielbretts in CSV-Notation.
	 * Speicherort: savegame/savegame.csv
	 */
	@Override
	public String belegungCSV() {

		/*
		 * try { pw = new PrintWriter(new FileWriter("savegame/belegung.csv"));
		 * } catch (FileNotFoundException e) { System.err.println(
		 * "DATEI ZUM SPEICHERN NICHT GEFUNDEN!"); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
		String blg = "";

		Spielfeld[][] belegung = this.brettArray;
		for (int i = this.brettArray.length - 1; i >= 0; i--) {
			for (int n = 0; n < belegung[i].length; n++) {
			
				blg +=belegung[n][i].getAusgabeID()+";";
				
				 }
			//blg += "\n";
				// pw.print(belegung[n][i].getAusgabeID() + "\t");
			}
			
			// pw.println();
		

		// pw.close();

		return blg;
	}

	// ############################################################# SPIELER
	// MUSS SPRINGEN ()
	// #######################################################################
	// ######################### BEREITS NOCHMAL KURZ UND SCHOEN GESCHRIEBEN -
	// NOCH NICHT IMPLEMENTIERT DA NOCH FEHLER AUSGEARBEITET WERDEN MUESSEN

	private void spielerMussSpringen() {

		// Bevor Spieler gewechselt wird und alles neu �berpr�ft wird, setze
		// bei allen Steinen kannSchlagen auf false. Auch spielerA,B
		// mussSpringen() wird auf false zur�ckgesetzt.
		
		//figurenListe.clear();

		this.sprungKonflikt = 0;
		
		spielerAktiv.setMussSpringen(false);

		//Spieler spielerAktiv = null;

		//FarbEnum farbeGegner = null;

//		if(this.spielerAktiv.getFarbe().equals(FarbEnum.schwarz)){
//			farbeAktiv = FarbEnum.schwarz;
//			farbeGegner = FarbEnum.weiss;
//		}
//		else if(this.spielerAktiv.getFarbe().equals(FarbEnum.weiss)){
//			farbeAktiv = FarbEnum.weiss;
//			farbeGegner = FarbEnum.schwarz;
//		}


		for (int i = 0; i < this.brettArray.length; i++) {
			for (int j = 0; j < this.brettArray[i].length; j++) {
				if (this.brettArray[i][j].getFigur() != null) {
					spielerA.setMussSpringen(false);
					spielerB.setMussSpringen(false);
					this.brettArray[i][j].getFigur().setKannSpringen(false); // Kennt
																				// nun
																				// keine
																				// Figur
																				// mehr
																				// die
																				// springen
																				// kann

				}
			}
		}

		// ----------------------------------KANN SPIELER MIT EINER FIGUR
		// SPRINGEN??--------------------------
		if (spielerAktiv.getAktiv() == true) {
			for (int i = 0; i < this.brettArray.length; i++) {
				for (int j = 0; j < this.brettArray[i].length; j++) {
					if (this.brettArray[i][j].getFigur() != null
							&& this.brettArray[i][j].getFigur().getFarbe() == farbeAktiv) {
						Spielfigur testSpieler = this.brettArray[i][j].getFigur();

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
						boolean[] sprungCases = new boolean[4];

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

								coordX += a;
								coordY += b;

							}

							if (this.brettArray.length - (coordX) > 1
									&& this.brettArray.length - (coordX) < this.brettArray.length
									&& this.brettArray.length - (coordY) < brettArray.length
									&& this.brettArray.length - (coordY) > 1
									&& brettArray[coordX][coordY].getFigur() != null) {
								if (brettArray[coordX][coordY].getFigur().getFarbe() == farbeGegner
										&& brettArray[(coordX + a)][(coordY + b)].getFigur() == null) {
									testSpieler.setKannSpringen(true);
									this.spielerAktiv.setMussSpringen(true);

									switch (caseNumber) { // Es kann entweder
															// nur fuer case 1
															// oder 2 bzw beide
															// true sein fuer
															// Schwarze Spieler,
															// analog dazu 3
															// oder 4 fuer
															// Weisse
									case 1:
										sprungCases[0] = true;
										testSpieler.setSprungCases(sprungCases);
										break;
									case 2:
										sprungCases[1] = true;
										testSpieler.setSprungCases(sprungCases);
										break;
									case 3:
										sprungCases[2] = true;
										testSpieler.setSprungCases(sprungCases);
										break;
									case 4:
										sprungCases[3] = true;
										testSpieler.setSprungCases(sprungCases);
										break;

									}

								}

							}
							caseNumber++;
							
						}
						if (testSpieler.getKannSpringen() == true) {											
							figurenListe.add(testSpieler);	
							testSpieler.setInListe(true);
							System.out.println("Spielfigur mit ID: " + testSpieler.getPosition().getID().toUpperCase() + " wird zur Liste hinzugefügt.");
							warKi = false;
							if(spielerAktiv.isKI()) warKi = true;
						}
					}
				}
			}

			if (spielerAktiv.getMussSpringen() == false) {
				System.out.println("Spieler " + spielerAktiv.getName() + " Sie sind am Zug!");

			}
		}
	}


	// #############################################################################
	// ENDE SPIELER MUSS SPRINGEN
	// ##################################################

	public void setBrettArray(Spielfeld[][] brettArray) {
		this.brettArray = brettArray;
	}

	public Spieler getSpielerA() {
		return spielerA;
	}

	public Spieler getSpielerB() {
		return spielerB;
	}

//	public void setSpielerA(Spieler spielerA) {
//		if (spielerA != null) {
//			this.spielerA = spielerA;
//		} else
//			throw new NullPointerException("SpielerA ist NULL!");
//	}
//
//	public void setSpielerB(Spieler spielerB) {
//		if (spielerB != null) {
//			this.spielerB = spielerB;
//		} else
//			throw new NullPointerException("SpielerB ist NULL!");
//	}

//	public void setSpielerAktiv(Spieler spielerAktiv) {
//		if (spielerAktiv != null) {
//			this.spielerAktiv = spielerAktiv;
//			// this.farbeAktiv = spielerAktiv.getFarbe();
//			if (spielerAktiv.getFarbe() == FarbEnum.schwarz) {
//				this.spielerA.setAktiv(true);
//				this.spielerB.setAktiv(false);
//			} else if (spielerAktiv.getFarbe() == FarbEnum.weiss) {
//				this.spielerB.setAktiv(true);
//				this.spielerA.setAktiv(false);
//			}
//			System.out.println("SpielerAktiv:" + farbeAktiv);
//		} else
//			throw new NullPointerException("SpielerAktiv ist NULL!");
//	}

	public Spieler getSpielerAktiv() {
		return spielerAktiv;
	}

	public Spielbrett getBrett() {
		return brett;
	}

	public boolean getSpielende() {
		return this.spielende;
	}

	public Spieler getWinner() {
		return this.winner;
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

	@Override
	public void pusten(String feld) {
		if (darfPusten) {
			int spalte = 0;
			int zeile = 0;
			boolean spzeGesetzt = false;
			for (int i = 0; i < this.brettArray.length; i++) {
				for (int n = 0; n < this.brettArray[i].length; n++) {
					if (brettArray[i][n].getHatFigur() && brettArray[i][n].getID().equals(feld)) {
						spalte = i;
						zeile = n;
						spzeGesetzt = true;
					}
				}
			}
			if (spzeGesetzt && brettArray[spalte][zeile].getFigur().getFarbe() != spielerAktiv.getFarbe()
					&& figurenListe.contains(brettArray[spalte][zeile].getFigur())) {
				brettArray[spalte][zeile].removeFigur();
			} else {
				throw new RuntimeException("Gewählte Figur kann nicht entfernt werden!");
			}

		} else
			throw new RuntimeException("Spieler darf nicht pusten!");
	}

	@Override
	public void spielerHzfg(String name, boolean ki) {
		if (spielerA == null) {
			spielerA = new Spieler(name, FarbEnum.schwarz, ki);
			this.spielerAktiv = spielerA;
			this.farbeAktiv = spielerA.getFarbe();
			this.spielerA.setAktiv(true);
		} else if (spielerB == null) {
			spielerB = new Spieler(name, FarbEnum.weiss, ki);
			this.farbeGegner = spielerB.getFarbe();
		} else
			throw new RuntimeException("Schon zwei Spieler vorhanden");
		if (spielerA != null && spielerB != null) {
			this.spielerWechsel();
		}

	}

	@Override
	public void speichern(String pfad) {
		// TODO Auto-generated method stub
		iDatenzugriff idz;
		System.out.println(pfad);
		String[] args = pfad.split("\\.");
		String typ = args[args.length-1].toLowerCase();

		switch (typ) {
		case "ser":
			idz = new DatenzugriffSerialisiert();
			idz.save(this);
			break;
		case "csv":
			idz = new DatenzugriffCSV();
			idz.save(this.toCSV());
			break;
		case "pdf":
			break;
		default:
			throw new RuntimeException("Filetype not supported");
		}

	}

	@Override
	public void laden(String pfad) {
System.out.println("ufufufufufufu");
		iDatenzugriff idz;
		String[] args = pfad.split("\\.");
		String typ = args[args.length - 1].toLowerCase();

		switch (typ) {
		case "ser":
			idz = new DatenzugriffSerialisiert();
			Spiel x = (Spiel) idz.load(pfad);
			this.aktiveSpielfigur = x.aktiveSpielfigur;
			this.brett = x.brett;
			this.brettArray = x.brettArray;
			this.farbeAktiv = x.farbeAktiv;
			this.spielerA = x.spielerA;
			this.spielerAktiv = x.spielerAktiv;
			this.spielerB = x.spielerB;
			break;
		case "csv":
			idz = new DatenzugriffCSV();
			String s = (String) idz.load(pfad);
			this.fromCSV(s);
			break;
		default:
			throw new RuntimeException("Filetype not supported");
		}

	}

	@Override
	public void neuesSpiel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mail() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(Object view) {
		//Wenn can not cast Exception, dann instanceof GUI, später auch Web
		if(view instanceof iMessage){
			msg = (iMessage) view;
		}else throw new RuntimeException("Couldn't add view");

	}

	private String toCSV() {

		String s = "";
		// -Spielfeld speichern-----------------------------------
		Spielfeld[][] belegung = brett.getNotation();
		for (int i = belegung.length - 1; i >= 0; i--) {
			for (int n = 0; n < belegung[i].length; n++) {
				s += (belegung[n][i].getAusgabeID() + ";");
			}
			//s += "\n";
		}
		s += "\n";
		// --------------------------------------------------------
		// -Spieler A speichern------------------------------------
		s += (this.getSpielerA().getFarbe() + "\n");
		s += (this.getSpielerA().getName() + "\n");
		s += (this.getSpielerA().getMussSpringen() + "\n");
		s += (this.getSpielerA().isKI() + "\n");

		// --------------------------------------------------------
		// -Spieler B speichern------------------------------------
		s += (this.getSpielerB().getFarbe() + "\n");
		s += (this.getSpielerB().getName() + "\n");
		s += (this.getSpielerB().getMussSpringen() + "\n");
		s += (this.getSpielerB().isKI() + "\n");
		s += (this.getSpielerB().getAktiv() + "\n"); // wird nur von spielerB
														// gespeichert, falls
														// spielerB aktiv ist,
														// ist spielerA nicht
														// aktiv und andersrum
		// --------------------------------------------------------

		return s;
	}

	private void fromCSV(String s) {
		String[] irgendwas = s.split("\n");
		char c;
		String id = null;
		String line = null;
		String[] field = null;
		Spielfeld[][] belegung = new Spielfeld[12][12]; // Wenn auch die
														// Spielfeldgröße
														// gespeichert werden
														// kann, dann muss hier
														// angepasst werden.

		for (int x = 0; x < irgendwas.length; x++) {
			line = irgendwas[0];
			field = line.split(";");
			int d=0; 
			
			if (x == 0) {
				

				for (int i = belegung.length - 1; i >= 0; i--) {
					c = 97;

					for (int n = 0; n < belegung[i].length; n++) {

						belegung[n][i] = new Spielfeld((char) (c + n) + "" + (i + 1));
						belegung[n][i].setXY(belegung[n][i].getID());
						belegung[n][i].setAusgabeID(field[d]);
						if (belegung[n][i].getAusgabeID().equals("[X]")) {
							belegung[n][i].setFigur(new Spielfigur(FarbEnum.schwarz, belegung[n][i]));
						} else if (belegung[n][i].getAusgabeID().equals("[O]")) {
							belegung[n][i].setFigur(new Spielfigur(FarbEnum.weiss, belegung[n][i]));
						} else if (belegung[n][i].getAusgabeID().equals("[*X*]")) {
							belegung[n][i].setFigur(new Spielfigur(FarbEnum.schwarz, belegung[n][i]));
							belegung[n][i].getFigur().setDame();
						} else if (belegung[n][i].getAusgabeID().equals("[*O*]")) {
							belegung[n][i].setFigur(new Spielfigur(FarbEnum.weiss, belegung[n][i]));
							belegung[n][i].getFigur().setDame();
						}
						d++;
					}
					
				}
				this.brettArray=belegung;

			}
			else{
				// -SpielerA einlesen und im Spiel Objekt setzen-------------------
				spielerA = null;
				Spieler spielerC = new Spieler("spielerA", null);

				spielerC.setFarbe(this.toFarbEnum(irgendwas[x++]));
				spielerC.setName(irgendwas[++x]);
				spielerC.setMussSpringen(toBoolean(irgendwas[x++]));
				if (toBoolean(irgendwas[x++]) == true) {
					KI ki = new KI_Dame(spielerC);
					spielerC.setKi(ki);
				}

				// ----------------------------------------------------------------

				// -SpielerB einlesen und im Spiel Objekt setzen-------------------
				spielerB = null;
				Spieler spielerD = new Spieler("spielerB", null);

				spielerD.setFarbe(this.toFarbEnum(irgendwas[x++]));
				spielerD.setName(irgendwas[++x]);
				spielerD.setMussSpringen(toBoolean(irgendwas[x++]));
				if (toBoolean(irgendwas[x++]) == true) {
					KI ki = new KI_Dame(spielerD);
					spielerD.setKi(ki);
				}
				spielerD.setAktiv(toBoolean(irgendwas[x++]));

				// ----------------------------------------------------------------
				// -SpielerB.getAktiv() einlesen und spielerAktiv setzen-----------



				// ----------------------------------------------------------------

				this.spielerHzfg(spielerC.getName(), spielerC.isKI());
				this.spielerHzfg(spielerD.getName(), spielerD.isKI());
			break;	

				
			}
		}

		// ----------------------------------------------------------------

		

	
	}

	private FarbEnum toFarbEnum(String farbe) {
		if (farbe.equals("schwarz")) {
			return FarbEnum.schwarz;
		} else if (farbe.equals("weiss")) {
			return FarbEnum.weiss;
		} else
			throw new RuntimeException("Geladene Farbe gehört nicht zum Farbenum");
	}

	private boolean toBoolean(String bool) {
		if (bool.equals("true"))
			return true;
		else if (bool.equals("false"))
			return false;
		else
			throw new RuntimeException("Gelesener Wert ist kein boolean!");
	}

}