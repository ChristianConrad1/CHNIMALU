package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class EventHandler implements ActionListener{
	private GUI g;
	private Menu m;
public EventHandler(GUI g){
		this.g = g;
}
public EventHandler(Menu m){
	this.m = m;
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
		if (m!=null && e.getSource() == m.getNeues()) {
			m.neuesSpielMenu();
		}
		if (m!=null && e.getSource() == m.getLaden()) {
			m.oeffneFileChooser();
			m.geladenesSpielStarten();
		}
		if (m!=null && e.getSource() == m.getEnde()) {
			int yn = JOptionPane.showConfirmDialog(null, "Wollen Sie das Spiel beenden?", "Sicher?",
					JOptionPane.YES_NO_OPTION);
			if (yn == 0) {
				m.dispose(); // Zuerst alle referenzen etc. l�schen, dann soft
								// close
			} else
				return;
		}
		if (m!=null && e.getSource() == m.getStart()) {
			m.neuesSpielStarten();
		}

	}

}
