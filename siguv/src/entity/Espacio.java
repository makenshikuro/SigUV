package entity;

import java.io.Serializable;
import java.sql.SQLException;

import javax.persistence.*;

import org.postgis.Polygon;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;


/**
 * The persistent class for the espacios database table.
 * 
 */

public class Espacio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String idespacio;

	private String bloque;

	private String boundingbox;

	private String nombreen;

	private String nombrees;

	private String nombrevl;

	private Integer piso;

	private String tipo;

	private Boolean visibilidad;

	
	private Coordenada coordenada;


	private Edificio edificio;

	
	private List<Panorama> panoramas;


	private List<Profesor> profesores;

	public Espacio() {
		visibilidad = true;
	}


	public String getIdespacio() {
		return this.idespacio;
	}

	public void setIdespacio(String idespacio) {
		this.idespacio = idespacio;
	}

	public String getBloque() {
		return this.bloque;
	}

	public void setBloque(String bloque) {
		this.bloque = bloque;
	}

	public void setBoundingbox(String boundingbox) {
		this.boundingbox = boundingbox;
	}
	
	
	public Polygon getPolygonBoundingbox() throws SQLException {
		Polygon poly = new Polygon(boundingbox);
		
		return poly;
	}

	public void setPolygonBoundingbox(Polygon boundingbox) {
		String geom = boundingbox.toString();
		this.boundingbox = geom;
	}

	public String getBoundingbox() {
		return boundingbox;
	}

	public String getNombreen() {
		return this.nombreen;
	}

	public void setNombreen(String nombreen) {
		this.nombreen = nombreen;
	}

	public String getNombrees() {
		return this.nombrees;
	}

	public void setNombrees(String nombrees) {
		this.nombrees = nombrees;
	}

	public String getNombrevl() {
		return this.nombrevl;
	}

	public void setNombrevl(String nombrevl) {
		this.nombrevl = nombrevl;
	}

	public Integer getPiso() {
		return this.piso;
	}

	public void setPiso(Integer piso) {
		this.piso = piso;
	}

	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getVisibilidad() {
		return this.visibilidad;
	}

	public void setVisibilidad(Boolean visibilidad) {
		this.visibilidad = visibilidad;
	}

	public Coordenada getCoordenada() {
		return this.coordenada;
	}

	public void setCoordenada(Coordenada coordenada) {
		this.coordenada = coordenada;
	}

	public Edificio getEdificio() {
		return this.edificio;
	}

	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
	}

	public List<Panorama> getPanoramas() {
		return this.panoramas;
	}

	public void setPanoramas(List<Panorama> panoramas) {
		this.panoramas = panoramas;
	}

	public Panorama addPanorama(Panorama panorama) {
		getPanoramas().add(panorama);
		panorama.setEspacio(this);

		return panorama;
	}

	public Panorama removePanorama(Panorama panorama) {
		getPanoramas().remove(panorama);
		panorama.setEspacio(null);

		return panorama;
	}

	public List<Profesor> getProfesores() {
		return this.profesores;
	}

	public void setProfesores(List<Profesor> profesores) {
		this.profesores = profesores;
	}

}