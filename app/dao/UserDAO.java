package dao;

import entity.User;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDAO extends AbstractDAO<User> {

	public UserDAO(EntityManager em) {
		super(em);
	}

	@Override
	public void delete(User entity) {
		EntityManager em = JPA.em();
		User user = em.find(User.class, entity.getId());
		if(user != null) {
			em.remove(entity);
		}

	}

	@Override
	public void create(User entity) {
		JPA.em().persist(entity);
	}

	@Override
	public void update(User entity) {
		JPA.em().persist(entity);
	}

	public static List<User> list(Integer pageNumber, Integer pageSize) {
		EntityManager em = JPA.em();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		Root<User> from = criteriaQuery.from(User.class);

		CriteriaQuery<User> select = criteriaQuery.select(from);
		TypedQuery<User> q = em.createQuery(select);
		q.setFirstResult((pageNumber - 1) * pageSize);
		q.setMaxResults(pageSize);

		return q.getResultList();
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

	public User findByToken(String token) {
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<User> query = builder.createQuery(User.class);
			Root<User> u = query.from(User.class);
			query.select(u).where(builder.equal(u.get("token"), token));
			TypedQuery<User> q = em.createQuery(query);
			return q.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

}
