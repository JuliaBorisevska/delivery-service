package dto;

import entity.Order;
import entity.Status;

import java.util.ArrayList;


/**
 * Created by hanna.kubarka on 30.03.2015.
 */
public class OrderDTO {

    public Long id;
    public String description;
    public Double price;
    //public String customer;
    //public String recipient;
    //public String user;
    public String orderStatus;
    public String date;
    //public String nextStatus;

    public static OrderDTO getOrder(Order order) {

        OrderDTO dto = new OrderDTO();
        dto.id = order.getId();
        dto.description = order.getDescription();
        dto.price  = order.getTotalPrice();
        //dto.customer = order.getCustomerByContactId().getContactByContactId().getLastName();
        StringBuilder stringdate = new StringBuilder();
        stringdate
                .append(order.getOrderDate().toLocalDateTime().getHour())
                .append(":")
                .append(order.getOrderDate().toLocalDateTime().getMinute())
                .append(":")
                .append(order.getOrderDate().toLocalDateTime().getSecond())
                .append(" ")
                .append(order.getOrderDate().toLocalDateTime().getMonth().name())
                .append(" ")
                .append(order.getOrderDate().toLocalDateTime().getYear());

        dto.date = stringdate.toString();


        //dto.user = order.getUserByUserId().getContactByContactId().getLastName();
        //dto.recipient = order.getRecipientByContactId().getContactByContactId().getLastName();
        dto.orderStatus = order.getStatusByStatusId().getTitle();
        return dto;

    }
}
