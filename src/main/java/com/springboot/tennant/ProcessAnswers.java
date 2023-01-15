package com.springboot.tennant;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RestController
@CrossOrigin
public class ProcessAnswers implements ErrorController{
	
	private long gesamt = 0;
	private boolean koQ1 = false;
	private final double total = 6500;
    private static final String PATH = "/error";

//	@Autowired
//    private JavaMailSender javaMailSender;
//	
    
    private void sendMail() throws AddressException, MessagingException, UnsupportedEncodingException {
    	
    	
    	Properties prop = new Properties();
    	prop.put("mail.smtp.user", "benjamin.koubik@gmx.de");
    	prop.put("mail.smtp.password", "3072inneB@");
    	prop.put("mail.smtp.auth", true);
    	prop.put("mail.smtp.starttls.enable", "true");
    	prop.put("mail.smtp.host", "mail.gmx.net");
    	prop.put("mail.smtp.port", "587");
    	prop.put("mail.smtp.ssl.trust", "*");
    	prop.put("mail.smtp.ssl.protocols","TLSv1.2");

    	
    	Session session = Session.getInstance(prop, new Authenticator() {
    	    @Override
    	    protected PasswordAuthentication getPasswordAuthentication() {
    	        return new PasswordAuthentication("benjamin.koubik@gmx.de", "3072inneB@");
    	    }
    	});
    	
    	Message message = new MimeMessage(session);
    	message.setFrom(new InternetAddress("benjamin.koubik@gmx.de","UNENU"));
    	message.setRecipients(
    	  Message.RecipientType.TO, InternetAddress.parse("benjamin.koubik@gmx.de"));
    	message.setSubject("Mail Subject");

    	String msg = "This is my first email using JavaMailer";

    	MimeBodyPart mimeBodyPart = new MimeBodyPart();
    	mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

    	Multipart multipart = new MimeMultipart();
    	multipart.addBodyPart(mimeBodyPart);

    	message.setContent(multipart);

    	Transport.send(message);
    	
//		SimpleMailMessage msg = new SimpleMailMessage();
//		msg.setTo("to_1@gmail.com", "to_2@gmail.com", "to_3@yahoo.com");
//
//		msg.setSubject("Testing from Spring Boot");
//		msg.setText("Hello World \n Spring Boot Email");
//
//		javaMailSender.send(msg);
    	
    }
    
    public double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }
    
    @GetMapping("/")
    public String sayHi() {
    	return "HELLO";
    }
    
	@PostMapping("/process")
	public String sayHi(HttpServletResponse response,HttpServletRequest request) throws UnsupportedEncodingException {
		
		
		String email = request.getParameter("email");

		
		//HAUPTFRAGEN
		String question1 = request.getParameter("question1");
		String question2 = request.getParameter("question2");
		String question3 = request.getParameter("question3");
		String question4 = request.getParameter("question4");
		String question5 = request.getParameter("question5");
		String question6 = request.getParameter("question6");
		String question7 = request.getParameter("question7");
		
		
		//ZUSATZFRAGEN
		double einkommen = Double.parseDouble(request.getParameter("einkommen"));
		
		String question2Z = request.getParameter("question2Z");
		String question3Z = request.getParameter("question3Z");
		String question4Z = request.getParameter("question4Z");
		String question5Z = request.getParameter("question5Z");
		String question6Z = request.getParameter("question6Z");
		String question7Z = request.getParameter("question7Z");
		String question8Z = request.getParameter("question8Z");
		

		
		double percent = calculatePercentage(einkommen, total);
		
		
		String convertpercent = String.valueOf(percent);
		
		
		String[] split = convertpercent.split("\\.");
		
	

		System.out.println("IN PROZENT % " +split[0]+","+split[1].substring(0,2));
		
		//AUSWERTUNG HAUPTFRAGEN
		if(question1.equals("ja") || question2.equals("ja") || question3.equals("ja") || question4.equals("nein") || question5.equals("nein") || question6.equals("nein") || question7.equals("nein"))
		{
			koQ1 = true;
		}
		
		//AUSWERTUNG ZUSATZFRAGEN
		
		if(question2Z.equals("nein")) {
			gesamt += 10;
		}
		if(question3Z.equals("nein")) {
			gesamt += 10;
		}
		if(question3Z.equals("ja - 5")) {
			gesamt += 5;
		}
		if(question3Z.equals("ja")) {
			gesamt += 10;
		}
		if(question4Z.equals("ja - 10")) {
			gesamt += 10;
		}
		if(question4Z.equals("ja - 5")) {
			gesamt += 5;
		}
		if(question4Z.equals("ja - 3")) {
			gesamt += 3;
		}
		if(question5Z.equals("ja - 5")) {
			gesamt += 5;
		}
		if(question5Z.equals("nein")) {
			gesamt += 10;
		}
		if(question6Z.equals("ja - 30")) {
			gesamt += 10;
		}
		if(question6Z.equals("ja - 25")) {
			gesamt += 8;
		}
		if(question6Z.equals("ja - 20")) {
			gesamt += 8;
		}
		if(question6Z.equals("ja - 15")) {
			gesamt += 4;
		}
		if(question6Z.equals("ja - 10")) {
			gesamt += 4;
		}
		if(question7Z.equals("ja - 10")) {
			gesamt += 10;
		}
		if(question7Z.equals("ja - 5")) {
			gesamt += 5;
		}
		if(question8Z.equals("nein - 10")) {
			gesamt += 10;
		}
		
		
		try {
			sendMail();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(koQ1)
		{
	
			return "KO";
		}
		else
		{
			
			return "Gesamt: " + gesamt;
		}
		
		
	}
	
	 @RequestMapping(value = PATH)
	    public String error() {
	        return "Error handling";
	    }

	 public String getErrorPath() {
	        return PATH;
	    }
	 
}
