package dto;

import com.fasterxml.jackson.databind.JsonNode;
import entity.User;

public class UserDTO {
	private Integer id;
	private String firstName;
	private String lastName;
	private String middleName;
	private String companyTitle;
	private String roleTitle;
	private JsonNode menu;
	private String login;
	
	public static UserDTO getUser(User user) {
		UserDTO dto = new UserDTO();
		dto.id = user.getId();
        dto.firstName = user.getContactByContactId().getFirstName();
		dto.lastName = user.getContactByContactId().getLastName();
		dto.middleName = user.getContactByContactId().getMiddleName();
		dto.companyTitle = user.getContactByContactId().getCompanyByCompanyId().getTitle();
		dto.roleTitle = user.getRoleByRoleId().getTitle();
		dto.login = user.getLogin();
        return dto;
    }

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

	public String getRoleTitle() {
		return roleTitle;
	}

	public void setRoleTitle(String roleTitle) {
		this.roleTitle = roleTitle;
	}

	public JsonNode getMenu() {
		return menu;
	}

	public void setMenu(JsonNode menu) {
		this.menu = menu;
	}

	public String getCompanyTitle() {
		return companyTitle;
	}

	public void setCompanyTitle(String companyTitle) {
		this.companyTitle = companyTitle;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	
	

}
