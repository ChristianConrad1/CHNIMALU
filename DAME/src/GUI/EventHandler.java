package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Basisklassen.Spielfeld;

public class EventHandler implements ActionListener{
	private GUI g;
	private Menu m;
	private Spielfeld f;
	private static boolean buttonClick; //Wenn ein button geklickt wurde, dann true, da Methode auf zweiten Button warten muss
	private static String eingabeButton;
	
public EventHandler(GUI g){
		this.g = g;
}
public EventHandler(Menu m){
	this.m = m;
}
public EventHandler(Spielfeld f){
	this.f = f;
}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (g != null) {
			if (e.getSource() == g.getMenuItemStart()) {
				m = new Menu("Startmenü");
				m.neuesSpielMenu();
				g.dispose();
			}
			if (e.getSource() == g.getMenuItemLoad()) {
				m = new Menu("Startmenü");
				g.dispose();
				m.oeffneFileChooser();
				m.geladenesSpielStarten();
			}
			if (e.getSource() == g.getMenuItemSave()) {
				JOptionPane.showMessageDialog(null, "Hier wird noch Speichern implementiert", "Speichern",
						JOptionPane.INFORMATION_MESSAGE);
			}
			if(e.getSource() == g.getbSubmit()){
				//HIER MUSS WAS REIN
				String s=g.getEingabe().getText();	//Unser String mit Strich Notation z.B. a4-b5
				System.out.println(s);
				int split=s.indexOf('-');
				
				String eingabe = s.substring(0,split);
				String ausgabe = s.substring(split+1);
				
				System.out.println(eingabe +" ausgabe:"+ ausgabe);
				
				g.bewegeSpielfigur(eingabe, ausgabe);
			}
		}
		
		if(f!=null){
			if(e.getSource() == f){
				if(buttonClick==false){
				System.out.println("Waehle Zielfeld!"); //Wird an Textbox oder so ausgegeben!
				eingabeButton=f.getID();
				buttonClick=true;
				}
				if(buttonClick==true){
					String ausgabeButton=f.getID();

					g.bewegeSpielfigur(eingabeButton, ausgabeButton);
					ausgabeButton=" ";
					buttonClick=false;
				}
			}
		}
		
		
		if(m!=null){
		if (e.getSource() == m.getNeues()) {
			m.neuesSpielMenu();
		}
		if (e.getSource() == m.getLaden()) {
			m.oeffneFileChooser();
			m.geladenesSpielStarten();
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
