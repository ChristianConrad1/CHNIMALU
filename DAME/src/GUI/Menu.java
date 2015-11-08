package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Menu extends JFrame implements ActionListener{
	private JPanel mainPanel;
	private JButton laden;
	private JButton neues;
	private JButton ende;
	private JButton start;

	public Menu(String titel, int breite, int hoehe) {
		//-JFrame erstellen
		super(titel);
		this.setSize(breite, hoehe);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		//---------------------------------
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		

	
		
		neues = new JButton("Neues Spiel");
		neues.setPreferredSize(new Dimension(180, 50));
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0,0,30,0);
		neues.addActionListener(this);
		mainPanel.add(neues, c);
		
		laden = new JButton("Spiel laden");
		laden.setPreferredSize(new Dimension(180, 50));
		c.gridy = 2;
		laden.addActionListener(this);
		mainPanel.add(laden, c);

		ende = new JButton("Ende");
		ende.setPreferredSize(new Dimension(180, 50));
		c.gridy = 3;
		ende.addActionListener(this);
		mainPanel.add(ende, c);
		
	
		
		this.add(mainPanel, BorderLayout.CENTER);
		this.setVisible(true);
		
		
	}
	public void neuesSpiel(){
		
		//Neuen JFrame und JPanel für neues Menü erstellen
		JFrame neuesSpiel = new JFrame("Neues Spiel");
		neuesSpiel.setSize(super.getWidth(), super.getHeight());
		neuesSpiel.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		neuesSpiel.setLocationRelativeTo(null);
		JPanel secPanel = new JPanel();
		secPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//--------------------------------------------------
		
		//Textfelder, Checkboxen und Button erstellen und positonieren
		JTextField nameA = new JTextField("Name SpielerA");
		nameA.setPreferredSize(new Dimension(180, 20));
		c.insets = new Insets(0,0,30,0);
		c.gridx = 1;
		c.gridy = 0;
		secPanel.add(nameA,c);
		JCheckBox aIstKi = new JCheckBox("Ist KI?");
		c.gridy = 1;
		secPanel.add(aIstKi,c);
		JTextField nameB = new JTextField("Name SpielerB");
		nameB.setPreferredSize(new Dimension(180, 20));
		c.gridy = 2;
		secPanel.add(nameB,c);
		JCheckBox bIstKi = new JCheckBox("Ist KI?");
		c.gridy = 3;
		secPanel.add(bIstKi,c);
		start = new JButton("Spiel starten");
		c.gridy = 4;
		start.addActionListener(this);
		secPanel.add(start,c);
		//-------------------------------------------------
		
		//JPanel auf neuen JFrame hinzufügen und Sichtbar machen
		neuesSpiel.add(secPanel, BorderLayout.CENTER);
		neuesSpiel.setVisible(true);
		this.dispose(); //schließt den alten JFrame (Startmenü)
		//------------------------------------------------------
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == neues){
			neuesSpiel();
		}
		if(e.getSource() == laden){
			JFileChooser jfc = new JFileChooser("savegame");
			jfc.showOpenDialog(null);
		}
		if(e.getSource() == ende){
			int yn = JOptionPane.showConfirmDialog(null, "Wollen Sie das Spiel beenden?", "Sicher?", JOptionPane.YES_NO_OPTION);
			if(yn == 0){
			this.dispose(); //Zuerst alle referenzen etc. l�schen, dann soft close 
			}else return;
		}
		if(e.getSource() == start){
			JOptionPane.showMessageDialog( null, "Hier wuerde jetzt ein neues Spiel gestartet werden...." );
		}
	}

}
