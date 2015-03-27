package dao;

import entity.Order;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by hanna.kubarka on 26.03.2015.
 */
public class OrderDAO extends AbstractDAO<Order> {

    public OrderDAO(EntityManager em) {
        super(em);
    }

    @Override
    public void delete(Order entity) {

        EntityManager em = JPA.em();
        Order order = em.find(Order.class, entity.getId());
        if(order!=null){
            em.remove(order);
        }
    }

    @Override
    public void create(Order entity) {
        JPA.em().persist(entity);
    }

    @Override
    public void update(Order entity) {
        JPA.em().persist(entity);

    }

    public List<Order> getOrderList(Integer pageNumber, Integer pageSize){
        EntityManager em = JPA.em();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> from = criteriaQuery.from(Order.class);

        CriteriaQuery<Order> select = criteriaQuery.select(from);
        TypedQuery<Order> q = em.createQuery(select);
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }

    public Order getOrderById(Integer orderId){

        EntityManager em = JPA.em();
        return em.find(Order.class, orderId);
    }

    public List<Order> getOrderListByCustomer(Integer pageNumber, Integer pageSize, Integer customerId){
        EntityManager em = JPA.em();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> from = criteriaQuery.from(Order.class);
        CriteriaQuery<Order> select = criteriaQuery.select(from).where(criteriaBuilder.equal(from.get("customer_contact_id"), customerId));
        TypedQuery<Order> q = em.createQuery(select);
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }
    public List<Order> getOrderListByRecipient(Integer pageNumber, Integer pageSize, Integer recipientId){

        EntityManager em = JPA.em();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> from = criteriaQuery.from(Order.class);
        CriteriaQuery<Order> select = criteriaQuery.select(from).where(criteriaBuilder.equal(from.get("recipient_contact_id"),recipientId));
        TypedQuery<Order> q = em.createQuery(select);
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }

}
