package dao;

import entity.Company;
import entity.Contact;
import entity.User;
import play.Logger;
import play.Logger.ALogger;
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
	private static ALogger logger = Logger.of(UserDAO.class);
	
	public UserDAO(EntityManager em) {
		super(em);
	}

	@Override
	public void delete(User entity) {
		logger.info("Start delete with user id - {} and company id - {}",entity.getId(), entity.getContactByContactId().getCompanyByCompanyId().getId());
		/*User user = em.find(User.class, entity.getId());
		if(user != null) {
			em.remove(user);
		}*/
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> fromUser = query.from(User.class);
		Join<User, Contact> fromContact = fromUser.join("contactByContactId");
		Join<Contact, Company> fromCompany = fromContact.join("companyByCompanyId");
		query.select(fromUser).where(criteriaBuilder.equal(fromCompany.get("id"), entity.getContactByContactId().getCompanyByCompanyId().getId()),
										criteriaBuilder.equal(fromUser.get("id"), entity.getId()));
		List<User> users = em.createQuery(query).getResultList();
		if(!users.isEmpty()) {
			em.remove(users.get(0));
			logger.info("User with id - {} and company id - {} is deleted",entity.getId(), entity.getContactByContactId().getCompanyByCompanyId().getId());
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
