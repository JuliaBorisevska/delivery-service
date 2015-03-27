package dao;

import entity.Contact;
import play.db.jpa.JPA;

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
        EntityManager em = JPA.em();
        Contact contact = em.find(Contact.class, entity.getId());
        if(contact != null) {
            em.remove(contact);
        }
    }

    @Override
    public void create(Contact entity) {
        JPA.em().persist(entity);
    }

    @Override
    public void update(Contact entity) {
        JPA.em().persist(entity);
    }

    public List<Contact> getContactList(Integer pageNumber, Integer pageSize) {
        EntityManager em = JPA.em();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> from = criteriaQuery.from(Contact.class);

        CriteriaQuery<Contact> select = criteriaQuery.select(from);
        TypedQuery<Contact> q = em.createQuery(select);
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }
}
