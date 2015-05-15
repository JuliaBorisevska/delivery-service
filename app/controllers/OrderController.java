package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Pattern;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.ContactDAO;
import dao.OrderDAO;
import dao.UserDAO;
import dto.OrderDTO;
import dto.OrderDetailsDTO;
import dto.OrderHistoryDTO;
import entity.*;
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
    @Pattern("order_selection")
    public static Result getOrder(Long id) {
        logger.info("Get order with id: {}", id);
        try {
            User user = Application.recieveUserByToken();
            if (user == null) {
                return badRequest(Json.toJson(
                        new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
            }
            Company company = user.getContactByContactId().getCompanyByCompanyId();
            OrderDAO orderDAO = new OrderDAO(JPA.em());
            Order order = orderDAO.getOrderById(id);
            if (order == null || order.getCustomerByContactId().getCompanyByCompanyId().getId() != company.getId()) {
                return notFound(Json.toJson(new Reply<>(Status.ERROR, MessageManager.getProperty("order.notfound"))));
            }
            List<String> statusTitleListFromJson = ConfigContainer.getInstance().getRolesHandler().getOrdersList(user.getRoleByRoleId().getName());
            if (statusTitleListFromJson != null && !statusTitleListFromJson.contains(order.getStatusByStatusId().getTitle())) {
                return badRequest(Json.toJson(
                        new Reply<>(Status.ERROR, MessageManager.getProperty("status.access.error"))));
            }
            ObjectNode result = Json.newObject();
            result.put("order", Json.toJson(OrderDetailsDTO.getOrderDetails(order)));
            result.put("statuslist", Json.toJson(ConfigContainer.getInstance().getStatusHandler().getStatusList(order.getStatusByStatusId().getTitle())));
            List<OrderHistoryDTO> dtoHistory = new ArrayList<>();
            for (OrderHistory history : order.getOrderHistory()) {
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
    @Pattern("order_addition")
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
    @Pattern("order_list")
    public static Result listStatuses() {
        try {
            User user = Application.recieveUserByToken();
            List<entity.Status> statusList;
            OrderDAO orderDAO = new OrderDAO(JPA.em());
            if (user == null) {
                return badRequest(Json.toJson(
                        new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
            }
            if (ConfigContainer.getInstance().getRolesHandler().hasOrders(user.getRoleByRoleId().getName())) {
                List<String> statusTitleList = ConfigContainer.getInstance().getRolesHandler().getOrdersList(user.getRoleByRoleId().getName());
                statusList = orderDAO.getStatusList(statusTitleList);
            } else {
                statusList = orderDAO.getStatusList();
            }
            ObjectNode result = Json.newObject();
            result.put("statusList", Json.toJson(statusList));
            return ok(Json.toJson(
                    new Reply<>(Status.SUCCESS, result)));
        } catch (IOException | ParseException e) {
            logger.error("Exception in listStatuses method: {} ", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
    }

    @Transactional
    @Pattern("order_list")
    public static Result listOrders(Integer pageNumber, Integer pageSize, String status) {
        logger.info("Get list of orders; number of page: {}, size of page: {}, status: {}", pageNumber, pageSize, status);
        try {
            EntityManager em = JPA.em();
            OrderDAO orderDAO = new OrderDAO(em);
            if (pageNumber == null || pageSize == null || pageNumber <= 0 || pageNumber <= 0) {
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
            if (StringUtils.isBlank(status)) {
                if (statusTitleListFromJson != null) {
                    statusTitleList = statusTitleListFromJson;
                }
            } else {
                if (statusTitleListFromJson != null && !statusTitleListFromJson.contains(status)) {
                    return badRequest(Json.toJson(
                            new Reply<>(Status.ERROR, MessageManager.getProperty("status.access.error"))));
                } else {
                    statusTitleList = new ArrayList<String>();
                    statusTitleList.add(status);
                }
            }
            if (statusTitleList != null) {
                total = orderDAO.getLength(company, statusTitleList);
                orderList = orderDAO.getOrderList(pageNumber, pageSize, company, statusTitleList);
            } else {
                total = orderDAO.getLength(company);
                orderList = orderDAO.getOrderList(pageNumber, pageSize, company);
            }
            Integer totalPages = Double.valueOf(Math.ceil((double) total / pageSize)).intValue();
            List<OrderDTO> dtoList = new ArrayList<>();
            for (Order ord : orderList) {
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
    @Pattern("order_updating")
    public static Result changeOrderStatus() {
        try {
            OrderDAO orderDAO = new OrderDAO(JPA.em());
            final Map<String, String[]> values = request().body().asFormUrlEncoded();
            Long orderId = null;
            String nextStatus = null;
            String comment = null;
            try {
                if (!values.containsKey("id") || !values.containsKey("status") || !values.containsKey("comment")) {
                    throw new IllegalArgumentException("Some parameters are missing");
                } else {
                    if (StringUtils.isEmpty(values.get("id")[0]) || "undefined".equals(values.get("id")[0]))
                        throw new IllegalArgumentException("id of order is empty or undefined");
                    if (StringUtils.isEmpty(values.get("status")[0]) || "undefined".equals(values.get("status")[0]))
                        throw new IllegalArgumentException("new status of order is empty or undefined");
                    orderId = Long.valueOf(values.get("id")[0]);
                    nextStatus = values.get("status")[0];
                    comment = values.get("comment")[0];
                }
            } catch (IllegalArgumentException e) {
                logger.error("Incorrect parameters", e);
                return badRequest(Json.toJson(
                        new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
            }
            User user = Application.recieveUserByToken();
            if (user == null) {
                return badRequest(Json.toJson(
                        new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
            }
            Company company = user.getContactByContactId().getCompanyByCompanyId();
            Order order = orderDAO.getOrderById(orderId);
            if (order == null || order.getCustomerByContactId().getCompanyByCompanyId().getId() != company.getId()) {
                return notFound(Json.toJson(new Reply<>(Status.ERROR, MessageManager.getProperty("order.notfound"))));
            }
            String currentStatus = order.getStatusByStatusId().getTitle();
            List<String> statusList = ConfigContainer.getInstance().getStatusHandler().getStatusList(currentStatus);
            entity.Status next = orderDAO.findStatusByTitle(nextStatus);
            if (statusList.contains(nextStatus) && next != null) {
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
                history.setUserComment(StringUtils.isBlank(comment) ? null : comment);
                orderDAO.changeOrderStatus(order, next, history);
                return ok(Json.toJson(
                        new Reply<>(Status.SUCCESS, null)));
            } else {
                logger.error("Can't change status of the order from {} to {}", currentStatus, nextStatus);
                return badRequest(Json.toJson(
                        new Reply<>(Status.ERROR, MessageManager.getProperty("message.status.unchanged"))));
            }

        } catch (NumberFormatException e) {
            logger.error("Error in changeOrderStatus method: {}", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("order.wrong.fields"))));
        } catch (IOException | ParseException e) {
            logger.error("Error in changeOrderStatus method: {}", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
    }

    @Transactional
    @Pattern("order_addition")
    public static Result createOrder() {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        logger.info("Start createOrder with parameters: description - {}, price - {}, processMngId - {}, deliveryMngId - {}, customerId - {}, recipientId - {}",
                values.get("description")[0], values.get("price")[0], values.get("processMngId")[0],
                values.get("deliveryMngId")[0], values.get("customerId")[0], values.get("recipientId")[0]);
        Order order = new Order();
        Reply<String> reply = new Reply<>();
        if (!checkAndSetOrderFields(values, order, reply)) {
            return badRequest(Json.toJson(
                    reply));
        }
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, null)));
    }


    @Transactional
    @Pattern("order_updating")
    @Restrict({@Group("ORDER_MNG"), @Group("SUPERVISOR")})
    public static Result updateOrder(Long id) {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        logger.info("Start updateOrder with parameters: description - {}, price - {}, processMngId - {}, deliveryMngId - {}, customerId - {}, recipientId - {}",
                values.get("description")[0], values.get("price")[0], values.get("processMngId")[0],
                values.get("deliveryMngId")[0], values.get("customerId")[0], values.get("recipientId")[0]);
        OrderDAO orderDAO = new OrderDAO(JPA.em());
        Order order = orderDAO.getOrderById(id);
        Reply<String> reply = new Reply<>();
        if (!checkAndSetOrderFields(values, order, reply)) {
            return badRequest(Json.toJson(
                    reply));
        }
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, null)));
    }

    @Transactional
    private static boolean checkAndSetOrderFields(Map<String, String[]> values, Order order, Reply<String> reply) {
        try {
            String description = StringUtils.isBlank(values.get("description")[0]) ? null : values.get("description")[0];
            Double price = StringUtils.isBlank(values.get("price")[0]) ? null : Double.valueOf(values.get("price")[0]);
            if (price == null || description == null) {
                reply.data = MessageManager.getProperty("order.empty.fields");
                return false;
            }
            Long processMngId = StringUtils.isBlank(values.get("processMngId")[0]) ? null : Long.valueOf(values.get("processMngId")[0]);
            Long deliveryMngId = StringUtils.isBlank(values.get("deliveryMngId")[0]) ? null : Long.valueOf(values.get("deliveryMngId")[0]);
            Long customerId = StringUtils.isBlank(values.get("customerId")[0]) ? null : Long.valueOf(values.get("customerId")[0]);
            Long recipientId = StringUtils.isBlank(values.get("recipientId")[0]) ? null : Long.valueOf(values.get("recipientId")[0]);
            if (processMngId == null || deliveryMngId == null || customerId == null || recipientId == null) {
                reply.data = MessageManager.getProperty("order.empty.fields");
                return false;
            }
            User user = Application.recieveUserByToken();
            if (user == null) {
                reply.data = MessageManager.getProperty("authentification.error");
                return false;
            }
            Company company = user.getContactByContactId().getCompanyByCompanyId();
            EntityManager em = JPA.em();
            OrderDAO orderDAO = new OrderDAO(em);
            ContactDAO contactDAO = new ContactDAO(em);
            UserDAO userDAO = new UserDAO(em);
            Contact customer = contactDAO.findById(customerId);
            Contact recipient = contactDAO.findById(recipientId);
            User processMng = userDAO.findById(processMngId);
            User deliveryMng = userDAO.findById(deliveryMngId);
            if (customer == null || recipient == null || processMng == null || deliveryMng == null
                    || customer.getCompanyByCompanyId().getId() != company.getId()
                    || recipient.getCompanyByCompanyId().getId() != company.getId()
                    || processMng.getContactByContactId().getCompanyByCompanyId().getId() != company.getId()
                    || deliveryMng.getContactByContactId().getCompanyByCompanyId().getId() != company.getId()
                    || !PROCESS_MNG_ROLE_NAME.equals(processMng.getRoleByRoleId().getName())
                    || !DELIVERY_MNG_ROLE_NAME.equals(deliveryMng.getRoleByRoleId().getName())) {
                reply.data = MessageManager.getProperty("order.wrong.fields");
                return false;
            }
            order.setDescription(description);
            order.setTotalPrice(price);
            order.setCustomerByContactId(customer);
            order.setRecipientByContactId(recipient);
            order.setProcessMngByUserId(processMng);
            order.setDeliveryMngByUserId(deliveryMng);
            if (order.getId() == null) {
                entity.Status firstStatus = orderDAO.findStatusByTitle(ConfigContainer.getInstance().getStatusHandler().getFirstStatusTitle());
                if (firstStatus == null) {
                    reply.data = MessageManager.getProperty("order.wrong.fields");
                    return false;
                }
                DateTime now = new DateTime(DateTimeZone.getDefault());
                order.setOrderDate(new Timestamp(now.getMillis()));
                order.setUserByUserId(user);
                order.setStatusByStatusId(firstStatus);
                orderDAO.create(order);
            }
            return true;
        } catch (NumberFormatException e) {
            logger.error("Error in checkAndSetOrderFields method: {}", e);
            reply.data = MessageManager.getProperty("order.wrong.fields");
            return false;
        } catch (IOException | ParseException e) {
            logger.error("Error in checkAndSetOrderFields method: {}", e);
            reply.data = MessageManager.getProperty("message.error");
            return false;
        }
    }

}
