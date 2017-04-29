package com.srgood.reasons.impl.config;

import com.google.common.io.Files;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ConfigFileManager {
    private final String fileName;

    private boolean parseCompleted = false;
    private Document parsedDocument;

    public ConfigFileManager(String fileName) {
        this.fileName = fileName;
    }

    public ConfigFileManager parse() {
        try {
            parsedDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(fileName));
            parseCompleted = true;
            return this;
        } catch (Exception e) {
            return null;
        }
    }

    public Document getDocument() {
        checkState();
        return parsedDocument;
    }

    public void save() {
        checkState();
        try {
            Files.write(generateXML(), new File(fileName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Exception when writing config: ");
            e.printStackTrace();
        }
    }


    private String generateXML() {
        checkState();
        try {
            Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer();
            xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // Set indent amount
            xmlTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            xmlTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            xmlTransformer.setOutputProperty(OutputKeys.METHOD, "xml");

            DOMSource domSource = new DOMSource(parsedDocument);
            StringWriter stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);
            xmlTransformer.transform(domSource, streamResult);
            return cleanXML(stringWriter.toString());
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private String cleanXML(String dirtyXML) {
        String[] lines = dirtyXML.split("\n");
        StringBuilder buffer = new StringBuilder();

        for (String line : lines) {
            if (!Objects.equals(line.trim(), "") /* don't write out blank lines */) {
                line = line.replace("\f", "").replace("\r", "").replaceAll("\\s+$", "");
                buffer.append(line).append("\n");
            }
        }

        return buffer.toString();
    }

    private void checkState() {
        if (!parseCompleted) {
            throw new IllegalStateException();
        }
    }
}
