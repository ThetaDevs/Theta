package com.srgood.reasons.config;

import com.srgood.reasons.ReasonsMain;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.srgood.reasons.config.ConfigBasicUtils.getDocumentLock;

public class ConfigPersistenceUtils {
    private static final String DEFAULT_CONFIG_TEXT = "<config />";
    private static final String CONFIG_FILE_NAME = "theta.xml";

    public static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public static String generateDirtyXML() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        // Beautify XML
        // Set do indents
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        // Set indent amount
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        try {
            getDocumentLock().readLock().lock();
            DOMSource source = new DOMSource(ConfigBasicUtils.getDocument());
            StringWriter stringWriter = new StringWriter();
            StreamResult result = new StreamResult(stringWriter);
            transformer.transform(source, result);
            return stringWriter.toString();
        } finally {
            getDocumentLock().readLock().unlock();
        }
    }

    public static String generateCleanXML() throws TransformerException {
        return cleanXMLString(generateDirtyXML());
    }

    //TODO fix the exceptions here
    public static void writeXML() throws TransformerException {
        String cleanXML = generateCleanXML();

        try (FileWriter fileWriter = new FileWriter(new File(CONFIG_FILE_NAME)); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(cleanXML);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String cleanXMLString(String dirtyXML) {

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

    public static void initConfig() throws IOException {
        File inputFile = new File(CONFIG_FILE_NAME);

        try (ByteArrayInputStream byteInputStream =
                     new ByteArrayInputStream(
                             inputFile.exists()
                             ? Files.readAllBytes(inputFile.toPath())
                             : DEFAULT_CONFIG_TEXT.getBytes())) {
            initConfigFromStream(byteInputStream);
        }

        scheduleConfigAutoSaving();
    }

    private static void scheduleConfigAutoSaving() {
        EXECUTOR_SERVICE.schedule(() -> {
            try {
                writeXML();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 3, TimeUnit.MINUTES);
    }

    public static void initConfigFromStream(InputStream inputStream) {
        try {
            getDocumentLock().writeLock().lock();

            ConfigGuildUtils.resetServers();

            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domInput = domFactory.newDocumentBuilder();

            Document doc = domInput.parse(inputStream);
            ConfigBasicUtils.setDocument(doc);
            doc.getDocumentElement().normalize();

            // <config> element
            Element rootElem = doc.getDocumentElement();
            // <server> element list
            NodeList ServerNodes = rootElem.getElementsByTagName("server");
            for (int i = 0; i < ServerNodes.getLength(); i++) {
                Element ServerNode = (Element) ServerNodes.item(i);

                ConfigGuildUtils.addServer(ServerNode.getAttribute("id"), ServerNode);
            }
            ReasonsMain.prefix = ConfigBasicUtils.getOrCreateFirstSubElement(ConfigBasicUtils.getOrCreateFirstSubElement(rootElem, "default"), "prefix", "#!")
                                                 .getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getDocumentLock().writeLock().unlock();
        }
    }
}
