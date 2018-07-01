package entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.List;


/**
 * The persistent class for the coordenadas database table.
 * 
 */
@Entity
@Table(name="coordenadas")
@NamedQuery(name="Coordenada.findAll", query="SELECT c FROM Coordenada c")
public class Coordenada implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String idcoordenada;

	private String descripcion;

	private double latitud;

	private double longitud;

	//bi-directional many-to-one association to Edificio
	@OneToMany(mappedBy="coordenada")
	@JsonBackReference
	private List<Edificio> edificios;

	//bi-directional many-to-one association to Espacio
	@OneToMany(mappedBy="coordenada")
	@JsonBackReference
	private List<Espacio> espacios;

	public Coordenada() {
	}

	public String getIdcoordenada() {
		return this.idcoordenada;
	}

	public void setIdcoordenada(String idcoordenada) {
		this.idcoordenada = idcoordenada;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getLatitud() {
		return this.latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return this.longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public List<Edificio> getEdificios() {
		return this.edificios;
	}

	public void setEdificios(List<Edificio> edificios) {
		this.edificios = edificios;
	}

	public Edificio addEdificio(Edificio edificio) {
		getEdificios().add(edificio);
		edificio.setCoordenada(this);

		return edificio;
	}

	public Edificio removeEdificio(Edificio edificio) {
		getEdificios().remove(edificio);
		edificio.setCoordenada(null);

		return edificio;
	}

	public List<Espacio> getEspacios() {
		return this.espacios;
	}

	public void setEspacios(List<Espacio> espacios) {
		this.espacios = espacios;
	}

	public Espacio addEspacio(Espacio espacio) {
		getEspacios().add(espacio);
		espacio.setCoordenada(this);

		return espacio;
	}

	public Espacio removeEspacio(Espacio espacio) {
		getEspacios().remove(espacio);
		espacio.setCoordenada(null);

		return espacio;
	}

}