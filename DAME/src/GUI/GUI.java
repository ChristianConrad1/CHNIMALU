package GUI;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Basisklassen.FarbEnum;
import Basisklassen.Spiel;
import Basisklassen.Spielbrett;
import Basisklassen.Spieler;

public class GUI extends JFrame implements ActionListener{
	
	private  JPanel mainJpanel;
	private  GUI g;
	private  JMenuBar menuBar;
	private  JMenu menuGame;
	private  JMenuItem menuItemStart;
	private  JMenuItem menuItemSave;
	private  JMenuItem menuItemLoad;
	

	
	private Spielbrett spielBrett;
	private Spieler spielerA;
	private Spieler spielerB;
	private Spiel spiel;

	
public GUI(String nameSpielerA, boolean aIstKi, String nameSpielerB, boolean bIstKi){

	initNeuesSpiel(nameSpielerA, aIstKi, nameSpielerB, bIstKi);
	
	this.setVisible(true);
	this.setTitle("Dame V1.0");
	this.setSize(700, 700);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	mainJpanel = new JPanel();
	this.getContentPane().add(mainJpanel);
	addMenuBar();	//Fuege MenuBar hinzu
	setupLayout();	//Erstelle unser Layout
	
}
public GUI(Spiel s){

	initGeladenesSpiel(s);
	
	this.setVisible(true);
	this.setTitle("Dame V1.0");
	this.setSize(700, 700);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	mainJpanel = new JPanel();
	this.getContentPane().add(mainJpanel);
	addMenuBar();	//Fuege MenuBar hinzu
	setupLayout();	//Erstelle unser Layout
	
}
public void initNeuesSpiel(String nameSpielerA, boolean aIstKi, String nameSpielerB, boolean bIstKi){
	spielBrett = new Spielbrett();
	

	spielerA = new Spieler(nameSpielerA, FarbEnum.schwarz, aIstKi, spielBrett); 
	spielerB = new Spieler(nameSpielerB, FarbEnum.weiss, bIstKi, spielBrett); 
	
	spiel = new Spiel(spielerA, spielerB, spielBrett);
	
}
public void initGeladenesSpiel(Spiel s){
	this.spiel = s;
	this.spielBrett = this.spiel.getBrett();
}

public void addMenuBar(){ //Hier werden alle Buttons etc fuer das Menu hinzugefuegt
	
	menuBar = new JMenuBar();
	menuGame = new JMenu("Menü");
	menuItemStart = new JMenuItem("Neues Spiel starten");
	menuItemSave = new JMenuItem("Spiel speichern");
	menuItemLoad = new JMenuItem("Spiel laden");
	
	
	menuGame.add(menuItemStart);
	menuGame.add(menuItemSave);
	menuGame.add(menuItemLoad);
	
	menuItemStart.addActionListener(this);	//Fuege fuer alle Menupunkte die etwas ausfuehren/aufrufen einen Action-Listener ein
	menuItemSave.addActionListener(this);	//Fuege fuer alle Menupunkte die etwas ausfuehren/aufrufen einen Action-Listener ein
	menuItemLoad.addActionListener(this);	//Fuege fuer alle Menupunkte die etwas ausfuehren/aufrufen einen Action-Listener ein
	
	menuBar.add(menuGame);
	this.setJMenuBar(menuBar);
	
	
	
}

public void setupLayout(){	//Hier wird das Layout angepasst. Das ist der Kern unserer GUI, Buttons, Bereiche etc. werden hierr�ber definiert
	
	this.mainJpanel.setLayout(new BorderLayout()); //Unser Haupt-JPanel entspricht einem Border-Layout
	
	JPanel westPanel = new JPanel();	//Fuge fuer den jeweiligen Bereich der zu bearbeiten gilt neue Panels ein
	JPanel eastPanel = new JPanel();
	JPanel northPanel = new JPanel();
	JPanel southPanel = new JPanel();
	JPanel centerPanel = new JPanel();
	
	JTextField eingabe = new JTextField("a4-b5");
	
	JLabel ueberschrift = new JLabel("(ID-Startfeld)-(ID-Zielfeld) eingeben");
	
	JButton bWEST = new JButton("WEST");
	JButton bSubmit = new JButton("Durchführen");
	JButton bNORTH = new JButton("NORTH");
	JButton bSOUTH = new JButton("SOUTH");
	JButton bCENTER = new JButton("CENTER");
	
	westPanel.add(bWEST);
	westPanel.add(new JLabel("Hier kommt was hin"));
	westPanel.setLayout(new GridLayout(5,1));	//Erzeuge testhaft ein neues Gridlayout, dass seine Komponenten in 1 Spalte und 5 Zeilen unterteilt
	
	eastPanel.add(ueberschrift);
	eastPanel.add(eingabe);	
	eastPanel.add(bSubmit);
	eastPanel.setLayout(new GridLayout(3,1)); //Gebe dem jeweiligen Panel ein dafuer sinnvolles Layout (je nachdem ,wie wir das realisieren)
	
	northPanel.add(bNORTH);
	northPanel.setLayout(new GridLayout(1,1));
	
	southPanel.add(bSOUTH);
	southPanel.setLayout(new GridLayout(1,1));
	
	//centerPanel.add(bCENTER);
	
	centerPanel.add(spielBrett);
	centerPanel.setLayout(new GridLayout(1,1));
	
	
	this.mainJpanel.add(westPanel, BorderLayout.WEST); //Fuege alle Panels ihres Zustaendigkeitsbereichs zu
	this.mainJpanel.add(eastPanel, BorderLayout.EAST); 
	this.mainJpanel.add(northPanel, BorderLayout.NORTH); 
	this.mainJpanel.add(southPanel, BorderLayout.SOUTH); 
	this.mainJpanel.add(centerPanel, BorderLayout.CENTER); 
	

}


public void actionPerformed(ActionEvent e){ //Hier werden Action-Events abgefangen. Wobei ich diese gerne spaeter als externe Klasse haette
	
	if(e.getSource()==menuItemStart){
		Menu m = new Menu("Startmenü", 300, 400);
		m.neuesSpielMenu();
		this.dispose();
	}
	if(e.getSource()==menuItemLoad){
		Menu m = new Menu("Startmenü", 300, 400);
		this.dispose();
		m.oeffneFileChooser();
		m.geladenesSpielStarten();
	}
	if(e.getSource()==menuItemSave){
		JOptionPane.showMessageDialog(null, "Hier wird noch Speichern implementiert", "Speichern", JOptionPane.INFORMATION_MESSAGE);
	}
	
}
	
}
