package security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import dao.UserDAO;
import entity.User;
import play.Logger;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.html.accesserror;

/**
 * Created by antonkw on 30.03.2015.
 */
public class MyDeadboltHandler extends AbstractDeadboltHandler {
    private static Logger.ALogger logger = Logger.of(MyDeadboltHandler.class);
    @Override
    public F.Promise<Result> beforeAuthCheck(Http.Context context) {
        // returning null means that everything is OK.  Return a real result if you want a redirect to a login page or
        // somewhere else
        return F.Promise.pure(null);
    }

    @Override
    public Subject getSubject(final Http.Context var1) {
        User user = null;
        String token = var1.request().cookie("token").value();
        UserDAO userDAO = new UserDAO(JPA.em());
        user = userDAO.findByToken(token);
        user.setPermissions(PermissionsHandler.getInstance().getPermissions(user.getRoleByRoleId()));
        logger.info("check role of user: {}, role: {}", user.getIdentifier(), user.getRoles().get(0).getName());
        return user;
    }

    @Override
    public F.Promise<Result> onAuthFailure(Http.Context context,
                                           String content) {
        // you can return any result from here - forbidden, etc
        logger.warn("access failure on uri {}", context.request().uri());
        return F.Promise.promise(new F.Function0() {
            public Result apply() throws Throwable {
                return Results.unauthorized(accesserror.render());
            }
        });
    }

    @Override
    public DynamicResourceHandler getDynamicResourceHandler(Http.Context context) {
        return new MyAlternativeDynamicResourceHandler();
    }
}