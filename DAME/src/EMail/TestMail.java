package EMail;

public class TestMail {
	
	public static void main(String[]args){
		Mail m = new Mail("ZeebNiki@Googlemail.com", "test JAVAEMAIL", "INHALT", "C:/test.png", "test.png", null, null);
	System.out.println("Sendet Email"); //Wird direkt ausgef�hrt - EMAIL wird ja �ber externen thread geschickt/ausgef�hrt
	}

}
