package dao;

import entity.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UserDAO extends AbstractDAO<User> {

	public UserDAO(EntityManager em) {
		super(em);
	}

	@Override
	public void delete(User entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create(User entity) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(User entity) {
		// TODO Auto-generated method stub
		
	}
	
	public User findByLogin(String login) {
        try{
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> u = query.from(User.class);
            query.select(u).where(builder.equal(u.get("login"), login));
            TypedQuery<User> q = em.createQuery(query);
            return q.getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

}
