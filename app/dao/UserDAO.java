package dao;

import entity.*;
import play.Logger;
import play.Logger.ALogger;

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
	public static final String ACTIVE_USER = "active";
	public static final String INACTIVE_USER = "inactive";
	
	public UserDAO(EntityManager em) {
		super(em);
	}

	@Override
	public void delete(User entity) {
		logger.info("Start delete with user id - {} and company id - {}",entity.getId(), entity.getContactByContactId().getCompanyByCompanyId().getId());
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> fromUser = query.from(User.class);
		Join<User, Contact> fromContact = fromUser.join("contactByContactId");
		Join<Contact, Company> fromCompany = fromContact.join("companyByCompanyId");
		query.select(fromUser).where(criteriaBuilder.equal(fromCompany.get("id"), entity.getContactByContactId().getCompanyByCompanyId().getId()),
										criteriaBuilder.equal(fromUser.get("id"), entity.getId()));
		List<User> users = em.createQuery(query).getResultList();
		UserState state = findByStateTitle(INACTIVE_USER);
		if(!users.isEmpty()) {
			User user = users.get(0);
			user.setUserStateByUserStateId(state);
			update(user);
			logger.info("User with id - {} and company id - {} is inactive",entity.getId(), entity.getContactByContactId().getCompanyByCompanyId().getId());
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
	
	public User findById(Long id) {
        return em.find(User.class, id);
    }
	
	public UserState findByStateTitle(String title) {

        try{
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<UserState> query = builder.createQuery(UserState.class);
            Root<UserState> u = query.from(UserState.class);
            query.select(u).where(builder.equal(u.get("title"), title));
            TypedQuery<UserState> q = em.createQuery(query);
            return q.getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }
	
	
	public Long total(Company company) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> from = countQuery.from(User.class);
		Join<User, Contact> contact = from.join("contactByContactId");
		Join<Contact, Company> comp = contact.join("companyByCompanyId");
		UserState state = findByStateTitle(ACTIVE_USER);
        countQuery.select(criteriaBuilder.count(from)).where(criteriaBuilder.equal(comp.get("id"), company.getId()),
        													criteriaBuilder.equal(from.get("userStateByUserStateId"), state));
        Long lngth = em.createQuery(countQuery).getSingleResult();
        logger.info("Method total with parameter company - {} returns length - {}", company.getTitle(), lngth);
        return lngth;
    }
	
	public Long total(Company company, String roleTitle) {
        	CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            Root<User> from = countQuery.from(User.class);
    		Join<User, Contact> contact = from.join("contactByContactId");
    		Join<Contact, Company> comp = contact.join("companyByCompanyId");
    		UserState state = findByStateTitle(ACTIVE_USER);
            countQuery.select(criteriaBuilder.count(from)).where(criteriaBuilder.equal(comp.get("id"), company.getId()),
            													criteriaBuilder.equal(from.get("userStateByUserStateId"), state),
            													criteriaBuilder.equal(from.get("roleByRoleId").get("name"), roleTitle));
            Long lngth = em.createQuery(countQuery).getSingleResult();
            logger.info("Method total with parameters company - {}, roleTitle - {} returns length - {}", company.getTitle(), roleTitle, lngth);
            return lngth;
		
    }

	public List<User> list(Integer pageNumber, Integer pageSize, Company company) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		Root<User> from = criteriaQuery.from(User.class);
		Join<User, Contact> contact = from.join("contactByContactId");
		Join<Contact, Company> comp = contact.join("companyByCompanyId");
		UserState state = findByStateTitle(ACTIVE_USER);
		CriteriaQuery<User> select = criteriaQuery.select(from).where(criteriaBuilder.equal(comp.get("id"), company.getId()),
				criteriaBuilder.equal(from.get("userStateByUserStateId"), state));
		TypedQuery<User> q = em.createQuery(select);
		q.setFirstResult((pageNumber - 1) * pageSize);
		q.setMaxResults(pageSize);

		return q.getResultList();
	}
	
	public List<User> list(Integer pageNumber, Integer pageSize, Company company, String roleTitle) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		Root<User> from = criteriaQuery.from(User.class);
		Join<User, Contact> contact = from.join("contactByContactId");
		Join<Contact, Company> comp = contact.join("companyByCompanyId");
		UserState state = findByStateTitle(ACTIVE_USER);
		CriteriaQuery<User> select = criteriaQuery.select(from).where(criteriaBuilder.equal(comp.get("id"), company.getId()),
				criteriaBuilder.equal(from.get("userStateByUserStateId"), state),
				criteriaBuilder.equal(from.get("roleByRoleId").get("name"), roleTitle));
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
            UserState state = findByStateTitle(ACTIVE_USER);
            query.select(u).where(builder.equal(u.get("identifier"), login),
					builder.equal(u.get("userStateByUserStateId"), state));
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

	public List<SecurityRole> listRoles() {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<SecurityRole> criteriaQuery = criteriaBuilder.createQuery(SecurityRole.class);
		Root<SecurityRole> from = criteriaQuery.from(SecurityRole.class);
		CriteriaQuery<SecurityRole> select = criteriaQuery.select(from);
		TypedQuery<SecurityRole> q = em.createQuery(select);
		return q.getResultList();

	}

}
