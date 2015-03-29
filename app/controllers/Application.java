package controllers;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.EntityManager;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.BaseController.Reply;
import controllers.BaseController.Status;
import dao.UserDAO;
import dto.UserDTO;
import entity.Role;
import entity.User;
import play.*;
import play.Logger.ALogger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import dto.UserDTO;
import play.libs.Json;
import play.mvc.Result;
import resource.MessageManager;
import views.html.main;

import java.util.Map;

public class Application extends BaseController {
	private static ALogger logger = Logger.of(Application.class);
	private static final String FILE_CONFIG_NAME = "conf/privileges.json";
	
    public static Result index() {
    	logger.info("Start index method");
    	return ok(main.render());
    }
    
    public static Result getSectionsFromJson() {
    	logger.info("Start getSectionsFromJson method");
    	try{
    		ObjectMapper mapper = new ObjectMapper();
    		JsonNode rootNode = mapper.readTree(new File(FILE_CONFIG_NAME));
    		JsonNode sectionNode = rootNode.path("sections");
    		logger.info("Section node: {}", sectionNode.toString());
    		return ok(Json.toJson(
                    new Reply<>(Status.SUCCESS, sectionNode)));
    	}catch(IOException e){
    		logger.error("Exception in index method ", e);
    		return badRequest(Json.toJson(
                            new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
    	}
    }

    @Transactional
    public static Result login() {
    	try{
    		final Map<String, String[]> values = request().body().asFormUrlEncoded();
    		logger.debug("Start login method with parameters: {}", values);
    		String login = values.get("user")[0];
    		String password = values.get("password")[0];
    		EntityManager em = JPA.em();
    		UserDAO dao = new UserDAO(em);
    		User user = dao.findByLogin(login);
    		if(user == null) {
    		    return badRequest(Json.toJson(
    		            new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
    		}
    		if(BCrypt.checkpw(password, user.getPassword())) {
    			UserDTO userDTO = UserDTO.getUser(user);
    			ObjectMapper mapper = new ObjectMapper();
    			JsonNode rootNode = mapper.readTree(new File(FILE_CONFIG_NAME));
    			Iterator<JsonNode> roleNodes = rootNode.path("roles").elements();
    			while (roleNodes.hasNext()){
    				JsonNode role = roleNodes.next();
    				if (role.path("title").asText().equals(userDTO.getRoleTitle())){
    					userDTO.setMenu(role.path("menu"));
    					break;
    				}
    			}
    			if (userDTO.getMenu()==null){
    				//userDTO.setMenu(rootNode.path("default"));
    				return badRequest(Json.toJson(
                            new Reply<>(Status.ERROR, MessageManager.getProperty("role.error"))));
    			}
    			return ok(Json.toJson(
                    new Reply<>(Status.SUCCESS, userDTO)));
    		}else {
                return badRequest(Json.toJson(
                        new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
            }
    	}catch(IOException e){
    		logger.error("Exception in index method ", e);
    		return badRequest(Json.toJson(
                            new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
    	}     
    }

    public static Result logout() {
 
        return ok();
    }
}
