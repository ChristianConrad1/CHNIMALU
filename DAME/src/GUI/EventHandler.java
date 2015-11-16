package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Interfaces.iBediener;

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
				JFileChooser jfc = new JFileChooser("savegame");
				jfc.showSaveDialog(null);
				String pfad = jfc.getSelectedFile().getAbsolutePath();
				System.out.println(pfad);
				b.speichern(pfad);

			}
			if(e.getSource() == g.getbSubmit()){
				//HIER MUSS WAS REIN
				String s=g.getEingabe().getText();	//Unser String mit Strich Notation z.B. a4-b5
				System.out.println(s);
				int split=s.indexOf('-');
				
				String eingabe = s.substring(0,split);
				String ausgabe = s.substring(split+1);
				
				System.out.println(eingabe +" ausgabe:"+ ausgabe);
				
				b.bewegeSpielfigur(eingabe, ausgabe);
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
//		if (e.getSource() == m.getLaden()) {
//			//Muss Fenster zur Dateiauswahl aufrufen, die einen Pfad angibt
//			JFileChooser jfc = new JFileChooser("savegame");
//			jfc.showOpenDialog(null);
//			String pfad = jfc.getSelectedFile().getAbsolutePath();
//			b.laden(pfad);
//			
////			m.dispose();
////			g=new GUI();
////			g.initNeuesSpiel("s", false, "ss", false);
//
//			
//		}
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
