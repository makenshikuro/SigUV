package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import entity.Usuario;

public class UsuarioDAOImpl extends DAOImpl<String, Usuario> implements UsuarioDAO{

	private EntityManager em;
	public UsuarioDAOImpl(EntityManager em) {
		super(em, Usuario.class);
		this.em = em;
	}
	
	@Override
	public Usuario getUsuarioById(String id) {return null;}

	@Override
	public List<Usuario> getUsuario() {return null;}

	@Override
	public void createUsuario(Usuario p) {}

	@Override
	public void deleteUsuario(Usuario p) {}

	@Override
	public String getCount() {
		TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Usuario c", Long.class);
		return query.getSingleResult().toString();
	}

	@Override
	public List<Usuario> getUsuarios() {
		String dynQuery = "select u from Usuario u ";
		Query query = em.createQuery(dynQuery);
		
		
		List<Usuario> u = query.getResultList();
		System.out.println(u.get(0).getNombre()+" "+ u.get(0).getRole());
		return u;
	}
	@Override
	public List<String> getAllUsuariosNames(){
		TypedQuery<String> query = em.createQuery(
		        "SELECT c.usuario FROM Usuario c", String.class);
				return query.getResultList();
		
	}

	@Override
	public Usuario getUsuarioByEmail(String id) {
		String dynQuery = "select u from Usuario u where u.email = :email ";
		Query query = em.createQuery(dynQuery).setParameter("email", id);
		System.out.println("Query " + query.getResultList().toString());
		List<Usuario> u = query.getResultList();

		return u.get(0);
	}

	@Override
	public Usuario getByUUID(String uuid) {
		String dynQuery = "select u from Usuario u where u.uuid = :uuid ";
		Query query = em.createQuery(dynQuery).setParameter("uuid", uuid);
		System.out.println("Query " + query.getResultList().toString());
		List<Usuario> u = query.getResultList();

		return u.get(0);
	}

}
