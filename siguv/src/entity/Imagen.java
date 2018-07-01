package entity;

import java.io.InputStream;

import javax.servlet.http.Part;

public class Imagen {

	String nombre;
	String denominacion;
	String extension;
	int nivel;
	int size;
	String tipo;
	Part archivo;
	InputStream is;
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Part getArchivo() {
		return archivo;
	}
	public void setArchivo(Part archivo) {
		this.archivo = archivo;
	}
	public String getDenominacion() {
		return denominacion;
	}
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = "."+extension;
	}
	public void setInputStream(InputStream is) {
		this.is = is;
	}
	public InputStream getInputStream() {
		return is;
	}
	
	
}
