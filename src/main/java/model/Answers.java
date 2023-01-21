package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("useranswers")
public class Answers implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String _id;
	private String email;
	private long points;
	private boolean ko;
	private List<String> answers;
	private boolean erledigt;
	private int mehrBezahlen;
	private double einkommen;
	private Date ausgefülltAm;

	public Answers() {

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public boolean isErledigt() {
		return erledigt;
	}

	public void setErledigt(boolean erledigt) {
		this.erledigt = erledigt;
	}

	public boolean isKo() {
		return ko;
	}

	public void setKo(boolean ko) {
		this.ko = ko;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public int getMehrBezahlen() {
		return mehrBezahlen;
	}

	public void setMehrBezahlen(int mehrBezahlen) {
		this.mehrBezahlen = mehrBezahlen;
	}

	public double getEinkommen() {
		return einkommen;
	}

	public void setEinkommen(double einkommen) {
		this.einkommen = einkommen;
	}

	public Date getAusgefülltAm() {
		return ausgefülltAm;
	}

	public void setAusgefülltAm(Date ausgefülltAm) {
		this.ausgefülltAm = ausgefülltAm;
	}

	public Answers(String email, long points, boolean ko, List<String> answers, boolean erledigt, int mehrBezahlen,
			double einkommen, Date ausgefülltAm) {
		super();
		this.email = email;
		this.points = points;
		this.ko = ko;
		this.answers = answers;
		this.erledigt = erledigt;
		this.mehrBezahlen = mehrBezahlen;
		this.einkommen = einkommen;
		this.ausgefülltAm = ausgefülltAm;
	}



}
