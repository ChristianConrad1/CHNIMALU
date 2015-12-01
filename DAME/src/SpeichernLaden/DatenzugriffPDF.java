package SpeichernLaden;

import java.io.FileOutputStream;

import Basisklassen.Spiel;
import Interfaces.iDatenzugriff;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class DatenzugriffPDF implements iDatenzugriff{
	
	private Spiel s;
	

	@Override
	public void save(Object o, String pfad) {
		
		if(o instanceof Spiel){
		this.s = (Spiel) o; //K�nnte wichtig sein, um weitere Informationen in die PDF zu schreiben, bisher noch nicht benutzt
		}
		else throw new RuntimeException("Gespeichertes Objekt ist kein Spiel");
		try{
		Document document = new Document();
		Image img=null;
		img = Image.getInstance("savegame/screenshotDame.png");
		img.scaleToFit(400, 400);
		
		PdfWriter.getInstance(document, new FileOutputStream(pfad));
		document.open();
		document.add(img);
		if(s.getSpielerA().isKI()){
			document.add(new Paragraph("Spieler 1 mit dem Namen: "+s.getSpielerA().getName()+" ist eine KI"));
		}
		else{
			document.add(new Paragraph("Spieler 1 mit dem Namen: "+s.getSpielerA().getName()));
		}
		if(s.getSpielerB().isKI()){
			document.add(new Paragraph("Spieler 2 mit dem Namen: "+s.getSpielerB().getName()+" ist eine KI"));
		}
		else{
			document.add(new Paragraph("Spieler 2 mit dem Namen: "+s.getSpielerB().getName()));
		}
		document.add(new Paragraph("Aktiver Spieler: "+s.getSpielerAktiv().getName()));
		document.close();
		}
		catch(Exception e){
			throw new RuntimeException("Fehler beim Speichern von PDF aufgetreten!");
		}
	}

	@Override
	public Object load(String pfad) {
		// TODO Auto-generated method stub
		return null;
	}
}