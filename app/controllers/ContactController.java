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
import search.ContactSearchBean;
import search.ContactSearchService;

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
        if (contact == null) {
            return notFound(Json.toJson(new Reply()));
        }
        Reply<Contact> reply = new Reply<>(Status.SUCCESS, contact);
        return ok(Json.toJson(reply));
    }

    @Transactional
    @Pattern("contact_list")
    public static Result listContacts(Integer pageNumber, Integer pageSize) {
        logger.info("get list of contacts, page: {}, size: {}", pageNumber, pageSize);

        if (pageNumber == null || pageSize == null || pageNumber <= 0 || pageNumber <= 0) {
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
    @Pattern("contact_search")
    public static Result findContacts() {

        //ContactDAO contactDAO = new ContactDAO(JPA.em());
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        try {
        	//ContactSearchService service = new ContactSearchService();
        	//ContactSearchBean searchBean = new ContactSearchBean();
        	//service.search(searchBean);
            
        	//setContactFields(contact, values);

        } catch (Exception e) {
            logger.error("Exception in findContacts method: {} ", e);
        }
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, null)
        ));
    }

    @Transactional
    @Pattern("contact_addition")
    public static Result createContact() {
        Contact contact = new Contact();
        User user = Application.recieveUserByToken();
        if (user == null) {
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
        }
        Company company = user.getContactByContactId().getCompanyByCompanyId();
        ContactDAO contactDAO = new ContactDAO(JPA.em());
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        try {
            setContactFields(contact, values);
            contact.setCompanyByCompanyId(company);
        } catch (IllegalArgumentException | ClassCastException e) {
            logger.error("exception during set contact fields", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
        contactDAO.create(contact);
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, contact)
        ));
    }

    @Transactional
    @Pattern("contact_updating")
    public static Result updateContact(Long id) {

        ContactDAO contactDAO = new ContactDAO(JPA.em());
        Contact contact = contactDAO.findById(id);
        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        try {
            setContactFields(contact, values);

        } catch (IllegalArgumentException | ClassCastException e) {
            logger.error("exception during set contact fields", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
        contactDAO.update(contact);
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, contact)
        ));
    }

    private static void setContactFields(Contact contact, Map<String, String[]> values) throws IllegalArgumentException, ClassCastException {
        if (values.containsKey("firstName") || StringUtils.isNotEmpty(values.get("firstName")[0])) {
            contact.setFirstName(values.get("firstName")[0]);
        } else {
            throw new IllegalArgumentException("first name is missing or empty");
        }
        if (values.containsKey("lastName") || StringUtils.isNotEmpty(values.get("lastName")[0])) {
            contact.setLastName(values.get("lastName")[0]);
        } else {
            throw new IllegalArgumentException("last name is missing or empty");
        }
        if (values.containsKey("middleName") || StringUtils.isNotEmpty(values.get("middleName")[0])) {
            contact.setMiddleName(values.get("middleName")[0]);
        } else {
            throw new IllegalArgumentException("middle name is missing or empty");
        }
        if (values.containsKey("birthday")) {
            contact.setBirthday(Date.valueOf(values.get("birthday")[0]));
        }

        if (values.containsKey("email") && StringUtils.isNotEmpty(values.get("email")[0])) {
            contact.setEmail(values.get("email")[0]);
        }
        if (values.containsKey("town") && StringUtils.isNotEmpty(values.get("town")[0])) {
            contact.setTown(values.get("town")[0]);
        }
        if (values.containsKey("street") && StringUtils.isNotEmpty(values.get("street")[0])) {
            contact.setStreet(values.get("street")[0]);
        }
        Integer house = null;
        Integer flat = null;

        try {
            if (StringUtils.isNotEmpty(values.get("house")[0])) {
                house = Integer.valueOf(values.get("house")[0]);
                contact.setHouse(house);
            }

            if (StringUtils.isNotEmpty(values.get("flat")[0])) {
                flat = Integer.valueOf(values.get("flat")[0]);
                contact.setFlat(flat);
            }
        } catch (ClassCastException cce) {
            throw cce;
        }

    }

    @Transactional
    @Pattern("contact_deleting")
    public static Result deleteContacts(String ids) {
        try {
            logger.info("Start deleteContacts method with ids: {}", ids);
            User user = Application.recieveUserByToken();
            if (user == null) {
                return badRequest(Json.toJson(
                        new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
            }
            Company company = user.getContactByContactId().getCompanyByCompanyId();
            java.util.regex.Pattern separator;
            separator = java.util.regex.Pattern.compile(",");
            String[] idArray = separator.split(ids);
            ContactDAO contactDAO = new ContactDAO(JPA.em());
            for (String id : idArray) {
                Contact contact = new Contact();
                contact.setCompanyByCompanyId(company);
                contact.setId(Long.parseLong(id));
                contactDAO.delete(contact);
            }
            return ok(Json.toJson(
                    new Reply<>(Status.SUCCESS, ids)
            ));
        } catch (Exception e) {
            logger.error("Error in deleteContacts method: {}", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
    }
}
