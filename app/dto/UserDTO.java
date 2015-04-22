package dto;

import com.fasterxml.jackson.databind.JsonNode;
import entity.User;

public class UserDTO extends PersonDTO{
	
	private Long id;
	private String companyTitle;
	private String roleTitle;
	private JsonNode menu;
	private String login;
	
	public static UserDTO getUser(User user) {
		UserDTO dto = new UserDTO();
		PersonDTO.setPerson(dto, user.getContactByContactId());
		dto.id = user.getId();
        //dto.firstName = user.getContactByContactId().getFirstName();
		//dto.lastName = user.getContactByContactId().getLastName();
		//dto.middleName = user.getContactByContactId().getMiddleName();
		dto.companyTitle = user.getContactByContactId().getCompanyByCompanyId().getTitle();
		dto.login = user.getIdentifier();
		dto.roleTitle = user.getRoleByRoleId().getName();
		return dto;
    }

	

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	

}
