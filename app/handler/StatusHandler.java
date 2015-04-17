package handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.Logger;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by antonkw on 10.04.2015.
 */
public class StatusHandler extends AbstractPrivelegesHandler {
    private static Logger.ALogger logger = Logger.of(StatusHandler.class);

    private Iterator<JsonNode> statusNodes = null;

    private Map<String, List<String>> statusMap = null;

    private Map<String, String> translateMap = null;

    private ObjectMapper mapper = null;

    private JsonNode rootNode = null;

    protected StatusHandler() throws IOException, ParseException {
        mapper = new ObjectMapper();
        rootNode = mapper.readTree(new File(FILE_CONFIG_NAME));
        parse();
    }

    private void updateStatusNodes() {
        statusNodes = rootNode.path("status").elements();
    }

    @Override
    public void parse() throws ParseException {
        statusMap = new HashMap<>();
        translateMap = new HashMap<>();
        updateStatusNodes();
        JsonNode status, arrNode = null;
        while (statusNodes.hasNext()) {
            status = statusNodes.next();
            translateMap.put(status.path("id").asText(), status.path("title").asText());
        }
        updateStatusNodes();
        while (statusNodes.hasNext()) {
            List<String> statusList = new ArrayList<>();
            status = statusNodes.next();
            arrNode = status.path("choice");
            if (arrNode.isArray()) {
                for (final JsonNode objNode : arrNode) {
                    statusList.add(getTranslate(objNode.asText()));
                }
            } else {
                throw new ParseException("list of permission is not array (check priveleges.json)", 0);
            }
            statusMap.put(status.path("title").asText(), statusList);
            translateMap.put(status.path("id").asText(), status.path("title").asText());
        }
    }

    private String getTranslate(String id) {
        if (has(id)) {
            if (translateMap.get(id) == null) {
                logger.warn("status with id {} not have title(translate)", id);
                return null;
            }
            return translateMap.get(id);
        } else {
            logger.warn("status with id {} not exist", id);
            return null;
        }
    }

    public List<String> getStatusList(String title) {
        if (statusMap.containsKey(title)) {
            return statusMap.get(title);
        } else {
            logger.warn("status {} is not exist", title);
            return null;
        }
    }

    public boolean has(String statusName) {
        return statusMap.containsKey(statusName);
    }
}
