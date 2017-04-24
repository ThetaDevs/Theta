package com.srgood.reasons.config;

import net.dv8tion.jda.core.entities.Guild;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
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
            getDocumentLock().writeLock().lock();
            Document doc = getDocument();
            Element elementDefault = getOrCreateFirstSubElement(doc.getDocumentElement(), "default");

            ConfigBasicUtils.nodeListToList(elementDefault.getChildNodes()).stream().filter(Element.class::isInstance).forEach(n -> {
                Element elem = (Element) n;
                if (getFirstSubElement(elementServer, elem.getTagName()) == null) {
                    elementServer.appendChild(elem.cloneNode(true));
                }
            });
        } finally {
            getDocumentLock().writeLock().unlock();
        }
    }

    private static void initGuildConfig(Guild guild) {
        try {
            getDocumentLock().writeLock().lock();
            Document doc = getDocument();
            Element elementServer = doc.createElement("server");

            Element elementServers = getOrCreateFirstSubElement(doc.getDocumentElement(), "servers");

            Attr attrID = doc.createAttribute("id");
            attrID.setValue(guild.getId());
            elementServer.setAttributeNode(attrID);

            addMissingDefaultElementsToGuild(elementServer);

            elementServers.appendChild(elementServer);
            servers.put(guild.getId(), elementServer);
        } finally {
            getDocumentLock().writeLock().unlock();
        }
    }

    static void deleteGuildConfig(Guild guild) {
        try {
            getDocumentLock().writeLock().lock();
            getGuildNode(guild).getParentNode().removeChild(getGuildNode(guild));
        } finally {
            getDocumentLock().writeLock().unlock();
        }
    }

    static boolean guildPropertyExists(Guild guild, String property) {
        try {
            getDocumentLock().readLock().lock();
            Element propertyElement = ConfigBasicUtils.getElementFromPath(getGuildNode(guild), property);
            return propertyElement != null;
        } finally {
            getDocumentLock().readLock().unlock();
        }
    }

    static String getGuildSimpleProperty(Guild guild, String property) {
        try {
            getDocumentLock().readLock().lock();

            if (!guildPropertyExists(guild, property)) {
                return null;
            }

            Element propertyElement = ConfigBasicUtils.getElementFromPath(getGuildNode(guild), property);
            return propertyElement.getTextContent();
        } finally {
            getDocumentLock().readLock().unlock();
        }
    }

    static <T> T getGuildSerializedProperty(Guild guild, String property, Class<T> propertyClass) {
        String raw = getGuildSimpleProperty(guild, property);

        if (raw == null || raw.equals("null")) {
            return null;
        }

        byte[] decoded = Base64.getDecoder().decode(raw);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decoded);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object deserialized = objectInputStream.readObject();
            return propertyClass.cast(deserialized);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void setGuildSimpleProperty(Guild guild, String property, String value) {
        try {
            getDocumentLock().writeLock().lock();
            Element propertyElement = ConfigBasicUtils.getOrCreateElementFromPath(getGuildNode(guild), property);
            propertyElement.setTextContent(value);
        } finally {
            getDocumentLock().writeLock().unlock();
        }
    }

    static void setGuildSerializedProperty(Guild guild, String property, Object value) {
        try {
            if (value == null) {
                setGuildSimpleProperty(guild, property, "null");
                return;
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            byte[] serialized = byteArrayOutputStream.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(serialized);
            setGuildSimpleProperty(guild, property, base64);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
