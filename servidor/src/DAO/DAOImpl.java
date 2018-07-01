package DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class DAOImpl<K,T> implements DAO<K,T>{
	
	private EntityManager em;
	private Class<T> entityClass;
	
	protected DAOImpl(EntityManager em, Class<T> entityClass) {
		this.em = em;
		this.entityClass = entityClass;
	}

	public List<T> findAll(){
		Query q = em.createQuery("from " + this.entityClass.getName());
		return q.getResultList();	
	}
	
	public T getById(K id){
		return em.find(entityClass, id);
	}

	public void create(T entity) {
		//em.getTransaction().begin();
		em.persist(entity);
		//em.getTransaction().commit();
	}

	public void update(T entity) {
		//this.em.getTransaction().begin();
		em.merge(entity);
		//em.getTransaction().commit();
	}

	public void delete(T entity) {
		//this.em.getTransaction().begin();
		em.remove(entity);
		//em.flush();
		//em.getTransaction().commit();
	}

	public void deleteById(K entityId) {
		//this.em.getTransaction().begin();
		T entity = em.find(entityClass, entityId);
		em.remove(entity);
		//em.getTransaction().commit();
	}

	@Override
	public List<T> findByCriteria(String criteria) {
		Query q = em.createQuery("from " + this.entityClass.getName() +" where "+criteria);
		
		return q.getResultList();	
	}
	
	

}
