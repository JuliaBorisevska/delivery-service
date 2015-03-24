package dao;

import javax.persistence.EntityManager;



public abstract class AbstractDAO <T> {
	protected EntityManager em;
	
	public AbstractDAO(EntityManager em) {
        this.em = em;
    }
	
	public abstract void delete(T entity);
	public abstract void create(T entity);
	public abstract void update(T entity);

}
