package entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the profesores database table.
 * 
 */

public class Profesor implements Serializable {
	private static final long serialVersionUID = 1L;

	private String idprofesor;

	private String usuario;

	private Boolean visibilidad;

	//bi-directional many-to-many association to Espacio
	
	private List<Espacio> espacios;

	public Profesor() {
		visibilidad = true;
	}

	public String getIdprofesor() {
		return this.idprofesor;
	}

	public void setIdprofesor(String idprofesor) {
		this.idprofesor = idprofesor;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Boolean getVisibilidad() {
		return this.visibilidad;
	}

	public void setVisibilidad(Boolean visibilidad) {
		this.visibilidad = visibilidad;
	}

	public List<Espacio> getEspacios() {
		return this.espacios;
	}

	public void setEspacios(List<Espacio> espacios) {
		this.espacios = espacios;
	}
	

}