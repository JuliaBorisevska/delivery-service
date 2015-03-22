package entity;


import play.data.validation.Constraints;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by antonkw on 22.03.2015.
 */
@Entity
@Table(name = "order", schema = "", catalog = "delivery_service")
public class OrderEntity {
    @GeneratedValue
    private int id;
    private Timestamp orderDate;
    private String description;
    private double totalPrice;
    @Constraints.Required
    private int statusId;
    @Constraints.Required
    private int userId;
    @Constraints.Required
    private int customerContactId;
    @Constraints.Required
    private int recipientContactId;

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

    @Basic
    @Column(name = "status_id")
    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @Basic
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "customer_contact_id")
    public int getCustomerContactId() {
        return customerContactId;
    }

    public void setCustomerContactId(int customerContactId) {
        this.customerContactId = customerContactId;
    }

    @Basic
    @Column(name = "recipient_contact_id")
    public int getRecipientContactId() {
        return recipientContactId;
    }

    public void setRecipientContactId(int recipientContactId) {
        this.recipientContactId = recipientContactId;
    }
}
