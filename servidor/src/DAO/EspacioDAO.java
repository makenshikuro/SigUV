package DAO;

import java.util.List;

import entity.Espacio;

public interface EspacioDAO {
	public Espacio getEspacioById(String id);

	public Espacio getEspacioByLatLng(float lat, float lng, int piso);

	public List<Espacio> getEspacios();

	public List<Espacio> getEspaciosByEdificio(String id);

	public List<String> getAllEspaciosNames();

	public int getCount();

	public void createEspacio(Espacio p);

	public void deleteEspacio(Espacio p);

}
