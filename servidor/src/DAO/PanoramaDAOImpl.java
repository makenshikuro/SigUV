package DAO;

import java.util.List;

import javax.persistence.EntityManager;

import entity.Panorama;

public class PanoramaDAOImpl extends DAOImpl<Integer, Panorama> implements PanoramaDAO {

	private EntityManager em;

	public PanoramaDAOImpl(EntityManager em) {
		super(em, Panorama.class);
		this.em = em;
	}
	
	@Override
	public Panorama getPanoramaById(int id) {
		return this.getById(id);
	}

	@Override
	public List<Panorama> getPanorama() {
		return this.findAll();
	}

	@Override
	public void createPanorama(Panorama p) {
		this.create(p);
		
	}

	@Override
	public void deletePanorama(Panorama p) {
		this.delete(p);
	}
	
}
