package search;

import java.sql.Date;


public class ContactSearchBean extends SearchBean{

	private String firstName;
	private String lastName;
	private String middleName;
	private Date dateMin;
	private Date dateMax;
	private Integer companyId;
	private String town;
	private String street;
	private Integer house;
	private Integer flat;
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public Date getDateMin() {
		return dateMin;
	}
	public void setDateMin(Date dateMin) {
		this.dateMin = dateMin;
	}
	public Date getDateMax() {
		return dateMax;
	}
	public void setDateMax(Date dateMax) {
		this.dateMax = dateMax;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public Integer getHouse() {
		return house;
	}
	public void setHouse(Integer house) {
		this.house = house;
	}
	public Integer getFlat() {
		return flat;
	}
	public void setFlat(Integer flat) {
		this.flat = flat;
	}
	
	
	
}
