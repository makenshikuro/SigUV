package DAO;

import java.util.List;

import javax.persistence.EntityManager;

import entity.Coordenada;

public class CoordenadaDAOImpl extends DAOImpl<String, Coordenada> implements CoordenadaDAO{

	

	private EntityManager em;
	public CoordenadaDAOImpl(EntityManager em) {
		super(em, Coordenada.class);
		this.em = em;
	}
	
	@Override
	public Coordenada getCoordenadaById(String id) {
		return this.getById(id);
	}

	@Override
	public List<Coordenada> getCoordenadas() {
		return this.findAll();
	}

	@Override
	public void createCoordenada(Coordenada c) {
		this.create(c);
	}

	@Override
	public void deleteCoordenada(Coordenada c) {
		this.delete(c);
	}

}
