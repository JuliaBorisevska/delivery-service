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

    private Map<String, List<String>> statusMap = null;

    protected StatusHandler() throws IOException, ParseException {
        parse();
    }

    @Override
    public void parse() throws ParseException, IOException {
        statusMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(FILE_CONFIG_NAME));
        Iterator<JsonNode> statusNodes = rootNode.path("status").elements();
        JsonNode status, arrNode = null;
        while (statusNodes.hasNext()) {
            List<String> statusList = new ArrayList<>();
            status = statusNodes.next();
            arrNode = status.path("choice");
            if (arrNode.isArray()) {
                for (final JsonNode objNode : arrNode) {
                    statusList.add(objNode.asText());
                }
            } else {
                throw new ParseException("list of permission is not array (check priveleges.json)", 0);
            }
            statusMap.put(status.path("title").asText(), statusList);
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
