package search;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;

import entity.Contact;


public class SearchContactService {

	public static void searchContacts(){
        Client client = ClientProvider.instance().getClient();
       
	}
	
	public static Map<String, Object> putContactJsonDocument(Contact contact){

		Map<String, Object> jsonDocument = new HashMap<String, Object>();

		jsonDocument.put("id", contact.getId());
		jsonDocument.put("first_name", contact.getFirstName());
		jsonDocument.put("last_name", contact.getLastName());
		jsonDocument.put("middle_name", contact.getMiddleName());
		jsonDocument.put("birth_date", contact.getBirthday());
		jsonDocument.put("email", contact.getEmail());
		jsonDocument.put("company_id", contact.getCompanyByCompanyId().getId());
		jsonDocument.put("town", contact.getTown());
		jsonDocument.put("street", contact.getStreet());
		jsonDocument.put("house", contact.getHouse());
		jsonDocument.put("flat", contact.getFlat());

		return jsonDocument;
	}

}