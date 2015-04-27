package dao;

import entity.Company;
import entity.Contact;
import entity.Order;
import entity.OrderHistory;
import entity.Status;
import entity.User;
import entity.UserState;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.ListAttribute;

import play.Logger;
import play.Logger.ALogger;

import java.util.List;


public class OrderDAO extends AbstractDAO<Order> {
	private static ALogger logger = Logger.of(OrderDAO.class);
	
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

    @Override
    public void create(Order entity) {
        em.persist(entity);
    }

    @Override
    public void update(Order entity) {
        em.persist(entity);
    }
    
    public Status findStatusByTitle(String title){
    	try{
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Status> query = builder.createQuery(Status.class);
            Root<Status> s = query.from(Status.class);
            query.select(s).where(builder.equal(s.get("title"), title));
            TypedQuery<Status> q = em.createQuery(query);
            return q.getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }
    
    public void addToOrderHistory(OrderHistory history){
    	logger.info("Start addToOrderHistory method");
    	em.persist(history);
    }
    
    public void changeOrderStatus(Order order, Status nextStatus, OrderHistory history){
    	logger.info("Start changeOrderStatus method with orderId - {}, nextStatus - {}", order.getId(), nextStatus);
    	order.setStatusByStatusId(nextStatus);
    	history.setOrderByOrderId(order);
    	addToOrderHistory(history);
    }
    
    public Order getOrderById(Long orderId, Company company){
    	logger.info("Start getOrderById method with orderId - {}", orderId);
    	Order order = em.find(Order.class, orderId);
    	if (order != null){
    		if (order.getCustomerByContactId().getCompanyByCompanyId().getId()!=company.getId()){
        		logger.warn("Order with orderId - {} doesn't belong the company with id - {}", orderId, company.getId());
        		order = null;
    		}
    	}else{
    		logger.warn("No result found for orderId - {}", orderId);
        }
    	return order;
    }
    
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
