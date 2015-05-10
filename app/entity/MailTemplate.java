package entity;

/**
 * Created by antonkw on 09.05.2015.
 */
public class MailTemplate {
    public MailTemplate(String title, String html) {
        this.title = title;
        this.html = html;
    }

    private String title;
    private String html;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}