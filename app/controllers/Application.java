package controllers;

import be.objectify.deadbolt.java.actions.Unrestricted;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.UserDAO;
import dto.UserDTO;
import entity.User;

import org.mindrot.jbcrypt.BCrypt;

import play.Logger;
import play.Logger.ALogger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Crypto;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import resource.MessageManager;
import views.html.main;

import javax.persistence.EntityManager;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@Unrestricted
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
    public static Result getCurrentUser() {
    	try{
    	logger.info("Start getCurrentUser method");
    	User user = null;
        String token = request().cookie("token").value();  //проверка на null и сообщение, что пользователь не авторизован
        UserDAO userDAO = new UserDAO(JPA.em());
        user = userDAO.findByToken(token);
        if (user == null) {
			return badRequest(Json.toJson(
		            new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
		}
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
			return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("role.error"))));
		}
		return ok(Json.toJson(
            new Reply<>(Status.SUCCESS, userDTO)));
    	}catch(IOException e){
    		logger.error("Exception in getCurrentUser method ", e);
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
			if (user == null) {
				return badRequest(Json.toJson(
    		            new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
    		}
    		if(BCrypt.checkpw(password, user.getPassword())) {
    			UserDTO userDTO = UserDTO.getUser(user);
				String token = Crypto.generateToken();
				logger.info("token {} have been generated", token);
				response().setCookie("token", token, 5000);
				user.setToken(token);
				dao.update(user);
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
    		logger.error("Exception in login method ", e);
    		return badRequest(Json.toJson(
                            new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
    	}     
    }

    public static Result logout() {
 
        return ok();
    }
}
