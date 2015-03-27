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

    @GeneratedValue
    private Integer id;
    private Timestamp orderDate;
    private String description;
    private Double totalPrice;
    @Constraints.Required
    private Status statusByStatusId;
    @Constraints.Required
    private User userByUserId;
    @Constraints.Required
    private User customerByContactId;
    @Constraints.Required
    private User recipientByContactId;
    private List<OrderHistory> orderHistory;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    @JoinColumn(name = "customer_contact_id", referencedColumnName = "id", nullable = false)
    public User getCustomerByContactId() {
        return customerByContactId;
    }

    public void setCustomerByContactId(User customerByContactId) {
        this.customerByContactId = customerByContactId;
    }

    @ManyToOne
    @JoinColumn(name = "recipient_contact_id", referencedColumnName = "id", nullable = false)
    public User getRecipientByContactId() {
        return recipientByContactId;
    }

    public void setRecipientByContactId(User recipientByContactId) {
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

    @OneToMany(mappedBy = "orderByOrderId")
    public List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderHistory> orderHistory) {
        this.orderHistory = orderHistory;
    }
}
