package dao;

import entity.Company;
import play.db.jpa.JPA;

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
public class CompanyDAO extends AbstractDAO<Company> {

    public CompanyDAO(EntityManager em) {
        super(em);
    }

    @Override
    public void delete(Company entity) {

        delete(entity.getId().longValue());
    }

    public void delete(Long id) {

        EntityManager em = JPA.em();
        Company Company = em.find(Company.class, id);
        if(Company != null) {
            em.remove(Company);
        }
    }

    @Override
    public void create(Company entity) {
        JPA.em().persist(entity);
    }

    @Override
    public void update(Company entity) {
        JPA.em().persist(entity);
    }

    public List<Company> getCompanyList(Integer pageNumber, Integer pageSize) {

        EntityManager em = JPA.em();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
        Root<Company> from = criteriaQuery.from(Company.class);

        CriteriaQuery<Company> select = criteriaQuery.select(from);
        TypedQuery<Company> q = em.createQuery(select);
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }

    public Company findById(Long id) {

        EntityManager em = JPA.em();
        return em.find(Company.class, id);
    }

    public Long numberOfCompanies() {

        EntityManager em = JPA.em();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Company.class)));
        return em.createQuery(countQuery).getSingleResult();
    }

    public Company findByName(String title) {

        try{
            EntityManager em = JPA.em();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Company> query = builder.createQuery(Company.class);
            Root<Company> u = query.from(Company.class);
            query.select(u).where(builder.equal(u.get("title"), title));

            TypedQuery<Company> q = em.createQuery(query);
            return q.getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }
}
