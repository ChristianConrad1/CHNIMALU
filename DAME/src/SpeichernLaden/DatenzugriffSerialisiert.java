package SpeichernLaden;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Basisklassen.Spiel;
import Interfaces.iDatenzugriff;


public class DatenzugriffSerialisiert implements iDatenzugriff{
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Spiel s;
	public DatenzugriffSerialisiert(){

	}
	public void setSpiel(Spiel s){
		if(s!=null){
			this.s = s;
		}
		else throw new NullPointerException("Spiel ist NULL");	
	}
	@Override
	public void save(Spiel s) {
		try{
			oos = new ObjectOutputStream(new FileOutputStream("savegame/save.ser"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		this.setSpiel(s);
		try {
			oos.writeObject(this.s);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Spiel load() {
		try{
			ois = new ObjectInputStream(new FileInputStream("savegame/save.ser"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		Spiel s;
		try{
			s = (Spiel) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			s = null;
		} finally{
			try {
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return s;
		
	}

}
