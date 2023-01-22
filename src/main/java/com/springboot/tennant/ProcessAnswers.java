package com.springboot.tennant;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Answers;

@RestController
@CrossOrigin
public class ProcessAnswers implements ErrorController {

	private long gesamt = 0;
	private boolean koQ1 = false;
	private final double total = 6500;
	private static final String PATH = "/error";
	private List<String> anwersList;

	@Autowired
	private UserRepository repository;

	public double calculatePercentage(double obtained, double total) {
		return obtained * 100 / total;
	}

	@PostMapping("/mailFragebogen")
	public void sendMailLink(HttpServletResponse response, HttpServletRequest request) {
		String email = request.getParameter("email");

		try {

			sendMailLink(email);

			PrintWriter out = response.getWriter();

			out.print("Frageboge versandt");

			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	

	@Scheduled(cron = "0 1 1 * * ?", zone = "Europe/Paris")
	private void check24() throws AddressException, UnsupportedEncodingException, MessagingException {

		Instant now = Instant.now();

		List<Answers> allAnswers = repository.findAll();

		for (Answers answers : allAnswers) {

			Date then = answers.getAusgefülltAm();

			Instant aus = then.toInstant();

			Instant twentyFourHoursEarlier = now.minus(24, ChronoUnit.HOURS);

			Boolean within24Hours = (!aus.isBefore(twentyFourHoursEarlier)) && aus.isBefore(now);
			
			if(!within24Hours)
			{
				sendMail();
				
				
			}

		}

	

	}

	private Boolean sendMailLink(String email)
			throws AddressException, MessagingException, UnsupportedEncodingException {

		Properties prop = new Properties();
		prop.put("mail.smtp.user", "benjamin.koubik@gmx.de");
		prop.put("mail.smtp.password", "3072inneB@");
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "mail.gmx.net");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.ssl.trust", "*");
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

		Session session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("benjamin.koubik@gmx.de", "3072inneB@");
			}
		});

		Message message = new MimeMessage(session);
		message.setContent(message, "text/html");
		message.setFrom(new InternetAddress("benjamin.koubik@gmx.de", "UNENU"));
		message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(email));
		message.setSubject("Fragebogen");

		String msg = "Guten Tag, Herr/Frau...,<br>";
		msg += "Bitte klicken Sie auf folgenden Link und füllen Sie den Fragebogen verantwortungsbewusst aus.<br><br>";
		msg += "https://stately-torte-bef106.netlify.app <br>";
		msg += "Vielen Dank!";

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		message.setContent(multipart);

		Transport.send(message);

		return true;

	}

	@PostMapping("/process")
	public String sayHi(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {

		anwersList = new ArrayList<String>();

		String wert = "";

		String email = request.getParameter("email");

		// HAUPTFRAGEN
		String question1 = request.getParameter("question1");
		String question2 = request.getParameter("question2");
		String question3 = request.getParameter("question3");
		String question4 = request.getParameter("question4");
		String question5 = request.getParameter("question5");
		String question6 = request.getParameter("question6");
		String question7 = request.getParameter("question7");

		anwersList.add(question1);
		anwersList.add(question2);
		anwersList.add(question3);
		anwersList.add(question4);
		anwersList.add(question5);
		anwersList.add(question6);
		anwersList.add(question7);

		// ZUSATZFRAGEN

		String einkommenString = String.valueOf(request.getParameter("einkommen"));

		double einkommen;

		if (einkommenString.contains(".")) {
			String[] einkommenSplit = einkommenString.split("\\.");
			einkommen = Double.parseDouble(einkommenSplit[0]);
		} else if (einkommenString.contains(",")) {
			String[] einkommenSplit = einkommenString.split("\\,");
			einkommen = Double.parseDouble(einkommenSplit[0]);

		} else {
			einkommen = Double.parseDouble(einkommenString);
		}

		String question2Z = request.getParameter("question2Z");
		String question3Z = request.getParameter("question3Z");
		String question4Z = request.getParameter("question4Z");
		String question5Z = request.getParameter("question5Z");
		String question6Z = request.getParameter("question6Z");
		String question7Z = request.getParameter("question7Z");
		String question8Z = request.getParameter("question8Z");

		anwersList.add(question2Z);
		anwersList.add(question3Z);
		anwersList.add(question4Z);
		anwersList.add(question5Z);
		anwersList.add(question6Z);
		anwersList.add(question7Z);
		anwersList.add(question8Z);

		double percent = calculatePercentage(einkommen, total);

		if (percent > 1 && percent < 15) {
			koQ1 = true;
		}
		if (percent > 15 && percent < 33) {
			gesamt += 3;
		} else if (percent > 33 && percent < 40) {
			gesamt += 5;
		} else if (percent > 40) {
			gesamt += 10;
		}

		String convertpercent = String.valueOf(percent);

		String[] split = convertpercent.split("\\.");

		// AUSWERTUNG HAUPTFRAGEN
		if (question1.equals("ja") || question2.equals("ja") || question3.equals("ja") || question4.equals("nein")
				|| question5.equals("nein") || question6.equals("nein") || question7.equals("nein")) {
			koQ1 = true;
		}

		// AUSWERTUNG ZUSATZFRAGEN

		if (question2Z.equals("nein")) {
			gesamt += 10;
		}
		if (question3Z.equals("nein")) {
			gesamt += 10;
		}
		if (question3Z.equals("ja - 5")) {
			gesamt += 5;
		}
		if (question3Z.equals("ja")) {
			gesamt += 10;
		}
		if (question4Z.equals("ja - 10")) {
			gesamt += 10;
		}
		if (question4Z.equals("ja - 5")) {
			gesamt += 5;
		}
		if (question4Z.equals("ja - 3")) {
			gesamt += 3;
		}
		if (question5Z.equals("ja - 5")) {
			gesamt += 5;
		}
		if (question5Z.equals("nein")) {
			gesamt += 10;
		}
		if (question6Z.equals("ja - 30")) {
			gesamt += 10;
		}
		if (question6Z.equals("ja - 25")) {
			gesamt += 8;
		}
		if (question6Z.equals("ja - 20")) {
			gesamt += 8;
		}
		if (question6Z.equals("ja - 15")) {
			gesamt += 4;
		}
		if (question6Z.equals("ja - 10")) {
			gesamt += 4;
		}
		if (question7Z.equals("ja - 10")) {
			gesamt += 10;
		}
		if (question7Z.equals("ja - 5")) {
			gesamt += 5;
		}
		if (question8Z.equals("nein - 10")) {
			gesamt += 10;
		}

		boolean status = saveDataDB(email, question6Z, einkommen);
		

		if (status) {
			wert = "OK";
		} else {
			wert = "Fehler";
		}

		return wert;

	}

	private Boolean sendMail() throws AddressException, MessagingException, UnsupportedEncodingException {

		Properties prop = new Properties();
		prop.put("mail.smtp.user", "benjamin.koubik@gmx.de");
		prop.put("mail.smtp.password", "3072inneB@");
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "mail.gmx.net");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.ssl.trust", "*");
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

		Session session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("benjamin.koubik@gmx.de", "3072inneB@");
			}
		});

		Message message = new MimeMessage(session);
		message.setContent(message, "text/html");
		message.setFrom(new InternetAddress("benjamin.koubik@gmx.de", "UNENU"));
		message.addRecipients(Message.RecipientType.CC, InternetAddress.parse("benjamin.koubik@gmx.de"));
		message.setSubject("Antwort Fragebogen");

		String msg = "Guten Tag, Herr/Frau...,<br/>";
		msg += "Vielen Dank für Ihr Vertrauen.<br/>";
		msg +=	"Leider müssen wir Ihnen absagen, aufgrund...<br/>";
		msg +=	"Wir bedanken uns herzlich und wünschen Ihnen auf Ihrer Suche nach einem passendne Objekt viel Erfolg!<br/>";

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);

		message.setContent(multipart);

		Transport.send(message);

		return true;

	}

	private Boolean saveDataDB(String email, String mehr, Double einkommen) {
		int mehrBezahlenInProzent = 0;

		if (mehr.equals("ja - 10")) {
			mehrBezahlenInProzent = 10;
		} else if (mehr.equals("ja - 15")) {
			mehrBezahlenInProzent = 15;
		} else if (mehr.equals("ja - 20")) {
			mehrBezahlenInProzent = 20;
		} else if (mehr.equals("ja - 25")) {
			mehrBezahlenInProzent = 25;
		} else if (mehr.equals("ja - 30")) {
			mehrBezahlenInProzent = 30;
		}

		Answers an = repository.findByEmail(email);
		an.setPoints(gesamt);
		an.setAnswers(anwersList);
		an.setKo(koQ1);
		an.setMehrBezahlen(mehrBezahlenInProzent);
		an.setEinkommen(einkommen);
		an.setAusgefülltAm(new Date());

		repository.save(an);

		return true;
	}

	@RequestMapping(value = PATH)
	public String error() {
		return "Error handling";
	}

	public String getErrorPath() {
		return PATH;
	}

}
