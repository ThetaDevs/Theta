package com.srgood.reasons.config;

import com.srgood.reasons.commands.PermissionLevels;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.srgood.reasons.config.ConfigBasicUtils.lockAndGetDocument;
import static com.srgood.reasons.config.ConfigBasicUtils.releaseDocument;

class ConfigRoleUtils {
    private static Element getRolesElement(Guild guild) {
        return ConfigGuildUtils.getGuildComplexPropertyElement(guild, "roles");
    }

    private static List<Node> getRoleNodeListFromGuild(Guild guild) {
        try {
            lockAndGetDocument();
            Element rolesElem = getRolesElement(guild);

            return ConfigBasicUtils.nodeListToList(rolesElem.getElementsByTagName("role"));
        } finally {
            releaseDocument();
        }
    }

    static PermissionLevels roleToPermission(Guild guild, Role role) {
        try {
            lockAndGetDocument();
            PermissionLevels permission = null;
            if (role == null) {
                throw new IllegalArgumentException("role cannot be null");
            }

            List<Node> roleNodeList = getRoleNodeListFromGuild(guild);

            String roleID = role.getId();

            for (Node n : roleNodeList) {
                Element roleElem = (Element) n;
                String roleXMLName = roleElem.getAttribute("name");

                if (!roleID.equals(roleElem.getTextContent())) {
                    continue;
                }

                for (PermissionLevels permLevel : PermissionLevels.values()) {
                    if (permLevel.getLevel() >= (permission == null ? PermissionLevels.STANDARD : permission).getLevel() && permLevel.getXMLName().equals(roleXMLName)) {
                        permission = permLevel;
                    }
                }
            }

            return permission;
        } finally {
            releaseDocument();
        }
    }

    static boolean guildHasRoleForPermission(Guild guild, PermissionLevels roleLevel) {
        try {
            lockAndGetDocument();
            List<Node> roleElementList = getRoleNodeListFromGuild(guild);

            for (Node n : roleElementList) {
                Element roleElem = (Element) n;
                if (roleElem.getAttribute("name").equals(roleLevel.getXMLName())) {
                    return true;
                }
            }
            return false;
        } finally {
            releaseDocument();
        }
    }

    static void registerRoleConfig(Guild guild, Role role, PermissionLevels roleLevel) {
        try {
            Document doc = lockAndGetDocument();
            Element elementRoles = ConfigBasicUtils.getFirstSubElement(ConfigGuildUtils.getGuildNode(guild), "roles");

            Element elementRole = doc.createElement("role");
            Attr roleAttr = doc.createAttribute("name");
            roleAttr.setValue(roleLevel.getXMLName());
            elementRole.setAttributeNode(roleAttr);
            elementRole.setTextContent(role.getId());

            elementRoles.appendChild(elementRole);
        } finally {
            releaseDocument();
        }
    }

    static void deregisterRoleConfig(Guild guild, String roleID) {
        try {
            lockAndGetDocument();
            Element elementRole = null;

            List<Node> roleNodeList = getRoleNodeListFromGuild(guild);
            for (Node n : roleNodeList) {
                Element elem = (Element) n;
                String textContent = elem.getTextContent();
                if (textContent.equals(roleID)) {
                    elementRole = elem;
                    break;
                }
            }
            if (elementRole == null) {
                return;
            }
            elementRole.getParentNode().removeChild(elementRole);
        } finally {
            releaseDocument();
        }
    }

    static Set<String> getGuildRegisteredRoleIDs(Guild guild) {
        return getRoleNodeListFromGuild(guild).stream().map(Node::getTextContent).collect(Collectors.toSet());
    }

    static Set<Role> getGuildRolesFromPermissionLevel(Guild guild, PermissionLevels permissionLevel) {
        try {
            lockAndGetDocument();
            String permissionName = permissionLevel.getXMLName();
            return getRoleNodeListFromGuild(guild).stream().filter(n -> n instanceof Element).map(n -> (Element) n).filter(elem -> elem.getAttribute("name").equals(permissionName)).map(Node::getTextContent).map(guild::getRoleById).collect(Collectors.toSet());
        } finally {
            releaseDocument();
        }
    }
}
