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
	public void save(Object o) {
		
		this.s=s; //Könnte wichtig sein, um weitere Informationen in die PDF zu schreiben, bisher noch nicht benutzt
		
		try{
		Document document = new Document();
		Image img=null;
		img = Image.getInstance("savegame/screenshotDame.png");
		img.scaleToFit(400, 400);
		
		PdfWriter.getInstance(document, new FileOutputStream("savegame/Dame.pdf"));
		document.open();
		document.add(img);
		document.add(new Paragraph("TEXT!"));
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