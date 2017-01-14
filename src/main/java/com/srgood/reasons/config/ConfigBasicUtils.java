package com.srgood.reasons.config;

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

    static Element getFirstSubElement(Element parent, String subTagName) {
        try {
            lockDocument();
            List<Node> nList = nodeListToList(parent.getElementsByTagName(subTagName));
            return (Element) (nList.size() > 0 ? nList.get(0) : null);
        } finally {
            releaseDocument();
        }
    }

    static Element getOrCreateFirstSubElement(Element parent, String subTagName) {
        return getOrCreateFirstSubElement(parent, subTagName, null);
    }

    static Element getOrCreateFirstSubElement(Element parent, String subTagName, String defaultValue) {
        try {
            Document doc = lockAndGetDocument();
            Element ret = getFirstSubElement(parent, subTagName);
            if (ret == null) {
                ret = doc.createElement(subTagName);
                parent.appendChild(ret);
                if (defaultValue != null) {
                    ret.setTextContent(defaultValue);
                }
            }
            return ret;
        } finally {
            releaseDocument();
        }
    }

}
