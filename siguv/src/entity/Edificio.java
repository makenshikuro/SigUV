package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;


/**
 * The persistent class for the edificios database table.
 * 
 */

public class Edificio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String idedificio;

	private String bottomleft;

	private String direccion;

	private String enlace;

	private String nombreen;

	private String nombrees;

	private String nombrevl;

	private String telefono;

	private String topleft;

	private String topright;

	private String xano;
	
	private Coordenada coordenada;

	private List<Espacio> espacios;

	
	private List<Plano> planos;

	
	private List<Usuario> usuarios;

	public Edificio() {
	}

	public String getIdedificio() {
		return this.idedificio;
	}

	public void setIdedificio(String idedificio) {
		this.idedificio = idedificio;
	}

	public String getBottomleft() {
		return this.bottomleft;
	}

	public void setBottomleft(String bottomleft) {
		this.bottomleft = bottomleft;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEnlace() {
		return this.enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
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

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTopleft() {
		return this.topleft;
	}

	public void setTopleft(String topleft) {
		this.topleft = topleft;
	}

	public String getTopright() {
		return this.topright;
	}

	public void setTopright(String topright) {
		this.topright = topright;
	}

	public String getXano() {
		return this.xano;
	}

	public void setXano(String xano) {
		this.xano = xano;
	}

	public Coordenada getCoordenada() {
		return this.coordenada;
	}

	public void setCoordenada(Coordenada coordenada) {
		this.coordenada = coordenada;
	}

	public List<Espacio> getEspacios() {
		return this.espacios;
	}

	public void setEspacios(List<Espacio> espacios) {
		this.espacios = espacios;
	}

	public Espacio addEspacio(Espacio espacio) {
		getEspacios().add(espacio);
		espacio.setEdificio(this);

		return espacio;
	}

	public Espacio removeEspacio(Espacio espacio) {
		getEspacios().remove(espacio);
		espacio.setEdificio(null);

		return espacio;
	}

	public List<Plano> getPlanos() {
		return this.planos;
	}

	public void setPlanos(List<Plano> planos) {
		this.planos = planos;
	}

	public Plano addPlano(Plano plano) {
		getPlanos().add(plano);
		plano.setEdificio(this);

		return plano;
	}

	public Plano removePlano(Plano plano) {
		getPlanos().remove(plano);
		plano.setEdificio(null);

		return plano;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}