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



	
	private PrintWriter pw;
	private BufferedReader br;
	public DatenzugriffCSV(){	
	}
	

	@Override
	public void save(Object o, String pfad) {
	
		try {
			pw = new PrintWriter(new FileWriter(pfad));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.write((String)o);
		
		pw.close();
		
	}
	@Override
	public Object load(String pfad) {
		try {
			br = new BufferedReader(new FileReader(pfad));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String res = "";
		String s;
		try {
			s = br.readLine();

		while(s != null){
			res+=s + "\n";
			s = br.readLine();
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
		
		
	}
	


}
