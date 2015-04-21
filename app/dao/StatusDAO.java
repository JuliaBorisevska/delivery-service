package dao;

import entity.Status;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.List;

/**
 * Created by hanna.kubarka on 31.03.2015.
 */
public class StatusDAO extends AbstractDAO<Status> {



    public StatusDAO(EntityManager em) {
        super(em);
    }

    @Override
    public void delete(Status entity) {
        Status status = em.find(Status.class, entity.getId());
        if(status!=null){
            em.remove(status);
        }
    }

    public void delete(Integer id){
        Status status = em.find(Status.class, id);
        if(status!=null){
            em.remove(status);
        }
    }

    @Override
    public void create(Status entity) {
        em.persist(entity);
    }

    @Override
    public void update(Status entity) {
        em.persist(entity);
    }

    public List<Status> getStatusList(){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Status> criteriaQuery = criteriaBuilder.createQuery(Status.class);
        Root<Status> from = criteriaQuery.from(Status.class);
        CriteriaQuery<Status> select = criteriaQuery.select(from);
        TypedQuery<Status> q = em.createQuery(select);
        return q.getResultList();
    }
    
    public List<Status> getStatusList(List<String> statusTitleList){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Status> criteriaQuery = criteriaBuilder.createQuery(Status.class);
        Root<Status> from = criteriaQuery.from(Status.class);
        Expression<String> exp = from.get("title");
        Predicate predicate = exp.in(statusTitleList);
        CriteriaQuery<Status> select = criteriaQuery.select(from).where(predicate);
        TypedQuery<Status> q = em.createQuery(select);
        return q.getResultList();
    }

    public Status getStatusById(Integer statusId){
        return em.find(Status.class, statusId);
    }


}
