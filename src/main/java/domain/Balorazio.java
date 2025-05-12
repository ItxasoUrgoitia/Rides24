package domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Balorazio implements Serializable {
	@Id @XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue
	private Integer ID;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@XmlIDREF
	private User userJarri;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@XmlIDREF
	private User userJaso;
	
	private String deskribapena;
	private int nota;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@XmlIDREF
	private Eskaera eskaera;
	
	public Balorazio(User userJarri, User userJaso, String deskribapena, int nota, Eskaera eskaera) {
		super();
		this.userJarri = userJarri;
		this.userJaso = userJaso;
		this.deskribapena = deskribapena;
		this.nota = nota;
		this.eskaera = eskaera;
	}
	public Balorazio() {}
	public Eskaera getEskaera() {
		return eskaera;
	}
	public void setEskaera(Eskaera eskaera) {
		this.eskaera = eskaera;
	}
	@Override
	public String toString() {
		return "Balorazio [userJarri=" + userJarri + ", userJaso=" + userJaso + ", deskribapena=" + deskribapena
				+ ", nota=" + nota + "]";
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public User getUserJarri() {
		return userJarri;
	}
	public void setUserJarri(User userJarri) {
		this.userJarri = userJarri;
	}
	public User getUserJaso() {
		return userJaso;
	}
	public void setUserJaso(User userJaso) {
		this.userJaso = userJaso;
	}
	public String getDeskribapena() {
		return deskribapena;
	}
	public void setDeskribapena(String deskribapena) {
		this.deskribapena = deskribapena;
	}
	public int getNota() {
		return nota;
	}
	public void setNota(int nota) {
		this.nota = nota;
	}
	
	
}
