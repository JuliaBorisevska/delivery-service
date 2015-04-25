/**
 * Created by antonkw on 25.04.2015.
 */

import org.junit.Test;
import play.mvc.Result;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class ApplicationControllerTest {
    @Test
    public void callIndex() {
        Result result = callAction(
                controllers.routes.ref.Application.index()
        );
        assertThat(status(result)).isEqualTo(OK);
        assertThat(contentType(result)).isEqualTo("text/html");
        assertThat(charset(result)).isEqualTo("utf-8");
        assertThat(contentAsString(result)).contains("Delivery Service");
    }


}
