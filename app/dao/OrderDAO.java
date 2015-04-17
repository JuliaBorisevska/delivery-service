package dao;

import entity.Company;
import entity.Contact;
import entity.Order;
import entity.Status;
import entity.User;

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
 * Created by hanna.kubarka on 26.03.2015.
 */
public class OrderDAO extends AbstractDAO<Order> {

    public OrderDAO(EntityManager em) {
        super(em);
    }

    @Override
    public void delete(Order entity) {
        Order order = em.find(Order.class, entity.getId());
        if(order!=null){
            em.remove(order);
        }
    }

    public void delete(Integer id){
        Order order = em.find(Order.class, id);
        if(order!=null){
            em.remove(order);
        }
    }

    @Override
    public void create(Order entity) {
        em.persist(entity);
    }

    @Override
    public void update(Order entity) {
        em.persist(entity);
    }

    public Order getOrderById(Integer orderId){
        return em.find(Order.class, orderId);
    }
/*
    public List<Order> getOrderListByCustomer(Integer pageNumber, Integer pageSize, Integer customerId){
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
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> from = criteriaQuery.from(Order.class);
        CriteriaQuery<Order> select = criteriaQuery.select(from).where(criteriaBuilder.equal(from.get("recipient_contact_id"),recipientId));
        TypedQuery<Order> q = em.createQuery(select);
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }
*/
    
    public List<Order> getOrderList(Integer pageNumber, Integer pageSize, Company company){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> fromOrder = criteriaQuery.from(Order.class);
		Join<Order, Contact> fromContact = fromOrder.join("customerByContactId");
        CriteriaQuery<Order> select = criteriaQuery.select(fromOrder).where(criteriaBuilder.equal(fromContact.get("companyByCompanyId"), company));
        TypedQuery<Order> q = em.createQuery(select);
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }
    
    public Long  getLength(Company company){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Order> fromOrder = countQuery.from(Order.class);
        Join<Order, Contact> fromContact = fromOrder.join("customerByContactId");
        countQuery.select(criteriaBuilder.count(fromOrder)).where(criteriaBuilder.equal(fromContact.get("companyByCompanyId"), company));
        Long lngth = em.createQuery(countQuery).getSingleResult();
        return lngth;
    }

    
    public List<Order> getOrderList(Integer pageNumber, Integer pageSize, Company company, List<String> statusTitleList){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> fromOrder = criteriaQuery.from(Order.class);
		Join<Order, Contact> fromContact = fromOrder.join("customerByContactId");
		Join<Order, Status> fromStatus = fromOrder.join("statusByStatusId");
        Expression<String> exp = fromStatus.get("title");
        Predicate predicate = exp.in(statusTitleList);
        CriteriaQuery<Order> select = criteriaQuery.select(fromOrder).where(criteriaBuilder.equal(fromContact.get("companyByCompanyId"), company),
        																	predicate);
        TypedQuery<Order> q = em.createQuery(select);
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }
    
    public Long  getLength(Company company, List<String> statusTitleList){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Order> fromOrder = countQuery.from(Order.class);
        Join<Order, Contact> fromContact = fromOrder.join("customerByContactId");
        Join<Order, Status> fromStatus = fromOrder.join("statusByStatusId");
        Expression<String> exp = fromStatus.get("title");
        Predicate predicate = exp.in(statusTitleList);
        countQuery.select(criteriaBuilder.count(fromOrder)).where(criteriaBuilder.equal(fromContact.get("companyByCompanyId"), company),
        														predicate);
        Long lngth = em.createQuery(countQuery).getSingleResult();
        return lngth;
    }

}
