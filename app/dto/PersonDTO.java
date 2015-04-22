package dto;

import play.Logger;
import play.Logger.ALogger;
import entity.Contact;

public class PersonDTO {
	private static ALogger logger = Logger.of(PersonDTO.class);
	public Long contactId;
	public String firstName;
	public String lastName;
	public String middleName;
	
	
	
	public static PersonDTO getPerson(Contact contact) {
		PersonDTO dto = new PersonDTO();
		setPerson(dto, contact);
		return dto;
    }
	
	public static void setPerson(PersonDTO dto, Contact contact) {
		logger.info("Start setPerson: id - {}, firstName - {}, lastName - {}", contact.getId(), contact.getFirstName(), contact.getLastName());
		dto.contactId = contact.getId();
		dto.firstName = contact.getFirstName();
		dto.lastName = contact.getLastName();
		dto.middleName = contact.getMiddleName();
    }
}
