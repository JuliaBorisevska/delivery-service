package entities;

import play.data.validation.Constraints;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by antonkw on 22.03.2015.
 */
@Entity
@Table(name = "order_history", schema = "", catalog = "delivery_service")
public class OrderHistoryEntity {
    @GeneratedValue
    private int id;
    private Timestamp modificationDate;
    private String userComment;
    @Constraints.Required
    private int orderId;
    @Constraints.Required
    private int statusId;
    @Constraints.Required
    private int userId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "modification_date")
    public Timestamp getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Timestamp modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Basic
    @Column(name = "user_comment")
    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    @Basic
    @Column(name = "order_id")
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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
}
