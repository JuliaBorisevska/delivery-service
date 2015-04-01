package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.UserDAO;
import dto.UserDTO;
import entity.Company;
import entity.User;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import resource.MessageManager;

import java.util.ArrayList;
import java.util.List;


public class UserController extends BaseController {
	
    @Transactional
    public static Result listUsers(Integer pageNumber, Integer pageSize) {
        if(pageNumber == null || pageSize == null || pageNumber <= 0 || pageNumber <= 0) {
                return badRequest(Json.toJson(
                		new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }
        ///////////////////
        Company company = new Company();
        company.setId(1);
        ///////////////////
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
	
	
}
