package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Interfaces.iDatenzugriff;
import SpeichernLaden.DatenzugriffCSV;
import SpeichernLaden.DatenzugriffSerialisiert;

public class Menu extends JFrame implements ActionListener{
	private JLabel mainLabel; //Hauptarbeitsflache ist nun ein Label, auf Grund des Backgroundicons
	private JButton laden;
	private JButton neues;
	private JButton ende;
	private JButton start;
	
	private JTextField nameA;
	private JTextField nameB;
	private JCheckBox aIstKi;
	private JCheckBox bIstKi;
	
	private JFrame neuesSpiel;
	
	private JFileChooser jfc; 
	
	private iDatenzugriff d;
	private DatenzugriffSerialisiert d1;
	
	private  BufferedImage image;
	
	private GUI g;

	public Menu(String titel) {
		//-JFrame erstellen
		super(titel);
		this.setSize(600,500);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		//---------------------------------
		
		mainLabel = new JLabel();
		mainLabel.setIcon(new ImageIcon ("TITLEBACKGROUND.png"));
		mainLabel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
	
	
		
		neues = new JButton(new ImageIcon("neuesSpiel.png")); // NEUES SPIEL
		neues.setPreferredSize(new Dimension(180, 50));
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(0,0,30,0);
		neues.addActionListener(this);
		mainLabel.add(neues, c);
		
		laden = new JButton(new ImageIcon("spielLaden.png")); //SPIEL LADEN
		laden.setPreferredSize(new Dimension(180, 50));
		c.gridy = 2;
		laden.addActionListener(this);
		mainLabel.add(laden, c);

		ende = new JButton(new ImageIcon("spielEnde.png")); // ENDE
		ende.setPreferredSize(new Dimension(180, 50));
		c.gridy = 3;
		ende.addActionListener(this);
		mainLabel.add(ende, c);
		
	
		
		this.add(mainLabel, BorderLayout.CENTER);
		this.setVisible(true);
		
		
	}
	
	class ImagePanel extends JComponent {
	    private Image image;
	    public ImagePanel(Image image) {
	        this.image = image;
	    }
	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(image, 0, 0, this);
	    }
	}
	
	public void neuesSpielStarten(){
		g = new GUI(nameA.getText(), aIstKi.isSelected(), nameB.getText(), bIstKi.isSelected());
		neuesSpiel.dispose();
		
		
	}
	public void geladenesSpielStarten(){
		d1 = new DatenzugriffSerialisiert();
		d = d1;
		//System.out.println(jfc.getSelectedFile().getAbsolutePath());
		g = new GUI(d.load(jfc.getSelectedFile().getPath()));
		this.dispose();
		
		
	}
	public void oeffneFileChooser(){
		jfc = new JFileChooser("savegame");
		jfc.showOpenDialog(null);
	}
	public void neuesSpielMenu(){
		
		//Neuen JFrame und JPanel für neues Menü erstellen
		neuesSpiel = new JFrame("Neues Spiel");
		neuesSpiel.setSize(super.getWidth(), super.getHeight());
		neuesSpiel.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		neuesSpiel.setLocationRelativeTo(null);
		
		JLabel secLabel = new JLabel(); // ist nun JLabel wegen hintergrundicon
		secLabel.setIcon(new ImageIcon("TITLEBACKGROUND.png"));
		secLabel.setLayout(new GridBagLayout());
		
		
		GridBagConstraints c = new GridBagConstraints();
		//--------------------------------------------------
		
		//Textfelder, Checkboxen und Button erstellen und positonieren
		nameA = new JTextField("Name SpielerA");
		nameA.setPreferredSize(new Dimension(180, 20));
		c.insets = new Insets(0,0,30,0);
		c.gridx = 1;
		c.gridy = 0;
		secLabel.add(nameA,c);
		aIstKi = new JCheckBox("Ist KI?");
		c.gridy = 1;
		secLabel.add(aIstKi,c);
		nameB = new JTextField("Name SpielerB");
		nameB.setPreferredSize(new Dimension(180, 20));
		c.gridy = 2;
		secLabel.add(nameB,c);
		bIstKi = new JCheckBox("Ist KI?");
		c.gridy = 3;
		secLabel.add(bIstKi,c);
		start = new JButton("Starte Spiel");
		c.gridy = 4;
		start.addActionListener(this);
		secLabel.add(start,c);
		//-------------------------------------------------
		
		//JPanel auf neuen JFrame hinzufügen und Sichtbar machen
		neuesSpiel.add(secLabel, BorderLayout.CENTER);
		neuesSpiel.setVisible(true);
		this.dispose(); //schließt den alten JFrame (Startmenü)
		//------------------------------------------------------
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == neues){
			neuesSpielMenu();
		}
		if(e.getSource() == laden){
			oeffneFileChooser();
			geladenesSpielStarten();
		}
		if(e.getSource() == ende){
			int yn = JOptionPane.showConfirmDialog(null, "Wollen Sie das Spiel beenden?", "Sicher?", JOptionPane.YES_NO_OPTION);
			if(yn == 0){
			this.dispose(); //Zuerst alle referenzen etc. l�schen, dann soft close 
			}else return;
		}
		if(e.getSource() == start){
			this.neuesSpielStarten();
		}
	}
	
}
