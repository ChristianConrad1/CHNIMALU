package EMail;

public class TestMail {
	
	public static void main(String[]args){
		Mail m = new Mail("ZeebNiki@Googlemail.com", "test JAVAEMAIL", "INHALT", "savegame/Dame.pdf", "Dame.pdf", null, null);
	System.out.println("Sendet Email"); //Wird direkt ausgeführt - EMAIL wird ja über externen thread geschickt/ausgeführt
	}

}
