package dao;

import entity.Company;
import entity.Contact;
import entity.Order;
import play.Logger;
import play.Logger.ALogger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.List;

/**
 * @Author ValentineS. Created 24.03.2015.
 */
public class ContactDAO extends AbstractDAO<Contact> {
	private static ALogger logger = Logger.of(ContactDAO.class);
	
    public ContactDAO(EntityManager em) {
        super(em);
    }

    @Override
    public void delete(Contact entity) {
    	logger.info("Start delete with contact id - {} and company id - {}",entity.getId(), entity.getCompanyByCompanyId().getId());
    	CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Contact> query = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> fromContact = query.from(Contact.class);
		Join<Contact, Company> fromCompany = fromContact.join("companyByCompanyId");
		query.select(fromContact).where(criteriaBuilder.equal(fromCompany.get("id"), entity.getCompanyByCompanyId().getId()),
										criteriaBuilder.equal(fromContact.get("id"), entity.getId()));
		List<Contact> contacts = em.createQuery(query).getResultList();
		if(!contacts.isEmpty()) {
			em.remove(contacts.get(0));
			logger.info("Contact with id - {} and company id - {} is deleted",entity.getId(), entity.getCompanyByCompanyId().getId());
		}
    }

    @Override
    public void create(Contact entity) {
        em.persist(entity);
    }

    @Override
    public void update(Contact entity) {
        em.persist(entity);
    }

    public List<Contact> getContactList(Integer pageNumber, Integer pageSize, Company company) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> from = criteriaQuery.from(Contact.class);
        Join<Contact, Company> comp = from.join("companyByCompanyId");

        CriteriaQuery<Contact> select = criteriaQuery.select(from);
        //where
        criteriaQuery.where(criteriaBuilder.equal(comp.get("id"), company.getId()));

        TypedQuery<Contact> q = em.createQuery(select);
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }

    public List<Contact> getContactList(Integer pageNumber, Integer pageSize) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> from = criteriaQuery.from(Contact.class);

        CriteriaQuery<Contact> select = criteriaQuery.select(from);

        TypedQuery<Contact> q = em.createQuery(select);
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }

    public Contact findById(Long id) {
        return em.find(Contact.class, id);
    }

    public Long numberOfContacts(Company company) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root<Contact> from = countQuery.from(Contact.class);
        Join<Contact, Company> comp = from.join("companyByCompanyId");

        countQuery.select(criteriaBuilder.count(from))
                .where(criteriaBuilder.equal(comp.get("id"), company.getId()));

        return em.createQuery(countQuery).getSingleResult();
    }
    
    public List<Contact> getContactListByIds(List<Long> ids){
    	CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> fromContact = criteriaQuery.from(Contact.class);
        Expression<Long> exp = fromContact.get("id");
        Predicate predicate = exp.in(ids);
        CriteriaQuery<Contact> select = criteriaQuery.select(fromContact).where(predicate);
        TypedQuery<Contact> q = em.createQuery(select);
        return q.getResultList();
    } 
}
