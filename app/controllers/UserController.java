package controllers;

import be.objectify.deadbolt.java.actions.Pattern;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.ContactDAO;
import dao.UserDAO;
import dto.UserDTO;
import entity.*;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.Logger.ALogger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import resource.MessageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UserController extends BaseController {
    private static ALogger logger = Logger.of(UserController.class);

    private static void setUsersParams(User user, Map<String, String[]> values) {
        String login = null;
        if (values.containsKey("login")) {
            login = values.get("login")[0];
            logger.info("start adding user with login '{}'", login);
        }
        String password = null;
        if (values.containsKey("password")) {
            password = values.get("password")[0];
        }
        Contact contact = null;
        Long numberOfContact = null;
        Integer idOfRole = null;
        String titleOfRole = null;
        if (values.containsKey("role[id]") && values.containsKey("role[title]")) {
            idOfRole = Integer.valueOf(values.get("role[id]")[0]);
            titleOfRole = values.get("role[title]")[0];
        } else {
            if (!values.containsKey("role")) {
                throw new IllegalArgumentException("role is misssing");
            }
        }
        try {
            if (values.containsKey("contactId")) {
                if (!StringUtils.isEmpty(values.get("contactId")[0]) && !"undefined".equals(values.get("contactId")[0])) {
                    numberOfContact = Long.valueOf(values.get("contactId")[0]);
                    ContactDAO contactDAO = new ContactDAO(JPA.em());
                    user.setContactByContactId(contactDAO.findById(numberOfContact));
                }
            }
            logger.info("id of role of new user '{}' - {}, title of role- {}, id of contact - {}", login, idOfRole, titleOfRole, numberOfContact);
        } catch (ClassCastException e) {
            throw e;
        }
        if (StringUtils.isEmpty(login) || StringUtils.isEmpty(password) || numberOfContact == null ||
                StringUtils.isEmpty(titleOfRole) || idOfRole == null) {

            throw new IllegalArgumentException("login/password/num.of contact/titleOfRole/idOfRole misssing");
        }
        user.setIdentifier(login);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        SecurityRole securityRole = new SecurityRole();
        securityRole.setId(idOfRole);
        securityRole.setName(titleOfRole);
        user.setRoleByRoleId(securityRole);
    }

    @Transactional
    @Pattern("user_updating")
    public static Result updateUser() {
        User user = new User();
        UserDAO userDAO = new UserDAO(JPA.em());
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        Long id = null;

        try {
            if (values.containsKey("id") && !StringUtils.isEmpty(values.get("id")[0])) {
                id = Long.parseLong(values.get("id")[0]);
            } else
                throw new IllegalArgumentException("id is missing or empty");
            setUsersParams(user, values);
            UserState userState = userDAO.findByStateTitle(UserDAO.ACTIVE_USER);
            user.setUserStateByUserStateId(userState);
            userDAO.update(user);
        } catch (IllegalArgumentException | ClassCastException exception) {
            logger.error("Adding user exception: ", exception);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, user)
        ));
    }

    @Transactional
    @Pattern("user_addition")
    public static Result createUser() {
        User user = new User();
        UserDAO userDAO = new UserDAO(JPA.em());
        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        try {
            setUsersParams(user, values);
            UserState userState = userDAO.findByStateTitle(UserDAO.ACTIVE_USER);
            user.setUserStateByUserStateId(userState);
            userDAO.create(user);

        } catch (IllegalArgumentException | ClassCastException exception) {
            logger.error("Adding user exception: ", exception);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, user)
        ));
    }

    @Transactional
    @Pattern("user_list")
    public static Result listRoles() {
        logger.info("Start listRoles method");
        UserDAO userDAO = new UserDAO(JPA.em());
        List<SecurityRole> roleList = userDAO.listRoles();
        ObjectNode result = Json.newObject();
        result.put("list", Json.toJson(roleList));
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, result)
        ));
    }

    @Transactional
    @Pattern("user_list")
    public static Result listUsers(Integer pageNumber, Integer pageSize, String role) {
        logger.info("Get list of users; number of page: {}, size of page: {}, role: {}", pageNumber, pageSize, role);
        if (pageNumber == null || pageSize == null || pageNumber <= 0 || pageNumber <= 0) {
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
        Long total;
        List<User> userList;
        if (StringUtils.isBlank(role)){
            total = dao.total(company);
            userList = dao.list(pageNumber, pageSize, company);
        }else{
            total = dao.total(company, role);
            userList = dao.list(pageNumber, pageSize, company, role);
        }
        Integer totalPages = Double.valueOf(Math.ceil((double) total / pageSize)).intValue();
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
    @Pattern("user_deleting")
    public static Result deleteUsers(String ids) {
        logger.info("Start deleteUsers method with ids: {}", ids);
        try {
            java.util.regex.Pattern separator;
            separator = java.util.regex.Pattern.compile(",");
            String[] idArray = separator.split(ids);
            User currentUser = Application.recieveUserByToken();
            if (currentUser == null) {
                return badRequest(Json.toJson(
                        new Reply<>(Status.ERROR, MessageManager.getProperty("authentification.error"))));
            }
            Company company = currentUser.getContactByContactId().getCompanyByCompanyId();
            UserDAO dao = new UserDAO(JPA.em());
            for (String id : idArray) {
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
        } catch (Exception e) {
            logger.error("Error in deleteUsers method: {}", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
    }
}
