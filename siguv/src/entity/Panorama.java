package entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the panoramas database table.
 * 
 */

public class Panorama implements Serializable {
	private static final long serialVersionUID = 1L;

	private String idpanorama;

	private String enlace;

	private Integer panorama;

	//bi-directional many-to-one association to Espacio
	
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