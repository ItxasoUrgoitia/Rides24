package domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Erreklamazioa implements Serializable{
	//@Id 
	//@GeneratedValue
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@XmlID
	@Id 
	//@XmlJavaTypeAdapter(IntegerAdapter.class)
	//@GeneratedValue
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne//(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	//@XmlIDREF
	private User errekJarri;
	
	@Override
	public String toString() {
		return "Erreklamazioa [id=" + id + ", errekJarri=" + errekJarri + ", errekJaso=" + errekJaso + ", eskaera="
				+ eskaera + ", deskribapena=" + deskribapena + ", mota=" + mota + ", diru=" + diru + ", larri=" + larri
				+ "]";
	}
	@ManyToOne//(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@XmlIDREF
	private User errekJaso;
	
	//@ManyToOne//(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	//@XmlIDREF
	@OneToOne
	private Eskaera eskaera;
	private String deskribapena;
	private ErrekMota mota;
	private float diru;
	public enum ErrekMota{
		PENDING,
		REJECTED,
		ACCEPTED,
		ADMIN
	}
	private ErrekLarri larri;
	public enum ErrekLarri{
		TXIKIA,
		ERTAINA,
		HANDIA,
	}
	
	public Erreklamazioa(User errekJarri, User errekJaso, Eskaera eskaera, String deskribapena,
			float diru, ErrekLarri larri) { //Erreklamazioa(User, Driver, Eskaera, String, float)
		super();
		this.errekJarri = errekJarri;
		this.errekJaso = errekJaso;
		this.eskaera = eskaera;
		this.deskribapena = deskribapena;
		this.mota = ErrekMota.PENDING;
		this.diru = diru;
		this.larri=larri;
	}
	public Erreklamazioa() {
		
	}
	public Erreklamazioa(User errekJarri, User errekJaso, Eskaera eskaera, String deskribapena, float diru) {
		super();
		this.errekJarri = errekJarri;
		this.errekJaso = errekJaso;
		this.eskaera = eskaera;
		this.deskribapena = deskribapena;
		this.mota = ErrekMota.PENDING;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getErrekJarri() {
		return errekJarri;
	}
	public void setErrekJarri(User errekJarri) {
		this.errekJarri = errekJarri;
	}
	public User getErrekJaso() {
		return errekJaso;
	}
	public void setErrekJaso(User errekJaso) {
		this.errekJaso = errekJaso;
	}
	public Eskaera getEskaera() {
		return eskaera;
	}
	public void setEskaera(Eskaera eskaera) {
		this.eskaera = eskaera;
	}
	public String getDeskribapena() {
		return deskribapena;
	}
	public void setDeskribapena(String deskribapena) {
		this.deskribapena = deskribapena;
	}
	public ErrekMota getMota() {
		return mota;
	}
	public ErrekLarri getLarri() {
		return larri;
	}
	public void setLarri(ErrekLarri larri) {
		this.larri = larri;
	}
	public void setMota(ErrekMota mota) {
		this.mota = mota;
	}
	public float getDiru() {
		return diru;
	}
	public void setDiru(float diru) {
		this.diru = diru;
	}
	

}
