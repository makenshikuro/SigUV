package DAO;

import java.util.List;

public interface DAO<K,T> {
	   public T getById(K id );
	   public List<T> findAll();
	   public void create(T entity);
	   public void update(T entity);
	   public void delete(T entity );
	   public void deleteById(K entityId );
	   public List<T> findByCriteria(String criteria);
}
