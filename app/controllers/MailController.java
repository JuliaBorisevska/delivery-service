package controllers;


import be.objectify.deadbolt.java.actions.Pattern;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entity.MailTemplate;
import org.apache.commons.lang3.StringUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import play.Logger;
import play.libs.Json;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;
import play.mvc.Result;
import resource.MessageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by antonkw on 09.05.2015.
 */
public class MailController extends BaseController {
    private static Logger.ALogger logger = Logger.of(MailController.class);

    List<MailTemplate> listTemplates = null;

    @Pattern("update_templates")
    public static Result reloadTemplates() {
        MailConfigurator.reload();
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, Json.newObject())
        ));
    }

    public static Result send() {
        STGroup group = null;
        ST template = null;
        String title = null;
        String adresses = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        if (!values.containsKey("title"))
            throw new IllegalArgumentException("title parameter is missing");
        title = values.get("title")[0];
        if (StringUtils.isEmpty(title))
            throw new IllegalArgumentException("title parameter is empty");
        if (!values.containsKey("adresses"))
            throw new IllegalArgumentException("adresses parameter is missing");
        adresses = values.get("adresses")[0];
        Email email = null;
        String adress = null;
        try {
            rootNode = mapper.readTree(adresses);
            logger.info(rootNode.textValue());
            if (!rootNode.isArray()) {
                throw new IllegalArgumentException("adresses list is not json array");
            }
            group = new STGroupFile(MailConfigurator.TEMPLATE_NAME,
                    MailConfigurator.FIRST_DELIMETER, MailConfigurator.SECOND_DELIMETER);
            if (!group.getTemplateNames().contains(title)) {
                for (JsonNode objNode : rootNode) {

                    if (!objNode.has("email"))
                        throw new IllegalArgumentException("email arg. is misssing");
                    email = new Email();
                    email.addTo(objNode.path("email").asText());
                    email.setSubject(title.substring(1));   //remove slash before title of template
                    email.setFrom(MailConfigurator.SERVICE_NAME);

                    email.setBodyHtml(values.get("html")[0]);
                    String id = MailerPlugin.send(email);
                }


                return ok(Json.toJson(
                        new Reply<>(Status.SUCCESS, Json.newObject())
                ));
            }
            for (JsonNode objNode : rootNode) {
                if (!objNode.has("firstName"))
                    throw new IllegalArgumentException("first name arg. is misssing");
                if (!objNode.has("lastName"))
                    throw new IllegalArgumentException("last Name arg. is misssing");
                if (!objNode.has("middleName"))
                    throw new IllegalArgumentException("middle Name arg. is misssing");
                if (!objNode.has("email"))
                    throw new IllegalArgumentException("email arg. is misssing");
                email = new Email();
                email.addTo(objNode.path("email").asText());
                email.setSubject(title.substring(1));   //remove slash before title of template
                email.setFrom(MailConfigurator.SERVICE_NAME);
                template = group.getInstanceOf(title);
                logger.info((String) template.getAttributes().keySet().toArray()[0]);
                if (template.getAttributes().keySet().contains("firstname")) {
                    template.add("firstname", objNode.path("firstName").asText());
                }
                if (template.getAttributes().keySet().contains("lastname")) {
                    template.add("lastname", objNode.path("lastName").asText());
                }
                if (template.getAttributes().keySet().contains("middlename")) {
                    template.add("middlename", objNode.path("middleName").asText());
                }
                if (template.getAttributes().keySet().contains("admin")) {
                    template.add("admin", MailConfigurator.ADMINISTATION);
                }
                if (template.getAttributes().keySet().contains("mail")) {
                    template.add("mail", MailConfigurator.MAIL);
                }
                email.setBodyHtml(template.render());
                String id = MailerPlugin.send(email);
            }

        } catch (Exception e) {
            logger.error("Exception during sending mail", e);
            return badRequest(Json.toJson(
                    new Reply<>(Status.ERROR, MessageManager.getProperty("message.error"))));
        }

        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, Json.newObject())
        ));
    }

    public static Result listTemplates() {
        ObjectNode result = Json.newObject();
        result.put("list", Json.toJson(MailConfigurator.getInstance().getRawTemplatesList()));
        return ok(Json.toJson(
                new Reply<>(Status.SUCCESS, result)
        ));
    }
}

class MailConfigurator {
    private static Logger.ALogger logger = Logger.of(MailConfigurator.class);

    final static String SERVICE_NAME = "DeliveryService<antonkwsky@gmail.com>";

    final static String ADMINISTATION = "Delivery Service management";

    final static String MAIL = "mail@deliveryservice.com";

    final static String TEMPLATE_NAME = "conf/spam.stg";

    final static char FIRST_DELIMETER = '$';

    final static char SECOND_DELIMETER = '$';

    public STGroup getGroup() {
        return group;
    }

    private static volatile MailConfigurator instance;

    private STGroup group = null;

    private List<MailTemplate> rawTemplateStrings = null;

    public List<MailTemplate> getRawTemplatesList() {
        logger.info("get list of raw(not rendered) templates, count: {}", rawTemplateStrings.size());
        return rawTemplateStrings;
    }

    public static void reload() {
        instance = null;
        getInstance();
    }

    private MailConfigurator() {
        logger.info("new MailConfigurator created");
        rawTemplateStrings = new ArrayList<>();
        group = new STGroupFile(TEMPLATE_NAME, FIRST_DELIMETER, SECOND_DELIMETER);
        for (String titleOfTemplate : group.getTemplateNames()) {
            rawTemplateStrings.add(new MailTemplate(titleOfTemplate, group.rawGetTemplate(titleOfTemplate).getTemplateSource()));
        }
    }

    public static MailConfigurator getInstance() {
        MailConfigurator localContainer = instance;

        if (localContainer == null) {
            synchronized (MailConfigurator.class) {
                localContainer = instance;
                if (localContainer == null) {
                    instance = localContainer = new MailConfigurator();
                }
            }
        }
        return localContainer;
    }
}



