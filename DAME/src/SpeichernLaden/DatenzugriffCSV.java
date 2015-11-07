package SpeichernLaden;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import Basisklassen.FarbEnum;
import Basisklassen.KI;
import Basisklassen.KI_Dame;
import Basisklassen.Spiel;
import Basisklassen.Spielbrett;
import Basisklassen.Spieler;
import Basisklassen.Spielfeld;
import Basisklassen.Spielfigur;
import Interfaces.iDatenzugriff;

public class DatenzugriffCSV implements iDatenzugriff{

	private Spiel s;
	private Spieler spielerA; //attribute können wahrscheinlich entfernt werden
	private Spieler spielerB;
	private Spieler spielerAktiv; 
	private Spielbrett brett;
	
	private PrintWriter pw;
	private BufferedReader br;
	public DatenzugriffCSV(){	
	}
	

	@Override
	public void save(Spiel s) {
		this.setSpiel(s);
		try {
			pw = new PrintWriter(new FileWriter("savegame/save.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//-Spielfeld speichern-----------------------------------
		Spielfeld[][] belegung = brett.getNotation();
		for (int i = belegung.length - 1; i >= 0; i--) {
			for (int n = 0; n < belegung[i].length; n++) {
				pw.print(belegung[n][i].getAusgabeID() + ";");
			}
			pw.println();
		}
		//--------------------------------------------------------
		//-Spieler A speichern------------------------------------
		pw.println(this.getSpielerA().getFarbe());
		pw.println(this.getSpielerA().getName());
		pw.println(this.getSpielerA().getMussSpringen());
		pw.println(this.getSpielerA().isKI());

		//--------------------------------------------------------
		//-Spieler B speichern------------------------------------
		pw.println(this.getSpielerB().getFarbe());
		pw.println(this.getSpielerB().getName());
		pw.println(this.getSpielerB().getMussSpringen());
		pw.println(this.getSpielerB().isKI());
		pw.println(this.getSpielerB().getAktiv()); //wird nur von spielerB gespeichert, falls spielerB aktiv ist, 
												   //ist spielerA nicht aktiv und andersrum
		//--------------------------------------------------------
		
		pw.close();
		
	}
	@Override
	public Spiel load() {
		try {
			br = new BufferedReader(new FileReader("savegame/save.csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//-Brett in machBrett() einlesen und im Spiel Objekt setzten------
		this.machBrett();
		//----------------------------------------------------------------
		
		//-SpielerA einlesen und im Spiel Objekt setzen-------------------
		spielerA = new Spieler("spielerA", null);
		try {
			this.spielerA.setFarbe(this.toFarbEnum(br.readLine()));
			this.spielerA.setName(br.readLine());
			this.spielerA.setMussSpringen(toBoolean(br.readLine()));
			if(toBoolean(br.readLine()) == true){
				KI ki = new KI_Dame(spielerA, this.brett);
				this.spielerA.setKi(ki);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//----------------------------------------------------------------
		
		//-SpielerB einlesen und im Spiel Objekt setzen-------------------
		spielerB = new Spieler("spielerB", null);
		try {
			this.spielerB.setFarbe(this.toFarbEnum(br.readLine()));
			this.spielerB.setName(br.readLine());
			this.spielerA.setMussSpringen(toBoolean(br.readLine()));
			if(toBoolean(br.readLine()) == true){
				KI ki = new KI_Dame(spielerB, this.brett);
				this.spielerB.setKi(ki);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//----------------------------------------------------------------
		//-SpielerB.getAktiv() einlesen und spielerAktiv setzen-----------
		try {
			if(br.readLine().equals("true")){
				this.spielerAktiv = this.spielerB;
			}
			else this.spielerAktiv = this.spielerA;
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		//----------------------------------------------------------------
		
		s = new Spiel(spielerA, spielerB, brett);
		s.getSpielerA().setAktiv(false);
		s.setSpielerAktiv(spielerAktiv);
		
		return s;
	}
	
	private void setSpiel(Spiel s) {
		if(s != null){
			this.s = s;
		}else throw new NullPointerException("Spiel ist NULL!");
		this.setBrett();
		this.setSpielerA();
		this.setSpielerB();
		
	}
	public Spieler getSpielerA() {
		return spielerA;
	}
	public void setSpielerA() {
		if(s.getSpielerA() != null){
			this.spielerA = s.getSpielerA();
		}
		else throw new NullPointerException("SpielerA ist NULL!");
	}
	public Spieler getSpielerB() {
		return spielerB;
	}
	private void setSpielerB() {
		if(s.getSpielerB() != null){
			this.spielerB = s.getSpielerB();
		}
		else throw new NullPointerException("SpielerB ist NULL!");
	}
	public Spieler getSpielerAktiv() {
		return spielerAktiv;
	}
	private void setSpielerAktiv() {
		if(s.getSpielerAktiv() != null){
			this.spielerAktiv = s.getSpielerAktiv();
		}
		else throw new NullPointerException("SpielerAktiv ist NULL!");
	}
	public Spielbrett getBrett() {
		return brett;
	}
	private void setBrett() {
		if(s.getBrett() != null){
			this.brett = s.getBrett();
		}
		else throw new NullPointerException("Brett ist NULL!");
	}
	private void machBrett(){
		char c;
		String id = null;
		String line = null;
		String[] field = null;
		Spielfeld[][] belegung = new Spielfeld[12][12]; //Wenn auch die Spielfeldgröße gespeichert werden kann, dann muss hier angepasst werden.
		for (int i = belegung.length - 1; i >= 0; i--) {
			c = 97;
			try {
				line = br.readLine();
				field = line.split(";");
				System.out.println(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int n = 0; n < belegung[i].length; n++) {
				
					belegung[n][i] = new Spielfeld((char)(c+n)+""+(i+1));
					belegung[n][i].setXY(belegung[n][i].getID());
					belegung[n][i].setAusgabeID(field[n]);
					if(belegung[n][i].getAusgabeID().equals("[X]")){
						belegung[n][i].setFigur(new Spielfigur(FarbEnum.schwarz, belegung[n][i]));
					}
					else if(belegung[n][i].getAusgabeID().equals("[O]")){
						belegung[n][i].setFigur(new Spielfigur(FarbEnum.weiss, belegung[n][i]));
					}
					else if(belegung[n][i].getAusgabeID().equals("[*X*]")){
						belegung[n][i].setFigur(new Spielfigur(FarbEnum.schwarz, belegung[n][i]));
						belegung[n][i].getFigur().setDame();	
					}
					else if(belegung[n][i].getAusgabeID().equals("[*O*]")){
						belegung[n][i].setFigur(new Spielfigur(FarbEnum.weiss, belegung[n][i]));
						belegung[n][i].getFigur().setDame();	
					}	
					
			}	
			System.out.println();
		}
		this.brett = new Spielbrett();
		this.brett.setBrett(belegung);
	}
	private FarbEnum toFarbEnum(String farbe){
		if(farbe.equals("schwarz")){
			return FarbEnum.schwarz;
		}
		else if(farbe.equals("weiss")){
			return FarbEnum.weiss;
		}
		else throw new RuntimeException("Geladene Farbe gehört nicht zum Farbenum");
	}
	private boolean toBoolean(String bool){
		if(bool.equals("true")) return true;
		else if(bool.equals("false")) return false;
		else throw new RuntimeException("Gelesener Wert ist kein boolean!");
	}


}
