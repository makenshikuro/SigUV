package utils;

import javax.servlet.http.Part;

public class Imagen {

	String nombre;
	int size;
	String tipo;
	Part archivo;
	
	
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
	
	
}
