package EMail;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail extends Thread {
	
	
	
	private Properties p; 
	
	private class MailAuthenticator extends Authenticator{
		private String user,password;
		public MailAuthenticator(){
			this.user=""+p.get("mail.smtp.user");
			this.password=""+p.get("mail.smtp.password");
		}
		 @Override
		public PasswordAuthentication getPasswordAuthentication(){
			 //String to Chararray umgeändert(?)
			return new PasswordAuthentication(user, password);
		}
	}
	
	public Mail(String an, String betreff, String text, String anhangPfad1,
			String anhangName1, String anhangPfad2, String anhangName2){
		if((an==null) || (an.length()==0)) return; 
		p = new Properties();
		p.put("mail.smtp.host","smtp.gmail.com");  //HOST
		p.put("mail.smtp.user","chnimaluhsrt"); //USER
		
		p.put("mail.smtp.password","hsrtmkiinf2");//PASSWORD
		p.put("mail.smtp.socketFactory.port","465");
		p.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		p.put("mail.smtp.auth","true");
		p.put("mail.smtp.port","465");
		p.put("von","chnimaluhsrt@gmail.com"); //SENDEREMAIL
		p.put("an",an);
		p.put("betreff",betreff);
		p.put("text",text);
	
	
	if(anhangPfad1==null)
		p.put("anhangPfad1","");
	else
		p.put("anhangPfad1",anhangPfad1);
	if(anhangName1==null)
		p.put("anhangName1","");
	else
		p.put("anhangName1",anhangName1);
	if(anhangPfad2==null)
		p.put("anhangPfad2","");
	else
		p.put("anhangPfad2",anhangPfad2);
		
	
	if(anhangName2==null)
		p.put("anhangName2","");
	else
		p.put("anhangName2",anhangName2);
		this.start();
	}
	@Override
	public void run(){
		try{
			System.out.println("Start Mailing an"+p.getProperty("an"));
			MailAuthenticator auth=new MailAuthenticator();
			Session session = Session.getDefaultInstance(p, auth);
			Message msg=new MimeMessage(session);
			msg.setFrom(new InternetAddress(p.getProperty("von")));
			msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(p.getProperty("an"),false));
			msg.setSubject(p.getProperty("betreff"));
					//1. Teil ist die Nachricht
					MimeBodyPart bodyNachricht = new MimeBodyPart();
					bodyNachricht.setText(p.getProperty("text"));
					Multipart body = new MimeMultipart(); //Multipart is a container that holds multiple body parts. Multipart provides methods to retrieve and set its subparts.
					body.addBodyPart(bodyNachricht);
					//2. Teil sind Anhänge
					for(int i=1; i<=2; i++){
						if((!p.getProperty("anhangPfad"+i).equals(""))&&(!p.getProperty("anhangName"+i).equals(""))){
							MimeBodyPart bodyAnhang = new MimeBodyPart();
							DataSource source = new FileDataSource(p.getProperty("anhangPfad"+i));
							bodyAnhang.setDataHandler(new DataHandler(source));
							bodyAnhang.setFileName(p.getProperty("anhangName"+i));
							body.addBodyPart(bodyAnhang);
						}
					}
					msg.setContent(body);
					msg.setSentDate(new Date());
					Transport.send(msg);
					System.out.println("Mailing an "+p.getProperty("an")+" erfolgreich beendet.");
		}
		catch (Exception e){
			System.out.println("Mailing an "+p.getProperty("an")+" FEHLGESCHLAGEN:");
			e.printStackTrace();
		}
	}

}
