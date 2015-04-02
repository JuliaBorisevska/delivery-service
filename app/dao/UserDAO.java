package dao;

import entity.Company;
import entity.Contact;
import entity.User;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;



import java.util.List;

public class UserDAO extends AbstractDAO<User> {

	public UserDAO(EntityManager em) {
		super(em);
	}

	@Override
	public void delete(User entity) {
		User user = em.find(User.class, entity.getId());
		if(user != null) {
			em.remove(entity);
		}

	}

	@Override
	public void create(User entity) {
		em.persist(entity);
	}

	@Override
	public void update(User entity) {
		em.persist(entity);
	}
	
	public Long total(Company company) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> from = countQuery.from(User.class);
		Join<User, Contact> contact = from.join("contactByContactId");
		Join<Contact, Company> comp = contact.join("companyByCompanyId");
        countQuery.select(criteriaBuilder.count(from)).where(criteriaBuilder.equal(comp.get("id"), company.getId()));
        return em.createQuery(countQuery).getSingleResult();
    }

	public List<User> list(Integer pageNumber, Integer pageSize, Company company) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		Root<User> from = criteriaQuery.from(User.class);
		Join<User, Contact> contact = from.join("contactByContactId");
		Join<Contact, Company> comp = contact.join("companyByCompanyId");
		CriteriaQuery<User> select = criteriaQuery.select(from).where(criteriaBuilder.equal(comp.get("id"), company.getId()));
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
            query.select(u).where(builder.equal(u.get("identifier"), login));
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
