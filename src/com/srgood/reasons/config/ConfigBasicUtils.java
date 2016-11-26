package com.srgood.reasons.config;

import net.dv8tion.jda.core.utils.SimpleLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ConfigBasicUtils {
    private static Document document;
    private static Lock documentLock = new ReentrantLock();

    static Document lockAndGetDocument() {
        lockDocument();
        return document;
    }

    static void lockDocument() {
        documentLock.lock();
    }

    static void releaseDocument() {
        documentLock.unlock();
    }

    static void setDocument(Document document) {
        try {
            lockDocument();
            ConfigBasicUtils.document = document;
        } finally {
            releaseDocument();
        }
    }

    static List<Node> nodeListToList(NodeList nl) {
        List<Node> ret = new ArrayList<>();

        for (int i = 0; i < nl.getLength(); i++) {
            ret.add(nl.item(i));
        }

        return ret;
    }

    public static boolean verifyConfig() {
        try {
            Document doc = lockAndGetDocument();
            SimpleLog.getLog("Reasons").warn("**XML IS BEING VERIFIED**");

            if (!doc.getDocumentElement().getTagName().equals("config")) {
                SimpleLog.getLog("Reasons").info("Invalid document element");
                return false;
            }

            NodeList defaultNodeList = doc.getDocumentElement().getElementsByTagName("default");
            if (defaultNodeList.getLength() != 1) {
                SimpleLog.getLog("Reasons").info("Not 1 default element");
                return false;
            }
            Element defaultElement = (Element) defaultNodeList.item(0);
            if (defaultElement.getElementsByTagName("prefix").getLength() != 1) {
                SimpleLog.getLog("Reasons").info("Not 1 default/prefix element");
                return false;
            }


            if (doc.getDocumentElement().getElementsByTagName("servers").getLength() != 1) {
                SimpleLog.getLog("Reasons").info("Not 1 servers element");
                return false;
            }

            for (Node n : nodeListToList(((Element) doc.getDocumentElement().getElementsByTagName("servers").item(0)).getElementsByTagName("server"))) {
                Element serverElement = (Element) n;

                if (serverElement.getElementsByTagName("prefix").getLength() != 1) {
                    SimpleLog.getLog("Reasons").info("Not 1 servers/server/prefix element");
                    return false;
                }

                NodeList rolesNodeList = serverElement.getElementsByTagName("roles");
                if (rolesNodeList.getLength() != 1) {
                    SimpleLog.getLog("Reasons").info("Not 1 servers/server/roles element");
                    return false;
                }
                if (((Element) rolesNodeList.item(0)).getElementsByTagName("role").getLength() < 1) {
                    SimpleLog.getLog("Reasons").info("Less than 1 servers/server/roles/role element");
                    return false;
                }

                NodeList commandsNodeList = serverElement.getElementsByTagName("commands");

                if (commandsNodeList.getLength() != 1) {
                    SimpleLog.getLog("Reasons").info("Not 1 servers/server/commands element");
                    return false;
                }
                NodeList commandNodeList = ((Element) commandsNodeList.item(0)).getElementsByTagName("command");
                if (commandNodeList.getLength() < 1) {
                    SimpleLog.getLog("Reasons").info("Less than 1 servers/server/commands/command element");
                    return false;
                }
                {
                    for (Node node : nodeListToList(commandNodeList)) {
                        Element commandElement = (Element) node;
                        if (commandElement.getElementsByTagName("permLevel").getLength() != 1) {
                            SimpleLog.getLog("Reasons").info("Not 1 servers/server/commands/command/permLevel element");
                            return false;
                        }
                        if (commandElement.getElementsByTagName("isEnabled").getLength() != 1) {
                            SimpleLog.getLog("Reasons").info("Not 1 servers/server/commands/command/isEnabled element");
                            return false;
                        }
                    }
                }
            }
        } finally {
            releaseDocument();
        }
        return true;
    }

    static Element getFirstSubElement(Element parent, String subTagName) {
        try {
            lockDocument();
            List<Node> nList = nodeListToList(parent.getElementsByTagName(subTagName));
            return (Element) (nList.size() > 0 ? nList.get(0) : null);
        } finally {
            releaseDocument();
        }
    }

}
