package security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import controllers.Application;
import entity.SecurityRole;
import entity.User;
import handler.ConfigContainer;
import play.Logger;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.html.accesserror;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;

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
        User user = new User();
        user = Application.recieveUserByToken();
        if (user != null) {
            try {
                user.setPermissions(ConfigContainer.getInstance().getRolesHandler().getPermissions(user.getRoleByRoleId()));
            } catch (IOException | ParseException e) {
                logger.error("Exception during set permissions", e);
            }
            logger.info("check role of user: {}, role: {}", user.getIdentifier(), user.getRoles().get(0).getName());
        } else {

            user = new User();
            user.setRoleByRoleId(new SecurityRole());
            user.setPermissions(Collections.<SecurityPermission>emptyList());
        }
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