package entity;
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/**
 * The persistent class for the panoramas database table.
 * 
 */
@Entity
@Table(name="panoramas")
@NamedQuery(name="Panorama.findAll", query="SELECT p FROM Panorama p")
public class Panorama implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String idpanorama;

	private String enlace;

	private Integer panorama;

	//bi-directional many-to-one association to Espacio
	@ManyToOne(fetch=FetchType.LAZY,cascade= CascadeType.REMOVE)
	@JoinColumn(name="idespacio")
	private Espacio espacio;

	public Panorama() {
	}

	public String getIdpanorama() {
		return this.idpanorama;
	}

	public void setIdpanorama(String idpanorama) {
		this.idpanorama = idpanorama;
	}

	public String getEnlace() {
		return this.enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	public Integer getPanorama() {
		return this.panorama;
	}

	public void setPanorama(Integer panorama) {
		this.panorama = panorama;
	}

	public Espacio getEspacio() {
		return this.espacio;
	}

	public void setEspacio(Espacio espacio) {
		this.espacio = espacio;
	}

}