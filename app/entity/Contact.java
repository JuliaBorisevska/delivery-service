package entity;

import javax.persistence.*;

import org.elasticsearch.client.Client;

import play.Logger;
import play.Logger.ALogger;
import search.ClientProvider;
import search.ContactSearchService;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "contact")
public class Contact {
	private static ALogger logger = Logger.of(Contact.class);
	
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private Date birthday;
    private String email;
    private Company companyByCompanyId;
    private String town;
    private String street;
    private Integer house;
    private Integer flat;
    private List<Phone> phones;

    @PostPersist
    @PostUpdate
    private void addToElasticSearch() {
    	ContactSearchService service = new ContactSearchService();
    	Client client = ClientProvider.instance().getClient();
    	client.prepareIndex(ContactSearchService.INDEX_NAME, ContactSearchService.TYPE_NAME, String.valueOf(this.id))
        .setSource(service.putJsonDocument(this)).execute().actionGet();
    	logger.info("Contact with id {} was added/updated to/in elasticsearch index delivery", this.id);
    }
    
    @PostRemove
    private void deleteFromElasticSearch() {
    	Client client = ClientProvider.instance().getClient();
    	client.prepareDelete(ContactSearchService.INDEX_NAME, ContactSearchService.TYPE_NAME, String.valueOf(this.id))
    	.execute().actionGet();
    	logger.info("Contact with id {} was deleted from elasticsearch index delivery", this.id);
    }
       
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "first_name", nullable = false, insertable = true, updatable = true)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = false, insertable = true, updatable = true)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "middle_name", nullable = true, insertable = true, updatable = true)
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middle_name) {
        this.middleName = middle_name;
    }

    @Basic
    @Column(name = "birth_date", nullable = true, insertable = true, updatable = true)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthDay) {
        this.birthday = birthDay;
    }

    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    public Company getCompanyByCompanyId() {
        return companyByCompanyId;
    }

    public void setCompanyByCompanyId(Company companyId) {
        this.companyByCompanyId = companyId;
    }

    @Basic
    @Column(name = "town", nullable = true, insertable = true, updatable = true)
    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Basic
    @Column(name = "street", nullable = true, insertable = true, updatable = true)
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Basic
    @Column(name = "house", nullable = true, insertable = true, updatable = true)
    public Integer getHouse() {
        return house;
    }

    public void setHouse(Integer house) {
        this.house = house;
    }

    @Basic
    @Column(name = "flat", nullable = true, insertable = true, updatable = true)
    public Integer getFlat() {
        return flat;
    }

    public void setFlat(Integer flat) {
        this.flat = flat;
    }

    @OneToMany(mappedBy = "contactByContactId")
    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }
}
