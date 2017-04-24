package com.srgood.reasons.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ConfigBasicUtils {
    private static Document document;
    private static final ReadWriteLock documentLock = new ReentrantReadWriteLock();

    static ReadWriteLock getDocumentLock() {
        return documentLock;
    }

    static Document getDocument() {
        return document;
    }

    static void setDocument(Document document) {
        try {
            getDocumentLock().writeLock().lock();
            ConfigBasicUtils.document = document;
        } finally {
            getDocumentLock().writeLock().unlock();
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
            getDocumentLock().readLock().lock();
            List<Node> nList = nodeListToList(parent.getElementsByTagName(subTagName));
            return (Element) (nList.size() > 0 ? nList.get(0) : null);
        } finally {
            getDocumentLock().readLock().unlock();
        }
    }

    private static final String PATH_SPLIT_REGEX = "[/.@\\\\]";

    static Element getElementFromPath(Element parent, String path) {
        try {
            getDocumentLock().readLock().lock();
            String[] parts = path.split(PATH_SPLIT_REGEX);
            Element firstPartElement = getFirstSubElement(parent, parts[0]);
            if (firstPartElement == null) {
                return null;
            }
            if (parts.length == 1) {
                return firstPartElement;
            } else {
                return getElementFromPath(firstPartElement, path.substring(parts[0].length() + 1));
            }
        } finally {
            getDocumentLock().readLock().unlock();
        }
    }

    static Element getOrCreateElementFromPath(Element parent, String path) {
        try {
            getDocumentLock().readLock().lock();
            Document doc = getDocument();
            String[] parts = path.split(PATH_SPLIT_REGEX);
            Element firstPartElement = getFirstSubElement(parent, parts[0]);
            if (firstPartElement == null) {
                try {
                    getDocumentLock().readLock().unlock();
                    getDocumentLock().writeLock().lock();
                    if (getFirstSubElement(parent, parts[0]) == null) {
                        Element newFirst = doc.createElement(parts[0]);
                        parent.appendChild(newFirst);
                        firstPartElement = newFirst;
                    }
                } finally {
                    getDocumentLock().writeLock().unlock();
                    getDocumentLock().readLock().lock();
                }
            }
            if (parts.length == 1) {
                return firstPartElement;
            }
            return getOrCreateElementFromPath(firstPartElement, path.substring(parts[0].length() + 1));
        } finally {
            getDocumentLock().readLock().unlock();
        }
    }

    static Element getOrCreateFirstSubElement(Element parent, String subTagName) {
        return getOrCreateFirstSubElement(parent, subTagName, null);
    }

    static Element getOrCreateFirstSubElement(Element parent, String subTagName, String defaultValue) {
        try {
            getDocumentLock().readLock().lock();
            Document doc = getDocument();
            Element ret = getFirstSubElement(parent, subTagName);
            if (ret == null) {
                try {
                    getDocumentLock().readLock().unlock();
                    getDocumentLock().writeLock().lock();
                    if (getFirstSubElement(parent, subTagName) == null) {
                        ret = doc.createElement(subTagName);
                        parent.appendChild(ret);
                        if (defaultValue != null) {
                            ret.setTextContent(defaultValue);
                        }
                    }
                } finally {
                    getDocumentLock().writeLock().unlock();
                    getDocumentLock().readLock().lock();
                }
            }
            return ret;
        } finally {
            getDocumentLock().readLock().unlock();
        }
    }

}
