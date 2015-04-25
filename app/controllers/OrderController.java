package controllers;

import be.objectify.deadbolt.java.actions.Pattern;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.OrderDAO;
import dao.StatusDAO;
import dto.OrderDTO;
import dto.OrderDetailsDTO;
import dto.OrderHistoryDTO;
import entity.Company;
import entity.Order;
import entity.OrderHistory;
import entity.User;
import handler.ConfigContainer;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.Logger.ALogger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import resource.MessageManager;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class OrderController extends BaseController {
	private static ALogger logger = Logger.of(OrderController.class);
	
	
    @Transactional
    @Pattern("ordadd")
    public static Result getOrder(Long id) {
        try {
        	User user = Application.recieveUserByToken();
        	if (user == null) {
    			return badRequest(Json.toJson(
    				new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
    		}
        	Company company = user.getContactByContactId().getCompanyByCompanyId();
        	OrderDAO orderDAO = new OrderDAO(JPA.em());
            Order order = orderDAO.getOrderById(id, company);
            if(order == null) {
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
    		StatusDAO statusDAO =  new StatusDAO(JPA.em());
    		if (user == null) {
    			return badRequest(Json.toJson(
					new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
    		}
			if (ConfigContainer.getInstance().getRolesHandler().hasOrders(user.getRoleByRoleId().getName())){
				List<String> statusTitleList = ConfigContainer.getInstance().getRolesHandler().getOrdersList(user.getRoleByRoleId().getName());
				statusList = statusDAO.getStatusList(statusTitleList);
			}else{
				statusList = statusDAO.getStatusList();
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
    @Pattern("ordadd")
    public static Result createOrder() {
    	OrderDAO orderDAO = new OrderDAO(JPA.em());
        Order order = new Order();
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String description = values.get("description")[0];
        String price = values.get("price")[0];
        order.setDescription(description);
        // caution: no validation!
        order.setTotalPrice(Integer.valueOf(price));
        orderDAO.create(order);

        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, order)
        ));
    }
/*
    @Transactional
    public static Result updateOrder(Integer id) {
    	OrderDAO orderDAO = new OrderDAO(JPA.em());
        Order order = orderDAO.getOrderById(id);
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String description = values.get("description")[0];
        String price = values.get("price")[0];
        order.setDescription(description);
        // caution: no validation!
        order.setTotalPrice(Integer.valueOf(price));
        orderDAO.update(order);
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, order)
        ));
    }

    @Transactional
    public static Result deleteOrder(Integer id) {
    	OrderDAO orderDAO = new OrderDAO(JPA.em());
        orderDAO.delete(id);
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, id)
        ));
    }

   */ 
}
