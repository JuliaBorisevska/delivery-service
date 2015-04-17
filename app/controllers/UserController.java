package controllers;

import be.objectify.deadbolt.java.actions.Pattern;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.UserDAO;
import dto.UserDTO;
import entity.Company;
import entity.Contact;
import entity.User;
import play.Logger;
import play.Logger.ALogger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import resource.MessageManager;

import java.util.ArrayList;
import java.util.List;



public class UserController extends BaseController {
	private static ALogger logger = Logger.of(UserController.class);
	
    @Transactional
    @Pattern("lst")
    public static Result listUsers(Integer pageNumber, Integer pageSize) {
        logger.info("Start listUsers method");
    	if(pageNumber == null || pageSize == null || pageNumber <= 0 || pageNumber <= 0) {
                return badRequest(Json.toJson(
                		new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
    	User user = Application.recieveUserByToken();
        if (user == null) {
			return badRequest(Json.toJson(
		            new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
		}
        Company company = user.getContactByContactId().getCompanyByCompanyId();
        UserDAO dao = new UserDAO(JPA.em());
        Long total = dao.total(company);
        Integer totalPages = Double.valueOf(Math.ceil((double) total / pageSize)).intValue();
        List<User> userList = dao.list(pageNumber, pageSize, company);
        List<UserDTO> dtoList = new ArrayList<>();
        for(User u : userList) {
            dtoList.add(UserDTO.getUser(u));
        }
        ObjectNode result = Json.newObject();
        result.put("totalPages", totalPages);
        result.put("list", Json.toJson(dtoList));

        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, result))
        );
    }
	
    @Transactional
    @Pattern("lst")
    public static Result deleteUsers(String ids) {
    	try{
    		logger.info("Start deleteUsers method with ids: {}", ids);
    		java.util.regex.Pattern separator;
    		separator = java.util.regex.Pattern.compile(",");
    		String [] idArray = separator.split(ids);
    		User currentUser = Application.recieveUserByToken();
            if (currentUser == null) {
    			return badRequest(Json.toJson(
    		            new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
    		}
            Company company = currentUser.getContactByContactId().getCompanyByCompanyId();
    		UserDAO dao = new UserDAO(JPA.em());
    		for (String id : idArray){
    			User user = new User();
    			Contact contact = new Contact();
    			contact.setCompanyByCompanyId(company);
    			user.setContactByContactId(contact);
    			user.setId(Long.parseLong(id));
        		dao.delete(user);
    		}
    		return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, ids)
    			));
    	}catch(Exception e){
    		logger.error("Error in deleteUsers method: {}", e);
    		return badRequest(Json.toJson(
            		new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
    	}
    }
}