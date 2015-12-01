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

	public DatenzugriffSerialisiert(){

	}

	@Override
	public void save(Object o, String pfad) {
		try{
			oos = new ObjectOutputStream(new FileOutputStream(pfad));
			System.out.println("Datei angelegt");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			oos.writeObject(o);
			System.out.println("Datei beschrieben");
			
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
	public Object load(String pfad) {
		try{
			ois = new ObjectInputStream(new FileInputStream(pfad));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		Object s;
		try{
			s = ois.readObject();
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
