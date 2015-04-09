package security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.SecurityRole;
import play.Logger;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by antonkw on 08.04.2015.
 */
public class PermissionsHandler {
    private static PermissionsHandler instance = null;

    private static Logger.ALogger logger = Logger.of(PermissionsHandler.class);

    private PermissionsHandler() {
        try {
            read();
        } catch (IOException e) {
            logger.error("Cannot read config file");
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
    }

    public static PermissionsHandler getInstance() {
        if (instance == null) {
            instance = new PermissionsHandler();
        }
        return instance;
    }

    public void reload() {
        instance = null;
    }


    private Map<String, List<SecurityPermission>> roles = new HashMap<>();

    private void read() throws ParseException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(ConfigFiles.getFileConfigName()));
        Iterator<JsonNode> roleNodes = rootNode.path("roles").elements();
        JsonNode role = null;
        JsonNode arrNode = null;
        while (roleNodes.hasNext()) {
            List<SecurityPermission> permissions = new ArrayList<>();
            role = roleNodes.next();
            arrNode = role.path("menu");
            if (arrNode.isArray()) {
                for (final JsonNode objNode : arrNode) {
                    permissions.add(new SecurityPermission(objNode.asText()));
                }
            } else {
                throw new ParseException("list of permission is not array (check priveleges.json)", 0);
            }
            roles.put(role.path("title").asText(), permissions);
        }
    }

    public List<SecurityPermission> getPermissions(SecurityRole role) {
        if (roles.containsKey(role.getName())) {
            return roles.get(role.getName());
        } else {
            logger.warn("role {} have no permissions", role.getName());
            return Collections.emptyList();
        }
    }

}
