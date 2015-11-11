package GUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Basisklassen.FarbEnum;
import Basisklassen.Spiel;
import Basisklassen.Spielbrett;
import Basisklassen.Spieler;
import Interfaces.iBediener;

public class GUI extends JFrame{
	
	private  JPanel mainJpanel;
	private  GUI g;
	private  JMenuBar menuBar;
	private  JMenu menuGame;
	private  JMenuItem menuItemStart;
	private  JMenuItem menuItemSave;
	private  JMenuItem menuItemLoad;
	
	private JTextField eingabe;
	private JButton bSubmit;
	

	private Spielbrett spielBrett;
	private Spieler spielerA;
	private Spieler spielerB;
	private Spiel spiel;
	
	private  iBediener ibediener;
	

	private EventHandler eh;

	
public GUI(String nameSpielerA, boolean aIstKi, String nameSpielerB, boolean bIstKi){

	initNeuesSpiel(nameSpielerA, aIstKi, nameSpielerB, bIstKi);
	guiStartup();
}
public GUI(Spiel s){
	initGeladenesSpiel(s);
	guiStartup();
}
public void guiStartup(){
	this.setTitle("Dame V1.0");
	this.setSize(1150, 900); //Größe des JFrames
	this.setMinimumSize(new Dimension(1150, 900)); //Minimalgröße des JFrames
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	mainJpanel = new JPanel();
	this.getContentPane().add(mainJpanel);
	eh = new EventHandler(this);
	addMenuBar();	//Fuege MenuBar hinzu
	setupLayout();	//Erstelle unser Layout
	this.setVisible(true);
	

}
public void initNeuesSpiel(String nameSpielerA, boolean aIstKi, String nameSpielerB, boolean bIstKi){
	spielBrett = new Spielbrett();
	

	spielerA = new Spieler(nameSpielerA, FarbEnum.schwarz, aIstKi, spielBrett); 
	spielerB = new Spieler(nameSpielerB, FarbEnum.weiss, bIstKi, spielBrett); 
	
	spiel = new Spiel(spielerA, spielerB, spielBrett);
	ibediener=spiel;
	
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
	
	menuItemStart.addActionListener(eh);	//Fuege fuer alle Menupunkte die etwas ausfuehren/aufrufen einen Action-Listener ein
	menuItemSave.addActionListener(eh);	//Fuege fuer alle Menupunkte die etwas ausfuehren/aufrufen einen Action-Listener ein
	menuItemLoad.addActionListener(eh);	//Fuege fuer alle Menupunkte die etwas ausfuehren/aufrufen einen Action-Listener ein
	
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
	
	eingabe = new JTextField("a4-b5");
	
	JLabel ueberschrift = new JLabel("(ID-Startfeld)-(ID-Zielfeld) eingeben");
	
	JButton bWEST = new JButton("WEST");
	bSubmit = new JButton("Durchführen");
	bSubmit.addActionListener(eh);
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
	
	centerPanel.setLayout(null); //kein layoutmanager, da spielBrett schon ein layout hat
	centerPanel.add(spielBrett);
	
	
	
	this.mainJpanel.add(westPanel, BorderLayout.WEST); //Fuege alle Panels ihres Zustaendigkeitsbereichs zu
	this.mainJpanel.add(eastPanel, BorderLayout.EAST); 
	this.mainJpanel.add(northPanel, BorderLayout.NORTH); 
	this.mainJpanel.add(southPanel, BorderLayout.SOUTH); 
	this.mainJpanel.add(centerPanel, BorderLayout.CENTER); 
	

}


public Spiel getSpiel() {
	return spiel;
}
public JTextField getEingabe() {
	return eingabe;
}
public JButton getbSubmit() {
	return bSubmit;
}
public JMenuItem getMenuItemStart() {
	return menuItemStart;
}
public JMenuItem getMenuItemSave() {
	return menuItemSave;
}
public JMenuItem getMenuItemLoad() {
	return menuItemLoad;
}
public void bewegeSpielfigur(String eingabe, String ausgabe) {
	String s1=eingabe; String s2=ausgabe;
	ibediener.bewegeSpielfigur(s1, s2);
}
	
}
