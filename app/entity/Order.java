package entity;


import play.data.validation.Constraints;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by antonkw on 22.03.2015.
 */
@Entity
@Table(name = "order", schema = "", catalog = "delivery_service")
public class Order {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp orderDate;
    @Constraints.Required
    private String description;
    @Constraints.Required
    private Double totalPrice;
    @Constraints.Required
    private Status statusByStatusId;
    @Constraints.Required
    private User userByUserId;
    @Constraints.Required
    private User processMngByUserId;
    @Constraints.Required
    private User deliveryMngByUserId;
    @Constraints.Required
    private Contact customerByContactId;
    @Constraints.Required
    private Contact recipientByContactId;
    private List<OrderHistory> orderHistory;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "order_date")
    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "total_price")
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "process_mng_id", referencedColumnName = "id", nullable = false)
    public User getProcessMngByUserId() {
		return processMngByUserId;
	}

	public void setProcessMngByUserId(User processMngByUserId) {
		this.processMngByUserId = processMngByUserId;
	}

	@ManyToOne
    @JoinColumn(name = "delivery_mng_id", referencedColumnName = "id", nullable = false)
	public User getDeliveryMngByUserId() {
		return deliveryMngByUserId;
	}

	public void setDeliveryMngByUserId(User deliveryMngByUserId) {
		this.deliveryMngByUserId = deliveryMngByUserId;
	}

	@ManyToOne
    @JoinColumn(name = "customer_contact_id", referencedColumnName = "id", nullable = false)
    public Contact getCustomerByContactId() {
        return customerByContactId;
    }

    public void setCustomerByContactId(Contact customerByContactId) {
        this.customerByContactId = customerByContactId;
    }

    @ManyToOne
    @JoinColumn(name = "recipient_contact_id", referencedColumnName = "id", nullable = false)
    public Contact getRecipientByContactId() {
        return recipientByContactId;
    }

    public void setRecipientByContactId(Contact recipientByContactId) {
        this.recipientByContactId = recipientByContactId;
    }

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    public Status getStatusByStatusId() {
        return statusByStatusId;
    }

    public void setStatusByStatusId(Status statusByStatusId) {
        this.statusByStatusId = statusByStatusId;
    }

    @OneToMany(mappedBy = "orderByOrderId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderHistory> orderHistory) {
        this.orderHistory = orderHistory;
    }
}
