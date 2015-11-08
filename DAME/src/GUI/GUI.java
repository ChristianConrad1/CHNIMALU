package GUI;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GUI extends JFrame implements ActionListener{
	
	private  JPanel mainJpanel;
	private  GUI g;
	private  JMenuBar menuBar;
	private  JMenu menuGame;
	private  JMenuItem menuItemStart;

	
public GUI(){

	this.setVisible(true);
	this.setTitle("Dame V1.0");
	this.setSize(700, 700);
	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	mainJpanel = new JPanel();
	this.getContentPane().add(mainJpanel);
	addMenuBar();	//Fuege MenuBar hinzu
	setupLayout();	//Erstelle unser Layout
	
}

public void addMenuBar(){
	
	menuBar = new JMenuBar();
	menuGame = new JMenu("Game");
	menuItemStart = new JMenuItem("Start New Game");
	
	menuBar.add(menuGame);
	menuGame.add(menuItemStart);
	this.setJMenuBar(menuBar);
	
	menuItemStart.addActionListener(this);	//Fuege fuer alle Menupunkte die etwas ausfuehren/aufrufen einen Action-Listener ein
	
}

public void setupLayout(){	//Hier wird Border-Layout angepasst, Buttons hinzugefuegt etc
	
	this.mainJpanel.setLayout(new BorderLayout()); //Unser Haupt-JPanel entspricht einem Border-Layout
	
	JPanel westPanel = new JPanel();	//Fuge fuer den jeweiligen Bereich der zu bearbeiten gilt neue Panels ein
	JPanel eastPanel = new JPanel();
	JPanel northPanel = new JPanel();
	JPanel southPanel = new JPanel();
	JPanel centerPanel = new JPanel();
	
	JButton bWEST = new JButton("WEST");
	JButton bEAST = new JButton("EAST");
	JButton bNORTH = new JButton("NORTH");
	JButton bSOUTH = new JButton("SOUTH");
	JButton bCENTER = new JButton("CENTER");
	
	westPanel.add(bWEST);
	westPanel.add(new JLabel("Hier kommt was hin"));
	westPanel.setLayout(new GridLayout(5,1));	//Erzeuge testhaft ein neues Gridlayout, dass seine Komponenten in 1 Spalte und 5 Zeilen unterteilt
	
	eastPanel.add(bEAST);	//Fuege testweise einen Button in das jeweilige Panel
	eastPanel.setLayout(new GridLayout(1,1)); //Gebe dem jeweiligen Panel ein dafuer sinnvolles Layout (je nachdem ,wie wir das realisieren)
	
	northPanel.add(bNORTH);
	northPanel.setLayout(new GridLayout(1,1));
	
	southPanel.add(bSOUTH);
	southPanel.setLayout(new GridLayout(1,1));
	
	centerPanel.add(bCENTER);
	centerPanel.setLayout(new GridLayout(1,1));
	
	
	this.mainJpanel.add(westPanel, BorderLayout.WEST); //Fuege alle Panels ihres Zustaendigkeitsbereichs zu
	this.mainJpanel.add(eastPanel, BorderLayout.EAST); 
	this.mainJpanel.add(northPanel, BorderLayout.NORTH); 
	this.mainJpanel.add(southPanel, BorderLayout.SOUTH); 
	this.mainJpanel.add(centerPanel, BorderLayout.CENTER); 
	

}

public void actionPerformed(ActionEvent e){
	
	if(e.getSource()==menuItemStart){
		 JOptionPane.showMessageDialog(null, "Start new game!", "Start Game", JOptionPane.ERROR_MESSAGE);
	}
	
}
	
}
