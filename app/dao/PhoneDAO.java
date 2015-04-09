package dao;

import entity.Phone;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @Author ValentineS. Created 28.03.2015.
 */
public class PhoneDAO extends AbstractDAO<Phone> {

    public PhoneDAO(EntityManager em) {
        super(em);
    }

    @Override
    public void delete(Phone entity) {

        delete(entity.getId().longValue());
    }

    public void delete(Long id) {

        Phone phone = em.find(Phone.class, id);
        if(phone != null) {
            em.remove(phone);
        }
    }

    @Override
    public void create(Phone entity) {
        em.persist(entity);
    }

    @Override
    public void update(Phone entity) {
        em.persist(entity);
    }

    public List<Phone> getPhoneList(Integer pageNumber, Integer pageSize) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Phone> criteriaQuery = criteriaBuilder.createQuery(Phone.class);
        Root<Phone> from = criteriaQuery.from(Phone.class);

        CriteriaQuery<Phone> select = criteriaQuery.select(from);
        TypedQuery<Phone> q = em.createQuery(select);
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }

    public Phone findById(Long id) {

        return em.find(Phone.class, id);
    }

    public Long numberOfPhones() {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Phone.class)));
        return em.createQuery(countQuery).getSingleResult();
    }

    public Phone findByName(String title) {

        try{
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Phone> query = builder.createQuery(Phone.class);
            Root<Phone> u = query.from(Phone.class);
            query.select(u).where(builder.equal(u.get("title"), title));

            TypedQuery<Phone> q = em.createQuery(query);
            return q.getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }
}
