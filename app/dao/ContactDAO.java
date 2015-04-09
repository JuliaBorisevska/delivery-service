package dao;

import entity.Contact;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @Author ValentineS. Created 24.03.2015.
 */
public class ContactDAO extends AbstractDAO<Contact> {

    public ContactDAO(EntityManager em) {
        super(em);
    }

    @Override
    public void delete(Contact entity) {

        delete(entity.getId().longValue());
    }

    public void delete(Long id) {

        Contact contact = em.find(Contact.class, id);
        if(contact != null) {
            em.remove(contact);
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

    public List<Contact> getContactList(Integer pageNumber, Integer pageSize, Integer companyId) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> from = criteriaQuery.from(Contact.class);

        CriteriaQuery<Contact> select = criteriaQuery.select(from);

        //where
        criteriaQuery.where(criteriaBuilder.equal(from.get("company_id"), companyId));

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

    public Long numberOfContacts() {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Contact.class)));
        return em.createQuery(countQuery).getSingleResult();
    }
}
