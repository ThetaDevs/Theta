package com.srgood.reasons.config;

import net.dv8tion.jda.core.entities.Guild;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.srgood.reasons.config.ConfigBasicUtils.getDocumentLock;

class ConfigRoleUtils {
    private static Element getRolesElement(Guild guild) {
        return ConfigBasicUtils.getOrCreateFirstSubElement(ConfigGuildUtils.getGuildNode(guild), "roles");
    }

    private static List<Node> getRoleNodeListFromGuild(Guild guild) {
            Element rolesElem = getRolesElement(guild);
            return ConfigBasicUtils.nodeListToList(rolesElem.getElementsByTagName("role"));
    }

    static void deregisterRoleConfig(Guild guild, String roleID) {
        try {
            getDocumentLock().writeLock().lock();
            Element elementRole = null;

            List<Node> roleNodeList = getRoleNodeListFromGuild(guild);
            for (Node n : roleNodeList) {
                Element elem = (Element) n;
                String textContent = elem.getTextContent();
                if (Objects.equals(textContent, roleID)) {
                    elementRole = elem;
                    break;
                }
            }
            if (elementRole == null) {
                return;
            }
            elementRole.getParentNode().removeChild(elementRole);
        } finally {
            getDocumentLock().writeLock().unlock();
        }
    }

    static Set<String> getGuildRegisteredRoleIDs(Guild guild) {
        return getRoleNodeListFromGuild(guild).stream().map(Node::getTextContent).collect(Collectors.toSet());
    }
}
