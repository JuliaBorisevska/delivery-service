package handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.SecurityRole;
import play.Logger;
import security.SecurityPermission;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by antonkw on 08.04.2015.
 */
public class PermissionsHandler extends AbstractPrivelegesHandler {
    private static Logger.ALogger logger = Logger.of(PermissionsHandler.class);

    private Map<String, List<SecurityPermission>> roles = null;

    private Map<String, JsonNode> jsonRoles = null;

    protected PermissionsHandler() throws IOException, ParseException {
        parse();
    }

    @Override
    public void parse() throws ParseException, IOException {
        roles = new HashMap<>();
        jsonRoles = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(new File(FILE_CONFIG_NAME));
        if (!rootNode.has("roles")) {
            throw new ParseException("roles path not exist", 0);
        }
        Iterator<JsonNode> roleNodes = rootNode.path("roles").elements();
        JsonNode role, arrNode = null;
        while (roleNodes.hasNext()) {
            List<SecurityPermission> permissions = new ArrayList<>();
            role = roleNodes.next();
            if (!role.has("menu")) {
                throw new ParseException("menu path not exist", 0);
            }
            arrNode = role.path("menu");
            if (arrNode.isArray()) {
                if (!role.has("title")) {
                    throw new ParseException("title path not exist", 0);
                }
                jsonRoles.put(role.path("title").asText(), arrNode);
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
            logger.warn("role {} is not existing", role.getName());
            return Collections.emptyList();
        }
    }

    public boolean hasRole(String roleName) {
        return roles.containsKey(roleName);
    }

    public JsonNode getJsonPermissions(String roleName) {
        if (roles.containsKey(roleName)) {
            return jsonRoles.get(roleName);
        } else {
            logger.warn("role {} is not existing", roleName);
            return null;
        }
    }

}
