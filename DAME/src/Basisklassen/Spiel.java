package Basisklassen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import EMail.Mail;
import GUI.Sounds;
import Interfaces.iBediener;
import Interfaces.iDatenzugriff;
import Interfaces.iMessage;
import SpeichernLaden.DatenzugriffCSV;
import SpeichernLaden.DatenzugriffPDF;
import SpeichernLaden.DatenzugriffSerialisiert;

public class Spiel implements iBediener, Serializable {
	private transient iMessage msg;

	private boolean darfPusten;
	private boolean hatGeschlagen;
	private boolean warKi;
	private boolean schlagTestZwei = false;
	private ArrayList<Spielfigur> figurenListe;
	private boolean wurdeBewegt;

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
		this.brett = new Spielbrett();

		this.brettArray = brett.getNotation();
		setAlleFiguren();
		sc = new Scanner(System.in);
		sound = new Sounds();

		figurenListe = new ArrayList<Spielfigur>();
		DamenMaker();
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
	 * bewegt eine Spielfigur von gew�hltem Feld auf ein anderes g�ltiges
	 * Feld
	 * 
	 * @param String1
	 *            eingabe Feld von Spielfigur
	 * @param String2
	 *            eingabe Zielfeld
	 */

	// **************************************BEWEGE SPIELFIGUR
	// METHODE********************************************
	public void bewegeSpielfigur(String s1, String s2) {
		System.out.println(this.figurenListe);
		brett.Umwandler(s1);
		int a = brett.getKoordX();
		int b = brett.getKoordY();
		brett.Umwandler(s2);
		int c = brett.getKoordX();
		int d = brett.getKoordY();

		spielerMussSpringen();
		this.setHatGeschlagen(false);
		// �berpr�fe ob unsere �bergebenen Koordinaten in unserem
		// Array-Feld
		// enthalten sind
		if (a < 0 | a > this.brettArray.length - 1) {
			throw new RuntimeException("Ihre X Koordinate ist nicht in unserem Spielfeld!");
		}
		if (b < 0 | b > this.brettArray.length - 1) {
			throw new RuntimeException("Ihre Y Koordinate ist nicht in unserem Spielfeld!");
		}

		// ------------------------------W�hle
		// Spielfigur--------------------------
		try {
			if (spielerAktiv.getAktiv() == true && spielerAktiv != null) {
				if (brettArray[a][b].getFigur() != null && brettArray[a][b].getFigur().getFarbe() == farbeAktiv) {
					this.aktiveSpielfigur = brettArray[a][b].getFigur();
					this.aktiveSpielfigur.setPosition(brettArray[a][b]);
				} else {
					msg.printError("Keine " + this.spielerAktiv.getFarbe() + "e Spielfigur ausgewaehlt!");
					throw new RuntimeException("Keine " + this.spielerAktiv.getFarbe() + "e Spielfigur ausgewaehlt!");

				}
			}

		} catch (Exception e) {
			// msg.printError(e.toString());
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

								// Spezialfall: Gew�hlte Spielfigur hat
								// Sprung,
								// nutzt diesen aber nicht! L�sche sie!!

								// -------------------------------------------------------SPRINGEN----------------------------------------------------

								// ---------------------------------AUF�HRUNG
								// DER SPRUNG
								// CASES:------------------------------------
								///////////////////////////////////////////////////////////////////////////////////
								// Von Hier
								// bis normales Ziehen fehlt noch eine
								// abbruchbedinung,
								// sonst bleibt er in der While, wenn man eine
								// Spielfigur bewegt, die eigentlich schlagen
								// könnte
								if (c == koordX && d == koordY) {
									if (this.brettArray[koordX][koordY].getFigur() != null
											&& this.brettArray[koordX][koordY].getFigur().getFarbe() == farbeGegner) {
										while (aktiveSpielfigur.getKannSpringen() == true
												&& this.spielerAktiv.getMussSpringen() == true
												&& this.brettArray[koordX1][koordY1].getFigur() == null) {

											this.aktiveSpielfigur.getPosition().removeFigur();
											this.brettArray[koordX][koordY].removeFigur();
											this.brettArray[koordX1][koordY1].setFigur(aktiveSpielfigur);
											aktiveSpielfigur.setPosition(this.brettArray[koordX1][koordY1]);
											sound.schlagSound();
											this.setHatGeschlagen(true);
											this.setWurdeBewegt(true);
											msg.printOk(this.spielerAktiv.getName() + " hat Sprung von "
													+ this.rewandler(koordX, koordY) + " nach "
													+ this.rewandler(koordX1, koordY1) + " ausgeführt.");

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
												spielerMussSpringen();
											}
											
											System.out.println("Sprung vollendet.");
											
										}
										this.spielerWechsel();
									}
									if ((aktiveSpielfigur.getPosition().getPosY() == 11 // ge�ndert
											// von
											// 11
											&& aktiveSpielfigur.getFarbe() == FarbEnum.schwarz)
											|| (aktiveSpielfigur.getPosition().getPosY() == 0
													&& aktiveSpielfigur.getFarbe() == FarbEnum.weiss)) {
										aktiveSpielfigur.setDame();
										sound.promoteSound();
										msg.printOk("Eine Figur ist zur Dame geworden!");
									}

								}
							}
						}
						System.out.println(hatGeschlagen);
						// *******************************************NORMALES
						// ZIEHEN!****************************************

						// --------------------------------------ALLGEMEINESZIEHEN----------------------------
						if (c == koordX && d == koordY && this.brettArray[c][d].getFigur() == null) {
							if (this.getHatGeschlagen() == false && aktiveSpielfigur != null
									&& (aktiveSpielfigur.getFarbe() == FarbEnum.schwarz
											&& d > aktiveSpielfigur.getPosition().getPosY())
											| (aktiveSpielfigur.getFarbe() == FarbEnum.weiss
													&& d < aktiveSpielfigur.getPosition().getPosY())) {

								aktiveSpielfigur.getPosition().removeFigur();
								aktiveSpielfigur.setPosition(this.brettArray[c][d]);
								this.brettArray[c][d].setFigur(aktiveSpielfigur);

								msg.printOk(spielerAktiv.getName() + " hat Zug von " + s1 + " nach " + s2
										+ " ausgeführt.");
								System.out.println("Zug vollendet!");
								this.setWurdeBewegt(true);
								sound.ziehSound();
								this.setHatGeschlagen(false);
							}
							if ((aktiveSpielfigur.getPosition().getPosY() == 11 // ge�ndert
																				// von
																				// 11
									&& aktiveSpielfigur.getFarbe() == FarbEnum.schwarz)
									|| (aktiveSpielfigur.getPosition().getPosY() == 0
											&& aktiveSpielfigur.getFarbe() == FarbEnum.weiss)) {
								aktiveSpielfigur.setDame();
								sound.promoteSound();
								msg.printOk("Eine Figur ist zur Dame geworden!");
							}

						}

						else {
							if (this.spielerAktiv.isKI() == false) {
								throw new RuntimeException("Waehlen sie bitte ein Feld, auf das sie ziehen koennen!");
							}

						}

						aktiveSpielfigur = null;
						spielerAktiv.setMussSpringen(false);
						spielerWechsel();

					}
				}
			}

			else if (spielerAktiv.getMussSpringen() == true) {
				throw new RuntimeException(
						"Waehlen sie bitte eine Figur die springen kann, bzw ein Feld das bespringbar ist!!");

			}

			// }

		} catch (Exception e) {
			// msg.printError(e.toString());

			// System.err.println(e.getMessage());
			// e.printStackTrace();

		}
		pustenCheck();
		this.setHatGeschlagen(false);
		this.setWurdeBewegt(false);
		isSpielende();

	}

	// ++++++++++++++++++++++++++++++++ BEWEGE DAME
	// ++++++++++++++++++++++++++++++
	private void bewegeDame(int c, int d) {
		int richtung = d - aktiveSpielfigur.getPosition().getPosY();
		int abstandx = Math.abs(c - aktiveSpielfigur.getPosition().getPosX());
		int abstandy = Math.abs(d - aktiveSpielfigur.getPosition().getPosY());
		try{
		if (abstandx == abstandy) { // Figur darf nur diagonal laufen
			
			if(c > aktiveSpielfigur.getPosition().getPosX() && d > aktiveSpielfigur.getPosition().getPosY()) {
				for(int i = 1; i < abstandx; i++){
					System.out.println(i);
					if (this.brettArray[aktiveSpielfigur.getPosition().getPosX() + i][aktiveSpielfigur.getPosition().getPosY() + i].getHatFigur() == true){
						throw new RuntimeException("bla 1");
					}
				}
			}
			if(c < aktiveSpielfigur.getPosition().getPosX() && d > aktiveSpielfigur.getPosition().getPosY()) {
				for(int i = 1; i < abstandx; i++){
					if (this.brettArray[aktiveSpielfigur.getPosition().getPosX() - i][aktiveSpielfigur.getPosition().getPosY() + i].getHatFigur() == true){
						throw new RuntimeException("bla 2");
					}
				}
			}
			if(c > aktiveSpielfigur.getPosition().getPosX() && d < aktiveSpielfigur.getPosition().getPosY()) {
				for(int i = 1; i < abstandx; i++){
					if (this.brettArray[aktiveSpielfigur.getPosition().getPosX() + i][aktiveSpielfigur.getPosition().getPosY() - i].getHatFigur() == true){
						throw new RuntimeException("bla 3");
					}
				}
			}
			if(c < aktiveSpielfigur.getPosition().getPosX() && d < aktiveSpielfigur.getPosition().getPosY()) {
				for(int i = 1; i < abstandx; i++){
					if (this.brettArray[aktiveSpielfigur.getPosition().getPosX() - i][aktiveSpielfigur.getPosition().getPosY() - i].getHatFigur() == true){
						throw new RuntimeException("bla 4");
					}
				}
			}
		
			if (this.brettArray[c][d].getFigur() == null) { // nur Bewegen

				aktiveSpielfigur.getPosition().removeFigur();
				this.brettArray[c][d].setFigur(aktiveSpielfigur);
				aktiveSpielfigur.setPosition(this.brettArray[c][d]);
				aktiveSpielfigur = null;
				sound.ziehSound();
				this.setWurdeBewegt(true);
				this.setHatGeschlagen(false);
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
						aktiveSpielfigur = brettArray[c - 1][d + 1].getFigur();

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
							aktiveSpielfigur = brettArray[c + 1][d + 1].getFigur();
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
						aktiveSpielfigur = brettArray[c - 1][d - 1].getFigur();
					}

					else if (this.brettArray[c + 1][d - 1].getFigur() != null) {
						throw new RuntimeException("Hier ist bereits eine Figur! Feld nicht bespringbar.");
					} else if (c > (aktiveSpielfigur.getPosition().getPosX())) {
						System.out.println("sprung nach rechts!");
						aktiveSpielfigur.getPosition().removeFigur();
						this.brettArray[c + 1][d - 1].setFigur(aktiveSpielfigur);
						aktiveSpielfigur.setPosition(this.brettArray[c + 1][d - 1]);
						this.brettArray[c][d].removeFigur();
						aktiveSpielfigur = brettArray[c + 1][d - 1].getFigur();
					}

				}
				sound.schlagSound();
				this.setHatGeschlagen(true);
				this.setWurdeBewegt(true);
				msg.printOk(this.spielerAktiv.getName() + " hat Sprung nach " + this.rewandler(c, d) + " ausgeführt.");

				System.out.println("Zug vollendet, muss allerdings nochmal springen wenn nochmal kann!");
				spielerAktiv.setMussSpringen(false);
				aktiveSpielfigur.setKannSpringen(false);
				spielerMussSpringen();
				while (aktiveSpielfigur.getKannSpringen() == true && this.spielerAktiv.getMussSpringen() == true) {

					if (aktiveSpielfigur.getKannSpringen() == true) {

						int[] zielfeld = aktiveSpielfigur.getZielfeld();
						int x = zielfeld[0];
						int y = zielfeld[1];
						aktiveSpielfigur.getPosition().removeFigur();
						this.brettArray[x][y].removeFigur();

						if (x > aktiveSpielfigur.getPosition().getPosX()
								&& y > aktiveSpielfigur.getPosition().getPosY()) {
							System.out.println("rechts oben");
							this.brettArray[x + 1][y + 1].setFigur(aktiveSpielfigur);
							aktiveSpielfigur.setPosition(this.brettArray[x + 1][y + 1]);
							aktiveSpielfigur = brettArray[x + 1][y + 1].getFigur();
						} else if (x > aktiveSpielfigur.getPosition().getPosX()
								&& y < aktiveSpielfigur.getPosition().getPosY()) {
							System.out.println("links oben");
							this.brettArray[x + 1][y - 1].setFigur(aktiveSpielfigur);
							aktiveSpielfigur.setPosition(this.brettArray[x + 1][y - 1]);
							aktiveSpielfigur = brettArray[x + 1][y - 1].getFigur();
						}

						else if (x < aktiveSpielfigur.getPosition().getPosX()
								&& y > aktiveSpielfigur.getPosition().getPosY()) {
							System.out.println("rechts unten");
							this.brettArray[x - 1][y + 1].setFigur(aktiveSpielfigur);
							aktiveSpielfigur.setPosition(this.brettArray[x - 1][y + 1]);
							aktiveSpielfigur = brettArray[x - 1][y + 1].getFigur();
						} else if (x < aktiveSpielfigur.getPosition().getPosX()
								&& y < aktiveSpielfigur.getPosition().getPosY()) {
							System.out.println("links unten");
							this.brettArray[x - 1][y - 1].setFigur(aktiveSpielfigur);
							aktiveSpielfigur.setPosition(this.brettArray[x - 1][y - 1]);
							aktiveSpielfigur = brettArray[x - 1][y - 1].getFigur();
						}

					}
					spielerMussSpringen();
				}
			}

		} else {
			throw new RuntimeException("Nicht diagonal");
		}

		// Pruefen ob beide Spieler noch Spielfiguren haben---------------
		// -------------------------------------------------------
		aktiveSpielfigur = null;
		System.out.println("Zug vollendet");
		spielerWechsel();
	}
		catch(Exception e){
			msg.printError("Ungueltiger Spielzug!");
		}
		
		pustenCheck();
		isSpielende();
		
	}

	// ############################### SPIELER WECHSEL
	// #########################################

	public void spielerWechsel() {
		pustenCheck();
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
		// spielerMussSpringen();
		System.out.println("Spieler mit der Farbe >>" + spielerAktiv.getFarbe().toString() + "<< und dem Namen >>"
				+ spielerAktiv.getName() + "<< ist aktiv.");
		msg.printSpielerAktiv("Spieler mit der Farbe >>" + spielerAktiv.getFarbe().toString() + "<< und dem Namen >>"
				+ spielerAktiv.getName() + "<< ist aktiv.");
		if (spielerAktiv.isKI()) { // spielerAktiv ist KI wenn ich gerade am Zug war und meinen Zug verkackt habe
			spielerMussSpringen(); //Hier m�sste ich ja dann eigentlich zu�rkc bekommen ob gepustet werden kann?
			//pustenCheck();
			//M�sste hier pusten, dann weiter machen?
			
			
			String[] zug = spielerAktiv.getKi().wasMacheIch(brett);
		
			System.out.println(
					"KI die Zug durchführen wird, gehört dem Spieler: " + spielerAktiv.getKi().spieler.getName());
			if (spielerA.isKI() && spielerB.isKI()) { // Zugbestätigungen bei
														// Ki										// nur, wenn Ki gegen Ki
				return;

			}
			bewegeSpielfigur(zug[0], zug[1]);

		}
	}

	private void pustenCheck() {
		if (this.getHatGeschlagen() == false && this.getWurdeBewegt() == true) {
			System.out.println("Ist hier rein gogoingt");
			if (figurenListe.size() == 1) {
				System.out.println("Ist hier rein gogoingt YOYOYO");
				System.out
						.println("Spielfigur mit ID: " + figurenListe.get(0).getPosition().getID() + " wird ENTFERNT!");
				figurenListe.get(0).getPosition().removeFigur();
				figurenListe.clear();

				warKi = false;
			} else if (!figurenListe.isEmpty()) {
				if (warKi) {// Doppelt Springen -> bei KI wird der erste Stein gel�scht
					System.out.println(
							"Spielfigur mit ID: " + figurenListe.get(0).getPosition().getID() + " wird ENTFERNT!");
					figurenListe.get(0).getPosition().removeFigur();
					figurenListe.clear();

					warKi = false;
				} else { //Der Spieler darf hingegen ausw�hlen, welcher Stein entfernt wird
					darfPusten = true;
					msg.printPusten("Der " + this.spielerAktiv.getFarbe() + "e Spieler hatte mehrere Schlagmöglichkeiten!"
							+ "\nWählen Sie einen Stein des Gegners, der entfernt werden soll.");

				}
				for (int f = 0; f < figurenListe.size() - 1; f++) {
					figurenListe.get(f).setInListe(false);
				}
			}
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

				blg += belegung[n][i].getAusgabeID() + ";";

			}
			// blg += "\n";
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

	// ############################################################# SPIELER
	// MUSS SPRINGEN ()
	// #######################################################################
	// ######################### BEREITS NOCHMAL KURZ UND SCHOEN GESCHRIEBEN -
	// NOCH NICHT IMPLEMENTIERT DA NOCH FEHLER AUSGEARBEITET WERDEN MUESSEN

	private void spielerMussSpringen() {

		// Bevor Spieler gewechselt wird und alles neu �berpr�ft wird, setze
		// bei allen Steinen kannSchlagen auf false. Auch spielerA,B
		// mussSpringen() wird auf false zur�ckgesetzt.

		figurenListe.clear();

		this.sprungKonflikt = 0;

		spielerAktiv.setMussSpringen(false);

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
							&& this.brettArray[i][j].getFigur().getFarbe() == this.spielerAktiv.getFarbe()) {
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
									testSpieler.setZielfeld(coordX, coordY);
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
						System.out.println("testSpieler kann springen: "+ testSpieler.getKannSpringen());
						if (testSpieler.getKannSpringen() == true) {
							if(spielerA.isKI()==false | spielerB.isKI()==false){
							figurenListe.add(testSpieler);
							testSpieler.setInListe(true);
							System.out.println("Spielfigur mit ID: " + testSpieler.getPosition().getID().toUpperCase()
									+ " wird zur Liste hinzugefügt.");
							}
							warKi = false;
							if (spielerAktiv.isKI())
						
								warKi = true;
								System.out.println("der BOI ist in der LISTE");
						}
						for(Spielfigur list: figurenListe){
							System.out.println("Boi in da list: "+list);
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

	public boolean getHatGeschlagen() {
		return hatGeschlagen;
	}

	public void setHatGeschlagen(boolean hatGeschlagen) {
		this.hatGeschlagen = hatGeschlagen;
	}

	public boolean getWurdeBewegt() {
		return wurdeBewegt;
	}

	public void setWurdeBewegt(boolean wurdeBewegt) {
		this.wurdeBewegt = wurdeBewegt;
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
			msg.printOkWindow("Der weisse Spieler mit dem Namen "+spielerB.getName()+"hat gewonnen!");
		} else if (weiss == 0) {
			this.winner = spielerA;
			msg.printOkWindow("Der schwarze Spieler mit dem Namen "+spielerA.getName()+" hat gewonnen!");
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
			if (spzeGesetzt && brettArray[spalte][zeile].getFigur().getFarbe() == spielerAktiv.getFarbe()
					&& figurenListe.contains(brettArray[spalte][zeile].getFigur())) {
				brettArray[spalte][zeile].removeFigur();
				this.figurenListe.clear();
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
		String typ = args[args.length - 1].toLowerCase();
		switch (typ) {
		case "ser":
			idz = new DatenzugriffSerialisiert();
			idz.save(this, pfad);
			break;
		case "csv":
			idz = new DatenzugriffCSV();
			idz.save(this.toCSV(), pfad);
			break;
		case "pdf":
			idz = new DatenzugriffPDF();
			idz.save(this, pfad);
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
	public void mail(String pfad) { //Sendet Email mit PDF als Anhang! -> Sollte Fehler werfen wenn keine PDF vorhanden
		this.speichern("savegame/Dame.pdf");
		String empf = JOptionPane.showInputDialog(null,"Bitte geben Sie die E-Mail-Adresse des Empfängers ein.", "Empfänger E-Mail eingeben", JOptionPane.QUESTION_MESSAGE);
		String splitter[] = pfad.split("/");
		String dateiname = splitter[splitter.length-1];
		Mail m = new Mail(empf, "Aktuelle Spielbelegung", "Guten Tag,\n\ndiese E-Mail enthält eine PDF oder ein Savegame mit der aktuellen Spielbrettbelegung und anderen essentiellen Infos des Spiels.\n\nMit freundlichen Grüßen\n\nGruppe A3\n\n",pfad, dateiname, null, null);
	}

	@Override
	public void init(Object view) {
		// Wenn can not cast Exception, dann instanceof GUI, später auch Web
		if (view instanceof iMessage) {
			msg = (iMessage) view;
		} else
			throw new RuntimeException("Couldn't add view");

	}

	private String toCSV() {

		String s = "";
		// -Spielfeld speichern-----------------------------------
		Spielfeld[][] belegung = brett.getNotation();
		for (int i = belegung.length - 1; i >= 0; i--) {
			for (int n = 0; n < belegung[i].length; n++) {
				s += (belegung[n][i].getAusgabeID() + ";");
			}
			// s += "\n";
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
			int d = 0;

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
				this.brettArray = belegung;

			} else {
				// -SpielerA einlesen und im Spiel Objekt
				// setzen-------------------
				spielerA = null;
				Spieler spielerC = new Spieler("spielerA", null);

				spielerC.setFarbe(this.toFarbEnum(irgendwas[x++]));
				spielerC.setName(irgendwas[x++]);
				spielerC.setMussSpringen(toBoolean(irgendwas[x++]));
				if (toBoolean(irgendwas[x++]) == true) {
					KI ki = new KI_Dame(spielerC);
					spielerC.setKi(ki);
				}

				// ----------------------------------------------------------------

				// -SpielerB einlesen und im Spiel Objekt
				// setzen-------------------
				spielerB = null;
				Spieler spielerD = new Spieler("spielerB", null);

				spielerD.setFarbe(this.toFarbEnum(irgendwas[x++]));
				spielerD.setName(irgendwas[x++]);
				spielerD.setMussSpringen(toBoolean(irgendwas[x++]));
				if (toBoolean(irgendwas[x++]) == true) {
					KI ki = new KI_Dame(spielerD);
					spielerD.setKi(ki);
				}
				spielerD.setAktiv(toBoolean(irgendwas[x++]));

				// ----------------------------------------------------------------
				// -SpielerB.getAktiv() einlesen und spielerAktiv
				// setzen-----------

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

	public String rewandler(int x, int y) {
		Character a;
		Integer b;

		a = (char) ((char) x + 97);
		b = y + 1;

		String c = a.toString();
		String d = b.toString();

		String s = c + d;
		return s;
	}

	@Override
	public String[] kiZug() {
		String[] zug = spielerAktiv.getKi().wasMacheIch(brett);
		return zug;
	}
	public void DamenMaker(){
		
//		this.brettArray[1][3].getFigur().setDame();
//		this.brettArray[3][3].getFigur().setDame();
//		this.brettArray[0][8].getFigur().setDame();
//		this.brettArray[2][8].getFigur().setDame();
	}
}