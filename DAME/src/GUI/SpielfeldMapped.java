package GUI;

import javax.swing.JButton;

public class SpielfeldMapped extends JButton{
	
	private String ID;
	
	String feldID;
	
	
	
	public SpielfeldMapped(String ID){
		
		
	this.setID(ID);
	
	this.setOpaque(false);
	this.setContentAreaFilled(false);
	this.setBorderPainted(false); //Falls rahmen angezeigt werden sollen, hier anschalten!
		
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

}
