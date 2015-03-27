package entity;

import javax.persistence.*;

/**
 * @Author ValentineS.
 */
@Entity
@Table(name = "phone")
public class Phone {

    private int id;
    private int countryCode;
    private int operatorCode;
    private int basicNumber;
    private PhoneType phoneTypeByPhoneTypeId;
    private String userComment;
    private Contact contactByContactId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "country_code", nullable = false, insertable = true, updatable = true)
    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    @Basic
    @Column(name = "operator_code", nullable = false, insertable = true, updatable = true)
    public int getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(int operatorCode) {
        this.operatorCode = operatorCode;
    }

    @Basic
    @Column(name = "basic_number", nullable = false, insertable = true, updatable = true)
    public int getBasicNumber() {
        return basicNumber;
    }

    public void setBasicNumber(int basicNumber) {
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
