package dto;

import com.fasterxml.jackson.databind.JsonNode;
import entity.User;

public class UserDTO {
	private String firstName;
	private String lastName;
	private String middleName;
	private int companyId;
	private String roleTitle;
	private JsonNode menu;
	
	public static UserDTO getUser(User user) {
		UserDTO dto = new UserDTO();
        dto.firstName = user.getContactByContactId().getFirstName();
		dto.lastName = user.getContactByContactId().getLastName();
		dto.middleName = user.getContactByContactId().getMiddleName();
		dto.companyId = user.getContactByContactId().getCompanyByCompanyId().getId();
		dto.roleTitle = user.getRoleByRoleId().getTitle();
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

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
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
	
	
	

}
