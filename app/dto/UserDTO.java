package dto;

import entity.Role;
import entity.User;

public class UserDTO {
	private String firstName;
	private String lastName;
	private String middleName;
	private int companyId;
	private String roleTitle;
	
	public static UserDTO getUser(User user) {
		UserDTO dto = new UserDTO();
        //dto.firstName = user.getContactByContactId().;
		//dto.lastName = user.getContactByContactId().;
		//dto.middleName = user.getContactByContactId().;
		//dto.companyId = user.getContactByContactId().;
		//dto.role = user.getRoleByRoleId().;
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

	public String getRole() {
		return roleTitle;
	}

	public void setRole(String role) {
		this.roleTitle = role;
	}
	
	
	

}
