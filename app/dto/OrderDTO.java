package dto;


import entity.Order;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


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
        StringBuilder stringDate = new StringBuilder();
        stringDate
        		.append(order.getOrderDate().toLocalDateTime().getDayOfMonth())
        		.append(" ")
        		.append(order.getOrderDate().toLocalDateTime().getMonth().name())
        		.append(" ")
        		.append(order.getOrderDate().toLocalDateTime().getYear())
        		.append(" ")
                .append(order.getOrderDate().toLocalDateTime().getHour())
                .append(":")
                .append(order.getOrderDate().toLocalDateTime().getMinute())
                .append(":")
                .append(order.getOrderDate().toLocalDateTime().getSecond());

        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.fromDateFields(order.getOrderDate());
        dto.date = dateTime.toString(fmt);
        dto.orderStatus = order.getStatusByStatusId().getTitle();
    }
}
