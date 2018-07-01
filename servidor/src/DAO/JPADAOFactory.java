package DAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPADAOFactory extends DAOFactory {

	private EntityManager getEntityManager(){
		EntityManagerFactory emf =
				Persistence.createEntityManagerFactory("siguv");
		return emf.createEntityManager();
	}

	@Override
	public CoordenadaDAO getCoordenadaDAO() {
		return new CoordenadaDAOImpl(getEntityManager());
	}

	@Override
	public EdificioDAO getEdificioDAO() {
		return new EdificioDAOImpl(getEntityManager());
	}

	@Override
	public EspacioDAO getEspacioDAO() {
		return new EspacioDAOImpl(getEntityManager());
	}

	@Override
	public PlanoDAO getPlanoDAO() {
		return new PlanoDAOImpl(getEntityManager());
	}

	@Override
	public PanoramaDAO getPanoramaDAO() {
		return new PanoramaDAOImpl(getEntityManager());
	}

	@Override
	public ProfesorDAO getProfesorDAO() {
		return new ProfesorDAOImpl(getEntityManager());
	}

	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new UsuarioDAOImpl(getEntityManager());
	}
	

}
