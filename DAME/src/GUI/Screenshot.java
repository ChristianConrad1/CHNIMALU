package GUI;

import java.awt.Component;
import java.awt.image.BufferedImage;

public class Screenshot {
	
	private Component c;
	
	public Screenshot(Component c){
	this.setC(c);
	}
	
	 public BufferedImage makeScreenshot() {
		 if(c!=null){
			    BufferedImage image = new BufferedImage (c.getWidth(), c.getHeight(),BufferedImage.TYPE_INT_RGB);
			    // Benutze paint Methode des Komponenten (hier: der GUI)
			    c.paint(image.getGraphics());
			    return image;
			  }
		return null;
	 }
	
	
	public Component getC() {
		return c;
	}

	public void setC(Component c) {
		if(c!=null){
		this.c = c;
		}
		else{
			throw new NullPointerException("Kein gültiges GUI-Objekt übergeben!");
		}
	}

}
