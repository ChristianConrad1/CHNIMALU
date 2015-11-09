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
			}
		}
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
