package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import entity.Espacio;

public class EspacioDAOImpl extends DAOImpl<String, Espacio> implements EspacioDAO {

	private EntityManager em;

	public EspacioDAOImpl(EntityManager em) {
		super(em, Espacio.class);
		this.em = em;
	}

	@Override
	public Espacio getEspacioById(String id) {
		return this.getById(id);
	}

	@Override
	public List<Espacio> getEspacios() {
		return this.findAll();
	}

	@Override
	public void createEspacio(Espacio p) {
		this.create(p);
	}

	@Override
	public void deleteEspacio(Espacio p) {
		this.delete(p);
	}

	@Override
	public List<Espacio> getEspaciosByEdificio(String id) {

		TypedQuery<Espacio> query = em.createQuery("SELECT c FROM Espacio c WHERE c.edificio.idedificio = :name",
				Espacio.class);
		return query.setParameter("name", id).getResultList();

	}

	@Override
	public List<String> getAllEspaciosNames() {

		TypedQuery<String> query = em.createQuery("SELECT c.idespacio FROM Espacio c", String.class);
		return query.getResultList();

	}

	@Override
	public int getCount() {
		TypedQuery<Integer> query = em.createQuery("SELECT COUNT(c) FROM Espacio c", Integer.class);
		return query.getSingleResult();
	}

	@Override
	public Espacio getEspacioByLatLng(float lat, float lng, int piso) {
		
		System.out.println("POJO Lat: " + lat);
		System.out.println("POJO LNG: " + lng);


		Query query = em.createNativeQuery(
				"SELECT * FROM public.espacios WHERE ST_Contains(ST_GeomFromText(CONCAT('POLYGON((',boundingbox,'))')),ST_GeomFromText('POINT("
						+ lat + " " + lng + ")')) and visibilidad = true and piso = :piso",
				Espacio.class)
				.setParameter("piso", piso);

		List<Espacio> e = query.getResultList();


		System.out.println("POJO: Finish size" + e.size());
		System.out.println("POJO: Finish id" + e.get(0).getIdespacio());




		return e.get(0);

	}

}
