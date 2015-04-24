package dto;

import entity.OrderHistory;

public class OrderHistoryDTO {
	
	public PersonDTO user;
	public String status;
    public String date;
    public String comment;
    
 public static OrderHistoryDTO getOrderHistory(OrderHistory orderHistory) {
    	
	 OrderHistoryDTO dto = new OrderHistoryDTO();
    	dto.user = PersonDTO.getPerson(orderHistory.getUserByUserId().getContactByContactId());
    	StringBuilder stringDate = new StringBuilder();
        stringDate
        		.append(orderHistory.getModificationDate().toLocalDateTime().getDayOfMonth())
        		.append(" ")
        		.append(orderHistory.getModificationDate().toLocalDateTime().getMonth().name())
        		.append(" ")
        		.append(orderHistory.getModificationDate().toLocalDateTime().getYear())
        		.append(" ")
                .append(orderHistory.getModificationDate().toLocalDateTime().getHour())
                .append(":")
                .append(orderHistory.getModificationDate().toLocalDateTime().getMinute())
                .append(":")
                .append(orderHistory.getModificationDate().toLocalDateTime().getSecond());

        dto.date = stringDate.toString();
        dto.comment = orderHistory.getUserComment();
        dto.status = orderHistory.getStatusByStatusId().getTitle();
        return dto;

    }

}
