package controllers;

import be.objectify.deadbolt.java.actions.Unrestricted;
import dao.UserDAO;
import dto.UserDTO;
import entity.User;
import handler.ConfigContainer;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.Logger.ALogger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Crypto;
import play.libs.Json;
import play.mvc.Result;
import resource.MessageManager;
import views.html.main;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@Unrestricted
public class Application extends BaseController {
    private static ALogger logger = Logger.of(Application.class);
    private static final String FILE_CONFIG_NAME = "conf/privileges.json";

    public static Result index() {
        logger.info("Start index method");
        return ok(main.render());
    }
    
    @Transactional
    public static User recieveUserByToken(){
    	logger.info("Start recieveUserByToken method");
    	User user = null;
    	if (request().cookie("token")!=null){
    		String token = request().cookie("token").value();
    		UserDAO userDAO = new UserDAO(JPA.em());
            user = userDAO.findByToken(token);
    	}
    	return user;
    }
    
    public static Result getSectionsFromJson() {
        logger.info("Start getSectionsFromJson method");
        try {
            return ok(Json.toJson(
                    new Reply<>(Status.SUCCESS, ConfigContainer.getInstance().getSectionHandler().getSectionNode())));
        } catch (IOException | ParseException e) {
            logger.error("Exception in index method ", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
    }

    @Transactional
    public static Result getCurrentUser() {
        try {
            logger.info("Start getCurrentUser method");
            User user = recieveUserByToken();
            if (user == null) {
    			return badRequest(Json.toJson(
    		            new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
    		}
            UserDTO userDTO = UserDTO.getUser(user);
            try {
                if (ConfigContainer.getInstance().getPermissionHandler().hasRole(userDTO.getRoleTitle())) {
                    userDTO.setMenu(ConfigContainer.getInstance().getPermissionHandler().getJsonPermissions(userDTO.getRoleTitle()));
                } else {
                    throw new ParseException("role is not exist in configuration!", 0);
                }
            } catch (ParseException e) {
                logger.error("exception during setting menu for role {}", userDTO.getRoleTitle(), e);
                return badRequest(Json.toJson(
                        new Reply<>(Status.ERROR, MessageManager.getProperty("role.error"))));
            }
            return ok(Json.toJson(
                    new Reply<>(Status.SUCCESS, userDTO)));
        } catch (IOException e) {
            logger.error("Exception in getCurrentUser method ", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
    }

    @Transactional
    public static Result login() {
        try {
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
            if (BCrypt.checkpw(password, user.getPassword())) {
                UserDTO userDTO = UserDTO.getUser(user);
                String token = Crypto.generateToken();
                logger.info("token {} have been generated", token);
                response().setCookie("token", token, 5000);
                user.setToken(token);
                dao.update(user);
                try {
                   if (ConfigContainer.getInstance().getPermissionHandler().hasRole(userDTO.getRoleTitle())) {
                        userDTO.setMenu(ConfigContainer.getInstance().getPermissionHandler().getJsonPermissions(userDTO.getRoleTitle()));
                    } else {
                        throw new ParseException("role is not exist in configuration!", 0);
                    }
                } catch (ParseException e) {
                    logger.error("exception during setting menu for role {}", userDTO.getRoleTitle(), e);
                    return badRequest(Json.toJson(
                            new Reply<>(Status.ERROR, MessageManager.getProperty("role.error"))));
                }
                return ok(Json.toJson(
                        new Reply<>(Status.SUCCESS, userDTO)));
            } else {
                return badRequest(Json.toJson(
                        new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
            }
        } catch (IOException e) {
            logger.error("Exception in login method ", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
    }

    public static Result logout() {

        return ok();
    }
}
