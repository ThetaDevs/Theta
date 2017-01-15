package com.srgood.reasons.config;

import net.dv8tion.jda.core.entities.Guild;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

import static com.srgood.reasons.config.ConfigBasicUtils.*;

class ConfigGuildUtils {
    private static Map<String, Element> servers = new HashMap<>();

    static Element getGuildNode(Guild guild) {
        return servers.get(guild.getId());
    }

    static void ensureGuildInitted(Guild guild) {
        if (getGuildNode(guild) == null) {
            initGuildConfig(guild);
        } else {
            addMissingDefaultElementsToGuild(getGuildNode(guild));
        }
    }

    private static void addMissingDefaultElementsToGuild(Element elementServer) {
        try {
            Document doc = lockAndGetDocument();
            Element elementDefault = getOrCreateFirstSubElement(doc.getDocumentElement(), "default");

            ConfigBasicUtils.nodeListToList(elementDefault.getChildNodes()).stream().filter(n -> n instanceof Element).forEach(n -> {
                Element elem = (Element) n;
                if (getFirstSubElement(elementServer, elem.getTagName()) == null) {
                    elementServer.appendChild(elem.cloneNode(true));
                }
            });
        } finally {
            releaseDocument();
        }
    }

    private static void initGuildConfig(Guild guild) {
        try {
            Document doc = lockAndGetDocument();
            Element elementServer = doc.createElement("server");

            Element elementServers = getOrCreateFirstSubElement(doc.getDocumentElement(), "servers");

            Attr attrID = doc.createAttribute("id");
            attrID.setValue(guild.getId());
            elementServer.setAttributeNode(attrID);

            addMissingDefaultElementsToGuild(elementServer);

            elementServers.appendChild(elementServer);
            servers.put(guild.getId(), elementServer);
        } finally {
            releaseDocument();
        }
    }

    static void deleteGuildConfig(Guild guild) {
        try {
            lockDocument();
            getGuildNode(guild).getParentNode().removeChild(getGuildNode(guild));
        } finally {
            releaseDocument();
        }
    }

    static String getGuildSimpleProperty(Guild guild, String property) {
        try {
            lockDocument();
            Element propertyElement = getFirstSubElement(getGuildNode(guild), property);
            return propertyElement != null ? propertyElement.getTextContent() : null;
        } finally {
            releaseDocument();
        }
    }

    static void setGuildSimpleProperty(Guild guild, String property, String value) {
        try {
            Document document = lockAndGetDocument();
            Element guildElement = getGuildNode(guild);
            Element targetElement = getOrCreateFirstSubElement(guildElement, property);
            targetElement.setTextContent(value);
        } finally {
            releaseDocument();
        }
    }

    static Element getGuildComplexPropertyElement(Guild guild, String property) {
        return getFirstSubElement(getGuildNode(guild), property);
    }

    static String getGuildPrefix(Guild guild) {
        return getGuildSimpleProperty(guild, "prefix");
    }

    static void setGuildPrefix(Guild guild, String newPrefix) {
        setGuildSimpleProperty(guild, "prefix", newPrefix);
    }

    static void resetServers() {
        servers = new HashMap<>();
    }

    static void addServer(String guildID, Element guildElement) {
        servers.put(guildID, guildElement);
    }
}
