package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Basisklassen.Spiel;
import Basisklassen.Spieler;
import Interfaces.iBediener;
import Interfaces.iMessage;

public class GUI extends JFrame implements iMessage, Runnable{
	
	private ImageIcon blackStone;
	private ImageIcon whiteStone;

	
	
	private  JPanel mainJpanel;
	private  JMenuBar menuBar;
	private  JMenu menuGame;
	private  JMenuItem menuItemStart;
	private  JMenuItem menuItemSave;
	private  JMenuItem menuItemLoad;
	
	private JTextField eingabe;
	private JButton bSubmit;
	
	private JScrollPane jsp;
	private JTextArea jta;
	private JPanel felderPanel;
	private JLabel datenSpielerAktiv;
	
	private SpielbrettMapped brettMapped;
	private SpielfeldMapped[][] brettArray;
	
	private  iBediener ibediener; 
	
	private String[] spielerNamen;
	private boolean[] spielerSindKi;
	

	private EventHandler eh;

	  private BufferedImage image;
	
public GUI(){
	
//Spielbrett Erstellung braucht eine Methode	
	
	guiStartup();
	
}

public void guiStartup(){
	this.setTitle("Dame V1.0");
	 //Groe�e des JFrames
	 //Minimalgröße des JFrames
	this.setSize(1200, 1000);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	mainJpanel = new JPanel();
	this.getContentPane().add(mainJpanel);

	addMenuBar();	//Fuege MenuBar hinzu
	
	

}
public void initNeuesSpiel(String nameA, boolean aIstKi, String nameB, boolean bIstKi){

	
//	spielerA = new Spieler(nameSpielerA, FarbEnum.schwarz, aIstKi, spielBrett); 
//	spielerB = new Spieler(nameSpielerB, FarbEnum.weiss, bIstKi, spielBrett); 
	
	ibediener=new Spiel();
	ibediener.init(this);
	
	spielerNamen = new String[2];
	spielerSindKi = new boolean[2];
	spielerNamen[0] = nameA;
	spielerNamen[1] = nameB;
	spielerSindKi[0] = aIstKi;
	spielerSindKi[1] = bIstKi;
	
	
//	ibediener.spielerHzfg(nameA, aIstKi);
//	ibediener.spielerHzfg(nameB, bIstKi);
	
	eh = new EventHandler(this);
	
	initBrett(); //dies ist das GEMAPPTE spielbrett
	setupLayout();	//Erstelle unser Layout

	
}

public void initBrett() { //HIER WIRD ZU MAPPENDES BRETT ERSTELLT

    brettMapped = new SpielbrettMapped();
    brettMapped.setLayout(null);
    brettMapped.setPreferredSize(new Dimension(816,816));
    brettMapped.setMinimumSize(new Dimension(816,816));
	brettArray = brettMapped.getNotation();
	felderPanel = new JPanel();
	
	initImg();

	//Hier wird Brett zum GridBagLayout und setzt Standarticons
	
		int counter = 0;
	
		felderPanel.setLayout(new GridBagLayout()); //Layout vom Spielbrett
		felderPanel.setOpaque(false);
		felderPanel.setPreferredSize(new Dimension(720,720)); //Size vom Spielbrett
		GridBagConstraints c = new GridBagConstraints();
		for (int i = 11; i >= 0; i--) {
			c.gridy = counter;
			for (int n = 0; n < brettArray[i].length; n++) {
				c.gridx = n;
				brettArray[n][i].setPreferredSize(new Dimension(60,60));
				felderPanel.add(brettArray[n][i], c); //Spielbrett.add

				
			}
			
			counter++;
		}
		felderPanel.setBounds(50, 44, 720, 720);
		brettMapped.add(felderPanel);
		drawBrett();

	}

public void drawBrett(){
	
	//Gesamte CSV Notation wird in ein eindimensionales String-Array gespeichert, jeder String[index] enth�lt ein Feld 0-12 entspricht a12-l12
	String brettString = ibediener.belegungCSV();
	
	String[] field=brettString.split(";");
	

	//brettArray(Mapped) bekommt nun die neue Notation �bertragen:
	
	int count=0; //Die Variable um durch unseren CSV-String zu laufen
	for(int n=brettArray.length-1; n >= 0 ; n--){
		for(int k=0; k<brettArray[n].length; k++){
			if(field[count].equals("[O]")){
			brettArray[k][n].setIcon(whiteStone);
			brettArray[k][n].setRolloverIcon(blackStone);
			}
			if(field[count].equals("[X]")){
			brettArray[k][n].setIcon(blackStone);
			brettArray[k][n].setRolloverIcon(whiteStone);
			}
			if(field[count].equals("[ ]")){
			brettArray[k][n].setIcon(null);
			brettArray[k][n].setRolloverIcon(null);
			}
			
					count++;
		}
	}
	

}



public void initGeladenesSpiel(Spiel s){
	//this.spiel = s;
//	this.spielBrett = this.spiel.getBrett();
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
	datenSpielerAktiv = new JLabel();
	datenSpielerAktiv.setHorizontalAlignment(SwingConstants.CENTER);
	datenSpielerAktiv.setBackground(Color.CYAN);
	datenSpielerAktiv.setOpaque(true);
	
	
	jta = new JTextArea(5,1);
	jsp = new JScrollPane(jta);
	
	westPanel.add(bWEST);
	westPanel.add(new JLabel("Hier kommt was hin"));
	westPanel.setLayout(new GridLayout(5,1));	//Erzeuge testhaft ein neues Gridlayout, dass seine Komponenten in 1 Spalte und 5 Zeilen unterteilt
	
	eastPanel.add(ueberschrift);
	eastPanel.add(eingabe);	
	eastPanel.add(bSubmit);
	eastPanel.setLayout(new GridLayout(3,1)); //Gebe dem jeweiligen Panel ein dafuer sinnvolles Layout (je nachdem ,wie wir das realisieren)
	
	northPanel.setLayout(new GridLayout(1,1));
	northPanel.add(datenSpielerAktiv);
	
	
	southPanel.add(jsp);
	southPanel.setLayout(new GridLayout(1,1));

	
	centerPanel.setLayout(new BorderLayout());

	centerPanel.add(brettMapped, BorderLayout.CENTER);

	
	this.mainJpanel.add(westPanel, BorderLayout.WEST); //Fuege alle Panels ihres Zustaendigkeitsbereichs zu
	this.mainJpanel.add(eastPanel, BorderLayout.EAST); 
	this.mainJpanel.add(northPanel, BorderLayout.NORTH); 
	this.mainJpanel.add(southPanel, BorderLayout.SOUTH); 
	this.mainJpanel.add(centerPanel, BorderLayout.CENTER); 
	
	this.setVisible(true);
	Thread t1 = new Thread(this);
	t1.start();
	
	ibediener.spielerHzfg(spielerNamen[0], spielerSindKi[0]);
	ibediener.spielerHzfg(spielerNamen[1], spielerSindKi[1]);
	
	

}
public void initImg(){
	
	blackStone = new ImageIcon("res/img/blackStone.png");
	whiteStone = new ImageIcon("res/img/whiteStone.png");
}


public iBediener getIbediener() {
	return ibediener;
}

public Spiel getSpiel() {
	//return spiel;
	return null;
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
public  void bewegeSpielfigur(String eingabe, String ausgabe) {
	String s1=eingabe; String s2=ausgabe;
	ibediener.bewegeSpielfigur(s1, s2);
	
}
@Override
public void printError(String msg) {
	JOptionPane.showMessageDialog(null, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
	
}

	@Override
	public void printOk(String msg) {
		if (jta.getText().length() > 0) {
			jta.setText(jta.getText() + "\n" + msg);
		}
		else{
			jta.setText(msg);
		}
	}
@Override
public void printOkWindow(String msg) {
	JOptionPane.showMessageDialog(null, msg, "Bestätigen", JOptionPane.OK_CANCEL_OPTION);	
}

@Override
public void printPusten(String msg) {
	String feld = JOptionPane.showInputDialog(null, msg, "Figur zum Pusten w�hlen", JOptionPane.QUESTION_MESSAGE);
	ibediener.pusten(feld);

}
@Override
public void printSpielerAktiv(String msg) {
	this.datenSpielerAktiv.setText(msg);	
}

@Override
public void run() {
	while(true){
		this.drawBrett();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}


	
}
