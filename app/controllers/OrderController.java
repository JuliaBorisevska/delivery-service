package controllers;

import be.objectify.deadbolt.java.actions.Pattern;

import com.fasterxml.jackson.databind.node.ObjectNode;

import dao.ContactDAO;
import dao.OrderDAO;
import dao.UserDAO;
import dto.OrderDTO;
import dto.OrderDetailsDTO;
import dto.OrderHistoryDTO;
import entity.Company;
import entity.Contact;
import entity.Order;
import entity.OrderHistory;
import entity.User;
import handler.ConfigContainer;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import play.Logger;
import play.Logger.ALogger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import resource.MessageManager;

import javax.persistence.EntityManager;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class OrderController extends BaseController {
	private static ALogger logger = Logger.of(OrderController.class);
	public static final String PROCESS_MNG_ROLE_NAME = "PROCESS_MNG";
	public static final String DELIVERY_MNG_ROLE_NAME = "DELIVERY_MNG";
	
    @Transactional
    @Pattern("ordchange")
    public static Result getOrder(Long id) {
        try {
        	User user = Application.recieveUserByToken();
        	if (user == null) {
    			return badRequest(Json.toJson(
    				new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
    		}
        	Company company = user.getContactByContactId().getCompanyByCompanyId();
        	OrderDAO orderDAO = new OrderDAO(JPA.em());
            Order order = orderDAO.getOrderById(id);
            if(order == null || order.getCustomerByContactId().getCompanyByCompanyId().getId()!=company.getId()) {
                return notFound(Json.toJson(new Reply<>(Status.ERROR, MessageManager.getProperty("order.notfound"))));
            }
			List<String> statusTitleListFromJson = ConfigContainer.getInstance().getRolesHandler().getOrdersList(user.getRoleByRoleId().getName());
			if (statusTitleListFromJson!=null && !statusTitleListFromJson.contains(order.getStatusByStatusId().getTitle())){
				return badRequest(Json.toJson(
		               new Reply<>(Status.ERROR, MessageManager.getProperty("status.access.error"))));
			}
			ObjectNode result = Json.newObject();
    		result.put("order", Json.toJson(OrderDetailsDTO.getOrderDetails(order)));
    		result.put("statuslist", Json.toJson(ConfigContainer.getInstance().getStatusHandler().getStatusList(order.getStatusByStatusId().getTitle())));
    		List<OrderHistoryDTO> dtoHistory = new ArrayList<>();
    		for(OrderHistory history : order.getOrderHistory()) {
    			dtoHistory.add(OrderHistoryDTO.getOrderHistory(history));
    		}
    		result.put("orderHistory", Json.toJson(dtoHistory));
    		return ok(Json.toJson(
                        	new Reply<>(Status.SUCCESS, result)));
        } catch (IOException | ParseException e) {
			logger.error("Exception in getOrder method: {} ", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
		}
    }
    
    @Transactional
    @Pattern("ordadd")
    public static Result getFirstStatus() {
        try {
        	String firstStatus = ConfigContainer.getInstance().getStatusHandler().getFirstStatusTitle();
			ObjectNode result = Json.newObject();
			result.put("firstStatus", Json.toJson(firstStatus));
    		return ok(Json.toJson(
                        	new Reply<>(Status.SUCCESS, result)));
        } catch (IOException | ParseException e) {
			logger.error("Exception in getFirstStatus method: {} ", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
		}
    }

    @Transactional
    @Pattern("ordlst")
    public static Result listStatuses(){
    	try {
    		User user = Application.recieveUserByToken();
    		List<entity.Status> statusList;
    		OrderDAO orderDAO =  new OrderDAO(JPA.em());
    		if (user == null) {
    			return badRequest(Json.toJson(
					new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
    		}
			if (ConfigContainer.getInstance().getRolesHandler().hasOrders(user.getRoleByRoleId().getName())){
				List<String> statusTitleList = ConfigContainer.getInstance().getRolesHandler().getOrdersList(user.getRoleByRoleId().getName());
				statusList = orderDAO.getStatusList(statusTitleList);
			}else{
				statusList = orderDAO.getStatusList();
			}
	    	ObjectNode result = Json.newObject();
			result.put("statusList", Json.toJson(statusList));
			return ok(Json.toJson(
	                    	new Reply<>(Status.SUCCESS, result)));
		} catch (IOException | ParseException  e) {
			logger.error("Exception in listStatuses method: {} ", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
		}
    }
    
    @Transactional
    @Pattern("ordlst")
    public static Result listOrders(Integer pageNumber, Integer pageSize, String status) {
    	try{
    		EntityManager em = JPA.em();
    		OrderDAO orderDAO = new OrderDAO(em);
    		if(pageNumber == null || pageSize == null || pageNumber <= 0 || pageNumber <= 0) {
    			return badRequest(Json.toJson(
    					new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
    		}
    		User user = Application.recieveUserByToken();
    		if (user == null) {
    			return badRequest(Json.toJson(
    					new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
    		}
    		Company company = user.getContactByContactId().getCompanyByCompanyId();
    		Long total;
    		List<Order> orderList;
    		List<String> statusTitleList = null;
    		List<String> statusTitleListFromJson = ConfigContainer.getInstance().getRolesHandler().getOrdersList(user.getRoleByRoleId().getName());
    		if (StringUtils.isBlank(status)){
    			if (statusTitleListFromJson!=null){
    				statusTitleList = statusTitleListFromJson;
    			}	
    		}else{
    			if (statusTitleListFromJson!=null && !statusTitleListFromJson.contains(status)){
    				return badRequest(Json.toJson(
    		               new Reply<>(Status.ERROR, MessageManager.getProperty("status.access.error"))));
    			}else{
    				statusTitleList = new ArrayList<String>();
					statusTitleList.add(status);
    			}
    		}
    		if (statusTitleList!=null){
    			total = orderDAO.getLength(company, statusTitleList);
    			orderList = orderDAO.getOrderList(pageNumber, pageSize, company, statusTitleList);
    		}else{
    			total = orderDAO.getLength(company);
    			orderList = orderDAO.getOrderList(pageNumber, pageSize, company);
    		}
    		Integer totalPages = Double.valueOf(Math.ceil((double) total / pageSize)).intValue();
    		List<OrderDTO> dtoList = new ArrayList<>();
    		for(Order ord : orderList) {
    			dtoList.add(OrderDTO.getOrder(ord));
    		}

    		ObjectNode result = Json.newObject();
    		result.put("totalPages", totalPages);
    		result.put("list", Json.toJson(dtoList));

    		return ok(Json.toJson(
                        	new Reply<>(Status.SUCCESS, result))
    				);
    	} catch (IOException | ParseException e) {
            logger.error("Exception in listOrders method: {} ", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
    }

    @Transactional
    @Pattern("ordchange")
    public static Result changeOrderStatus() {
    	try{
    		OrderDAO orderDAO = new OrderDAO(JPA.em());
            final Map<String, String[]> values = request().body().asFormUrlEncoded();
            Long orderId = Long.valueOf(values.get("id")[0]);
            String nextStatus = values.get("status")[0];
        	String comment = values.get("comment")[0];
            User user = Application.recieveUserByToken();
        	if (user == null) {
        		return badRequest(Json.toJson(
					new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
        	}
        	Company company = user.getContactByContactId().getCompanyByCompanyId();
            Order order = orderDAO.getOrderById(orderId);
            if(order == null || order.getCustomerByContactId().getCompanyByCompanyId().getId()!=company.getId()) {
                return notFound(Json.toJson(new Reply<>(Status.ERROR, MessageManager.getProperty("order.notfound"))));
            }
            String currentStatus = order.getStatusByStatusId().getTitle();
            List<String> statusList= ConfigContainer.getInstance().getStatusHandler().getStatusList(currentStatus);
            entity.Status next = orderDAO.findStatusByTitle(nextStatus);
            if (statusList.contains(nextStatus) && next!=null){
            	logger.info("Start changeOrderStatus() with request parameters: orderId - {}, status - {}, comment - {}", orderId, nextStatus, comment);
            	DateTime now = new DateTime(DateTimeZone.getDefault());
            	//DateTime now = new DateTime(DateTimeZone.UTC);
            	//logger.info("Time : {}", now);
            	//logger.info("Default : {}", DateTimeZone.getDefault());
            	OrderHistory history = new OrderHistory();
            	//int offset = DateTimeZone.getDefault().getOffset(new DateTime());
            	//now = now.minusMillis(offset);
            	history.setModificationDate(new Timestamp(now.getMillis()));
            	history.setStatusByStatusId(next);
            	history.setUserByUserId(user);
            	history.setUserComment(StringUtils.isBlank(comment)?null:comment);
            	orderDAO.changeOrderStatus(order, next, history);
            	return ok(Json.toJson(
                        new Reply<>(Status.SUCCESS, null)));
            }else{
            	logger.error("Can't change status of the order from {} to {}", currentStatus, nextStatus);
        		return badRequest(Json.toJson(
                		new Reply<>(Status.ERROR, MessageManager.getProperty("message.status.unchanged"))));
            }
            
    	}catch(Exception e){
    		logger.error("Error in changeOrderStatus method: {}", e);
    		return badRequest(Json.toJson(
            		new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
    	}
    	
    }
    
    @Transactional
    @Pattern("ordadd")
    public static Result createOrder() {
    	try{          
            final Map<String, String[]> values = request().body().asFormUrlEncoded();
            logger.info("Start createOrder with parameters: description - {}, price - {}, processMngId - {}, deliveryMngId - {}, customerId - {}, recipientId - {}",
            		values.get("description")[0], values.get("price")[0], values.get("processMngId")[0],
            		values.get("deliveryMngId")[0], values.get("customerId")[0], values.get("recipientId")[0]);
            String description = StringUtils.isBlank(values.get("description")[0])?null:values.get("description")[0];
            Double price = StringUtils.isBlank(values.get("price")[0])?null:Double.valueOf(values.get("price")[0]);
            if (price == null || description==null) {
        		return badRequest(Json.toJson(
					new Reply<>(Status.ERROR, MessageManager.getProperty("order.empty.fields"))));
        	}
            Long processMngId = StringUtils.isBlank(values.get("processMngId")[0])?null:Long.valueOf(values.get("processMngId")[0]);
            Long deliveryMngId = StringUtils.isBlank(values.get("deliveryMngId")[0])?null:Long.valueOf(values.get("deliveryMngId")[0]);
            Long customerId = StringUtils.isBlank(values.get("customerId")[0])?null:Long.valueOf(values.get("customerId")[0]);
            Long recipientId = StringUtils.isBlank(values.get("recipientId")[0])?null:Long.valueOf(values.get("recipientId")[0]);
            if (processMngId == null || deliveryMngId==null || customerId==null || recipientId==null) {
        		return badRequest(Json.toJson(
					new Reply<>(Status.ERROR, MessageManager.getProperty("order.empty.fields"))));
        	}
            User user = Application.recieveUserByToken();
        	if (user == null) {
        		return badRequest(Json.toJson(
					new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
        	}
        	Company company = user.getContactByContactId().getCompanyByCompanyId();
        	EntityManager em = JPA.em();
    		OrderDAO orderDAO = new OrderDAO(em);
    		ContactDAO contactDAO = new ContactDAO(em);
    		UserDAO userDAO = new UserDAO(em);
            Order order = new Order();
            Contact customer = contactDAO.findById(customerId);
            Contact recipient = contactDAO.findById(recipientId);
            User processMng = userDAO.findById(processMngId);
            User deliveryMng = userDAO.findById(deliveryMngId);
            entity.Status firstStatus = orderDAO.findStatusByTitle(ConfigContainer.getInstance().getStatusHandler().getFirstStatusTitle());
            if (customer == null || recipient==null || processMng==null || deliveryMng==null
        		|| firstStatus==null || customer.getCompanyByCompanyId().getId()!=company.getId()
            	|| recipient.getCompanyByCompanyId().getId()!=company.getId()
            	|| processMng.getContactByContactId().getCompanyByCompanyId().getId()!=company.getId()
            	|| deliveryMng.getContactByContactId().getCompanyByCompanyId().getId()!=company.getId()
            	|| !PROCESS_MNG_ROLE_NAME.equals(processMng.getRoleByRoleId().getName())
            	|| !DELIVERY_MNG_ROLE_NAME.equals(deliveryMng.getRoleByRoleId().getName())) {
            	return badRequest(Json.toJson(
					new Reply<>(Status.ERROR, MessageManager.getProperty("order.wrong.fields"))));
        	}            
            order.setDescription(description);
            order.setTotalPrice(price);
            order.setCustomerByContactId(customer);
            order.setRecipientByContactId(recipient);
            order.setProcessMngByUserId(processMng);
            order.setDeliveryMngByUserId(deliveryMng);
            order.setUserByUserId(user);
            DateTime now = new DateTime(DateTimeZone.getDefault());
            order.setOrderDate(new Timestamp(now.getMillis()));
            order.setStatusByStatusId(firstStatus);
            orderDAO.create(order);
            return ok(Json.toJson(
                    new Reply<>(Status.SUCCESS, null)));
    	}catch(NumberFormatException e){
    		logger.error("Error in createOrder method: {}", e);
    		return badRequest(Json.toJson(
            		new Reply<>(Status.ERROR, MessageManager.getProperty("order.wrong.fields"))));
    	} catch (IOException | ParseException e) {
    		logger.error("Error in createOrder method: {}", e);
    		return badRequest(Json.toJson(
            		new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
		}
    }

    @Transactional
    public static Result updateOrder(Long id) {
    	try{          
            final Map<String, String[]> values = request().body().asFormUrlEncoded();
            logger.info("Start updateOrder with parameters: description - {}, price - {}, processMngId - {}, deliveryMngId - {}, customerId - {}, recipientId - {}",
            		values.get("description")[0], values.get("price")[0], values.get("processMngId")[0],
            		values.get("deliveryMngId")[0], values.get("customerId")[0], values.get("recipientId")[0]);
            String description = StringUtils.isBlank(values.get("description")[0])?null:values.get("description")[0];
            Double price = StringUtils.isBlank(values.get("price")[0])?null:Double.valueOf(values.get("price")[0]);
            if (price == null || description==null) {
        		return badRequest(Json.toJson(
					new Reply<>(Status.ERROR, MessageManager.getProperty("order.empty.fields"))));
        	}
            Long processMngId = StringUtils.isBlank(values.get("processMngId")[0])?null:Long.valueOf(values.get("processMngId")[0]);
            Long deliveryMngId = StringUtils.isBlank(values.get("deliveryMngId")[0])?null:Long.valueOf(values.get("deliveryMngId")[0]);
            Long customerId = StringUtils.isBlank(values.get("customerId")[0])?null:Long.valueOf(values.get("customerId")[0]);
            Long recipientId = StringUtils.isBlank(values.get("recipientId")[0])?null:Long.valueOf(values.get("recipientId")[0]);
            if (processMngId == null || deliveryMngId==null || customerId==null || recipientId==null) {
        		return badRequest(Json.toJson(
					new Reply<>(Status.ERROR, MessageManager.getProperty("order.empty.fields"))));
        	}
            User user = Application.recieveUserByToken();
        	if (user == null) {
        		return badRequest(Json.toJson(
					new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
        	}
        	Company company = user.getContactByContactId().getCompanyByCompanyId();
        	EntityManager em = JPA.em();
    		OrderDAO orderDAO = new OrderDAO(em);
    		ContactDAO contactDAO = new ContactDAO(em);
    		UserDAO userDAO = new UserDAO(em);
            Order order = orderDAO.getOrderById(id);
            Contact customer = contactDAO.findById(customerId);
            Contact recipient = contactDAO.findById(recipientId);
            User processMng = userDAO.findById(processMngId);
            User deliveryMng = userDAO.findById(deliveryMngId);
            if (customer == null || recipient==null || processMng==null || deliveryMng==null
        		|| customer.getCompanyByCompanyId().getId()!=company.getId()
            	|| recipient.getCompanyByCompanyId().getId()!=company.getId()
            	|| processMng.getContactByContactId().getCompanyByCompanyId().getId()!=company.getId()
            	|| deliveryMng.getContactByContactId().getCompanyByCompanyId().getId()!=company.getId()
            	|| !PROCESS_MNG_ROLE_NAME.equals(processMng.getRoleByRoleId().getName())
            	|| !DELIVERY_MNG_ROLE_NAME.equals(deliveryMng.getRoleByRoleId().getName())) {
            	return badRequest(Json.toJson(
					new Reply<>(Status.ERROR, MessageManager.getProperty("order.wrong.fields"))));
        	}            
            order.setDescription(description);
            order.setTotalPrice(price);
            order.setCustomerByContactId(customer);
            order.setRecipientByContactId(recipient);
            order.setProcessMngByUserId(processMng);
            order.setDeliveryMngByUserId(deliveryMng);
            return ok(Json.toJson(
                    new Reply<>(Status.SUCCESS, null)));
    	}catch(NumberFormatException e){
    		logger.error("Error in createOrder method: {}", e);
    		return badRequest(Json.toJson(
            		new Reply<>(Status.ERROR, MessageManager.getProperty("order.wrong.fields"))));
    	}
    }

   
}
