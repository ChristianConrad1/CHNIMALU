package GUI;

import javax.swing.JButton;

public class SpielfeldMapped extends JButton{
	
	private String ID;
	
	String feldID;
	
	private EventHandler eh;
	
	
	
	public SpielfeldMapped(String ID){
		
		
	this.setID(ID);
	
	this.setOpaque(false);
	this.setContentAreaFilled(false);
	this.setBorderPainted(false); //Falls rahmen angezeigt werden sollen, hier anschalten!
	
	eh = new EventHandler(this);
	this.addActionListener(eh);	
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

}
