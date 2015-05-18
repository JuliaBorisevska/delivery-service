package search;

import java.sql.Date;
import java.util.List;


public class OrderSearchBean extends SearchBean{
	private String customer;
	private String recipient;
	private List<String> statusTitleList;
	private Double priceMin;
	private Double priceMax;
	private Date dateMin;
	private Date dateMax;
	private Integer companyId;
	
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	public List<String> getStatusTitleList() {
		return statusTitleList;
	}
	public void setStatusTitleList(List<String> statusTitleList) {
		this.statusTitleList = statusTitleList;
	}
	public Double getPriceMin() {
		return priceMin;
	}
	public void setPriceMin(Double priceMin) {
		this.priceMin = priceMin;
	}
	public Double getPriceMax() {
		return priceMax;
	}
	public void setPriceMax(Double priceMax) {
		this.priceMax = priceMax;
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
	
}
