import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.*;

public class GUI extends JFrame{
	
	public JPanel jpanel;

	
public GUI(){

	this.setVisible(true);
	this.setTitle("Dame V1.0");
	this.setSize(800, 800);
	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	jpanel = new JPanel();
	this.getContentPane().add(jpanel);
	
}
	
}
