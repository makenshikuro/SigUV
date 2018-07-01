package DAO;

import java.util.List;

import entity.Profesor;

public interface ProfesorDAO {
		public Profesor getProfesorById(String id);
	    public List<Profesor> getProfesores();
	    public void createProfesor(Profesor p);
	    public void deleteProfesor(Profesor p);

	public String getCount();
	    public List<String> getAllProfesorNames();
}
