package com.srgood.reasons.impl.base.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class ConfigUtils {
    public static List<Node> nodeListToList(NodeList nodeList) {
        List<Node> ret = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            ret.add(nodeList.item(i));
        }

        return ret;
    }

    public static Stream<Element> getChildElementList(Element parent, String tagName) {
        return nodeListToList(parent.getElementsByTagName(tagName)).stream().map(Element.class::cast);
    }

    public static Stream<Element> getDirectDescendants(Element parent, String tagName) {
        return getChildElementList(parent, tagName).filter(elem -> Objects.equals(elem.getParentNode(), parent));
    }

    public static Element getOrCreateChildElement(Element parent, String property) {
        return getDirectDescendants(parent, property).findFirst().orElseGet(() -> createChild(parent, property));
    }

    public static Stream<Element> getDirectDescendantsWithAttributes(Element parent, String tagName, Map<String, String> attributeValueMap) {
        return getDirectDescendants(parent, tagName).filter(elem -> {
            for (Map.Entry<String, String> mapEntry : attributeValueMap.entrySet()) {
                if (!Objects.equals(elem.getAttribute(mapEntry.getKey()), mapEntry.getValue())) {
                    return false;
                }
            }
            return true;
        });
    }

    public static Element getOrCreateChildElementWithAttributes(Element parent, String tagName, Map<String, String> attributeValueMap) {
        return getDirectDescendantsWithAttributes(parent, tagName, attributeValueMap).findFirst().orElseGet(() -> {
            Element newChild = createChild(parent, tagName);
            for (Map.Entry<String, String> mapEntry : attributeValueMap.entrySet()) {
                newChild.setAttribute(mapEntry.getKey(), mapEntry.getValue());
            }
            return newChild;
        });
    }

    public static Element createChild(Element parent, String childTagName) {
        Element newElement = parent.getOwnerDocument().createElement(childTagName);
        parent.appendChild(newElement);
        return newElement;
    }
}
