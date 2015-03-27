package controllers;

import dto.UserDTO;
import play.libs.Json;
import play.mvc.Result;
import views.html.main;

import java.util.Map;

public class Application extends BaseController {

    public static Result index() {
        return ok(main.render());
    }

    //@Transactional
    public static Result login() {
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String login = values.get("user")[0];
        String password = values.get("password")[0];
        //EntityManager em = JPA.em();
        //UserDAO dao = new UserDAO(em);
        //User user = dao.findByLogin(login);
        //if(user == null) {
        //    return badRequest(Json.toJson(
        //            new Reply<>(Status.ERROR, "User not found"))
        //    );
        //}
        //if(BCrypt.checkpw(password, user.getPassword())) {
        //    session().clear();
           // session("company", user.);
        //    session().put("role", user.getRoleByRoleId().getTitle());
            
        	// UserDTO userDTO = UserDTO.getUser(user);
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Юлия");
        userDTO.setRole("Супервизор");
            return ok(Json.toJson(
                    new Reply<>(Status.SUCCESS, userDTO))
            );
        //} else {
        //    return badRequest(Json.toJson(
        //            new Reply<>(Status.ERROR, "Wrong password"))
        //    );
       // }
    }

    public static Result logout() {
        session().clear();
        return ok();
    }
}
