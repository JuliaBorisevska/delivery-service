package controllers;

import be.objectify.deadbolt.java.actions.Pattern;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.OrderDAO;
import dao.StatusDAO;
import dto.OrderDTO;
import entity.Order;
import logic.StatusOrder;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hanna.kubarka on 30.03.2015.
 */

public class OrderController extends BaseController {

    //private static OrderDAO orderDAO = new OrderDAO(JPA.em());
    //private static StatusDAO statusDAO =  new StatusDAO(JPA.em());

    @Transactional
    public static Result getOrder(Integer id) {
    	OrderDAO orderDAO = new OrderDAO(JPA.em());
        Order order = orderDAO.getOrderById(id);
        if(order == null) {
            return notFound(Json.toJson(new Reply()));
        }

        Reply<Order> reply = new Reply<>(Status.SUCCESS, order);
        return ok(Json.toJson(reply));
    }

    @Transactional
    public static Result setNextStatus(Integer id){
    	OrderDAO orderDAO = new OrderDAO(JPA.em());
        entity.Status st =  StatusOrder.nextStatus(orderDAO.getOrderById(id).getStatusByStatusId());
        orderDAO.getOrderById(id).setStatusByStatusId(st);
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, id)
        ));


    }

    @Transactional
    @Pattern("ordlst")
    public static Result listOrders(Integer pageNumber, Integer pageSize) {
    	EntityManager em = JPA.em();
    	OrderDAO orderDAO = new OrderDAO(em);
    	StatusDAO statusDAO =  new StatusDAO(em);
    	if(pageNumber == null || pageSize == null || pageNumber <= 0 || pageNumber <= 0) {
            return badRequest(Json.toJson(new Reply()));
        }
//        ArrayList<entity.Status> statuslist = (ArrayList) statusDAO.getStatusList();
        StatusOrder.setList((ArrayList)statusDAO.getStatusList());
        ArrayList<entity.Status> statuslist = StatusOrder.statusList;
        Long total = orderDAO.getLength();
        Integer totalPages = Double.valueOf(Math.ceil((double) total / pageSize)).intValue();
        List<Order> orderList = orderDAO.getOrderList(pageNumber, pageSize);
        List<OrderDTO> dtoList = new ArrayList<>();
        for(Order ord : orderList) {
            dtoList.add(OrderDTO.getOrder(ord, statuslist));
        }

        ObjectNode result = Json.newObject();
        result.put("totalPages", totalPages);
        result.put("list", Json.toJson(dtoList));

        return ok(Json.toJson(
                        new Reply<>(Status.SUCCESS, result))
        );
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

    
}
