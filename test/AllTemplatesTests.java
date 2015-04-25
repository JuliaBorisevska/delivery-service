import org.junit.Test;
import play.twirl.api.Content;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;

/**
 * Created by antonkw on 25.04.2015.
 */
public class AllTemplatesTests {
    @Test
    public void renderAccessErrorTemplateTest() {
        Content html = views.html.accesserror.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("access denied!");
    }

    @Test
    public void renderContactDetailsTemplateTest() {
        Content html = views.html.contactdetails.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("$root.chosenSectionId().id == \"ctadd\"");
    }

    @Test
    public void renderContactListTemplateTest() {
        Content html = views.html.contactlist.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("<!-- ko if: $root.chosenSectionId().id == \"ctlst\" -->");
    }

    @Test
    public void renderUserListTemplateTest() {
        Content html = views.html.list.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("<!-- ko if: $root.chosenSectionId().id == \"lst\" -->");
    }

    @Test
    public void renderLoginTemplateTest() {
        Content html = views.html.login.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("<!-- ko if: $root.chosenSectionId().id == \"lgn\" -->");
    }

    @Test
    public void renderOrderDetailsTemplateTest() {
        Content html = views.html.orderdetails.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("<div id=\"order-details\">");
    }

    @Test
    public void renderOrderListTemplateTest() {
        Content html = views.html.orderlist.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("<!-- ko if: $root.chosenSectionId().id == \"ordlst\" -->");
    }

    @Test
    public void renderOrderSearchTemplateTest() {
        Content html = views.html.ordersearch.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("<!-- ko if: $root.chosenSectionId().id == \"ordsearch\" -->");
    }

    @Test
    public void renderSettingsTemplateTest() {
        Content html = views.html.settings.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("<!-- ko if: $root.chosenSectionId().id == \"settings\" -->");
    }

    @Test
    public void renderUserDetailsTemplateTest() {
        Content html = views.html.userdetails.render();
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("if: $root.chosenSectionId().id == \"useradd\" ");
    }

}
