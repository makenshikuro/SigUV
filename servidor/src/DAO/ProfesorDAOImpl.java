package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import entity.Profesor;

public class ProfesorDAOImpl extends DAOImpl<String, Profesor> implements ProfesorDAO {

	private EntityManager em;
	public ProfesorDAOImpl(EntityManager em) {
		super(em, Profesor.class);
		this.em = em;
	}
	
	@Override
	public Profesor getProfesorById(String id) {
		return this.getById(id);
	}

	@Override
	public List<Profesor> getProfesores() {
		return this.findAll();
	}

	@Override
	public void createProfesor(Profesor p) {
		this.create(p);
	}

	@Override
	public String getCount() {
		TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Profesor c", Long.class);
		return query.getSingleResult().toString();
	}

	@Override
	public void deleteProfesor(Profesor p) {
		this.delete(p);
	}
	@Override
	public List<String> getAllProfesorNames(){
		TypedQuery<String> query = em.createQuery(
		        "SELECT c.idprofesor FROM Profesor c", String.class);
				return query.getResultList();
	}

}
