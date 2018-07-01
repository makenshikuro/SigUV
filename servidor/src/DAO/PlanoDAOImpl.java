package DAO;

import java.util.List;

import javax.persistence.EntityManager;

import entity.Plano;

public class PlanoDAOImpl extends DAOImpl<Integer, Plano> implements PlanoDAO {

	private EntityManager em;

	public PlanoDAOImpl(EntityManager em) {
		super(em, Plano.class);
		this.em = em;
	}
	
	@Override
	public Plano getPlanoById(int id) {
		return this.getById(id);
	}

	@Override
	public List<Plano> getPlano() {
		return this.findAll();
	}

	@Override
	public void createPlano(Plano p) {
		this.create(p);
	}

	@Override
	public void deletePlano(Plano p) {
		this.delete(p);
	}

}
