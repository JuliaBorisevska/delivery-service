import org.junit.Test;
import play.twirl.api.Content;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;


/**
 * Created by antonkw on 25.04.2015.
 */
public class MainTemplateTest {
    @Test
    public void renderTemplate() {
        Content html = views.html.main.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Delivery Service");
    }
}
