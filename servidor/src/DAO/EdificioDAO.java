package DAO;

import java.util.List;

import entity.Edificio;
import entity.Usuario;

public interface EdificioDAO {
	public Edificio getEdificioById(String id);
    public List<Edificio> getEdificios();
    public List<Edificio> getEdificiosEditor(String id);
    public List<String> getAllEdificiosNames();

	public String getCount();
    public void createEdificio(Edificio p);
    public void deleteEdificio (Edificio p);
}
