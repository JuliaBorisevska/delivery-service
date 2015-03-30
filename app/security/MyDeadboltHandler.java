package security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import dao.UserDAO;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import views.html.main;

/**
 * Created by antonkw on 30.03.2015.
 */
public class MyDeadboltHandler extends AbstractDeadboltHandler {
    public F.Promise<Result> beforeAuthCheck(Http.Context context) {
        // returning null means that everything is OK.  Return a real result if you want a redirect to a login page or
        // somewhere else
        return F.Promise.pure(null);
    }

    @Override
    public Subject getSubject(final Http.Context context) {
        String login = (String) context.session().get("login");
        UserDAO userDAO = new UserDAO(JPA.em());
        return userDAO.findByLogin(login);
    }

    @Override
    public F.Promise<Result> onAuthFailure(Http.Context context,
                                           String content) {
        // you can return any result from here - forbidden, etc
        return F.Promise.promise(new F.Function0<Result>() {
            @Override
            public Result apply() throws Throwable {
                return ok(main.render());
            }
        });
    }
}