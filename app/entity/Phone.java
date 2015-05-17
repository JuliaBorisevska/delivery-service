package entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Author ValentineS.
 */
@Entity
@Table(name = "phone")
public class Phone {

    private Integer id;
    private Integer countryCode;
    private Integer operatorCode;
    private Integer basicNumber;
    private PhoneType phoneTypeByPhoneTypeId;
    private String userComment;
    @JsonIgnore
    private Contact contactByContactId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "country_code", nullable = false, insertable = true, updatable = true)
    public Integer getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }

    @Basic
    @Column(name = "operator_code", nullable = false, insertable = true, updatable = true)
    public Integer getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(Integer operatorCode) {
        this.operatorCode = operatorCode;
    }

    @Basic
    @Column(name = "basic_number", nullable = false, insertable = true, updatable = true)
    public Integer getBasicNumber() {
        return basicNumber;
    }

    public void setBasicNumber(Integer basicNumber) {
        this.basicNumber = basicNumber;
    }

    @ManyToOne
    @JoinColumn(name = "phone_type_id", referencedColumnName = "id", nullable = false)
    public PhoneType getPhoneTypeByPhoneTypeId() {
        return phoneTypeByPhoneTypeId;
    }

    public void setPhoneTypeByPhoneTypeId(PhoneType phoneTypeByPhoneTypeId) {
        this.phoneTypeByPhoneTypeId = phoneTypeByPhoneTypeId;
    }

    @Basic
    @Column(name = "user_comment", nullable = true, insertable = true, updatable = true)
    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "contact_id", referencedColumnName = "id", nullable = false)
    public Contact getContactByContactId() {
        return contactByContactId;
    }

    public void setContactByContactId(Contact contactByContactId) {
        this.contactByContactId = contactByContactId;
    }

}
