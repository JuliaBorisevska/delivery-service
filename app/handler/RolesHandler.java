package handler;

import com.fasterxml.jackson.databind.JsonNode;
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
public class RolesHandler extends AbstractPrivelegesHandler {
    private static Logger.ALogger logger = Logger.of(RolesHandler.class);

    private Map<String, List<SecurityPermission>> roles = null;

    private Map<String, List<String>> orders = null;

    private Map<String, JsonNode> jsonRoles = null;

    private StatusHandler statusHandler = null;

    protected RolesHandler(StatusHandler statusHandler) throws IOException, ParseException {
        this.statusHandler = statusHandler;
        parse();
    }

    @Override
    public void parse() throws ParseException, IOException {
        roles = new HashMap<>();
        orders = new HashMap<>();
        jsonRoles = new HashMap<>();
        JsonNode rootNode = mapper.readTree(new File(FILE_CONFIG_NAME));
        List<String> bufferOrderList = null;
        if (!rootNode.has("roles")) {
            throw new ParseException("roles path not exist", 0);
        }
        Iterator<JsonNode> roleNodes = rootNode.path("roles").elements();
        JsonNode role, arrNode = null;
        List<SecurityPermission> permissions;
        while (roleNodes.hasNext()) {
            permissions = new ArrayList<>();
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
            if (role.has("order")) {
                arrNode = role.path("order");
                if (arrNode.isArray()) {
                    bufferOrderList = new ArrayList<>();
                    for (final JsonNode objNode : arrNode) {
                        bufferOrderList.add(statusHandler.getTranslate(objNode.asText()));
                    }
                    orders.put(role.path("title").asText(), bufferOrderList);
                } else {
                    throw new ParseException("list of orders is not array (check priveleges.json)", 0);
                }
            }
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

    public boolean hasOrders(String title) {
        return orders.containsKey(title);
    }

    public boolean hasRole(String roleName) {
        return roles.containsKey(roleName);
    }

    public JsonNode getJsonPermissions(String roleName) {
        if (hasRole(roleName)) {
            return jsonRoles.get(roleName);
        } else {
            logger.warn("role {} is not existing", roleName);
            return null;
        }
    }

    public List<String> getOrdersList(String title) {
        if (hasOrders(title)) {
            return orders.get(title);
        } else {
            return null;
        }

    }

    public void setStatusHandler(StatusHandler statusHandler) {
        this.statusHandler = statusHandler;
    }
}
