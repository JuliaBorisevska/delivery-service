package dto;

import entity.Contact;
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
    public String orderStatus;
    public String date;

    public static OrderDTO getOrder(Order order) {

        OrderDTO dto = new OrderDTO();
        setOrder(dto, order);
        return dto;
    }
    
    public static void setOrder(OrderDTO dto, Order order) {
    	dto.id = order.getId();
        dto.description = order.getDescription();
        dto.price  = order.getTotalPrice();
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
        dto.orderStatus = order.getStatusByStatusId().getTitle();
    }
}
