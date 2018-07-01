package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import entity.Edificio;

public class EdificioDAOImpl extends DAOImpl<String, Edificio> implements EdificioDAO {

	private EntityManager em;
	public EdificioDAOImpl(EntityManager em) {
		super(em, Edificio.class);
		this.em = em;
	}

	@Override
	public Edificio getEdificioById(String id) {
		return this.getById(id);
	}

	@Override
	public List<Edificio> getEdificios() {
		return this.findAll();
	}

	@Override
	public void createEdificio(Edificio p) {
		this.create(p);
		
	}

	@Override
	public String getCount() {
		TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Edificio c", Long.class);
		return query.getSingleResult().toString();
	}

	@Override
	public void deleteEdificio(Edificio p) {
		this.delete(p);
	}
	@Override
    public List<String> getAllEdificiosNames() {
		
		TypedQuery<String> query = em.createQuery(
        "SELECT c.idedificio FROM Edificio c", String.class);
		return query.getResultList();
		
    }
	@Override
    public List<Edificio> getEdificiosEditor(String user) {
//		System.out.println("DAO IMPl " + user);
		/*List<String> name = Arrays.asList(user);
		TypedQuery<Edificio> query = em.createQuery(
        "SELECT c FROM Edificio c WHERE IN(c.usuarios) ?1 ", Edificio.class)
		.setParameter(1, name);*/
	
		
		Query query = em.createNativeQuery("SELECT e.* FROM edificios e JOIN edificios_usuarios eu ON e.idedificio = eu.idedificio WHERE eu.username = :user", Edificio.class)
		.setParameter("user", user);
		
			
		return query.getResultList();
		
    }

	

}
