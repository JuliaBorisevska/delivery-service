package dto;

import entity.Order;

public class OrderDetailsDTO extends OrderDTO{

    public PersonDTO customer;
    public PersonDTO recipient;
    public UserDTO orderMng;
    public UserDTO processMng;
    public UserDTO deliveryMng;

    public static OrderDetailsDTO getOrderDetails(Order order) {
    	
    	OrderDetailsDTO dto = new OrderDetailsDTO();
    	OrderDTO.setOrder(dto, order);
    	dto.customer = PersonDTO.getPerson(order.getCustomerByContactId());
    	dto.recipient = PersonDTO.getPerson(order.getRecipientByContactId());
    	dto.orderMng = UserDTO.getUser(order.getUserByUserId());
    	dto.processMng = UserDTO.getUser(order.getProcessMngByUserId());
    	dto.deliveryMng = UserDTO.getUser(order.getDeliveryMngByUserId());
        return dto;

    }
}
