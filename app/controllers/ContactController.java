package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.CompanyDAO;
import dao.ContactDAO;
import entity.Company;
import entity.Contact;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author ValentineS. Created 28.03.2015.
 */

public class ContactController extends BaseController {

    @Transactional
    public static Result getContact(Long id) {

        ContactDAO contactDAO = new ContactDAO(JPA.em());
        Contact contact = contactDAO.findById(id);
        if(contact == null) {
            return notFound(Json.toJson(new Reply()));
        }

        Reply<Contact> reply = new Reply<>(Status.SUCCESS, contact);
        return ok(Json.toJson(reply));
    }

    @Transactional
    public static Result listContacts(Integer pageNumber, Integer pageSize) {

        if(pageNumber == null || pageSize == null || pageNumber <= 0 || pageNumber <= 0) {
            return badRequest(Json.toJson(new Reply()));
        }

        ContactDAO contactDAO = new ContactDAO(JPA.em());
        Long total = contactDAO.numberOfContacts();
        Integer totalPages = Double.valueOf(Math.ceil((double) total / pageSize)).intValue();
        List<Contact> contactList = contactDAO.getContactList(pageNumber, pageSize);

        ObjectNode result = Json.newObject();
        result.put("totalPages", totalPages);
        result.put("list", Json.toJson(contactList));

        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, result)
        ));
    }

    @Transactional
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

        String firstName = values.get("first_name")[0];
        String lastName = values.get("last_name")[0];
        String middleName = values.get("middle_name")[0];
        Date birthDay = Date.valueOf(values.get("birth_date")[0]);
        String email = values.get("email")[0];
        String town = values.get("town")[0];
        String street = values.get("street")[0];

        Company company = null;
        Integer house = null;
        Integer flat = null;
        try {
            house = Integer.valueOf(values.get("house")[0]);
        } catch (ClassCastException cce) {
            throw cce;
        }
        try {
            flat = Integer.valueOf(values.get("flat")[0]);
        } catch (ClassCastException cce) {
            throw cce;
        }
        try {
            company = new CompanyDAO(JPA.em()).findByName(values.get("company")[0]);
        } catch (Exception e) {
            throw e;
        }

        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setMiddleName(middleName);
        contact.setBirthday(birthDay);
        contact.setEmail(email);
        contact.setCompanyByCompanyId(company);
        contact.setTown(town);
        contact.setStreet(street);
        contact.setHouse(house);
        contact.setFlat(flat);
    }

    @Transactional
    public static Result deleteContact(Long id) {

        ContactDAO contactDAO = new ContactDAO(JPA.em());
        contactDAO.delete(id);
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, id)
        ));
    }
}
