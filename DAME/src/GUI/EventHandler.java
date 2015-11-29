package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Interfaces.iBediener;
import java.util.regex.Pattern;
public class EventHandler implements ActionListener{
	private static GUI g; //Ich weiß nicht ob das so passt, aber mir fiel nix anderes ein....
	private Menu m;
	private SpielfeldMapped f;
	private static boolean buttonClick; //Wenn ein button geklickt wurde, dann true, da Methode auf zweiten Button warten muss
	private static String eingabeButton;
	private iBediener b; 
	
public EventHandler(GUI g){
		this.g  = g; //Ich weiß nicht ob das so passt, aber mir fiel nix anderes ein....
		b = this.g.getIbediener(); 
}
public EventHandler(Menu m){
	this.m = m;
}
public EventHandler(SpielfeldMapped f){
	this.f = f;
	b = g.getIbediener(); 
}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Kennen sich �berhaupt...");
		if (g != null) {
			
			if(e.getSource() == g.getSendMail()){
				//Mache Screenshot vom aktuellen Stand:
				Screenshot s = new Screenshot(g.getBrettMapped());
				BufferedImage img = s.makeScreenshot();

				
			    try {
	                // write the image as a PNG
	                ImageIO.write(img,"png",new File("savegame/screenshotDame.png"));
	              } catch(Exception ex) {
	                ex.printStackTrace();
	              }
				b.mail();
			}
			
			if (e.getSource() == g.getMenuItemStart()) {
				
				g.dispose();
			}
			if (e.getSource() == g.getMenuItemLoad()) {
				JFileChooser jfc = new JFileChooser("savegame");
				jfc.showOpenDialog(null);
				String pfad = jfc.getSelectedFile().getAbsolutePath();
				b.laden(pfad);
			}
			if (e.getSource() == g.getMenuItemSave()) {

				
				//�bergebe Pfad f�r zu speichernde Datei:
				JFileChooser jfc = new JFileChooser("savegame");
				jfc.showSaveDialog(null);
				String pfad = jfc.getSelectedFile().getAbsolutePath();
				System.out.println(pfad);
				b.speichern(pfad);

			}
			if(e.getSource() == g.getbSubmit()){
				//HIER MUSS WAS REIN
				String s=g.getEingabe().getText();	//Unser String mit Strich Notation z.B. a4-b5
				if(s.matches("\\p{Lower}\\d-\\p{Lower}\\d")){
				int split=s.indexOf('-');
	
				String eingabe = s.substring(0,split);
				String ausgabe = s.substring(split+1);
				
				System.out.println(eingabe +" ausgabe:"+ ausgabe);
				
				b.bewegeSpielfigur(eingabe, ausgabe);
				}
				else{
					JOptionPane.showMessageDialog(null, "Eingabe entspricht nicht den vorgaben!");
				}
			}
		}
		
		if(f!=null){
			if(e.getSource() == f){
				if(buttonClick==false){
				System.out.println("Waehle Zielfeld!"); //Wird an Textbox oder so ausgegeben!
				eingabeButton=f.getID();
				System.out.println(f.getID());
				buttonClick=true;
				return; //muss sein, weil er sonst gleich beides ausführt!!
				}
				if(buttonClick==true){
					String ausgabeButton=f.getID();
					b.bewegeSpielfigur(eingabeButton, ausgabeButton);
					ausgabeButton="";
					buttonClick=false;
				}
			}
		}
		
		
		if(m!=null){
		if (e.getSource() == m.getNeues()) {
			m.neuesSpielMenu();
		}
		if (e.getSource() == m.getLaden()) {
			JFileChooser jfc = new JFileChooser("savegame");
			jfc.showOpenDialog(null);
			String pfad = jfc.getSelectedFile().getAbsolutePath();
			g=new GUI();
			g.initNeuesSpiel("s", false, "ss", false);
			b = g.getIbediener(); 
			b.laden(pfad);			
			m.dispose();
			

			
		}
		if (e.getSource() == m.getEnde()) {
			int yn = JOptionPane.showConfirmDialog(null, "Wollen Sie das Spiel beenden?", "Sicher?",
					JOptionPane.YES_NO_OPTION);
			if (yn == 0) {
				m.dispose(); // Zuerst alle referenzen etc. l�schen, dann soft
								// close
			} else
				return;
		}
		if (e.getSource() == m.getStart()) {
			m.neuesSpielStarten();
		}

	}
		
	}
}
