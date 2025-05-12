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
public class Alerta implements Serializable{
	
	public enum AlertMota{
		DIRUA_SARTU,
		DIRUA_ATERA,
		ESKAERA_KANTZELATU, //Bidaiari - Gidari
		BIDAIA_KANTZELATU, //Bidaiari
		ESKAERA_ONARTU, //Bidaiari
		ESKAERA_EGIN, //Gidari
		ESKAERA_EZONARTUA, //Bidaiari
		BIDAIA_AMAITUTA, //Bidaiari
		BALORATUTA,
		ERREKLAMATUTA,
		ERREKLAMAZIOA_ONARTUTA,
		ERREKLAMAZIOA_DEUSESTATUTA,
		ADMINAK_ERREKLAMAZIOA_DEUSESTATU,
		ADMINAK_ERREKLAMAZIOA_ONARTU
	}
	
	@Id @XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue
	private Integer ID;	
	private AlertMota mota; 
	
	@XmlIDREF
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private User user;
	
	public Alerta(User user, AlertMota mota ) {	
		this.user=user;
		this.mota=mota;
	}
	public Alerta() {	
		
	}
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}


	public AlertMota getMota() {
		return mota;
	}

	public void setMota(AlertMota mota) {
		this.mota = mota;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Alerta [ID=" + ID + ", mota=" + mota + "]";
	}


}
