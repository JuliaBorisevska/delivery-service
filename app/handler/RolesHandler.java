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

    private Map<String, List<String>> roles = null;

    private Map<String, List<String>> orders = null;

    private Map<String, JsonNode> jsonRoles = null;

    private Map<String, List<SecurityPermission>> sectionsPermissions = null;

    private StatusHandler statusHandler = null;

    protected RolesHandler(StatusHandler statusHandler) throws IOException, ParseException {
        this.statusHandler = statusHandler;
        rootNode = mapper.readTree(new File(FILE_CONFIG_NAME));
        parse();
        logger.info("roles handler start");
    }

    @Override
    public void parse() throws ParseException, IOException {
        roles = new HashMap<>();
        orders = new HashMap<>();
        jsonRoles = new HashMap<>();
        sectionsPermissions = new HashMap<>();
        List<String> bufferOrderList = null;
        if (!rootNode.has("roles")) {
            throw new ParseException("roles path not exist", 0);
        }
        Iterator<JsonNode> roleNodes = rootNode.path("roles").elements();
        JsonNode role, arrNode, section = null;
        List<String> sections;
        while (roleNodes.hasNext()) {
            sections = new ArrayList<>();
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
                    sections.add(objNode.asText());
                }
            } else {
                throw new ParseException("list of permission is not array (check priveleges.json)", 0);
            }
            roles.put(role.path("title").asText(), sections);
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
        if (!rootNode.has("sections")) {
            throw new ParseException("roles path not exist", 0);
        }
        Iterator<JsonNode> sectionNode = rootNode.path("sections").elements();
        List<SecurityPermission> permissions = null;
        while (sectionNode.hasNext()) {
            permissions = new ArrayList<>();
            section = sectionNode.next();
            if (!section.has("id")) {
                throw new ParseException("id path not exist", 0);
            }
            if (section.has("permissions")) {
                arrNode = section.path("permissions");
                if (arrNode.isArray()) {
                    for (JsonNode objNode : arrNode) {
                        permissions.add(new SecurityPermission(objNode.asText()));
                    }
                } else {
                    throw new ParseException("list of permission is not array (check priveleges.json)", 0);
                }
                sectionsPermissions.put(section.path("id").asText(), permissions);
            }


        }
    }

    public List<SecurityPermission> getPermissions(SecurityRole role) {
        logger.info("getting permissions of role {}", role.getName());
        if (roles.containsKey(role.getName())) {
            List<SecurityPermission> permissions = getPermissionsBySections(roles.get(role.getName()));
            logger.info("count of permissions of role {}: {}", role.getName(), permissions.size());
            return permissions;

        } else {
            logger.warn("role {} is not existing", role.getName());
            return Collections.emptyList();
        }
    }

    public List<SecurityPermission> getPermissionsBySections(List<String> sections) {
        logger.info("start getPermissionsBySetting function, count of sections: {}", sections.size());
        List<SecurityPermission> permissions = new ArrayList<>();
        for (String section : sections) {
            if (sectionsPermissions.containsKey(section)) {
                logger.debug("sections-permission map contains section {}", section);
                for (SecurityPermission permission : sectionsPermissions.get(section)) {
                    if (!permissions.contains(permission)) {
                        permissions.add(permission);
                        logger.debug("permission {} add to section {}", permission.getValue(), section);
                    }
                    logger.info("permission {} have contained yet at section {}", permission.getValue(), section);
                }
            } else {
                logger.warn("section {} is not existing at section list", section);
                return Collections.emptyList();
            }
        }
        return permissions;
    }

    public boolean hasOrders(String title) {
        return orders.containsKey(title);
    }

    public boolean hasRole(String roleName) {
        return roles.containsKey(roleName);
    }

    public JsonNode getJsonPermissions(String roleName) {
        if (hasRole(roleName)) {
            logger.info("permission of role {} : {}", roleName, jsonRoles.get(roleName).toString());
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
