package GUI;

import javax.swing.JButton;

import Basisklassen.Spielfeld;

public class SpielfeldMapped extends JButton{
	
	private String ID;
	
	String feldID;

	
	public SpielfeldMapped(String ID){
		
	this.setID(ID);
		
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

}
