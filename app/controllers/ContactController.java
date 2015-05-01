package controllers;

import be.objectify.deadbolt.java.actions.Pattern;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.ContactDAO;
import entity.Company;
import entity.Contact;
import entity.User;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.Logger.ALogger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import resource.MessageManager;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author ValentineS. Created 28.03.2015.
 */
//@Restrict(@Group("!group_name"))
// @Pattern("!permission_name")
public class ContactController extends BaseController {
	private static ALogger logger = Logger.of(ContactController.class);

    @Transactional
    @Pattern("contact_selection")
    public static Result getContact(Long id) {
        logger.info("get contact with id: {}", id);
        ContactDAO contactDAO = new ContactDAO(JPA.em());
        Contact contact = contactDAO.findById(id);
        if(contact == null) {
            return notFound(Json.toJson(new Reply()));
        }
        Reply<Contact> reply = new Reply<>(Status.SUCCESS, contact);
        return ok(Json.toJson(reply));
    }

    @Transactional
    @Pattern("contact_list")
    public static Result listContacts(Integer pageNumber, Integer pageSize) {
        logger.info("get list of contacts, page: {}, size: {}", pageNumber, pageSize);

        if(pageNumber == null || pageSize == null || pageNumber <= 0 || pageNumber <= 0) {
            return badRequest(Json.toJson(new Reply()));
        }

        User user = Application.recieveUserByToken();
        if (user == null) {
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
        }

        ContactDAO contactDAO = new ContactDAO(JPA.em());
        Long total = contactDAO.numberOfContacts(user.getContactByContactId().getCompanyByCompanyId());
        Integer totalPages = Double.valueOf(Math.ceil((double) total / pageSize)).intValue();
        List<Contact> contactList = contactDAO.getContactList(pageNumber, pageSize, user.getContactByContactId().getCompanyByCompanyId());

        ObjectNode result = Json.newObject();
        result.put("totalPages", totalPages);
        result.put("list", Json.toJson(contactList));

        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, result)
        ));
    }

    @Transactional
    @Pattern("contact_addition")
    public static Result createContact() {
        Contact contact = new Contact();
        ContactDAO contactDAO = new ContactDAO(JPA.em());
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        try {
            setContactFields(contact, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        contactDAO.create(contact);

        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, contact)
        ));
    }

    @Transactional
    @Pattern("contact_addition")
    public static Result updateContact(Long id) {

        ContactDAO contactDAO = new ContactDAO(JPA.em());
        Contact contact = contactDAO.findById(id);
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        try {
            setContactFields(contact, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        contactDAO.update(contact);

        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, contact)
        ));
    }

    private static void setContactFields(Contact contact, Map<String, String[]> values) {

        String firstName = values.get("firstName")[0];
        String lastName = values.get("lastName")[0];
        String middleName = values.get("middleName")[0];
        Date birthDay = Date.valueOf(values.get("birthday")[0]);

        String email = null;
        String town =  null;
        String street = null;
        Integer house = null;
        Integer flat = null;

        if (StringUtils.isNotEmpty(values.get("email")[0])) {
            email = values.get("email")[0];
        }
        if (StringUtils.isNotEmpty(values.get("town")[0])) {
            town = values.get("town")[0];
        }

        if (StringUtils.isNotEmpty(values.get("street")[0])) {
            street = values.get("street")[0];
        }

        try {
            if (StringUtils.isNotEmpty(values.get("house")[0])) {
                house = Integer.valueOf(values.get("house")[0]);
            }
        } catch (ClassCastException cce) {
            throw cce;
        }
        try {
            if (StringUtils.isNotEmpty(values.get("flat")[0])) {
                flat = Integer.valueOf(values.get("flat")[0]);
            }
        } catch (ClassCastException cce) {
            throw cce;
        }

        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setMiddleName(middleName);
        contact.setBirthday(birthDay);
        contact.setEmail(email);
        contact.setTown(town);
        contact.setStreet(street);
        contact.setHouse(house);
        contact.setFlat(flat);
    }

    @Transactional
    @Pattern("contact_deleting")
    public static Result deleteContacts(String ids) {
    	try{
    		logger.info("Start deleteContacts method with ids: {}", ids);
    		User user = Application.recieveUserByToken();
            if (user == null) {
    			return badRequest(Json.toJson(
    		            new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
    		}
            Company company = user.getContactByContactId().getCompanyByCompanyId();
            java.util.regex.Pattern separator;
    		separator = java.util.regex.Pattern.compile(",");
    		String [] idArray = separator.split(ids);
    		ContactDAO contactDAO = new ContactDAO(JPA.em());
    		for (String id : idArray){
    			Contact contact = new Contact();
    			contact.setCompanyByCompanyId(company);
    			contact.setId(Long.parseLong(id));
    			contactDAO.delete(contact);
    		}
    		return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, ids)
    			));
    	}catch(Exception e){
    		logger.error("Error in deleteContacts method: {}", e);
    		return badRequest(Json.toJson(
            		new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
    	}
    }
}
